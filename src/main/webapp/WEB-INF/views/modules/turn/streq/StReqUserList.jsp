<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<html>
<head>
    <title>复制人员子表管理</title>
    <meta name="decorator" content="default"/>
    <script type="text/javascript">
        $(document).ready(function () {

        });

        function page(n, s) {
            $("#pageNo").val(n);
            $("#pageSize").val(s);
            $("#searchForm").submit();
            return false;
        }
    </script>
</head>
<body>
<ul class="nav nav-tabs">
    <li class="active"><a href="${ctx}/turn/streq/turnSTReqMain/userList">规培人员列表</a></li>
    <li><a href="${ctx}/turn/streq/turnSTReqMain/userForm">新规培人员添加</a></li>
</ul>
<form:form id="searchForm" modelAttribute="turnSTReqMain" action="${ctx}/turn/streq/turnSTReqMain/userList"
           method="post" class="breadcrumb form-search">
    <ul class="ul-form">
        <li><label>姓名：</label>
            <form:input path="theChild.userName" htmlEscape="false" maxlength="64" class="input-medium"/>
        </li>
        <li><label>性别：</label>
            <form:select path="theChild.sex" class="input-medium">
                <form:option value="" label=""/>
                <form:options items="${fns:getDictList('sex')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
            </form:select>
        </li>
        <li><label>所属基地：</label>
            <form:select path="reqBase" class="input-medium">
                <form:option value="" label=""/>
                <form:options items="${fns:getDictList('dep_base')}" itemLabel="label" itemValue="value"
                              htmlEscape="false"/>
            </form:select>
        </li>
        <li><label>学号：</label>
            <form:input path="theChild.userNumber" htmlEscape="false" maxlength="64" class="input-medium"/>
        </li>
        <li><label>年级：</label>
            <form:select path="theChild.grade" class="input-medium">
                <form:option value="" label=""/>
                <form:options items="${fns:getDictList('grade')}" itemLabel="label" itemValue="value"
                              htmlEscape="false"/>
            </form:select>
        </li>
        <li><label>学员性质：</label>
            <form:select path="theChild.userClass" class="input-medium">
                <form:option value="" label=""/>
                <form:options items="${fns:getDictList('user_class')}" itemLabel="label" itemValue="value"
                              htmlEscape="false"/>
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
        <th>所属标准</th>
        <th>学号</th>
        <th>年级</th>
        <th>学员性质</th>
        <th>大组编号</th>
        <shiro:hasPermission name="turn:streq:turnSTReqMain:edit">
            <th>操作</th>
        </shiro:hasPermission>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${userList}" var="stUser">
        <tr>
            <td><a href="${ctx}/turn/streq/turnSTReqMain/userForm?id=${stUser.requirementId.id}&theChild.id=${stUser.id}">
                    ${stUser.userName}
            </a></td>
            <td>
                    ${fns:getDictLabel(stUser.sex, 'sex', '')}
            </td>
            <td>
                    ${fns:getDictLabel(stUser.reqBase, 'dep_base', '')}
            </td>
            <td>
                    ${stUser.requirementId.name}
            </td>
            <td>
                    ${stUser.userNumber}
            </td>
            <td>
                    ${fns:getDictLabel(stUser.grade, 'grade', '')}
            </td>
            <td>
                    ${fns:getDictLabel(stUser.userClass, 'user_class', '')}
            </td>
            <td>
                    ${stUser.groupId}
            </td>
            <shiro:hasPermission name="turn:streq:turnSTReqMain:edit">
                <td>
                    <%--<a href="${ctx}/turn/streq/turnSTReqMain/userForm?id=${turnSTReqMain.id}&theChild.id=${theChild.id}">修改</a>--%>
                    <a href="${ctx}/turn/streq/turnSTReqMain/userDelete?id=${stUser.requirementId.id}&theChild.id=${stUser.id}"
                       onclick="return confirmx('确认要删除该人员记录吗？', this.href)">删除</a>
                </td>
            </shiro:hasPermission>
        </tr>
    </c:forEach>
    </tbody>
</table>
</body>
</html>