package com.windfarm.defect.controller;

import com.windfarm.defect.dto.MaintenanceWindowCreateDTO;
import com.windfarm.defect.dto.MaintenanceWindowUpdateDTO;
import com.windfarm.defect.entity.MaintenanceWindow;
import com.windfarm.defect.enums.WindowStatus;
import com.windfarm.defect.service.MaintenanceWindowService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Tag(name = "检修窗口管理", description = "检修窗口评估与管理接口")
@RestController
@RequestMapping("/maintenance-windows")
public class MaintenanceWindowController {

    @Autowired
    private MaintenanceWindowService maintenanceWindowService;

    @Operation(summary = "创建检修窗口", description = "检修班评估并创建停机检修窗口")
    @PostMapping
    public ResponseEntity<Map<String, Object>> createWindow(
            @Valid @RequestBody MaintenanceWindowCreateDTO dto,
            @RequestHeader(value = "X-User", defaultValue = "maintenance_team") String operator) {
        MaintenanceWindow window = maintenanceWindowService.createWindow(dto, operator);
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "检修窗口创建成功");
        result.put("data", window);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "确认检修窗口")
    @PutMapping("/{windowId}/confirm")
    public ResponseEntity<Map<String, Object>> confirmWindow(
            @Parameter(description = "窗口ID") @PathVariable Long windowId,
            @RequestHeader(value = "X-User", defaultValue = "station_manager") String operator) {
        MaintenanceWindow window = maintenanceWindowService.confirmWindow(windowId, operator);
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "检修窗口已确认");
        result.put("data", window);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "开始检修")
    @PutMapping("/{windowId}/start")
    public ResponseEntity<Map<String, Object>> startWindow(
            @Parameter(description = "窗口ID") @PathVariable Long windowId,
            @RequestBody MaintenanceWindowUpdateDTO dto,
            @RequestHeader(value = "X-User", defaultValue = "maintenance_team") String operator) {
        MaintenanceWindow window = maintenanceWindowService.startWindow(windowId, dto, operator);
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "检修已开始");
        result.put("data", window);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "完成检修")
    @PutMapping("/{windowId}/complete")
    public ResponseEntity<Map<String, Object>> completeWindow(
            @Parameter(description = "窗口ID") @PathVariable Long windowId,
            @RequestBody MaintenanceWindowUpdateDTO dto,
            @RequestHeader(value = "X-User", defaultValue = "maintenance_team") String operator) {
        MaintenanceWindow window = maintenanceWindowService.completeWindow(windowId, dto, operator);
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "检修已完成");
        result.put("data", window);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "取消检修窗口")
    @PutMapping("/{windowId}/cancel")
    public ResponseEntity<Map<String, Object>> cancelWindow(
            @Parameter(description = "窗口ID") @PathVariable Long windowId,
            @RequestParam(required = false) String reason,
            @RequestHeader(value = "X-User", defaultValue = "station_manager") String operator) {
        MaintenanceWindow window = maintenanceWindowService.cancelWindow(windowId, reason, operator);
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "检修窗口已取消");
        result.put("data", window);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "查询检修窗口列表")
    @GetMapping
    public ResponseEntity<Map<String, Object>> listWindows(
            @Parameter(description = "机组ID") @RequestParam(required = false) Long turbineId,
            @Parameter(description = "缺陷ID") @RequestParam(required = false) Long defectId,
            @Parameter(description = "窗口状态") @RequestParam(required = false) WindowStatus status) {
        List<MaintenanceWindow> windows = maintenanceWindowService.listWindows(turbineId, defectId, status);
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "查询成功");
        result.put("data", windows);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "查询检修窗口详情")
    @GetMapping("/{windowId}")
    public ResponseEntity<Map<String, Object>> getWindowDetail(
            @Parameter(description = "窗口ID") @PathVariable Long windowId) {
        MaintenanceWindow window = maintenanceWindowService.getWindowDetail(windowId);
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "查询成功");
        result.put("data", window);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "检查风速是否可登塔")
    @GetMapping("/wind-speed-check")
    public ResponseEntity<Map<String, Object>> checkWindSpeed(
            @Parameter(description = "风速(m/s)") @RequestParam double windSpeed) {
        boolean canClimb = maintenanceWindowService.canScheduleTowerClimb(windSpeed);
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "查询成功");
        result.put("data", Map.of("canClimb", canClimb, "windSpeed", windSpeed));
        return ResponseEntity.ok(result);
    }
}
