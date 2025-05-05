package group6.demo.controller;

import group6.demo.config.TestingModeConfig;
import group6.demo.dto.DailyRevenueDTO;
import group6.demo.dto.WeeklyRevenueDTO;
import group6.demo.service.WeeklyRevenueService;
import group6.demo.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/weekly-revenue")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:5174"}, allowCredentials = "true")
public class WeeklyRevenueController {

    @Autowired
    private WeeklyRevenueService weeklyRevenueService;

    @Autowired
    private JwtUtil jwtUtil;
    
    @Autowired
    private TestingModeConfig testingModeConfig;

    /**
     * 获取当前周收入统计
     */
    @GetMapping("/current")
    public ResponseEntity<?> getCurrentWeekRevenue(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        try {
            // 测试模式下不需要验证
            if (testingModeConfig.isTestingEnabled()) {
                WeeklyRevenueDTO revenue = weeklyRevenueService.generateCurrentWeekRevenue();
                return ResponseEntity.ok(revenue);
            }
            
            // 检查Authorization头是否存在
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(401).body("unauthorized: please provide a valid token");
            }

            // 验证用户是否是管理员
            String token = authHeader.substring(7);
            Integer role = jwtUtil.extractRole(token);

            if (role != 0) {
                return ResponseEntity.status(403).body("insufficient permissions: admin rights required");
            }

            WeeklyRevenueDTO revenue = weeklyRevenueService.generateCurrentWeekRevenue();
            return ResponseEntity.ok(revenue);
        } catch (io.jsonwebtoken.JwtException e) {
            return ResponseEntity.status(401).body("invalid token: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("failed to get current week revenue: " + e.getMessage());
        }
    }

    /**
     * 获取指定日期所在周的收入统计
     */
    @GetMapping("/by-date")
    public ResponseEntity<?> getWeeklyRevenueByDate(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date date) {
        try {
            // 测试模式下不需要验证
            if (testingModeConfig.isTestingEnabled()) {
                WeeklyRevenueDTO revenue = weeklyRevenueService.getWeeklyRevenueByDate(date);
                return ResponseEntity.ok(revenue);
            }
            
            // 检查Authorization头是否存在
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(401).body("unauthorized: please provide a valid token");
            }

            // 验证用户是否是管理员
            String token = authHeader.substring(7);
            Integer role = jwtUtil.extractRole(token);

            if (role != 0) {
                return ResponseEntity.status(403).body("insufficient permissions: admin rights required");
            }

            WeeklyRevenueDTO revenue = weeklyRevenueService.getWeeklyRevenueByDate(date);
            return ResponseEntity.ok(revenue);
        } catch (io.jsonwebtoken.JwtException e) {
            return ResponseEntity.status(401).body("invalid token: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("failed to get weekly revenue by date: " + e.getMessage());
        }
    }

    /**
     * 获取最近几周的收入统计
     */
    @GetMapping("/recent")
    public ResponseEntity<?> getRecentWeeklyRevenues(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @RequestParam(defaultValue = "4") int count) {
        try {
            // 检查Authorization头是否存在
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(401).body("unauthorized: please provide a valid token");
            }

            // 验证用户是否是管理员
            String token = authHeader.substring(7);
            Integer role = jwtUtil.extractRole(token);

            if (role != 0) {
                return ResponseEntity.status(403).body("insufficient permissions: admin rights required");
            }

            List<WeeklyRevenueDTO> revenues = weeklyRevenueService.getRecentWeeklyRevenues(count);
            return ResponseEntity.ok(revenues);
        } catch (io.jsonwebtoken.JwtException e) {
            return ResponseEntity.status(401).body("invalid token: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("failed to get recent weekly revenues: " + e.getMessage());
        }
    }

    /**
     * 获取指定日期范围内的收入统计
     */
    @GetMapping("/range")
    public ResponseEntity<?> getWeeklyRevenuesByDateRange(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
        try {
            // 检查Authorization头是否存在
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(401).body("unauthorized: please provide a valid token");
            }

            // 验证用户是否是管理员
            String token = authHeader.substring(7);
            Integer role = jwtUtil.extractRole(token);

            if (role != 0) {
                return ResponseEntity.status(403).body("insufficient permissions: admin rights required");
            }

            List<WeeklyRevenueDTO> revenues = weeklyRevenueService.getWeeklyRevenuesByDateRange(startDate, endDate);
            return ResponseEntity.ok(revenues);
        } catch (io.jsonwebtoken.JwtException e) {
            return ResponseEntity.status(401).body("invalid token: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("failed to get weekly revenues by date range: " + e.getMessage());
        }
    }

    /**
     * 手动触发收入统计更新
     */
    @PostMapping("/update")
    public ResponseEntity<?> updateWeeklyRevenue(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        try {
            // 测试模式下不需要验证
            if (testingModeConfig.isTestingEnabled()) {
                weeklyRevenueService.updateWeeklyRevenue();
                return ResponseEntity.ok("revenue statistics updated successfully");
            }
            
            // 检查Authorization头是否存在
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(401).body("unauthorized: please provide a valid token");
            }

            // 验证用户是否是管理员
            String token = authHeader.substring(7);
            Integer role = jwtUtil.extractRole(token);

            if (role != 0) {
                return ResponseEntity.status(403).body("insufficient permissions: admin rights required");
            }

            weeklyRevenueService.updateWeeklyRevenue();
            return ResponseEntity.ok("revenue statistics updated successfully");
        } catch (io.jsonwebtoken.JwtException e) {
            return ResponseEntity.status(401).body("invalid token: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("failed to update revenue statistics: " + e.getMessage());
        }
    }

    /**
     * 获取指定周内每日的收入统计
     */
    @GetMapping("/daily")
    public ResponseEntity<?> getDailyRevenuesInWeek(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date weekStartDate) {
        try {
            // 检查Authorization头是否存在
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(401).body("未授权: 请提供有效的令牌");
            }

            // 验证用户是否是管理员
            String token = authHeader.substring(7);
            Integer role = jwtUtil.extractRole(token);

            if (role != 0) {
                return ResponseEntity.status(403).body("权限不足: 需要管理员权限");
            }

            List<DailyRevenueDTO> dailyRevenues = weeklyRevenueService.getDailyRevenuesInWeek(weekStartDate);
            return ResponseEntity.ok(dailyRevenues);
        } catch (io.jsonwebtoken.JwtException e) {
            return ResponseEntity.status(401).body("无效的令牌: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("获取每日收入统计失败: " + e.getMessage());
        }
    }

    /**
     * 获取指定日期范围内的每日收入统计
     */
    @GetMapping("/daily-range")
    public ResponseEntity<?> getDailyRevenuesByDateRange(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
        try {
            // 检查Authorization头是否存在
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(401).body("未授权: 请提供有效的令牌");
            }

            // 验证用户是否是管理员
            String token = authHeader.substring(7);
            Integer role = jwtUtil.extractRole(token);

            if (role != 0) {
                return ResponseEntity.status(403).body("权限不足: 需要管理员权限");
            }

            List<DailyRevenueDTO> dailyRevenues = weeklyRevenueService.getDailyRevenuesByDateRange(startDate, endDate);
            return ResponseEntity.ok(dailyRevenues);
        } catch (io.jsonwebtoken.JwtException e) {
            return ResponseEntity.status(401).body("无效的令牌: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("获取每日收入统计失败: " + e.getMessage());
        }
    }

    /**
     * 获取最近n天的每日收入统计
     */
    @GetMapping("/daily-recent")
    public ResponseEntity<?> getRecentDailyRevenues(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @RequestParam(defaultValue = "7") int days) {
        try {
            // 检查Authorization头是否存在
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(401).body("未授权: 请提供有效的令牌");
            }

            // 验证用户是否是管理员
            String token = authHeader.substring(7);
            Integer role = jwtUtil.extractRole(token);

            if (role != 0) {
                return ResponseEntity.status(403).body("权限不足: 需要管理员权限");
            }

            List<DailyRevenueDTO> dailyRevenues = weeklyRevenueService.getRecentDailyRevenues(days);
            return ResponseEntity.ok(dailyRevenues);
        } catch (io.jsonwebtoken.JwtException e) {
            return ResponseEntity.status(401).body("无效的令牌: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("获取最近每日收入统计失败: " + e.getMessage());
        }
    }

    /**
     * 手动触发每日收入统计更新
     */
    @PostMapping("/update-daily")
    public ResponseEntity<?> updateDailyRevenue(
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        try {
            // 检查Authorization头是否存在
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(401).body("未授权: 请提供有效的令牌");
            }

            // 验证用户是否是管理员
            String token = authHeader.substring(7);
            Integer role = jwtUtil.extractRole(token);

            if (role != 0) {
                return ResponseEntity.status(403).body("权限不足: 需要管理员权限");
            }

            // 获取昨天的日期
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_MONTH, -1);
            Date yesterday = calendar.getTime();

            // 手动更新昨天的收入统计
            weeklyRevenueService.generateAndSaveDailyRevenue(yesterday);
            return ResponseEntity.ok("每日收入统计更新成功");
        } catch (io.jsonwebtoken.JwtException e) {
            return ResponseEntity.status(401).body("无效的令牌: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("更新每日收入统计失败: " + e.getMessage());
        }
    }

    /**
     * 生成并保存指定日期的每日收入统计
     */
    @PostMapping("/generate-daily")
    public ResponseEntity<?> generateDailyRevenue(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date date) {
        try {
            // 检查Authorization头是否存在
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(401).body("未授权: 请提供有效的令牌");
            }

            // 验证用户是否是管理员
            String token = authHeader.substring(7);
            Integer role = jwtUtil.extractRole(token);

            if (role != 0) {
                return ResponseEntity.status(403).body("权限不足: 需要管理员权限");
            }

            // 生成并保存指定日期的收入统计
            DailyRevenueDTO dailyRevenue = weeklyRevenueService.generateAndSaveDailyRevenue(date);
            return ResponseEntity.ok(dailyRevenue);
        } catch (io.jsonwebtoken.JwtException e) {
            return ResponseEntity.status(401).body("无效的令牌: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("生成每日收入统计失败: " + e.getMessage());
        }
    }
}