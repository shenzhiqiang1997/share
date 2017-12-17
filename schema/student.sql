USE daily;
CREATE TABLE student(
  `student_id` CHAR(13) COMMENT '学号',
  `name` VARCHAR(20) COMMENT '姓名',
  `sex` INT COMMENT '性别',
  `major` VARCHAR(20) COMMENT '专业',
  `contact_way` VARCHAR(20) COMMENT '联系方式',
  PRIMARY KEY student_pk(student_id)
)ENGINE=InnoDB DEFAULT CHARSET=UTF8 COMMENT '学生表';