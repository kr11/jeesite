<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>复制人员子表管理</title>
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
		<li class="active"><a href="${ctx}/copy/copyUser/">复制人员子表列表</a></li>
		<shiro:hasPermission name="copy:copyUser:edit"><li><a href="${ctx}/copy/copyUser/form">复制人员子表添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="copyUser" action="${ctx}/copy/copyUser/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>姓名：</label>
				<form:input path="userName" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label>性别：</label>
				<form:select path="sex" class="input-medium">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('sex')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label>所属基地：</label>
				<form:select path="reqBase" class="input-medium">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('dep_base')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label>学号：</label>
				<form:input path="userNumber" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label>年级：</label>
				<form:select path="grade" class="input-medium">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('grade')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label>学员性质：</label>
				<form:select path="studentClass" class="input-medium">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('user_class')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>姓名</th>
				<th>性别</th>
				<th>所属基地</th>
				<th>学号</th>
				<th>年级</th>
				<th>学员性质</th>
				<th>大组编号</th>
				<shiro:hasPermission name="copy:copyUser:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="copyUser">
			<tr>
				<td><a href="${ctx}/copy/copyUser/form?id=${copyUser.id}">
					${copyUser.userName}
				</a></td>
				<td>
					${fns:getDictLabel(copyUser.sex, 'sex', '')}
				</td>
				<td>
					${fns:getDictLabel(copyUser.reqBase, 'dep_base', '')}
				</td>
				<td>
					${copyUser.userNumber}
				</td>
				<td>
					${fns:getDictLabel(copyUser.grade, 'grade', '')}
				</td>
				<td>
					${fns:getDictLabel(copyUser.studentClass, 'user_class', '')}
				</td>
				<td>
					${copyUser.groupId}
				</td>
				<shiro:hasPermission name="copy:copyUser:edit"><td>
    				<a href="${ctx}/copy/copyUser/form?id=${copyUser.id}">修改</a>
					<a href="${ctx}/copy/copyUser/delete?id=${copyUser.id}" onclick="return confirmx('确认要删除该复制人员子表吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>