<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>规培标准表管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			//$("#name").focus();
			$("#inputForm").validate({
				submitHandler: function(form){
					loading('正在提交，请稍等...');
					form.submit();
				},
				errorContainer: "#messageBox",
				errorPlacement: function(error, element) {
					$("#messageBox").text("输入有误，请先更正。");
					if (element.is(":checkbox")||element.is(":radio")||element.parent().is(".input-append")){
						error.appendTo(element.parent().parent());
					} else {
						error.insertAfter(element);
					}
				}
			});
		});
		function addRow(list, idx, tpl, row){
			$(list).append(Mustache.render(tpl, {
				idx: idx, delBtn: true, row: row
			}));
			$(list+idx).find("select").each(function(){
				$(this).val($(this).attr("data-value"));
			});
			$(list+idx).find("input[type='checkbox'], input[type='radio']").each(function(){
				var ss = $(this).attr("data-value").split(',');
				for (var i=0; i<ss.length; i++){
					if($(this).val() == ss[i]){
						$(this).attr("checked","checked");
					}
				}
			});
		}
		function delRow(obj, prefix){
			var id = $(prefix+"_id");
			var delFlag = $(prefix+"_delFlag");
			if (id.val() == ""){
				$(obj).parent().parent().remove();
			}else if(delFlag.val() == "0"){
				delFlag.val("1");
				$(obj).html("&divide;").attr("title", "撤销删除");
				$(obj).parent().parent().addClass("error");
			}else if(delFlag.val() == "1"){
				delFlag.val("0");
				$(obj).html("&times;").attr("title", "删除");
				$(obj).parent().parent().removeClass("error");
			}
		}
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/turn/streq/turnSTReqMain/">规培标准表列表</a></li>
		<li class="active"><a href="${ctx}/turn/streq/turnSTReqMain/form?id=${turnSTReqMain.id}">规培标准表<shiro:hasPermission name="turn:streq:turnSTReqMain:edit">${not empty turnSTReqMain.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="turn:streq:turnSTReqMain:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="turnSTReqMain" action="${ctx}/turn/streq/turnSTReqMain/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>		
		<div class="control-group">
			<label class="control-label">规培标准名：</label>
			<div class="controls">
				<form:input path="name" htmlEscape="false" maxlength="64" class="input-xlarge required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<%--<div class="control-group">--%>
			<%--<label class="control-label">所属存档id：</label>--%>
			<%--<div class="controls">--%>
				<%--<form:input path="archiveId" htmlEscape="false" maxlength="64" class="input-xlarge required"/>--%>
				<%--<span class="help-inline"><font color="red">*</font> </span>--%>
			<%--</div>--%>
		<%--</div>--%>
		<div class="control-group">
			<label class="control-label">总时长：</label>
			<div class="controls">
				<form:input path="totalLength" htmlEscape="false" maxlength="64" class="input-xlarge required digits"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">时间单位：</label>
			<div class="controls">
				<form:select path="timgUnit" class="input-xlarge required">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('req_time_unit')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">备注信息：</label>
			<div class="controls">
				<form:textarea path="remarks" htmlEscape="false" rows="4" maxlength="255" class="input-xxlarge "/>
			</div>
		</div>
			<div class="control-group">
				<label class="control-label">排班_规培标准数据表子表：</label>
				<div class="controls">
					<table id="contentTable" class="table table-striped table-bordered table-condensed">
						<thead>
							<tr>
								<th class="hide"></th>
								<th>科室名</th>
								<th>时间长度</th>
								<shiro:hasPermission name="turn:streq:turnSTReqMain:edit"><th width="10">&nbsp;</th></shiro:hasPermission>
							</tr>
						</thead>
						<tbody id="turnSTReqChildList">
						</tbody>
						<shiro:hasPermission name="turn:streq:turnSTReqMain:edit"><tfoot>
							<tr><td colspan="4"><a href="javascript:" onclick="addRow('#turnSTReqChildList', turnSTReqChildRowIdx, turnSTReqChildTpl);turnSTReqChildRowIdx = turnSTReqChildRowIdx + 1;" class="btn">新增</a></td></tr>
						</tfoot></shiro:hasPermission>
					</table>
					<script type="text/template" id="turnSTReqChildTpl">//<!--
						<tr id="turnSTReqChildList{{idx}}">
							<td class="hide">
								<input id="turnSTReqChildList{{idx}}_id" name="turnSTReqChildList[{{idx}}].id" type="hidden" value="{{row.id}}"/>
								<input id="turnSTReqChildList{{idx}}_delFlag" name="turnSTReqChildList[{{idx}}].delFlag" type="hidden" value="0"/>
							</td>
							<td>
								<select id="turnSTReqChildList{{idx}}_departmentName" name="turnSTReqChildList[{{idx}}].departmentName" data-value="{{row.departmentName}}" class="input-small required">
									<option value=""></option>
									<c:forEach items="${departmentList}" var="dep">
										<option value="${dep.id}@${dep.departmentName}">${dep.departmentName}</option>
									</c:forEach>
								</select>
							</td>
							<td>
								<input id="turnSTReqChildList{{idx}}_timeLength" name="turnSTReqChildList[{{idx}}].timeLength" type="text" value="{{row.timeLength}}" maxlength="64" class="input-small required"/>
							</td>
							<shiro:hasPermission name="turn:streq:turnSTReqMain:edit"><td class="text-center" width="10">
								{{#delBtn}}<span class="close" onclick="delRow(this, '#turnSTReqChildList{{idx}}')" title="删除">&times;</span>{{/delBtn}}
							</td></shiro:hasPermission>
						</tr>//-->
					</script>
					<script type="text/javascript">
						var turnSTReqChildRowIdx = 0, turnSTReqChildTpl = $("#turnSTReqChildTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
						$(document).ready(function() {
							var data = ${fns:toJson(turnSTReqMain.turnSTReqChildList)};
							for (var i=0; i<data.length; i++){
								addRow('#turnSTReqChildList', turnSTReqChildRowIdx, turnSTReqChildTpl, data[i]);
								turnSTReqChildRowIdx = turnSTReqChildRowIdx + 1;
							}
						});
					</script>
				</div>
			</div>
		<div class="form-actions">
			<shiro:hasPermission name="turn:streq:turnSTReqMain:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>