<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>权利管理</title>
	<meta name="decorator" content="ani"/>
	<script type="text/javascript">

		$(document).ready(function() {

		});
		function save() {
            var isValidate = jp.validateForm('#inputForm');//校验表单
            if(!isValidate){
                return false;
			}else{
                jp.loading();
                jp.post("${ctx}/right/yybRight/save",$('#inputForm').serialize(),function(data){
                    if(data.success){
                        jp.getParent().refresh();
                        var dialogIndex = parent.layer.getFrameIndex(window.name); // 获取窗口索引
                        parent.layer.close(dialogIndex);
                        jp.success(data.msg)

                    }else{
                        jp.error(data.msg);
                    }
                })
			}

        }
	</script>
</head>
<body class="bg-white">
		<form:form id="inputForm" modelAttribute="yybRight" class="form-horizontal">
		<form:hidden path="id"/>	
		<table class="table table-bordered">
		   <tbody>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>名称：</label></td>
					<td class="width-35">
						<form:input path="name" htmlEscape="false" maxlength="10"  minlength="2"   class="form-control required"/>
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>增长率：</label></td>
					<td class="width-35">
						<form:input path="rate" htmlEscape="false"    class="form-control required isFloatGtZero"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">备注信息：</label></td>
					<td class="width-35">
						<form:input path="remarks" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"></td>
		   			<td class="width-35" ></td>
		  		</tr>
		 	</tbody>
		</table>
	</form:form>
</body>
</html>