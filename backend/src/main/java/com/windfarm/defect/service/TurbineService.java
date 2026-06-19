package com.windfarm.defect.service;

import com.windfarm.defect.entity.WindTurbine;
import com.windfarm.defect.enums.TurbineStatus;
import com.windfarm.defect.exception.BusinessException;
import com.windfarm.defect.repository.WindTurbineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TurbineService {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(TurbineService.class);

    @Autowired
    private WindTurbineRepository windTurbineRepository;

    public List<WindTurbine> listTurbines(String windFarm, TurbineStatus status) {
        if (windFarm != null) {
            return windTurbineRepository.findByWindFarmAndIsDeletedFalse(windFarm);
        } else if (status != null) {
            return windTurbineRepository.findByStatusAndIsDeletedFalse(status);
        }
        return windTurbineRepository.findByIsDeletedFalse();
    }

    public WindTurbine getTurbineById(Long id) {
        return windTurbineRepository.findById(id)
                .filter(t -> !t.getIsDeleted())
                .orElseThrow(() -> new BusinessException("机组不存在"));
    }

    public WindTurbine getTurbineByCode(String turbineCode) {
        return windTurbineRepository.findByTurbineCodeAndIsDeletedFalse(turbineCode)
                .orElseThrow(() -> new BusinessException("机组不存在"));
    }

    @Transactional
    public WindTurbine createTurbine(WindTurbine turbine, String operator) {
        if (windTurbineRepository.existsByTurbineCodeAndIsDeletedFalse(turbine.getTurbineCode())) {
            throw new BusinessException("机组编号已存在");
        }
        turbine.setIsDeleted(false);
        turbine.setCreateBy(operator);
        turbine.setUpdateBy(operator);
        if (turbine.getStatus() == null) {
            turbine.setStatus(TurbineStatus.RUNNING);
        }
        WindTurbine saved = windTurbineRepository.save(turbine);
        log.info("机组创建成功，机组编号: {}", saved.getTurbineCode());
        return saved;
    }

    @Transactional
    public WindTurbine updateTurbine(Long id, WindTurbine turbine, String operator) {
        WindTurbine existing = getTurbineById(id);

        existing.setTurbineName(turbine.getTurbineName());
        existing.setWindFarm(turbine.getWindFarm());
        existing.setCapacityKw(turbine.getCapacityKw());
        existing.setBladeCount(turbine.getBladeCount());
        existing.setTowerHeight(turbine.getTowerHeight());
        existing.setLocationDesc(turbine.getLocationDesc());
        existing.setRemark(turbine.getRemark());
        existing.setUpdateBy(operator);

        WindTurbine saved = windTurbineRepository.save(existing);
        log.info("机组更新成功，机组ID: {}", id);
        return saved;
    }

    @Transactional
    public void deleteTurbine(Long id, String operator) {
        WindTurbine turbine = getTurbineById(id);
        turbine.setIsDeleted(true);
        turbine.setUpdateBy(operator);
        windTurbineRepository.save(turbine);
        log.info("机组已删除，机组ID: {}", id);
    }

    @Transactional
    public WindTurbine updateStatus(Long id, TurbineStatus status, String operator) {
        WindTurbine turbine = getTurbineById(id);
        turbine.setStatus(status);
        turbine.setUpdateBy(operator);
        WindTurbine saved = windTurbineRepository.save(turbine);
        log.info("机组状态更新为: {}, 机组ID: {}", status.getDescription(), id);
        return saved;
    }
}
