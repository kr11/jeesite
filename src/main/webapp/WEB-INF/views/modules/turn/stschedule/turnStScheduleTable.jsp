<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<html>
<head>
    <title>排班-规培调度表管理</title>
    <meta name="decorator" content="default"/>
    <script type="text/javascript">
        $(document).ready(function () {

        });

        function switchPage(start, size) {
//            $("#pageStart").val(st);
            $("#tableStart").val(start);
            $("#tablePageSize").val(size);
            $("#searchForm").submit();
            return false;
        }

        function goToOneDayEdit(dayInt, depId) {
            $("#startInt").val(dayInt);
            $("#endInt").val(dayInt + 1);
            $("#depId").val(depId);
            $("#searchForm").attr("action", "${ctx}/turn/stschedule/turnStSchedule/list");
            $("#searchForm").submit();
            return false;
        }
    </script>
</head>
<body>
<ul class="nav nav-tabs">
    <li class="active">
        <a href="${ctx}/turn/stschedule/turnStSchedule/tableEdit?timeUnit=${turnStSchedule.timeUnit}">${turnStSchedule.timeUnitSysTemName}-排班</a>
    </li>
    <li><a href="${ctx}/turn/stschedule/turnStSchedule/list?timeUnit=${turnStSchedule.timeUnit}">人员排班记录</a></li>
    <%--<shiro:hasPermission name="turn:stschedule:turnStSchedule:edit"><li><a href="${ctx}/turn/stschedule/turnStSchedule/form">排班-规培调度表添加</a></li></shiro:hasPermission>--%>
</ul>
<form:form id="searchForm" modelAttribute="turnStSchedule" action="${ctx}/turn/stschedule/turnStSchedule/tableEdit"
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
            <%--<input id="user" name="user" type="hidden" value="${turnStSchedule.user}"/>--%>
            <%--<input id="userName" name="userName" type="hidden" value="${turnStSchedule.userName}"/>--%>
            <%--<input id="requirementId" name="requirementId" type="hidden"--%>
            <%--value="${turnStSchedule.requirementId}"/>--%>
        <input id="startInt" name="startInt" type="hidden" value="${turnStSchedule.startInt}"/>
        <input id="endInt" name="endInt" type="hidden" value="${turnStSchedule.endInt}"/>
            <%--<input id="startYandM" name="startYandM" type="hidden" value="${turnStSchedule.startYandM}"/>--%>
            <%--<input id="endYandM" name="endYandM" type="hidden" value="${turnStSchedule.endYandM}"/>--%>
            <%--<input id="endInt" name="endInt" type="hidden" value="${turnStSchedule.endInt}"/>--%>
        <input id="timeUnit" name="timeUnit" type="hidden" value="${turnStSchedule.timeUnit}"/>
        <input id="isFromCellClick" name="isFromCellClick" type="hidden" value="1"/>
            <%--<input id="reqStartYAndM" name="reqStartYAndM" type="hidden" value="${turnStSchedule.reqStartYAndM}"/>--%>
            <%--<input id="reqEndYAndM" name="reqEndYAndM" type="hidden" value="${turnStSchedule.reqEndYAndM}"/>--%>
            <%--<input id="startMonthUpOrDown" name="startMonthUpOrDown" type="hidden"--%>
            <%--value="${turnStSchedule.startMonthUpOrDown}"/>--%>
            <%--<input id="endMonthUpOrDown" name="endMonthUpOrDown" type="hidden"--%>
            <%--value="${turnStSchedule.endMonthUpOrDown}"/>--%>
        <li><label>每页列数：</label>
            <form:input path="tablePageSize" htmlEscape="false" maxlength="64" class="input-medium"/>
        </li>
        <li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
        <li class="clearfix"></li>
    </ul>
</form:form>
<button class="btn btn-primary" onclick="{ if(confirm('确定重置排到上一次生成结果？'))location.href=
        '/a/turn/stschedule/turnStSchedule/autoArrange?timeUnit=${turnStSchedule.timeUnit}&randomSeed='}"
>重置排班
</button>
<button class="btn btn-primary" onclick="{ if(confirm('确定重新排班？'))location.href=
        '/a/turn/stschedule/turnStSchedule/autoArrange?timeUnit=${turnStSchedule.timeUnit}&randomSeed=-1'}"
>重新排班
</button>
<%--<li><label>生成表单：</label>--%>
<%--<input id="randomSeed" name="randomSeed" value="${randomSeed}"--%>
<%--maxlength="64" class="input-medium"/>--%>
<%--</li>--%>
<sys:message content="${message}"/>
<table id="contentTable" class="table table-striped table-bordered table-condensed">
    <thead>
    <tr>
        <th>科室\日期</th>
        <c:forEach items="${editTableList.dateList}" var="date">
            <th>${date}~</th>
        </c:forEach>

        <%--<shiro:hasPermission name="turn:stschedule:turnStSchedule:edit">--%>
        <%--<th>操作</th>--%>
        <%--</shiro:hasPermission>--%>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${editTableList.lineList}" var="line">
        <tr>
            <td>
                    ${line.lineHeader[1]}
            </td>
            <c:forEach items="${line.cellList}" var="cell">
                <td
                        <c:if test="${cell != ''}">
                            onclick="
                            goToOneDayEdit(${cell.cellHeaderList[0]},'${line.lineHeader[0]}')
                            "
                        </c:if>
                >${cell}</td>
            </c:forEach>
        </tr>
    </c:forEach>
    </tbody>
</table>
<div class="pagination">
    <ul>
        <li><a href="javascript:"
               onclick="switchPage(${turnStSchedule.tableStart-turnStSchedule.tablePageSize},${turnStSchedule.tablePageSize});">«
            上一页</a></li>
        <li><a href="javascript:"
               onclick="switchPage(${turnStSchedule.tableStart+turnStSchedule.tablePageSize},${turnStSchedule.tablePageSize});">下一页
            »</a></li>
    </ul>
    <div style="clear:both;"></div>
</div>
</body>
</html>