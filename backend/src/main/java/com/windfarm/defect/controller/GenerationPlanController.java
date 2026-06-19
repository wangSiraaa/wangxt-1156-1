package com.windfarm.defect.controller;

import com.windfarm.defect.dto.GenerationPlanCreateDTO;
import com.windfarm.defect.entity.GenerationPlan;
import com.windfarm.defect.service.GenerationPlanService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Tag(name = "发电计划", description = "发电计划管理接口")
@RestController
@RequestMapping("/generation-plans")
@RequiredArgsConstructor
public class GenerationPlanController {

    private final GenerationPlanService generationPlanService;

    @Operation(summary = "创建发电计划")
    @PostMapping
    public ResponseEntity<Map<String, Object>> createPlan(
            @Valid @RequestBody GenerationPlanCreateDTO dto,
            @RequestHeader(value = "X-User", defaultValue = "dispatch") String operator) {
        GenerationPlan plan = generationPlanService.createPlan(dto, operator);
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "发电计划创建成功");
        result.put("data", plan);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "取消发电计划")
    @PutMapping("/{planId}/cancel")
    public ResponseEntity<Map<String, Object>> cancelPlan(
            @Parameter(description = "计划ID") @PathVariable Long planId,
            @RequestParam(required = false) String reason,
            @RequestHeader(value = "X-User", defaultValue = "dispatch") String operator) {
        GenerationPlan plan = generationPlanService.cancelPlan(planId, reason, operator);
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "发电计划已取消");
        result.put("data", plan);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "查询发电计划列表")
    @GetMapping
    public ResponseEntity<Map<String, Object>> listPlans(
            @Parameter(description = "机组ID") @RequestParam(required = false) Long turbineId,
            @Parameter(description = "计划状态") @RequestParam(required = false) String status) {
        List<GenerationPlan> plans = generationPlanService.listPlans(turbineId, status);
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "查询成功");
        result.put("data", plans);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "查询发电计划详情")
    @GetMapping("/{planId}")
    public ResponseEntity<Map<String, Object>> getPlanDetail(
            @Parameter(description = "计划ID") @PathVariable Long planId) {
        GenerationPlan plan = generationPlanService.getPlanDetail(planId);
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "查询成功");
        result.put("data", plan);
        return ResponseEntity.ok(result);
    }
}
