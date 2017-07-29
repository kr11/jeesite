<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<html>
<head>
    <title>排班-规培调度表管理</title>
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
    <li class="active"><a href="${ctx}/turn/stschedule/turnStSchedule/">排班-规培调度表列表</a></li>
    <shiro:hasPermission name="turn:stschedule:turnStSchedule:edit">
        <li><a href="${ctx}/turn/stschedule/turnStSchedule/form">排班-规培调度表添加</a></li>
    </shiro:hasPermission>
</ul>
<form:form id="searchForm" modelAttribute="turnStSchedule" action="${ctx}/turn/stschedule/turnStSchedule/" method="post"
           class="breadcrumb form-search">
    <input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
    <input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
    <ul class="ul-form">
        <li><label>用户名：</label>
            <form:select path="userName" class="input-medium">
                <form:option value="" label=""/>
                <form:options items="${fns:getDictList('')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
            </form:select>
        </li>
        <li><label>科室名：</label>
            <form:select path="depName" class="input-medium">
                <form:option value="" label=""/>
                <form:options items="${fns:getDictList('')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
            </form:select>
        </li>
        <li><label>开始时间半月整数：</label>
            <form:input path="startInt" htmlEscape="false" maxlength="64" class="input-medium"/>
        </li>
        <li><label>结束时间半月整数：</label>
            <form:input path="endInt" htmlEscape="false" maxlength="64" class="input-medium"/>
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
        <th>现开始时间</th>
        <th>现结束时间</th>
        <th>规定时长</th>
        <shiro:hasPermission name="turn:stschedule:turnStSchedule:edit">
            <th>操作</th>
        </shiro:hasPermission>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${diffList}" var="turnStSchedule">
        <tr>
            <td>
                    <%--<a href="${ctx}/turn/stschedule/turnStSchedule/form?id=${turnStSchedule.id}">--%>
                    <%--${turnStSchedule.userName}--%>
                    <%--</a>--%>
                <a onclick="$(searchForm${turnStSchedule.userName}${turnStSchedule.depName}).submit()">${turnStSchedule.userName}</a>
                <form:form id="searchForm${turnStSchedule.userName}${turnStSchedule.depName}"
                           modelAttribute="turnStSchedule"
                           action="${ctx}/turn/stschedule/turnStSchedule/form" style="display:none" method="post">
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
                    <input id="timeUnit" name="timeUnit" type="hidden" value="${turnStSchedule.timeUnit}"/>
                    <input id="reqStartYAndM" name="reqStartYAndM" type="hidden" value="${turnStSchedule.reqStartYAndM}"/>
                    <input id="reqEndYAndM" name="reqEndYAndM" type="hidden" value="${turnStSchedule.reqEndYAndM}"/>
                    <input id="startMonthUpOrDown" name="startMonthUpOrDown" type="hidden"
                           value="${turnStSchedule.startMonthUpOrDown}"/>
                    <input id="endMonthUpOrDown" name="endMonthUpOrDown" type="hidden"
                           value="${turnStSchedule.endMonthUpOrDown}"/>
                    <input id="btnSubmit" type="submit" value="${turnStSchedule.userName}"/>
                </form:form>
            </td>
            <td>
                    ${turnStSchedule.depName}
            </td>
            <td>
                    ${turnStSchedule.startYandM}
                <c:if test="${turnStSchedule.timeUnit == 'halfmonth'}">-${turnStSchedule.startMonthUpOrDown}</c:if>
            </td>
            <td>
                    ${turnStSchedule.endYandM}
                <c:if test="${turnStSchedule.timeUnit == 'halfmonth'}">-${turnStSchedule.endMonthUpOrDown}</c:if>
            </td>
            <td>
                    ${turnStSchedule.oughtTimeLength}
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
<div class="pagination">${page}</div>
</body>
</html>