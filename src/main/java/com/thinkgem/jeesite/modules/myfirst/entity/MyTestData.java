/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.myfirst.entity;

import com.thinkgem.jeesite.modules.sys.entity.User;
import org.hibernate.validator.constraints.Length;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 尝试性的用户Entity
 * @author Carrel
 * @version 2017-07-19
 */
public class MyTestData extends DataEntity<MyTestData> {
	
	private static final long serialVersionUID = 1L;
	//其实我们的字段完全是我们自己添加的部分，id和后面六个更新创建时间等等不会生成在这里
	//我们这里添加的字段就是user，Name，sex，standard和inDate
	private User user;		// 学号
	private String userName;		// 姓名
	private String sex;		// 性别（字典类型：sex）
	private String standard;		// 标准
	private Date inDate;		// 加入日期
	//有这两行是因为我们的查询方式是between
	private Date beginInDate;		// 开始 加入日期
	private Date endInDate;		// 结束 加入日期
	
	public MyTestData() {
		super();
	}

	public MyTestData(String id){
		super(id);
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	@Length(min=0, max=64, message="姓名长度必须介于 0 和 64 之间")
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	@Length(min=0, max=1, message="性别（字典类型：sex）长度必须介于 0 和 1 之间")
	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}
	
	@Length(min=0, max=64, message="标准长度必须介于 0 和 64 之间")
	public String getStandard() {
		return standard;
	}

	public void setStandard(String standard) {
		this.standard = standard;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getInDate() {
		return inDate;
	}

	public void setInDate(Date inDate) {
		this.inDate = inDate;
	}
	
	public Date getBeginInDate() {
		return beginInDate;
	}

	public void setBeginInDate(Date beginInDate) {
		this.beginInDate = beginInDate;
	}
	
	public Date getEndInDate() {
		return endInDate;
	}

	public void setEndInDate(Date endInDate) {
		this.endInDate = endInDate;
	}
		
}