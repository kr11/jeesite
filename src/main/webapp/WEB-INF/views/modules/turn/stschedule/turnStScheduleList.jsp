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
    <li>
        <a href="${ctx}/turn/stschedule/turnStSchedule/tableEdit?timeUnit=${turnStSchedule.timeUnit}">${turnStSchedule.timeUnitSysTemName}-排班</a>
    </li>
    <c:choose>
        <c:when test="${not empty turnStSchedule.isFromCellClick}">
            <li class="active"><a>当前单元格记录</a></li>
            <li><a
                    href="${ctx}/turn/stschedule/turnStSchedule/list?timeUnit=${turnStSchedule.timeUnit}">人员排班记录</a>
            </li>
        </c:when>
        <c:otherwise>
            <li class="active"><a
                    href="${ctx}/turn/stschedule/turnStSchedule/list?timeUnit=${turnStSchedule.timeUnit}">人员排班记录</a>
            </li>
        </c:otherwise>
    </c:choose>
</ul>
<c:if test="${empty turnStSchedule.isFromCellClick}">
    <form:form id="searchForm" modelAttribute="turnStSchedule" action="${ctx}/turn/stschedule/turnStSchedule/list"
               method="post"
               class="breadcrumb form-search">
        <input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
        <input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
        <input id="id" name="id" type="hidden" value="${turnStSchedule.id}"/>
        <input id="archiveId" name="archiveId" type="hidden" value="${turnStSchedule.archiveId}"/>
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
        <input id="reqStartYAndM" name="reqStartYAndM" type="hidden"
               value="${turnStSchedule.reqStartYAndM}"/>
        <input id="reqEndYAndM" name="reqEndYAndM" type="hidden" value="${turnStSchedule.reqEndYAndM}"/>
        <input id="startMonthUpOrDown" name="startMonthUpOrDown" type="hidden"
               value="${turnStSchedule.startMonthUpOrDown}"/>
        <input id="endMonthUpOrDown" name="endMonthUpOrDown" type="hidden"
               value="${turnStSchedule.endMonthUpOrDown}"/>
        <input id="isFromCellClick" name="isFromCellClick" type="hidden"
               value="${turnStSchedule.isFromCellClick}"/>

        <ul class="ul-form">
                <%--<li><label>用户名：</label>--%>
                <%--<form:select path="userName" class="input-medium">--%>
                <%--<form:options items="${fns:getDictList('')}" itemLabel="label" itemValue="value" htmlEscape="false"/>--%>
                <%--</form:select>--%>
                <%--</li>--%>
            <li><label>科室名：</label>
                    <%--${turnStSchedule.depId},${depId},${testSche.depId},${turnStSchedule.timeUnit},${timeUnit},${testSche.timeUnit}xxx--%>
                    <%--<div class="controls">--%>
                <form:select path="depId" class="input-xlarge">
                    <option value=""></option>
                    <%--<option value=""></option>--%>
                    <form:options items="${departmentList}" itemLabel="departmentName" itemValue="id"
                                  htmlEscape="false"/>
                </form:select>
                <%--<select id="depId"--%>
                        <%--name="depId" data-value="${turnStSchedule.depId}"--%>
                        <%--class="input-xlarge">--%>
                    <%--<option value=""></option>--%>
                    <%--<c:forEach items="${departmentList}" var="dep">--%>
                        <%--<option value="${dep.id}">${dep.departmentName}</option>--%>
                    <%--</c:forEach>--%>
                <%--</select>--%>
                    <%--</div>--%>
            </li>
            <li><label>显示已对：</label>
                <form:select path="isShowCorrect" class="input-medium">
                    <form:options items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value"
                                  htmlEscape="false"/>
                </form:select>
            </li>
            <li class="btns"><input id="btnSubmit2" class="btn btn-primary" type="submit" value="查询"/></li>
            <li class="clearfix"></li>
        </ul>
    </form:form>
</c:if>
<sys:message content="${message}"/>
<table id="contentTable" class="table table-striped table-bordered table-condensed">
    <thead>
    <tr>
        <th>用户名</th>
        <th>科室名</th>
        <th>现开始时间</th>
        <th>现结束时间</th>
        <th>规定时长</th>
        <th>是否已对</th>
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
                <a onclick="$('#searchForm${turnStSchedule.user}${turnStSchedule.depId}').submit()">${turnStSchedule.userName}</a>
                <form:form id="searchForm${turnStSchedule.user}${turnStSchedule.depId}"
                           modelAttribute="turnStSchedule"
                           action="${ctx}/turn/stschedule/turnStSchedule/form" style="display:none" method="post">
                    <input id="id" name="id" type="hidden" value="${turnStSchedule.id}"/>
                    <input id="archiveId" name="archiveId" type="hidden" value="${turnStSchedule.archiveId}"/>
                    <input id="depId2" name="depId" type="hidden" value="${turnStSchedule.depId}"/>
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
                    <input id="reqStartYAndM" name="reqStartYAndM" type="hidden"
                           value="${turnStSchedule.reqStartYAndM}"/>
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
            <td>
                    ${turnStSchedule.isCorrect}
            </td>
            <shiro:hasPermission name="turn:stschedule:turnStSchedule:edit">
                <td>
                    <a onclick="$('#searchForm${turnStSchedule.user}${turnStSchedule.depId}').submit()">修改</a>
                    <c:if test="${not empty turnStSchedule.id}">
                    <a href="${ctx}/turn/stschedule/turnStSchedule/delete?id=${turnStSchedule.id}&timeUnit=${turnStSchedule.timeUnit}"
                       onclick="return confirmx('确认要删除该记录吗？', this.href)">删除</a>
                    </c:if>
                </td>
            </shiro:hasPermission>
        </tr>
    </c:forEach>
    </tbody>
</table>
<div class="pagination">${page}</div>
</body>
</html>