package group6.demo.controller;

import group6.demo.dto.BankCardPaymentRequest;
import group6.demo.dto.BankCardPaymentResponse;
import group6.demo.entity.Order;
import group6.demo.entity.User;
import group6.demo.repository.OrderRepository;
import group6.demo.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/bank-payment")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:5174"}, allowCredentials = "true")
public class BankCardPaymentController {
    
    @Autowired
    private OrderRepository orderRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    /**
     * 银行卡支付接口
     * @param orderId 订单ID
     * @param request 支付请求（包含安全码）
     * @return 支付结果
     */
    @PostMapping("/{orderId}")
    public ResponseEntity<?> processBankCardPayment(
            @PathVariable Long orderId,
            @Valid @RequestBody BankCardPaymentRequest request) {
        try {
            // 查找订单
            Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("订单不存在"));
                
            // 验证订单状态
            if (order.getStatus() != 1) {
                throw new IllegalArgumentException("订单状态不正确，无法支付");
            }
            
            // 获取用户信息和银行卡
            User user = order.getUser();
            if (user.getBankCard() == null || user.getBankCard().isEmpty()) {
                throw new IllegalArgumentException("未找到银行卡信息，请先绑定银行卡");
            }
            
            // 模拟银行卡支付验证 (验证安全码)
            if (request.getSecurityCode() == null || request.getSecurityCode().length() < 3) {
                throw new IllegalArgumentException("无效的安全码");
            }
            
            // 模拟支付处理
            // 在实际环境中这里会调用银行支付网关API
            
            // 更新订单状态
            order.setStatus(2); // 已支付
            orderRepository.save(order);
            
            // 构建响应
            BankCardPaymentResponse response = new BankCardPaymentResponse();
            response.setSuccess(true);
            response.setMessage("支付成功");
            response.setOrderId(order.getId());
            // 获取银行卡后4位显示
            String bankCard = user.getBankCard();
            String last4 = bankCard.substring(Math.max(0, bankCard.length() - 4));
            response.setBankCardLast4(last4);
            response.setAmount(order.getPrice());
            response.setPaymentTime(new Date());
            
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("支付失败: " + e.getMessage());
        }
    }
    
    /**
     * 检查用户是否已绑定银行卡
     * @param userId 用户ID
     * @return 银行卡信息
     */
    @GetMapping("/check-card/{userId}")
    public ResponseEntity<?> checkBankCard(@PathVariable Long userId) {
        try {
            // 查找用户
            User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("用户不存在"));
                
            // 检查银行卡信息
            boolean hasBankCard = user.getBankCard() != null && !user.getBankCard().isEmpty();
            String maskedCard = null;
            
            if (hasBankCard) {
                String bankCard = user.getBankCard();
                // 只显示后4位，其余用*代替
                maskedCard = "**** **** **** " + bankCard.substring(Math.max(0, bankCard.length() - 4));
            }
            
            Map<String, Object> response = new HashMap<>();
            response.put("hasBankCard", hasBankCard);
            response.put("maskedCard", maskedCard);
            
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("查询银行卡失败: " + e.getMessage());
        }
    }
} 