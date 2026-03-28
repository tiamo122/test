-- 创建数据库
CREATE DATABASE classroombooking;
-- 使用数据库
USE classroombooking;
-- 创建 booking 表
CREATE TABLE booking (
    bk_bd_name VARCHAR(255),
    bk_pson_name VARCHAR(255),
    bk_reason VARCHAR(255),
    bk_time VARCHAR(255),
    bk_pson_id VARCHAR(255),
    bk_bd_section VARCHAR(255),
    bk_bd_state VARCHAR(255),
    les_name VARCHAR(255),
    tea_name VARCHAR(255),
    class_name VARCHAR(255),
    bk_apply_time VARCHAR(255),
    bk_refused_reason VARCHAR(255)
);
INSERT INTO booking (bk_bd_name, bk_pson_name, bk_reason, bk_time, bk_pson_id, bk_bd_section, bk_bd_state, les_name, tea_name, class_name) VALUES
('东九楼A101', '刘备', '社团活动', '2025-04-01', 'sa001', '1-2', '未审核', '微积分', '宋江', '信管2201班'),
('东九楼A102', '关羽', '学术讲座', '2025-04-02', 'stu001', '3-4', '已审核', '线性代数', '卢俊义', '物流2201班')
