<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>我的第一张表管理</title>
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
		<li class="active"><a href="${ctx}/myfirst/myTestData/">我的第一张表列表</a></li>
		<shiro:hasPermission name="myfirst:myTestData:edit"><li><a href="${ctx}/myfirst/myTestData/form">我的第一张表添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="myTestData" action="${ctx}/myfirst/myTestData/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>学号：</label>
				<sys:treeselect id="user" name="user.id" value="${myTestData.user.id}" labelName="user.name" labelValue="${myTestData.user.name}"
					title="用户" url="/sys/office/treeData?type=3" cssClass="input-small" allowClear="true" notAllowSelectParent="true"/>
			</li>
			<li><label>姓名：</label>
				<form:input path="userName" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label>性别（字典类型：sex）：</label>
				<form:radiobuttons path="sex" items="${fns:getDictList('sex')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
			</li>
			<li><label>标准：</label>
				<form:input path="standard" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label>加入日期：</label>
				<input name="beginInDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${myTestData.beginInDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/> - 
				<input name="endInDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${myTestData.endInDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>学号</th>
				<th>姓名</th>
				<th>性别（字典类型：sex）</th>
				<th>标准</th>
				<th>更新时间</th>
				<th>备注信息</th>
				<shiro:hasPermission name="myfirst:myTestData:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="myTestData">
			<tr>
				<td><a href="${ctx}/myfirst/myTestData/form?id=${myTestData.id}">
					${myTestData.user.name}
				</a></td>
				<td>
					${myTestData.userName}
				</td>
				<td>
					${fns:getDictLabel(myTestData.sex, 'sex', '')}
				</td>
				<td>
					${myTestData.standard}
				</td>
				<td>
					<fmt:formatDate value="${myTestData.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${myTestData.remarks}
				</td>
				<shiro:hasPermission name="myfirst:myTestData:edit"><td>
    				<a href="${ctx}/myfirst/myTestData/form?id=${myTestData.id}">修改</a>
					<a href="${ctx}/myfirst/myTestData/delete?id=${myTestData.id}" onclick="return confirmx('确认要删除该我的第一张表吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>