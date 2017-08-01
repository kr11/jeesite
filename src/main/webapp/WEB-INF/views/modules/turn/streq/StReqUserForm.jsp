<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<html>
<head>
    <title>人员管理</title>
    <meta name="decorator" content="default"/>
    <script type="text/javascript">
        $(document).ready(function () {
            //$("#name").focus();
            $("#inputForm").validate({
                submitHandler: function (form) {
                    loading('正在提交，请稍等...');
                    form.submit();
                },
                errorContainer: "#messageBox",
                errorPlacement: function (error, element) {
                    $("#messageBox").text("输入有误，请先更正。");
                    if (element.is(":checkbox") || element.is(":radio") || element.parent().is(".input-append")) {
                        error.appendTo(element.parent().parent());
                    } else {
                        error.insertAfter(element);
                    }
                }
            });
        });
    </script>
</head>
<body>
<ul class="nav nav-tabs">
    <li><a href="${ctx}/turn/streq/turnSTReqMain/userList">复制人员子表列表</a></li>
    <li class="active"><a
            href="${ctx}/turn/streq/turnSTReqMain/userForm?id=${turnSTReqMain.id}&theChild.id=${turnSTReqMain.theChild.id}">人员信息
        <shiro:hasPermission name="turn:streq:turnSTReqMain:edit">${not empty turnSTReqMain.theChild.id?'修改':'添加'}
        </shiro:hasPermission><shiro:lacksPermission name="turn:streq:turnSTReqMain:edit">查看</shiro:lacksPermission></a>
    </li>
</ul>
<br/>
<form:form id="inputForm" modelAttribute="turnSTReqMain" action="${ctx}/turn/streq/turnSTReqMain/userSave" method="post"
           class="form-horizontal">
    <form:hidden path="id"/>
    <form:hidden path="name"/>
    <form:hidden path="theChild.id"/>
    <sys:message content="${message}"/>
    <div class="control-group">
        <label class="control-label">所属基地</label>
        <div class="controls">
            <choose>
                <when test="${not empty turnSTReqMain.theChild.requirementName}">
                        ${turnSTReqMain.theChild.reqBase}
                </when>
                <otherwise>
                    <form:select path="turnSTReqMain.theChild.reqBase" class="input-medium">
                        <form:option value="" label=""/>
                        <form:options items="${fns:getDictList('dep_base')}" itemLabel="label" itemValue="value"
                                      htmlEscape="false"/>
                    </form:select>
                </otherwise>
            </choose>
                <%--<form:input path="theChild.requirementId" htmlEscape="false" maxlength="64" class="input-xlarge required"/>--%>
        </div>
    </div>
    <c:if test="${not empty turnSTReqMain.theChild.requirementName}">
        <div class="control-group">
            <label class="control-label">所属标准</label>
            <div class="controls">
                    <%--<form:input path="theChild.requirementId" htmlEscape="false" maxlength="64" class="input-xlarge required"/>--%>
                    ${turnSTReqMain.theChild.requirementName}
            </div>
        </div>
    </c:if>
    <div class="control-group">
        <label class="control-label">姓名：</label>
        <div class="controls">
            <form:input path="theChild.userName" htmlEscape="false" maxlength="64" class="input-xlarge required"/>
            <span class="help-inline"><font color="red">*</font> </span>
        </div>
    </div>
    <div class="control-group">
        <label class="control-label">性别：</label>
        <div class="controls">
            <form:select path="theChild.sex" class="input-xlarge required">
                <form:option value="" label=""/>
                <form:options items="${fns:getDictList('sex')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
            </form:select>
            <span class="help-inline"><font color="red">*</font> </span>
        </div>
    </div>
    <div class="control-group">
        <label class="control-label">学号：</label>
        <div class="controls">
            <form:input path="theChild.userNumber" htmlEscape="false" maxlength="64" class="input-xlarge "/>
        </div>
    </div>
    <div class="control-group">
        <label class="control-label">年级：</label>
        <div class="controls">
            <form:select path="theChild.grade" class="input-xlarge ">
                <form:option value="" label=""/>
                <form:options items="${fns:getDictList('grade')}" itemLabel="label" itemValue="value"
                              htmlEscape="false"/>
            </form:select>
        </div>
    </div>
    <div class="control-group">
        <label class="control-label">学员性质：</label>
        <div class="controls">
            <form:select path="theChild.userClass" class="input-xlarge ">
                <form:option value="" label=""/>
                <form:options items="${fns:getDictList('user_class')}" itemLabel="label" itemValue="value"
                              htmlEscape="false"/>
            </form:select>
        </div>
    </div>
    <div class="control-group">
        <label class="control-label">大组编号：</label>
        <div class="controls">
            <form:input path="theChild.groupId" htmlEscape="false" maxlength="64" class="input-xlarge "/>
        </div>
    </div>
    <div class="control-group">
        <label class="control-label">备注信息：</label>
        <div class="controls">
            <form:textarea path="theChild.remarks" htmlEscape="false" rows="4" maxlength="255" class="input-xxlarge "/>
        </div>
    </div>
    <div class="form-actions">
        <shiro:hasPermission name="turn:streq:turnSTReqMain:edit"><input id="btnSubmit" class="btn btn-primary"
                                                                         type="submit"
                                                                         value="保 存"/>&nbsp;</shiro:hasPermission>
        <input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
    </div>
</form:form>
</body>
</html>