/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.turn.entity.stschedule;

import com.thinkgem.jeesite.modules.turn.ReqTimeUnit;
import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 排班-规培调度表Entity
 * @author Carrel
 * @version 2017-07-29
 */
public class TurnStSchedule extends DataEntity<TurnStSchedule> {
	
	private static final long serialVersionUID = 1L;
	private String archiveId;		// 所属存档id
	private String user;		// 用户id
	private String userName;		// 用户名
	private String requirementId;		// 所属标准id
	private String depId;		// 科室id
	private String depName;		// 科室名
	private String startInt;		// 开始时间半月整数
	private String endInt;		// 结束时间半月整数

    private String startYandM;  //开始年月，字符串形式:Y-M，Y-M-上，以及某个周一的年月日
    private String endYandM;  //开始年月，字符串形式:Y-M，Y-M-上，以及某个周一的年月日
    private String oughtTimeLength; //应该时长：X个半月/X个月/X个五周
    private String reqStartYAndM; //该调度所属的标准的开始时间
    private String reqEndYAndM; //该调度所属的标准的结束时间

    public String getId(){
        return id;
    }
    private String startMonthUpOrDown;

    public String getStartMonthUpOrDown() {
        return startMonthUpOrDown;
    }

    public void setStartMonthUpOrDown(String startMonthUpOrDown) {
        this.startMonthUpOrDown = startMonthUpOrDown;
    }

    public String getEndMonthUpOrDown() {
        return endMonthUpOrDown;
    }

    public void setEndMonthUpOrDown(String endMonthUpOrDown) {
        this.endMonthUpOrDown = endMonthUpOrDown;
    }

    private String endMonthUpOrDown;
    public String getTimeUnit() {
        return timeUnit;
    }

    public void setTimeUnit(String timeUnit) {
        this.timeUnit = timeUnit;
    }

    private String timeUnit;

	public TurnStSchedule() {
		super();
	}

	public TurnStSchedule(String id){
		super(id);
	}

	@Length(min=1, max=64, message="所属存档id长度必须介于 1 和 64 之间")
	public String getArchiveId() {
		return archiveId;
	}

	public void setArchiveId(String archiveId) {
		this.archiveId = archiveId;
	}
	
	@Length(min=1, max=64, message="用户id长度必须介于 1 和 64 之间")
	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}
	
	@Length(min=1, max=64, message="用户名长度必须介于 1 和 64 之间")
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	@Length(min=1, max=64, message="所属标准id长度必须介于 1 和 64 之间")
	public String getRequirementId() {
		return requirementId;
	}

	public void setRequirementId(String requirementId) {
		this.requirementId = requirementId;
	}
	
	@Length(min=1, max=64, message="科室id长度必须介于 1 和 64 之间")
	public String getDepId() {
		return depId;
	}

	public void setDepId(String depId) {
		this.depId = depId;
	}
	
	@Length(min=1, max=64, message="科室名长度必须介于 1 和 64 之间")
	public String getDepName() {
		return depName;
	}

	public void setDepName(String depName) {
		this.depName = depName;
	}
	
	@Length(min=1, max=64, message="开始时间半月整数长度必须介于 1 和 64 之间")
	public String getStartInt() {
		return startInt;
	}

    public int getStartIntInt() {
        return Integer.valueOf(startInt);
    }

    public int getEndIntInt() {
        return Integer.valueOf(endInt);
    }

	public void setStartInt(String startInt) {
		this.startInt = startInt;
	}
	
	@Length(min=1, max=64, message="结束时间半月整数长度必须介于 1 和 64 之间")
	public String getEndInt() {
		return endInt;
	}

	public void setEndInt(String endInt) {
		this.endInt = endInt;
	}

    public String getStartYandM() {
        return startYandM;
    }

    public void setStartYandM(String startYandM) {
        this.startYandM = startYandM;
    }

    public String getEndYandM() {
        return endYandM;
    }

    public void setEndYandM(String endYandM) {
        this.endYandM = endYandM;
    }

    public String getOughtTimeLength() {
        return oughtTimeLength;
    }

    public void setOughtTimeLength(String oughtTimeLength) {
        this.oughtTimeLength = oughtTimeLength;
    }

    public String getReqStartYAndM() {
        return reqStartYAndM;
    }

    public void setReqStartYAndM(String reqStartYAndM) {
        this.reqStartYAndM = reqStartYAndM;
    }

    public String getReqEndYAndM() {
        return reqEndYAndM;
    }

    public void setReqEndYAndM(String reqEndYAndM) {
        this.reqEndYAndM = reqEndYAndM;
    }
}