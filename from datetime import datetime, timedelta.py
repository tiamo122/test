from datetime import datetime, timedelta 
 
# 当前时间 
current_date = datetime(2025, 4, 22)  # 根据你的当前时间设定 
 
# 教室信息和容量配置 
classrooms = {
    # 东九楼A系列(容量50)
    '东九楼A101': 50, '东九楼A102': 50, '东九楼A103': 50,
    '东九楼A201': 50, '东九楼A301': 50, '东九楼A302': 50,
    '东九楼A303': 50, '东九楼A401': 50, '东九楼A402': 50,
    '东九楼A403': 50, '东九楼A501': 50, '东九楼A502': 50,
    '东九楼A503': 50,
    
    # 东九楼B系列(容量100)
    '东九楼B101': 100, '东九楼B201': 100, '东九楼B202': 100,
    '东九楼B203': 100,
    
    # 东九楼C系列(容量200)
    '东九楼C101': 200, '东九楼C201': 200, '东九楼C202': 200,
    '东九楼C203': 200,
    
    # 东九楼D系列(容量150)
    '东九楼D101': 150, '东九楼D201': 150, '东九楼D202': 150,
    '东九楼D203': 150,
    
    # 西十二系列(容量150)
    '西十二N101': 150, '西十二S101': 150, '西十二N201': 150,
    '西十二N202': 150, '西十二N203': 150, '西十二S201': 150 
}
 
# 所有时间段 
time_sections = ['1-2', '3-4', '5-6', '7-8', '9-10', '11-12']
 
# 生成一个月的数据 
start_date = current_date -timedelta(days=21)
end_date = current_date
 
# 生成INSERT语句 
insert_statements = []
current_date = start_date 
while current_date <= end_date:
    for room, capacity in classrooms.items():  
        for section in time_sections:
            insert_sql = f"INSERT INTO buildings (build_name, build_state, build_time, build_section, build_capacity) VALUES ('{room}', 0, '{current_date.strftime('%Y-%m-%d')}',  '{section}', {capacity});"
            insert_statements.append(insert_sql) 
    current_date += timedelta(days=1)
 
# 输出生成的SQL语句 
for sql in insert_statements[:10]:  # 只打印前10条作为示例 
    print(sql)
 
print(f"\n共生成 {len(insert_statements)} 条INSERT语句")
 
# 可选：将SQL语句写入文件 
with open('classroom_inserts1.sql',  'w', encoding='utf-8') as f:
    f.write('\n'.join(insert_statements)) 
    print("SQL语句已写入 classroom_inserts.sql  文件")