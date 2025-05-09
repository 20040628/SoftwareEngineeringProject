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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/bank-payment")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:5174", "http://118.24.22.77"}, allowCredentials = "true")
public class BankCardPaymentController {
    
    @Autowired
    private OrderRepository orderRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    // 押金金额，固定50元
    private static final BigDecimal DEPOSIT_AMOUNT = new BigDecimal("50.00");
    
    /**
     * 银行卡支付接口
     * @param orderId 订单ID
     * @param request 支付请求（包含安全码）
     * @return 支付结果
     */
    @PostMapping("/{orderId}")
    @Transactional
    public ResponseEntity<?> processBankCardPayment(
            @PathVariable Long orderId,
            @Valid @RequestBody BankCardPaymentRequest request) {
        try {
            // 查找订单
            Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));
                
            // 验证订单状态
            if (order.getStatus() != 1) { // 状态必须是CREATED(已创建)
                throw new IllegalArgumentException("Invalid order status, cannot process payment");
            }
            
            // 获取用户信息和银行卡
            User user = order.getUser();
            if (user.getBankCard() == null || user.getBankCard().isEmpty()) {
                throw new IllegalArgumentException("Bank card information not found, please bind your bank card first");
            }
            
            // 检查银行卡余额是否足够（原价+押金）
            BigDecimal totalAmount = order.getPrice().add(DEPOSIT_AMOUNT);
            if (user.getBankBalance() == null || user.getBankBalance().compareTo(totalAmount) < 0) {
                throw new IllegalArgumentException("Insufficient bank card balance, payment failed");
            }
            
            // 模拟银行卡支付验证 (验证卡号)
            if (request.getBankCard() == null || !request.getBankCard().equals(user.getBankCard())) {
                throw new IllegalArgumentException("Invalid bank card");
            }
            
            // 设置支付方式为银行卡
            order.setPaymentMethod("BANK_CARD");
            
            // 添加押金信息
            order.setDepositPaid(true);
            order.setDepositAmount(DEPOSIT_AMOUNT);
            order.setDepositRefunded(false);
            
            // 从银行卡余额扣除总金额（订单金额+押金）
            BigDecimal newBalance = user.getBankBalance().subtract(totalAmount);
            user.setBankBalance(newBalance);
            userRepository.save(user);
            
            // 更新订单状态为已支付
            order.setStatus(2); // PAID (已支付)
            orderRepository.save(order);
            
            // 构建响应
            BankCardPaymentResponse response = new BankCardPaymentResponse();
            response.setSuccess(true);
            response.setMessage("Bank card payment successful");
            response.setOrderId(order.getId());
            // 获取银行卡后4位显示
            String bankCard = user.getBankCard();
            String last4 = bankCard.substring(Math.max(0, bankCard.length() - 4));
            response.setBankCardLast4(last4);
            response.setAmount(totalAmount); // 总金额（含押金）
            response.setPaymentTime(new Date());
            
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Payment failed: " + e.getMessage());
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
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
                
            // 检查银行卡信息
            boolean hasBankCard = user.getBankCard() != null && !user.getBankCard().isEmpty();
            String maskedCard = null;
            BigDecimal bankBalance = user.getBankBalance();
            
            if (hasBankCard) {
                String bankCard = user.getBankCard();
                // 只显示后4位，其余用*代替
                maskedCard = "**** **** **** " + bankCard.substring(Math.max(0, bankCard.length() - 4));
            }
            
            Map<String, Object> response = new HashMap<>();
            response.put("hasBankCard", hasBankCard);
            response.put("maskedCard", maskedCard);
            response.put("bankBalance", bankBalance);
            
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to query bank card: " + e.getMessage());
        }
    }
} 