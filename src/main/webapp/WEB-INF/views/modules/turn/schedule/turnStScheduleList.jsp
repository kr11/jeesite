<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>排班-规培调度表管理</title>
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
		<li class="active"><a href="${ctx}/turn/schedule/turnStSchedule/">排班-规培调度表列表</a></li>
		<shiro:hasPermission name="turn:schedule:turnStSchedule:edit"><li><a href="${ctx}/turn/schedule/turnStSchedule/form">排班-规培调度表添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="turnStSchedule" action="${ctx}/turn/schedule/turnStSchedule/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>用户名：</label>
				<form:input path="userName" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label>科室名：</label>
				<form:input path="depName" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>用户名</th>
				<th>科室名</th>
				<th>开始时间半月整数</th>
				<th>结束时间半月整数</th>
				<shiro:hasPermission name="turn:schedule:turnStSchedule:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="turnStSchedule">
			<tr>
				<td><a href="${ctx}/turn/schedule/turnStSchedule/form?id=${turnStSchedule.id}">
					${turnStSchedule.userName}
				</a></td>
				<td>
					${turnStSchedule.depName}
				</td>
				<td>
					${turnStSchedule.startInt}
				</td>
				<td>
					${turnStSchedule.endInt}
				</td>
				<shiro:hasPermission name="turn:schedule:turnStSchedule:edit"><td>
    				<a href="${ctx}/turn/schedule/turnStSchedule/form?id=${turnStSchedule.id}">修改</a>
					<a href="${ctx}/turn/schedule/turnStSchedule/delete?id=${turnStSchedule.id}" onclick="return confirmx('确认要删除该排班-规培调度表吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>