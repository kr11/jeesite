SET SESSION FOREIGN_KEY_CHECKS=0;


/* Create Tables */

-- 业务数据表
CREATE TABLE turn_archive
(
	-- 编号
	id varchar(64) NOT NULL COMMENT '编号',
	-- 存档名
	archive_name varchar(64) NOT NULL COMMENT '存档名',
	is_open char(1) NOT NULL,
	-- 创建者
	create_by varchar(64) NOT NULL COMMENT '创建者',
	-- 创建时间
	create_date datetime NOT NULL COMMENT '创建时间',
	-- 更新者
	update_by varchar(64) NOT NULL COMMENT '更新者',
	-- 更新时间
	update_date datetime NOT NULL COMMENT '更新时间',
	-- 备注信息
	remarks varchar(255) COMMENT '备注信息',
	-- 删除标记（0：正常；1：删除）
	del_flag char(1) DEFAULT '0' NOT NULL COMMENT '删除标记（0：正常；1：删除）',
	PRIMARY KEY (id)
) COMMENT = '业务数据表';



/* Create Indexes */

CREATE INDEX test_data_del_flag ON turn_archive ();



SET SESSION FOREIGN_KEY_CHECKS=0;

/* Create Tables */

-- 部门表
CREATE TABLE turn_department
(
	-- 编号
	id varchar(64) NOT NULL COMMENT '编号',
	-- 所属存档id
	archive_id varchar(64) NOT NULL COMMENT '所属存档id',
	-- 科室名
	department_name varchar(64) NOT NULL COMMENT '科室名',
	-- 实习所属大类
	practice_class varchar(64) NOT NULL COMMENT '实习所属大类',
	-- 是否开启
	is_used char(1) NOT NULL COMMENT '是否开启',
	-- 可互换科室编号
	exchange_department varchar(64) NOT NULL COMMENT '可互换科室编号',
	-- 科室变迁现在名字
	now_alias varchar(64) NOT NULL COMMENT '科室变迁现在名字',
	-- 创建者
	create_by varchar(64) NOT NULL COMMENT '创建者',
	-- 创建时间
	create_date datetime NOT NULL COMMENT '创建时间',
	-- 更新者
	update_by varchar(64) NOT NULL COMMENT '更新者',
	-- 更新时间
	update_date datetime NOT NULL COMMENT '更新时间',
	-- 备注信息
	remarks varchar(255) COMMENT '备注信息',
	-- 删除标记（0：正常；1：删除）
	del_flag char(1) DEFAULT '0' NOT NULL COMMENT '删除标记（0：正常；1：删除）',
	PRIMARY KEY (id)
) COMMENT = '部门表';



/* Create Indexes */

CREATE INDEX test_data_del_flag ON turn_department ();


SET SESSION FOREIGN_KEY_CHECKS=0;


/* Create Tables */

-- 键值对表
CREATE TABLE key_map
(
	-- 编号
	id varchar(64) NOT NULL COMMENT '编号',
	-- 键
	dict_key varchar(64) NOT NULL COMMENT '键',
	-- 值
	dict_value varchar(64) NOT NULL COMMENT '值',
	-- 创建者
	create_by varchar(64) NOT NULL COMMENT '创建者',
	-- 创建时间
	create_date datetime NOT NULL COMMENT '创建时间',
	-- 更新者
	update_by varchar(64) NOT NULL COMMENT '更新者',
	-- 更新时间
	update_date datetime NOT NULL COMMENT '更新时间',
	-- 备注信息
	remarks varchar(255) COMMENT '备注信息',
	-- 删除标记（0：正常；1：删除）
	del_flag char(1) DEFAULT '0' NOT NULL COMMENT '删除标记（0：正常；1：删除）',
	PRIMARY KEY (id)
) COMMENT = '键值对表';



/* Create Indexes */

CREATE INDEX test_data_del_flag ON key_map ();



SET SESSION FOREIGN_KEY_CHECKS=0;


/* Create Tables */

-- 排班-调度数据表
CREATE TABLE turn_st_schedule
(
	-- 编号
	id varchar(64) NOT NULL COMMENT '编号',
	-- 所属存档id
	archive_id varchar(64) NOT NULL COMMENT '所属存档id',
	-- 所属表格id
	requirement_id varchar(64) NOT NULL COMMENT '所属表格id',
	-- 科室id_at_名
	dep_id_at_name varchar(64) NOT NULL COMMENT '科室id_at_名',
	-- 开始时间半月整数
	start_int int(64) unsigned NOT NULL COMMENT '开始时间半月整数',
	-- 结束时间半月整数
	end_int int(64) unsigned COMMENT '结束时间半月整数',
	-- 创建者
	create_by varchar(64) NOT NULL COMMENT '创建者',
	-- 创建时间
	create_date datetime NOT NULL COMMENT '创建时间',
	-- 更新者
	update_by varchar(64) NOT NULL COMMENT '更新者',
	-- 更新时间
	update_date datetime NOT NULL COMMENT '更新时间',
	-- 备注信息
	remarks varchar(255) COMMENT '备注信息',
	-- 删除标记（0：正常；1：删除）
	del_flag char(1) DEFAULT '0' NOT NULL COMMENT '删除标记（0：正常；1：删除）',
	PRIMARY KEY (id)
) COMMENT = '排班-调度数据表';



/* Create Indexes */

CREATE INDEX test_data_del_flag ON turn_st_schedule ();



SET SESSION FOREIGN_KEY_CHECKS=0;

/* Create Tables */

-- 排班_规培标准数据表子表
CREATE TABLE turn_standardized_requirement_dep_child
(
	-- 编号
	id varchar(64) NOT NULL COMMENT '编号',
	-- 所属标准编号
	requirement_id varchar(64) NOT NULL COMMENT '所属标准编号',
	-- 科室_id
	department_id varchar(64) NOT NULL COMMENT '科室_id',
	-- 科室名
	department_name varchar(64) NOT NULL COMMENT '科室名',
	-- 时间长度
	time_length int(64) unsigned NOT NULL COMMENT '时间长度',
	-- 创建者
	create_by varchar(64) NOT NULL COMMENT '创建者',
	-- 创建时间
	create_date datetime NOT NULL COMMENT '创建时间',
	-- 更新者
	update_by varchar(64) NOT NULL COMMENT '更新者',
	-- 更新时间
	update_date datetime NOT NULL COMMENT '更新时间',
	-- 备注信息
	remarks varchar(255) COMMENT '备注信息',
	-- 删除标记（0：正常；1：删除）
	del_flag char(1) DEFAULT '0' NOT NULL COMMENT '删除标记（0：正常；1：删除）',
	PRIMARY KEY (id)
) COMMENT = '排班_规培标准数据表子表';


-- 排班_规培标准数据表母表
CREATE TABLE turn_standardized_requirement_main
(
	-- 编号
	id varchar(64) NOT NULL COMMENT '编号',
	-- 规培标准名
	name varchar(64) NOT NULL COMMENT '规培标准名',
	-- 所属存档id
	archive_id varchar(64) NOT NULL COMMENT '所属存档id',
	total_length int(64) unsigned NOT NULL,
	time_unit varchar(64) NOT NULL,
	-- 开始年at月
	start_y_at_m varchar(64) COMMENT '开始年at月',
	-- 结束年@月
	end_y_at_m varchar(64) COMMENT '结束年@月',
	-- 创建者
	create_by varchar(64) NOT NULL COMMENT '创建者',
	-- 创建时间
	create_date datetime NOT NULL COMMENT '创建时间',
	-- 更新者
	update_by varchar(64) NOT NULL COMMENT '更新者',
	-- 更新时间
	update_date datetime NOT NULL COMMENT '更新时间',
	-- 备注信息
	remarks varchar(255) COMMENT '备注信息',
	-- 删除标记（0：正常；1：删除）
	del_flag char(1) DEFAULT '0' NOT NULL COMMENT '删除标记（0：正常；1：删除）',
	-- 标准所属基地
	req_base varchar(64) COMMENT '标准所属基地',
	PRIMARY KEY (id)
) COMMENT = '排班_规培标准数据表母表';


-- 排班_规培标准_人员子表
CREATE TABLE turn_standardized_requirement_user_child
(
	-- 编号
	id varchar(64) NOT NULL COMMENT '编号',
	-- 所属标准编号
	requirement_id varchar(64) NOT NULL COMMENT '所属标准编号',
	-- 人员系统id
	user_id varchar(64) NOT NULL COMMENT '人员系统id',
	-- 姓名
	user_name varchar(64) NOT NULL COMMENT '姓名',
	-- 性别
	sex varchar(1) NOT NULL COMMENT '性别',
	user_number varchar(64),
	-- 年级
	grade varchar(64) COMMENT '年级',
	-- 学员性质
	class varchar(64) NOT NULL COMMENT '学员性质',
	-- 大组编号
	group_id varchar(64) COMMENT '大组编号',
	-- 创建者
	create_by varchar(64) NOT NULL COMMENT '创建者',
	-- 创建时间
	create_date datetime NOT NULL COMMENT '创建时间',
	-- 更新者
	update_by varchar(64) NOT NULL COMMENT '更新者',
	-- 更新时间
	update_date datetime NOT NULL COMMENT '更新时间',
	-- 备注信息
	remarks varchar(255) COMMENT '备注信息',
	-- 删除标记（0：正常；1：删除）
	del_flag char(1) DEFAULT '0' NOT NULL COMMENT '删除标记（0：正常；1：删除）',
	PRIMARY KEY (id)
) COMMENT = '排班_规培标准_人员子表';



/* Create Foreign Keys */

ALTER TABLE turn_standardized_requirement_dep_child
	ADD FOREIGN KEY (requirement_id)
	REFERENCES turn_standardized_requirement_main (id)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;


ALTER TABLE turn_standardized_requirement_user_child
	ADD FOREIGN KEY (requirement_id)
	REFERENCES turn_standardized_requirement_main (id)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;



/* Create Indexes */

CREATE INDEX test_data_del_flag ON turn_standardized_requirement_dep_child ();
CREATE INDEX test_data_del_flag ON turn_standardized_requirement_main ();
CREATE INDEX test_data_del_flag ON turn_standardized_requirement_user_child ();


SET SESSION FOREIGN_KEY_CHECKS=0;


/* Create Tables */

-- 规培人员表
CREATE TABLE standard_user
(
	-- 编号
	id varchar(64) NOT NULL COMMENT '编号',
	-- 存档名
	archive_name varchar(64) NOT NULL COMMENT '存档名',
	-- 性别
	gender char(1) NOT NULL COMMENT '性别',
	-- 用户名
	user_id varchar(64) NOT NULL COMMENT '用户名',
	-- 学号
	student_number varchar(64) NOT NULL COMMENT '学号',
	-- 学员性质
	student_type varchar(64) NOT NULL COMMENT '学员性质',
	-- 年级
	grade varbinary(64) COMMENT '年级',
	-- 规培标准id
	requirement_id varchar(64) NOT NULL COMMENT '规培标准id',
	-- 规培标准名
	requirement_name varchar(64) NOT NULL COMMENT '规培标准名',
	-- 创建者
	create_by varchar(64) NOT NULL COMMENT '创建者',
	-- 创建时间
	create_date datetime NOT NULL COMMENT '创建时间',
	-- 更新者
	update_by varchar(64) NOT NULL COMMENT '更新者',
	-- 更新时间
	update_date datetime NOT NULL COMMENT '更新时间',
	-- 备注信息
	remarks varchar(255) COMMENT '备注信息',
	-- 删除标记（0：正常；1：删除）
	del_flag char(1) DEFAULT '0' NOT NULL COMMENT '删除标记（0：正常；1：删除）',
	PRIMARY KEY (id),
	UNIQUE (student_number)
) COMMENT = '规培人员表';



/* Create Indexes */

CREATE INDEX test_data_del_flag ON standard_user ();






