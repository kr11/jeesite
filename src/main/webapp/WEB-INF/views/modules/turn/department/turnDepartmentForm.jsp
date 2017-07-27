<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>科室信息表管理</title>
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
		<li><a href="${ctx}/turn/department/turnDepartment/">科室信息表列表</a></li>
		<li class="active"><a href="${ctx}/turn/department/turnDepartment/form?id=${turnDepartment.id}">科室信息表<shiro:hasPermission name="turn:department:turnDepartment:edit">${not empty turnDepartment.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="turn:department:turnDepartment:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="turnDepartment" action="${ctx}/turn/department/turnDepartment/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>		
		<div class="control-group">
			<label class="control-label">科室名：</label>
			<div class="controls">
				<form:input path="departmentName" htmlEscape="false" maxlength="64" class="input-xlarge required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">所属大类：</label>
			<div class="controls">
				<form:select path="practiceClass" class="input-xlarge required">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('turn_practice_class')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">是否启用：</label>
			<div class="controls">
				<form:select path="isUsed" class="input-xlarge required">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">可换科室：</label>
			<div class="controls">
				<%--<form:input path="exchangeDepartmentId" htmlEscape="false" maxlength="64" class="input-xlarge "/>--%>
				<form:select path="exchangeDepartmentId" class="input-xlarge">
					<form:options items="${departmentList}" itemLabel="departmentName" itemValue="exchangeDepartmentId" htmlEscape="false"/>
				</form:select>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">科室现名：</label>
			<div class="controls">
				<%--<form:input path="nowAliasName" htmlEscape="false" maxlength="64" class="input-xlarge "/>--%>
				<form:select path="nowAliasName" class="input-xlarge">
					<form:options items="${departmentList}" itemLabel="departmentName" itemValue="nowAliasName" htmlEscape="false"/>
				</form:select>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">备注信息：</label>
			<div class="controls">
				<form:textarea path="remarks" htmlEscape="false" rows="4" maxlength="255" class="input-xxlarge "/>
			</div>
		</div>
		<div class="form-actions">
			<shiro:hasPermission name="turn:department:turnDepartment:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>