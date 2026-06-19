-- 风电场叶片缺陷复核系统 - 数据库初始化脚本
-- 本脚本仅用于 H2 内存数据库初始化

-- 创建机组表
CREATE TABLE IF NOT EXISTS wind_turbine (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    turbine_code VARCHAR(50) NOT NULL UNIQUE,
    turbine_name VARCHAR(100),
    wind_farm VARCHAR(100),
    capacity_kw DECIMAL(10,2),
    blade_count INT,
    tower_height DECIMAL(10,2),
    status VARCHAR(20),
    location_desc VARCHAR(255),
    remark VARCHAR(500),
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    create_by VARCHAR(50),
    update_by VARCHAR(50)
);

-- 缺陷记录表
CREATE TABLE IF NOT EXISTS defect_record (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    defect_code VARCHAR(50) UNIQUE,
    turbine_id BIGINT NOT NULL,
    blade_number INT NOT NULL,
    blade_position VARCHAR(50),
    defect_description VARCHAR(1000),
    defect_type VARCHAR(30),
    status VARCHAR(30),
    severity_level VARCHAR(20),
    defect_size DECIMAL(10,2),
    size_unit VARCHAR(10),
    photo_urls VARCHAR(1000),
    inspection_time TIMESTAMP,
    inspector VARCHAR(50),
    wind_speed_inspection DECIMAL(5,2),
    reshoot_count INT NOT NULL DEFAULT 0,
    need_reshoot BOOLEAN NOT NULL DEFAULT FALSE,
    evaluation_opinion VARCHAR(1000),
    evaluator VARCHAR(50),
    evaluation_time TIMESTAMP,
    review_conclusion VARCHAR(30),
    review_opinion VARCHAR(1000),
    reviewer VARCHAR(50),
    review_time TIMESTAMP,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    create_by VARCHAR(50),
    update_by VARCHAR(50)
);

-- 复拍记录表
CREATE TABLE IF NOT EXISTS reshoot_record (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    reshoot_code VARCHAR(50) UNIQUE,
    defect_id BIGINT NOT NULL,
    turbine_id BIGINT NOT NULL,
    reshoot_reason VARCHAR(500),
    scheduled_time TIMESTAMP,
    actual_time TIMESTAMP,
    wind_speed_scheduled DECIMAL(5,2),
    wind_speed_actual DECIMAL(5,2),
    photo_urls VARCHAR(1000),
    reshoot_result VARCHAR(1000),
    reshoot_operator VARCHAR(50),
    reshoot_order INT,
    is_completed BOOLEAN NOT NULL DEFAULT FALSE,
    remark VARCHAR(500),
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    create_by VARCHAR(50),
    update_by VARCHAR(50)
);

-- 检修窗口表
CREATE TABLE IF NOT EXISTS maintenance_window (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    window_code VARCHAR(50) UNIQUE,
    defect_id BIGINT,
    turbine_id BIGINT NOT NULL,
    planned_start_time TIMESTAMP,
    planned_end_time TIMESTAMP,
    actual_start_time TIMESTAMP,
    actual_end_time TIMESTAMP,
    expected_wind_speed DECIMAL(5,2),
    actual_wind_speed DECIMAL(5,2),
    status VARCHAR(20),
    window_type VARCHAR(20),
    maintenance_content VARCHAR(1000),
    maintenance_team VARCHAR(100),
    evaluator VARCHAR(50),
    evaluation_time TIMESTAMP,
    evaluation_opinion VARCHAR(1000),
    remark VARCHAR(500),
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    create_by VARCHAR(50),
    update_by VARCHAR(50)
);

-- 停机记录表
CREATE TABLE IF NOT EXISTS outage_record (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    outage_code VARCHAR(50) UNIQUE,
    turbine_id BIGINT NOT NULL,
    defect_id BIGINT,
    window_id BIGINT,
    turbine_status_before VARCHAR(20),
    turbine_status_after VARCHAR(20),
    outage_reason VARCHAR(500),
    planned_start_time TIMESTAMP,
    planned_end_time TIMESTAMP,
    actual_start_time TIMESTAMP,
    actual_end_time TIMESTAMP,
    is_active BOOLEAN NOT NULL DEFAULT FALSE,
    operator VARCHAR(50),
    approver VARCHAR(50),
    approval_time TIMESTAMP,
    remark VARCHAR(500),
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    create_by VARCHAR(50),
    update_by VARCHAR(50)
);

-- 发电计划表
CREATE TABLE IF NOT EXISTS generation_plan (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    plan_code VARCHAR(50) UNIQUE,
    turbine_id BIGINT NOT NULL,
    plan_date DATE,
    planned_output DECIMAL(10,2),
    planned_hours DECIMAL(5,2),
    status VARCHAR(20),
    cancel_reason VARCHAR(500),
    cancel_time TIMESTAMP,
    actual_output DECIMAL(10,2),
    remark VARCHAR(500),
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    create_by VARCHAR(50),
    update_by VARCHAR(50)
);

-- 创建索引
CREATE INDEX IF NOT EXISTS idx_defect_turbine ON defect_record(turbine_id);
CREATE INDEX IF NOT EXISTS idx_defect_status ON defect_record(status);
CREATE INDEX IF NOT EXISTS idx_reshoot_defect ON reshoot_record(defect_id);
CREATE INDEX IF NOT EXISTS idx_reshoot_turbine ON reshoot_record(turbine_id);
CREATE INDEX IF NOT EXISTS idx_window_turbine ON maintenance_window(turbine_id);
CREATE INDEX IF NOT EXISTS idx_window_status ON maintenance_window(status);
CREATE INDEX IF NOT EXISTS idx_outage_turbine ON outage_record(turbine_id);
CREATE INDEX IF NOT EXISTS idx_outage_active ON outage_record(is_active);
CREATE INDEX IF NOT EXISTS idx_plan_turbine ON generation_plan(turbine_id);
CREATE INDEX IF NOT EXISTS idx_plan_status ON generation_plan(status);
