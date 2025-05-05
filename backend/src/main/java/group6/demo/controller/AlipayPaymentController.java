package group6.demo.controller;

import group6.demo.dto.BankCardPaymentResponse;
import group6.demo.entity.Order;
import group6.demo.repository.OrderRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/alipay-payment")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:5174"}, allowCredentials = "true")
public class AlipayPaymentController {
    
    @Autowired
    private OrderRepository orderRepository;
    
    /**
     * 支付宝支付接口
     * @param orderId 订单ID
     * @return 支付结果
     */
    @PostMapping("/{orderId}")
    public ResponseEntity<?> processAlipayPayment(@PathVariable Long orderId) {
        try {
            // 查找订单
            Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("订单不存在"));
                
            // 验证订单状态
            if (order.getStatus() != 1) {
                throw new IllegalArgumentException("订单状态不正确，无法支付");
            }
            
            // 模拟支付宝支付处理
            // 在实际环境中这里会调用支付宝支付网关API
            
            // 设置支付方式为支付宝
            order.setPaymentMethod("ALIPAY");
            
            // 支付宝支付不收取押金，也不需要处理押金退还
            order.setDepositPaid(false);
            
            // 更新订单状态（保持为1-活跃状态，只有还车后才变为2-已完成）
            orderRepository.save(order);
            
            // 构建响应
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "支付宝支付成功");
            response.put("orderId", order.getId());
            response.put("amount", order.getPrice());
            response.put("paymentTime", new Date());
            
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Payment failed: " + e.getMessage());
        }
    }
} 