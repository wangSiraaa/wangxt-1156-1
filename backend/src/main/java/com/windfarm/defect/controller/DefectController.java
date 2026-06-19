package com.windfarm.defect.controller;

import com.windfarm.defect.dto.DefectEvaluateDTO;
import com.windfarm.defect.dto.DefectReviewDTO;
import com.windfarm.defect.dto.DefectUploadDTO;
import com.windfarm.defect.entity.DefectRecord;
import com.windfarm.defect.enums.DefectStatus;
import com.windfarm.defect.service.DefectService;
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

@Tag(name = "缺陷管理", description = "风电场叶片缺陷复核管理接口")
@RestController
@RequestMapping("/defects")
public class DefectController {

    @Autowired
    private DefectService defectService;

    @Operation(summary = "上传缺陷", description = "无人机巡检员上传叶片缺陷照片和信息")
    @PostMapping
    public ResponseEntity<Map<String, Object>> uploadDefect(
            @Valid @RequestBody DefectUploadDTO dto,
            @RequestHeader(value = "X-User", defaultValue = "drone_inspector") String operator) {
        DefectRecord defect = defectService.uploadDefect(dto, operator);
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "上传成功");
        result.put("data", defect);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "评估缺陷", description = "检修班评估缺陷，确定是否需要复拍")
    @PutMapping("/{defectId}/evaluate")
    public ResponseEntity<Map<String, Object>> evaluateDefect(
            @Parameter(description = "缺陷ID") @PathVariable Long defectId,
            @RequestBody DefectEvaluateDTO dto,
            @RequestHeader(value = "X-User", defaultValue = "maintenance_team") String evaluator) {
        DefectRecord defect = defectService.evaluateDefect(defectId, dto, evaluator);
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "评估完成");
        result.put("data", defect);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "复核缺陷", description = "场站负责人确认复核结论")
    @PutMapping("/{defectId}/review")
    public ResponseEntity<Map<String, Object>> reviewDefect(
            @Parameter(description = "缺陷ID") @PathVariable Long defectId,
            @Valid @RequestBody DefectReviewDTO dto,
            @RequestHeader(value = "X-User", defaultValue = "station_manager") String reviewer) {
        DefectRecord defect = defectService.reviewDefect(defectId, dto, reviewer);
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "复核完成");
        result.put("data", defect);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "查询缺陷列表")
    @GetMapping
    public ResponseEntity<Map<String, Object>> listDefects(
            @Parameter(description = "机组ID") @RequestParam(required = false) Long turbineId,
            @Parameter(description = "缺陷状态") @RequestParam(required = false) DefectStatus status) {
        List<DefectRecord> defects = defectService.listDefects(turbineId, status);
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "查询成功");
        result.put("data", defects);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "查询缺陷详情")
    @GetMapping("/{defectId}")
    public ResponseEntity<Map<String, Object>> getDefectDetail(
            @Parameter(description = "缺陷ID") @PathVariable Long defectId) {
        DefectRecord defect = defectService.getDefectDetail(defectId);
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "查询成功");
        result.put("data", defect);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "删除缺陷")
    @DeleteMapping("/{defectId}")
    public ResponseEntity<Map<String, Object>> deleteDefect(
            @Parameter(description = "缺陷ID") @PathVariable Long defectId,
            @RequestHeader(value = "X-User", defaultValue = "admin") String operator) {
        defectService.deleteDefect(defectId, operator);
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "删除成功");
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "获取风速阈值")
    @GetMapping("/wind-speed-threshold")
    public ResponseEntity<Map<String, Object>> getWindSpeedThreshold() {
        double threshold = defectService.getWindSpeedThreshold();
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "查询成功");
        result.put("data", Map.of("threshold", threshold));
        return ResponseEntity.ok(result);
    }
}
