<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>标签分类管理</title>
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
                jp.post("${ctx}/tagcatetory/yybTagCategory/save",$('#inputForm').serialize(),function(data){
                    if(data.success){
                        jp.getParent().refreshNode(data);
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
		<form:form id="inputForm" modelAttribute="yybTagCategory" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<table class="table table-bordered">
		   <tbody>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>名称：</label></td>
					<td class="width-35">
						<form:input path="name" htmlEscape="false" maxlength="10"  minlength="2"   class="form-control required"/>
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>排序：</label></td>
					<td class="width-35">
						<form:input path="sort" htmlEscape="false"    class="form-control required"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">备注信息：</label></td>
					<td class="width-35">
						<form:input path="remarks" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">父级编号：</label></td>
					<td class="width-35">
						<sys:treeselect id="parent" name="parent.id" value="${yybTagCategory.parent.id}" labelName="parent.name" labelValue="${yybTagCategory.parent.name}"
						title="父级编号" url="/tagcatetory/yybTagCategory/treeData" extId="${yybTagCategory.id}" cssClass="form-control " allowClear="true"/>
					</td>
		 	</tbody>
		</table>
		</form:form>
</body>
</html>