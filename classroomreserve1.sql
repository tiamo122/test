CREATE TABLE booking (
    bk_bd_name VARCHAR(255),
    bk_pson_name VARCHAR(255),
    bk_reason VARCHAR(255),
    bk_time DATE,
    bk_pson_id VARCHAR(255),
    bk_bd_section VARCHAR(255),
    bk_bd_state VARCHAR(255),
    les_name VARCHAR(255),
    tea_name VARCHAR(255),
    class_name VARCHAR(255),
    bk_refused_reason VARCHAR(255)
);

-- 插入 一些数据到 booking 表


INSERT INTO booking (bk_bd_name, bk_pson_name, bk_reason, bk_time, bk_pson_id, bk_bd_section, bk_bd_state, les_name, tea_name, class_name, bk_refused_reason) VALUES
('西十二N101', '马冬梅', '学院活动', '2025-06-09', 'sa001', '1-2', '已审核', NULL, NULL, NULL, NULL),
('西十二N101', '王五', '教学任务', '2025-06-09', 'te001', '3-4', '已审核', '线性代数', '王五', '物流2201班', NULL);

-- 创建 buildings 表
CREATE TABLE buildings (
    build_name VARCHAR(255),
    build_state VARCHAR(255),
    build_time DATE,
    build_section VARCHAR(255),
    build_capacity VARCHAR(255)
);

CREATE TABLE users (
    id VARCHAR(255),
    password VARCHAR(255),
    name VARCHAR(255),
    role VARCHAR(255),
    email VARCHAR(255),
    classname VARCHAR(255)
);


INSERT INTO users (id, password, name, role, email, classname) VALUES
('sa001', '123456', '马冬梅', '0', '12345678@qq.com',NULL),
('st001', '123456', '张三', '1', 'awsdjkl@qq.com','信管2201班'),
('te001', '123456', '王五', '2', '6666666@qq.com',NULL);
