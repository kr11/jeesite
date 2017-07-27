<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>科室信息表管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			
		});
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
        	return false;
        }
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/turn/department/turnDepartment/">科室信息表列表</a></li>
		<shiro:hasPermission name="turn:department:turnDepartment:edit"><li><a href="${ctx}/turn/department/turnDepartment/form">科室信息表添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="turnDepartment" action="${ctx}/turn/department/turnDepartment/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>科室名：</label>
				<form:input path="departmentName" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label>实习所属大类：</label>
				<form:select path="practiceClass" class="input-medium">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('turn_practice_class')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label>是否启用：</label>
				<form:select path="isUsed" class="input-medium">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label>可互换科室编号：</label>
				<form:input path="exchangeDepartmentId" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label>科室变迁现在名字：</label>
				<form:input path="nowAliasName" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>科室名</th>
				<th>实习所属大类</th>
				<th>是否启用</th>
				<th>可互换科室编号</th>
				<th>科室变迁现在名字</th>
				<shiro:hasPermission name="turn:department:turnDepartment:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="turnDepartment">
			<tr>
				<td><a href="${ctx}/turn/department/turnDepartment/form?id=${turnDepartment.id}">
					${turnDepartment.departmentName}
				</a></td>
				<td>
					${fns:getDictLabel(turnDepartment.practiceClass, 'turn_practice_class', '')}
				</td>
				<td>
					${fns:getDictLabel(turnDepartment.isUsed, 'yes_no', '')}
				</td>
				<td>
					${turnDepartment.exchangeDepartmentId}
				</td>
				<td>
					${turnDepartment.nowAliasName}
				</td>
				<shiro:hasPermission name="turn:department:turnDepartment:edit"><td>
    				<a href="${ctx}/turn/department/turnDepartment/form?id=${turnDepartment.id}">修改</a>
					<a href="${ctx}/turn/department/turnDepartment/delete?id=${turnDepartment.id}" onclick="return confirmx('确认要删除该科室信息表吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>