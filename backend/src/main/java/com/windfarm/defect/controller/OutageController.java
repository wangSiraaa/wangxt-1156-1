package com.windfarm.defect.controller;

import com.windfarm.defect.dto.OutageCreateDTO;
import com.windfarm.defect.entity.OutageRecord;
import com.windfarm.defect.service.OutageService;
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

@Tag(name = "停机管理", description = "机组停机记录管理接口")
@RestController
@RequestMapping("/outages")
public class OutageController {

    @Autowired
    private OutageService outageService;

    @Operation(summary = "创建停机记录")
    @PostMapping
    public ResponseEntity<Map<String, Object>> createOutage(
            @Valid @RequestBody OutageCreateDTO dto,
            @RequestHeader(value = "X-User", defaultValue = "station_manager") String operator) {
        OutageRecord outage = outageService.createOutage(dto, operator);
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "停机记录创建成功");
        result.put("data", outage);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "结束停机")
    @PutMapping("/{outageId}/end")
    public ResponseEntity<Map<String, Object>> endOutage(
            @Parameter(description = "停机记录ID") @PathVariable Long outageId,
            @RequestHeader(value = "X-User", defaultValue = "station_manager") String operator) {
        OutageRecord outage = outageService.endOutage(outageId, operator);
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "停机已结束");
        result.put("data", outage);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "查询停机记录列表")
    @GetMapping
    public ResponseEntity<Map<String, Object>> listOutages(
            @Parameter(description = "机组ID") @RequestParam(required = false) Long turbineId,
            @Parameter(description = "是否仅活动停机") @RequestParam(required = false) String isActive) {
        Boolean activeFilter = parseBoolean(isActive);
        List<OutageRecord> outages = outageService.listOutages(turbineId, activeFilter);
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "查询成功");
        result.put("data", outages);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "查询停机记录详情")
    @GetMapping("/{outageId}")
    public ResponseEntity<Map<String, Object>> getOutageDetail(
            @Parameter(description = "停机记录ID") @PathVariable Long outageId) {
        OutageRecord outage = outageService.getOutageDetail(outageId);
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "查询成功");
        result.put("data", outage);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "查询机组当前活动停机")
    @GetMapping("/active/turbine/{turbineId}")
    public ResponseEntity<Map<String, Object>> getActiveOutageByTurbine(
            @Parameter(description = "机组ID") @PathVariable Long turbineId) {
        OutageRecord outage = outageService.getActiveOutageByTurbine(turbineId);
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "查询成功");
        result.put("data", outage);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "检查机组是否停机")
    @GetMapping("/check/turbine/{turbineId}")
    public ResponseEntity<Map<String, Object>> checkTurbineStopped(
            @Parameter(description = "机组ID") @PathVariable Long turbineId) {
        boolean isStopped = outageService.isTurbineStopped(turbineId);
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "查询成功");
        result.put("data", Map.of("isStopped", isStopped));
        return ResponseEntity.ok(result);
    }

    private Boolean parseBoolean(String value) {
        if (value == null || value.isBlank() || "null".equalsIgnoreCase(value)) {
            return null;
        }
        return Boolean.parseBoolean(value);
    }
}
