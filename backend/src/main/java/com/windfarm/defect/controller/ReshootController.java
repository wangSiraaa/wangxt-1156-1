package com.windfarm.defect.controller;

import com.windfarm.defect.dto.ReshootCompleteDTO;
import com.windfarm.defect.dto.ReshootCreateDTO;
import com.windfarm.defect.entity.ReshootRecord;
import com.windfarm.defect.service.ReshootService;
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

@Tag(name = "复拍管理", description = "缺陷复拍任务管理接口")
@RestController
@RequestMapping("/reshoots")
public class ReshootController {

    @Autowired
    private ReshootService reshootService;

    @Operation(summary = "创建复拍任务")
    @PostMapping
    public ResponseEntity<Map<String, Object>> createReshoot(
            @Valid @RequestBody ReshootCreateDTO dto,
            @RequestHeader(value = "X-User", defaultValue = "maintenance_team") String operator) {
        ReshootRecord reshoot = reshootService.createReshoot(dto, operator);
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "复拍任务创建成功");
        result.put("data", reshoot);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "完成复拍任务")
    @PutMapping("/{reshootId}/complete")
    public ResponseEntity<Map<String, Object>> completeReshoot(
            @Parameter(description = "复拍ID") @PathVariable Long reshootId,
            @RequestBody ReshootCompleteDTO dto,
            @RequestHeader(value = "X-User", defaultValue = "drone_inspector") String operator) {
        ReshootRecord reshoot = reshootService.completeReshoot(reshootId, dto, operator);
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "复拍完成");
        result.put("data", reshoot);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "查询复拍列表")
    @GetMapping
    public ResponseEntity<Map<String, Object>> listReshoots(
            @Parameter(description = "缺陷ID") @RequestParam(required = false) Long defectId,
            @Parameter(description = "机组ID") @RequestParam(required = false) Long turbineId) {
        List<ReshootRecord> reshoots = reshootService.listReshoots(defectId, turbineId);
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "查询成功");
        result.put("data", reshoots);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "查询复拍详情")
    @GetMapping("/{reshootId}")
    public ResponseEntity<Map<String, Object>> getReshootDetail(
            @Parameter(description = "复拍ID") @PathVariable Long reshootId) {
        ReshootRecord reshoot = reshootService.getReshootDetail(reshootId);
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "查询成功");
        result.put("data", reshoot);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "删除复拍任务")
    @DeleteMapping("/{reshootId}")
    public ResponseEntity<Map<String, Object>> deleteReshoot(
            @Parameter(description = "复拍ID") @PathVariable Long reshootId,
            @RequestHeader(value = "X-User", defaultValue = "admin") String operator) {
        reshootService.deleteReshoot(reshootId, operator);
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "删除成功");
        return ResponseEntity.ok(result);
    }
}
