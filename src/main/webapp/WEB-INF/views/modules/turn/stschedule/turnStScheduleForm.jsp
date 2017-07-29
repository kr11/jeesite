<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>排班-规培调度表管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			//$("#name").focus();
			$("#inputForm").validate({
				submitHandler: function(form){
					loading('正在提交，请稍等...');
					form.submit();
				},
				errorContainer: "#messageBox",
				errorPlacement: function(error, element) {
					$("#messageBox").text("输入有误，请先更正。");
					if (element.is(":checkbox")||element.is(":radio")||element.parent().is(".input-append")){
						error.appendTo(element.parent().parent());
					} else {
						error.insertAfter(element);
					}
				}
			});
		});
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/turn/stschedule/turnStSchedule/">排班-规培调度表列表</a></li>
		<li class="active"><a href="${ctx}/turn/stschedule/turnStSchedule/form?id=${turnStSchedule.id}">排班-规培调度表<shiro:hasPermission name="turn:stschedule:turnStSchedule:edit">${not empty turnStSchedule.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="turn:stschedule:turnStSchedule:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="turnStSchedule" action="${ctx}/turn/stschedule/turnStSchedule/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>		
		<div class="control-group">
            <input id="id" name="id" type="hidden" value="${turnStSchedule.id}"/>
            <input id="user" name="user" type="hidden" value="${turnStSchedule.user}"/>
            <label class="control-label">用户名：</label>
			<div class="controls">
				<form:input path="userName" htmlEscape="false" value="${turnStSchedule.userName}"
                            class="input-xlarge required" readonly="readonly"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">科室名：</label>
            <div class="controls">
                <form:input path="depName" htmlEscape="false" value="${turnStSchedule.depName}"
                            class="input-xlarge required" readonly="readonly"/>
                <span class="help-inline"><font color="red">*</font> </span>
            </div>
		</div>
		<div class="control-group">
			<label class="control-label">开始时间（YYYY-MM-F/L）：</label>
			<div class="controls">
				<form:input path="startInt" htmlEscape="false" maxlength="64" class="input-xlarge required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">结束时间（YYYY-MM-F/L）：</label>
			<div class="controls">
				<form:input path="endInt" htmlEscape="false" maxlength="64" class="input-xlarge required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">备注信息：</label>
			<div class="controls">
				<form:textarea path="remarks" htmlEscape="false" rows="4" maxlength="255" class="input-xxlarge "/>
			</div>
		</div>
		<div class="form-actions">
			<shiro:hasPermission name="turn:stschedule:turnStSchedule:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>