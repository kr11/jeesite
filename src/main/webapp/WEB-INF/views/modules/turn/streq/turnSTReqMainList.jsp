<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>规培标准表管理</title>
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
		<li class="active"><a href="${ctx}/turn/streq/turnSTReqMain/">规培标准表列表</a></li>
		<shiro:hasPermission name="turn:streq:turnSTReqMain:edit"><li><a href="${ctx}/turn/streq/turnSTReqMain/form">规培标准表添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="turnSTReqMain" action="${ctx}/turn/streq/turnSTReqMain/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>规培标准名：</label>
				<form:input path="name" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label>总时长：</label>
				<form:input path="totalLength" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label>所属基地：</label>
				<form:select path="reqBase" class="input-medium">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('dep_base')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
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
				<th>规培标准名</th>
				<th>标准所属基地</th>
				<th>开始时间</th>
				<th>结束时间</th>
				<th>总时长</th>
				<th>更新时间</th>
				<th>备注信息</th>
				<shiro:hasPermission name="turn:streq:turnSTReqMain:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="turnSTReqMain">
			<tr>
				<td><a href="${ctx}/turn/streq/turnSTReqMain/form?id=${turnSTReqMain.id}">
					${turnSTReqMain.name}
				</a></td>
				<td>
						${fns:getDictLabel(turnSTReqMain.reqBase, 'dep_base', '')}
				</td>
				<td>
					${turnSTReqMain.startYAtM}
				</td>
				<td>
					${turnSTReqMain.endYAtM}
				</td>
				<td>
					${turnSTReqMain.totalLength}
				</td>
				<td>
					<fmt:formatDate value="${turnSTReqMain.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${turnSTReqMain.remarks}
				</td>
				<shiro:hasPermission name="turn:streq:turnSTReqMain:edit"><td>
    				<a href="${ctx}/turn/streq/turnSTReqMain/form?id=${turnSTReqMain.id}">修改</a>
					<a href="${ctx}/turn/streq/turnSTReqMain/delete?id=${turnSTReqMain.id}" onclick="return confirmx('确认要删除该规培标准表吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>