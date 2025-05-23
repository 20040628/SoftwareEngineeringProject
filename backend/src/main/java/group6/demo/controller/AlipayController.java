package group6.demo.controller;

import com.alipay.api.domain.AlipayTradePayModel;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradePayRequest;
import group6.demo.config.AlipayConfig;
import group6.demo.entity.Order;
import group6.demo.repository.OrderRepository;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping("/alipay")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:5174", "http://118.24.22.77"}, allowCredentials = "true")
public class AlipayController {
    @Resource
    AlipayConfig alipayConfig;
    @Autowired
    private OrderRepository orderRepository;
    private static final String GATEWAY_URL ="https://openapi-sandbox.dl.alipaydev.com/gateway.do";
    private static final String FORMAT ="JSON";
    private static final String CHARSET ="utf-8";
    private static final String SIGN_TYPE ="RSA2";

    @GetMapping("/pay/{orderId}")
    public void pay(@PathVariable Long orderId, HttpServletResponse response) {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        Order order = optionalOrder.orElse(null);

        try {
            AlipayClient alipayClient = new DefaultAlipayClient(GATEWAY_URL, alipayConfig.getAppId(),
                    alipayConfig.getAppPrivateKey(), FORMAT, CHARSET, alipayConfig.getAlipayPublicKey(), SIGN_TYPE);

            AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();

            AlipayTradePayModel model = new AlipayTradePayModel();
            String outTradeNo = System.currentTimeMillis() + "_" + orderId;
            model.setOutTradeNo(outTradeNo);
            model.setTotalAmount(order.getPrice().toString());
            model.setSubject("Your order");
            model.setBody("This is your rent order");
            model.setProductCode("FAST_INSTANT_TRADE_PAY");
            alipayRequest.setBizModel(model);

            String url = "http://localhost:5173/my-bookings";
            alipayRequest.setReturnUrl(url);

            String result = alipayClient.pageExecute(alipayRequest).getBody();
            response.setContentType("text/html;charset=utf-8");
            response.getWriter().write(result);
            response.getWriter().flush();

            order.setStatus(2);
            orderRepository.save(order);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/appPay/{orderId}")
    public void payApp(@PathVariable Long orderId, HttpServletResponse response) {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        Order order = optionalOrder.orElse(null);

        try {
            AlipayClient alipayClient = new DefaultAlipayClient(GATEWAY_URL, alipayConfig.getAppId(),
                    alipayConfig.getAppPrivateKey(), FORMAT, CHARSET, alipayConfig.getAlipayPublicKey(), SIGN_TYPE);
            AlipayTradeAppPayRequest alipayRequest = new AlipayTradeAppPayRequest();
            AlipayTradePayModel model = new AlipayTradePayModel();
            model.setOutTradeNo(orderId.toString());
            model.setTotalAmount(order.getPrice().toString());
            model.setSubject("Your order");
            model.setBody("This is your rent order");
            model.setProductCode("FAST_INSTANT_TRADE_PAY");
            alipayRequest.setBizModel(model);

            // 跳转到我们的页面，要改成最新前端的
            String url = "http://localhost:5173/my-bookings";
            alipayRequest.setReturnUrl(url);

            String result = alipayClient.sdkExecute(alipayRequest).getBody();
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().write("{\"orderString\": \"" + result + "\"}");
            response.getWriter().flush();
            response.getWriter().close();


            order.setStatus(2);
            orderRepository.save(order);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
// 后续可以改成一个新页面，显示订单已完成，但感觉用处不大
//    @PostMapping("/success")
//    public void success(@RequestParam("orderId") String orderId, HttpServletResponse response) throws IOException {
//        response.setContentType("text/html;charset=utf-8");
//        response.getWriter().println("订单Id为"+orderId+"的订单已经成功支付");
//    }
}