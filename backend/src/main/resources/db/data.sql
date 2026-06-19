INSERT INTO wind_turbine (turbine_code, turbine_name, wind_farm, capacity_kw, blade_count, tower_height, status, location_desc, is_deleted, create_by, update_by)
VALUES
('WT-001', '1号风机', '东风水电场', 2500.00, 3, 80.00, 'RUNNING', 'A区1号机位', FALSE, 'system', 'system'),
('WT-002', '2号风机', '东风水电场', 2500.00, 3, 80.00, 'RUNNING', 'A区2号机位', FALSE, 'system', 'system'),
('WT-003', '3号风机', '东风水电场', 2500.00, 3, 80.00, 'STOPPED', 'A区3号机位', FALSE, 'system', 'system'),
('WT-004', '4号风机', '东风水电场', 3000.00, 3, 90.00, 'RUNNING', 'B区1号机位', FALSE, 'system', 'system'),
('WT-005', '5号风机', '西阳风电场', 2000.00, 3, 75.00, 'RUNNING', 'C区1号机位', FALSE, 'system', 'system');

INSERT INTO defect_record (defect_code, turbine_id, blade_number, blade_position, defect_description, defect_type, status, severity_level, defect_size, size_unit, photo_urls, inspection_time, inspector, wind_speed_inspection, reshoot_count, need_reshoot, evaluation_opinion, evaluator, evaluation_time, review_conclusion, review_opinion, reviewer, review_time, is_deleted, create_by, update_by)
VALUES
('DEF202606190001', 1, 1, '叶片前缘中部', '发现疑似裂纹，长度约30cm', 'CRACK_SUSPECTED', 'PENDING_RESHOOT', 'HIGH', 30.00, 'cm', '/photos/def1_1.jpg,/photos/def1_2.jpg', TIMESTAMP '2026-06-18 10:30:00', 'drone01', 8.5, 0, TRUE, NULL, NULL, NULL, NULL, NULL, NULL, NULL, FALSE, 'drone01', 'drone01'),
('DEF202606190002', 2, 2, '叶片叶尖', '叶片表面腐蚀，面积约0.5平方米', 'CORROSION', 'PENDING_EVALUATION', 'MEDIUM', 0.50, 'm2', '/photos/def2_1.jpg', TIMESTAMP '2026-06-18 14:20:00', 'drone01', 6.2, 0, FALSE, NULL, NULL, NULL, NULL, NULL, NULL, NULL, FALSE, 'drone01', 'drone01'),
('DEF202606190003', 1, 3, '叶片后根部', '表面磨损，涂层脱落', 'ABRASION', 'PENDING_MAINTENANCE', 'LOW', 15.00, 'cm', '/photos/def3_1.jpg,/photos/def3_2.jpg', TIMESTAMP '2026-06-17 09:15:00', 'drone02', 7.0, 0, FALSE, '磨损程度较轻，建议下次例行检修时处理', 'engineer01', TIMESTAMP '2026-06-17 15:00:00', NULL, NULL, NULL, NULL, FALSE, 'drone02', 'engineer01'),
('DEF202606190004', 3, 1, '叶片前缘尖部', '雷击损伤，约20cm范围', 'LIGHTNING_STRIKE', 'CONFIRMED', 'CRITICAL', 20.00, 'cm', '/photos/def4_1.jpg', TIMESTAMP '2026-06-16 11:00:00', 'drone01', 10.0, 1, TRUE, '雷击损伤严重，需立即停机检修', 'engineer01', TIMESTAMP '2026-06-16 16:30:00', 'NEED_IMMEDIATE_MAINTENANCE', '确认需立即停机检修，已安排检修窗口', 'manager01', TIMESTAMP '2026-06-16 18:00:00', FALSE, 'drone01', 'manager01');

INSERT INTO reshoot_record (reshoot_code, defect_id, turbine_id, reshoot_reason, scheduled_time, actual_time, wind_speed_scheduled, wind_speed_actual, photo_urls, reshoot_result, reshoot_operator, reshoot_order, is_completed, remark, is_deleted, create_by, update_by)
VALUES
('RS202606190001', 4, 3, '雷击损伤确认，需近距离复拍', TIMESTAMP '2026-06-17 08:00:00', TIMESTAMP '2026-06-17 09:30:00', 5.5, 6.0, '/photos/reshoot4_1.jpg,/photos/reshoot4_2.jpg', '确认雷击损伤，范围约20cm，深度约2cm', 'drone01', 1, TRUE, '第一次复拍完成', FALSE, 'engineer01', 'drone01'),
('RS202606190002', 1, 1, '疑似裂纹需二次确认', TIMESTAMP '2026-06-20 07:00:00', NULL, 5.0, NULL, NULL, NULL, NULL, 1, FALSE, '计划明早起进行复拍', FALSE, 'engineer01', 'engineer01');

INSERT INTO maintenance_window (window_code, defect_id, turbine_id, planned_start_time, planned_end_time, actual_start_time, actual_end_time, expected_wind_speed, actual_wind_speed, status, window_type, maintenance_content, maintenance_team, evaluator, evaluation_time, evaluation_opinion, is_deleted, create_by, update_by)
VALUES
('MW202606190001', 4, 3, TIMESTAMP '2026-06-20 08:00:00', TIMESTAMP '2026-06-20 18:00:00', NULL, NULL, 6.5, NULL, 'CONFIRMED', 'EMERGENCY', '雷击损伤修复，叶片前缘修复', '检修一班', 'engineer01', TIMESTAMP '2026-06-17 10:00:00', '损伤严重需立即处理，预计1个工作日完成', FALSE, 'engineer01', 'engineer01'),
('MW202606190002', 1, 1, TIMESTAMP '2026-06-25 08:00:00', TIMESTAMP '2026-06-25 17:00:00', NULL, NULL, 7.0, NULL, 'PROPOSED', 'SCHEDULED', '疑似裂纹检查确认', '检修二班', 'engineer02', TIMESTAMP '2026-06-18 15:00:00', '待复拍确认后安排具体检修内容', FALSE, 'engineer02', 'engineer02');

INSERT INTO outage_record (outage_code, turbine_id, defect_id, window_id, turbine_status_before, turbine_status_after, outage_reason, planned_start_time, planned_end_time, actual_start_time, actual_end_time, is_active, operator, approver, approval_time, remark, is_deleted, create_by, update_by)
VALUES
('OUT202606190001', 3, 4, 1, 'RUNNING', 'STOPPED', '雷击损伤，需紧急停机检修', TIMESTAMP '2026-06-19 00:00:00', TIMESTAMP '2026-06-21 00:00:00', TIMESTAMP '2026-06-19 08:00:00', NULL, TRUE, 'operator01', 'manager01', TIMESTAMP '2026-06-19 07:30:00', '3号风机因雷击损伤停机', FALSE, 'manager01', 'manager01');

INSERT INTO generation_plan (plan_code, turbine_id, plan_date, planned_output, planned_hours, status, cancel_reason, cancel_time, actual_output, remark, is_deleted, create_by, update_by)
VALUES
('GP202606190001', 1, DATE '2026-06-20', 45000.00, 18.0, 'PUBLISHED', NULL, NULL, NULL, '6月20日发电计划', FALSE, 'dispatch01', 'dispatch01'),
('GP202606190002', 2, DATE '2026-06-20', 48000.00, 19.2, 'PUBLISHED', NULL, NULL, NULL, '6月20日发电计划', FALSE, 'dispatch01', 'dispatch01'),
('GP202606190003', 3, DATE '2026-06-20', 50000.00, 20.0, 'CANCELLED', '机组停机，发电计划自动取消', TIMESTAMP '2026-06-19 08:00:00', NULL, '3号风机停机取消发电计划', FALSE, 'dispatch01', 'system'),
('GP202606190004', 4, DATE '2026-06-20', 55000.00, 18.3, 'PUBLISHED', NULL, NULL, NULL, '6月20日发电计划', FALSE, 'dispatch01', 'dispatch01');
