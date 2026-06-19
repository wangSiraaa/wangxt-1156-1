package com.windfarm.defect.controller;

import com.windfarm.defect.entity.WindTurbine;
import com.windfarm.defect.enums.TurbineStatus;
import com.windfarm.defect.service.TurbineService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Tag(name = "机组管理", description = "风电机组管理接口")
@RestController
@RequestMapping("/turbines")
public class TurbineController {

    @Autowired
    private TurbineService turbineService;

    @Operation(summary = "查询机组列表")
    @GetMapping
    public ResponseEntity<Map<String, Object>> listTurbines(
            @Parameter(description = "风电场") @RequestParam(required = false) String windFarm,
            @Parameter(description = "机组状态") @RequestParam(required = false) TurbineStatus status) {
        List<WindTurbine> turbines = turbineService.listTurbines(windFarm, status);
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "查询成功");
        result.put("data", turbines);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "查询机组详情")
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getTurbineById(
            @Parameter(description = "机组ID") @PathVariable Long id) {
        WindTurbine turbine = turbineService.getTurbineById(id);
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "查询成功");
        result.put("data", turbine);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "根据编号查询机组")
    @GetMapping("/code/{turbineCode}")
    public ResponseEntity<Map<String, Object>> getTurbineByCode(
            @Parameter(description = "机组编号") @PathVariable String turbineCode) {
        WindTurbine turbine = turbineService.getTurbineByCode(turbineCode);
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "查询成功");
        result.put("data", turbine);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "创建机组")
    @PostMapping
    public ResponseEntity<Map<String, Object>> createTurbine(
            @RequestBody WindTurbine turbine,
            @RequestHeader(value = "X-User", defaultValue = "admin") String operator) {
        WindTurbine saved = turbineService.createTurbine(turbine, operator);
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "创建成功");
        result.put("data", saved);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "更新机组")
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateTurbine(
            @Parameter(description = "机组ID") @PathVariable Long id,
            @RequestBody WindTurbine turbine,
            @RequestHeader(value = "X-User", defaultValue = "admin") String operator) {
        WindTurbine saved = turbineService.updateTurbine(id, turbine, operator);
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "更新成功");
        result.put("data", saved);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "删除机组")
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteTurbine(
            @Parameter(description = "机组ID") @PathVariable Long id,
            @RequestHeader(value = "X-User", defaultValue = "admin") String operator) {
        turbineService.deleteTurbine(id, operator);
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "删除成功");
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "更新机组状态")
    @PutMapping("/{id}/status")
    public ResponseEntity<Map<String, Object>> updateStatus(
            @Parameter(description = "机组ID") @PathVariable Long id,
            @Parameter(description = "状态") @RequestParam TurbineStatus status,
            @RequestHeader(value = "X-User", defaultValue = "admin") String operator) {
        WindTurbine turbine = turbineService.updateStatus(id, status, operator);
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "状态更新成功");
        result.put("data", turbine);
        return ResponseEntity.ok(result);
    }
}
