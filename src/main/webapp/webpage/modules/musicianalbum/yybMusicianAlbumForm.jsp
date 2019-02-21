<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>专辑管理</title>
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
                jp.post("${ctx}/musicianalbum/yybMusicianAlbum/save",$('#inputForm').serialize(),function(data){
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
		<form:form id="inputForm" modelAttribute="yybMusicianAlbum" class="form-horizontal">
		<form:hidden path="id"/>	
		<table class="table table-bordered">
		   <tbody>
				<tr>
					<td class="width-15 active"><label class="pull-right">备注信息：</label></td>
					<td class="width-35">
						<form:input path="remarks" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">音乐人：</label></td>
					<td class="width-35">
						<sys:gridselect url="${ctx}/musician/yybMusician/data" id="yybMusician" name="yybMusician.id" value="${yybMusicianAlbum.yybMusician.id}" labelName="yybMusician.name" labelValue="${yybMusicianAlbum.yybMusician.name}"
							 title="选择音乐人" cssClass="form-control " fieldLabels="姓名" fieldKeys="name" searchLabels="姓名" searchKeys="name" ></sys:gridselect>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">专辑名：</label></td>
					<td class="width-35">
						<form:input path="name" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">封面：</label></td>
					<td class="width-35">
						<sys:fileUpload fileNumLimit="1" path="img"  value="${yybMusicianAlbum.img}" type="file" uploadPath="/musicianalbum/yybMusicianAlbum"/>
					</td>
				</tr>
		 	</tbody>
		</table>
	</form:form>
</body>
</html>