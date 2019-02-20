<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>音乐管理</title>
	<meta name="decorator" content="ani"/>
	<script type="text/javascript">

		$(document).ready(function() {

	        $('#publishTime').datetimepicker({
				 format: "YYYY-MM-DD HH:mm:ss"
		    });
		});
		function save() {
            var isValidate = jp.validateForm('#inputForm');//校验表单
            if(!isValidate){
                return false;
			}else{
                jp.loading();
                jp.post("${ctx}/music/yybMusic/save",$('#inputForm').serialize(),function(data){
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
		<form:form id="inputForm" modelAttribute="yybMusic" class="form-horizontal">
		<form:hidden path="id"/>	
		<table class="table table-bordered">
		   <tbody>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>歌名：</label></td>
					<td class="width-35">
						<form:input path="title" htmlEscape="false"    class="form-control required"/>
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>价格：</label></td>
					<td class="width-35">
						<form:input path="price" htmlEscape="false"    class="form-control required isFloatGtZero"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>歌手：</label></td>
					<td class="width-35">
						<sys:gridselect isMultiSelected="true" url="${ctx}/musician/yybMusician/data" id="yybMusician" name="yybMusician.id" value="${yybMusic.yybMusician.id}" labelName="yybMusician.name" labelValue="${yybMusic.yybMusician.name}"
							 title="选择歌手" cssClass="form-control required" fieldLabels="歌手" fieldKeys="name" searchLabels="歌手" searchKeys="name" ></sys:gridselect>
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>专辑：</label></td>
					<td class="width-35">
						<sys:gridselect url="${ctx}/data/albumData" id="yybMusicianAlbum" name="yybMusicianAlbum.id" value="${yybMusic.yybMusicianAlbum.id}" labelName="yybMusicianAlbum.name" labelValue="${yybMusic.yybMusicianAlbum.name}"
							 title="选择专辑" cssClass="form-control required" fieldLabels="专辑|歌手" fieldKeys="name|yybMusician.name" searchLabels="专辑|歌手" searchKeys="name|yybMusician.name" ></sys:gridselect>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>地址：</label></td>
					<td class="width-35">
						<sys:fileUpload path="url"  value="${yybMusic.url}" type="file" uploadPath="/music/yybMusic"/>
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>图片：</label></td>
					<td class="width-35">
						<sys:fileUpload path="img"  value="${yybMusic.img}" type="file" uploadPath="/music/yybMusic"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">标签：</label></td>
					<td class="width-35">
						<sys:gridselect url="${ctx}/tagcatetory/yybTagCategory/data" id="yybTagCategory" name="yybTagCategory.id" value="${yybMusic.yybTagCategory.id}" labelName="yybTagCategory.name" labelValue="${yybMusic.yybTagCategory.name}"
							 title="选择标签" cssClass="form-control " fieldLabels="名称" fieldKeys="name" searchLabels="名称" searchKeys="name" ></sys:gridselect>
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>时长：</label></td>
					<td class="width-35">
						<form:input path="musicTime" htmlEscape="false"    class="form-control required"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>发布时间：</label></td>
					<td class="width-35">
						<div class='input-group form_datetime' id='publishTime'>
							<input type='text'  name="publishTime" class="form-control required"  value="<fmt:formatDate value="${yybMusic.publishTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
							<span class="input-group-addon">
								<span class="glyphicon glyphicon-calendar"></span>
							</span>
						</div>
					</td>
					<td class="width-15 active"><label class="pull-right">备注信息：</label></td>
					<td class="width-35">
						<form:input path="remarks" htmlEscape="false"    class="form-control "/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">播放量：</label></td>
					<td class="width-35">
						<form:input path="playCount" htmlEscape="false"    class="form-control  isIntGtZero"/>
					</td>
					<td class="width-15 active"><label class="pull-right">收藏量：</label></td>
					<td class="width-35">
						<form:input path="likeCount" htmlEscape="false"    class="form-control  isIntGtZero"/>
					</td>
				</tr>
		 	</tbody>
		</table>
	</form:form>
</body>
</html>