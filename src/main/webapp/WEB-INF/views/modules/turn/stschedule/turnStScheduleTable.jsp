<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<html>
<head>
    <title>排班-规培调度表管理</title>
    <meta name="decorator" content="default"/>
    <script type="text/javascript">
        $(document).ready(function () {

        });

        function goToTableQuery(n, s) {
//            $("#pageStart").val(st);
            $("#pageNo").val(n);
            $("#pageSize").val(s);
            $("#searchForm").submit();
            return false;
        }

        function goToOneDayEdit() {
//            $("#pageNo").val(n);
//            $("#pageSize").val(s);
            $("#searchForm").attr("action", "${ctx}/turn/stschedule/turnStSchedule/list");
            $("#searchForm").submit();
            return false;
        }
    </script>
</head>
<body>
<ul class="nav nav-tabs">
    <li class="active"><a href="${ctx}/turn/stschedule/turnStSchedule/tableEdit">排班-表格</a></li>
    <%--<shiro:hasPermission name="turn:stschedule:turnStSchedule:edit"><li><a href="${ctx}/turn/stschedule/turnStSchedule/form">排班-规培调度表添加</a></li></shiro:hasPermission>--%>
</ul>
<form:form id="searchForm" modelAttribute="turnStSchedule" action="${ctx}/turn/stschedule/turnStSchedule/list"
           method="post" class="breadcrumb form-search">
    <%--<input id="pageNo" name="pageNo" type="hidden" value="${turnStSchedule.tablePageSize}"/>--%>
    <%--<input id="tablePageSize" name="tablePageSize" type="hidden" value="${turnStSchedule.tablePageSize}"/>--%>
    <input id="tableStart" name="tableStart" type="hidden" value="${turnStSchedule.tableStart}"/>
    <ul class="ul-form">
            <%--<li><label>用户名：</label>--%>
            <%--<form:input path="userName" htmlEscape="false" maxlength="64" class="input-medium"/>--%>
            <%--</li>--%>
            <%--<li><label>科室名：</label>--%>
            <%--<form:input path="depName" htmlEscape="false" maxlength="64" class="input-medium"/>--%>
            <%--</li>--%>
        <input id="id" name="id" type="hidden" value="${turnStSchedule.id}"/>
        <input id="archiveId" name="archiveId" type="hidden" value="${turnStSchedule.archiveId}"/>
        <input id="depId" name="depId" type="hidden" value="${turnStSchedule.depId}"/>
        <input id="depName" name="depName" type="hidden" value="${turnStSchedule.depName}"/>
        <input id="user" name="user" type="hidden" value="${turnStSchedule.user}"/>
        <input id="userName" name="userName" type="hidden" value="${turnStSchedule.userName}"/>
        <input id="requirementId" name="requirementId" type="hidden"
               value="${turnStSchedule.requirementId}"/>
        <input id="startInt" name="startInt" type="hidden" value="${turnStSchedule.startInt}"/>
        <input id="endInt" name="endInt" type="hidden" value="${turnStSchedule.endInt}"/>
        <input id="startYandM" name="startYandM" type="hidden" value="${turnStSchedule.startYandM}"/>
        <input id="endYandM" name="endYandM" type="hidden" value="${turnStSchedule.endYandM}"/>
            <%--<input id="endInt" name="endInt" type="hidden" value="${turnStSchedule.endInt}"/>--%>
        <input id="timeUnit" name="timeUnit" type="hidden" value="${turnStSchedule.timeUnit}"/>
        <input id="reqStartYAndM" name="reqStartYAndM" type="hidden" value="${turnStSchedule.reqStartYAndM}"/>
        <input id="reqEndYAndM" name="reqEndYAndM" type="hidden" value="${turnStSchedule.reqEndYAndM}"/>
        <input id="startMonthUpOrDown" name="startMonthUpOrDown" type="hidden"
               value="${turnStSchedule.startMonthUpOrDown}"/>
        <input id="endMonthUpOrDown" name="endMonthUpOrDown" type="hidden"
               value="${turnStSchedule.endMonthUpOrDown}"/>
        <li><label>每页列数：</label>
            <form:input path="tablePageSize" htmlEscape="false" maxlength="64" class="input-medium"/>
        </li>
        <li><label>排班大类：</label>
            <select id="timeUnit"
                    name="timeUnit" data-value="${ReqTimeUnit.values()[0]}"
                    class="input-medium required">
                <c:forEach items="${ReqTimeUnit.values()}" var="timeUnit">
                    <option value="${timeUnit}">${timeUnit.turnSysName}</option>
                </c:forEach>
            </select>
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
        <shiro:hasPermission name="turn:stschedule:turnStSchedule:edit">
            <th>操作</th>
        </shiro:hasPermission>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${page.list}" var="turnStSchedule">
        <tr>
            <td><a href="${ctx}/turn/stschedule/turnStSchedule/form?id=${turnStSchedule.id}">
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
            <shiro:hasPermission name="turn:stschedule:turnStSchedule:edit">
                <td>
                    <a href="${ctx}/turn/stschedule/turnStSchedule/form?id=${turnStSchedule.id}">修改</a>
                    <a href="${ctx}/turn/stschedule/turnStSchedule/delete?id=${turnStSchedule.id}"
                       onclick="return confirmx('确认要删除该排班-规培调度表吗？', this.href)">删除</a>
                </td>
            </shiro:hasPermission>
        </tr>
    </c:forEach>
    </tbody>
</table>
<div class="pagination">
    <ul>
        <li><a href="javascript:" onclick="page(${pageNo-1},'');">« 上一页</a></li>
        <li><a href="javascript:" onclick="page(${pageNo+1},'');">下一页 »</a></li>
    </ul>
    <div style="clear:both;"></div>
</div>
</body>
</html>