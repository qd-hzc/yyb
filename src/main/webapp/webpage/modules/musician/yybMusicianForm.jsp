<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>音乐人管理</title>
	<meta name="decorator" content="ani"/>
	<script type="text/javascript">

		$(document).ready(function() {
		});

		function save() {
                var n = 0;
                $.each($(".ccc"), function(i, value) {
                    if($(value).attr("name").indexOf("yybMusicianAlbumList")!= -1 ) {
                        $(value).attr("name","yybMusicianAlbumList["+n+"].img")
                        n++;
                    }
                })
            var isValidate = jp.validateForm('#inputForm');//校验表单
            if(!isValidate){
                return false;
			}else{
                jp.loading();
                jp.post("${ctx}/musician/yybMusician/save",$('#inputForm').serialize(),function(data){
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
			$(list+idx).find(".form_datetime").each(function(){
				 $(this).datetimepicker({
					 format: "YYYY-MM-DD HH:mm:ss"
			    });
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
<body class="bg-white">
		<form:form id="inputForm" modelAttribute="yybMusician" action="${ctx}/musician/yybMusician/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<table class="table table-bordered">
		   <tbody>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>姓名：</label></td>
					<td class="width-35">
						<form:input path="name" htmlEscape="false" maxlength="12"  minlength="2"   class="form-control required"/>
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>头像：</label></td>
					<td class="width-35">
						<sys:fileUpload path="headPhoto"  value="${yybMusician.headPhoto}" type="file" uploadPath="/musician/yybMusician"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">艺名：</label></td>
					<td class="width-35">
						<form:input path="stageName" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">地址：</label></td>
					<td class="width-35">
						<form:input path="address" htmlEscape="false"    class="form-control "/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>手机：</label></td>
					<td class="width-35">
						<form:input path="phone" htmlEscape="false"    class="form-control required isMobile"/>
					</td>
					<td class="width-15 active"><label class="pull-right">邮箱：</label></td>
					<td class="width-35">
						<form:input path="mail" htmlEscape="false"    class="form-control  email"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">身份证号：</label></td>
					<td class="width-35">
						<form:input path="idCard" htmlEscape="false"    class="form-control  isIdCardNo"/>
					</td>
					<td class="width-15 active"><label class="pull-right">身份证附件：</label></td>
					<td class="width-35">
						<sys:fileUpload path="idCardAttach"  value="${yybMusician.idCardAttach}" type="file" uploadPath="/musician/yybMusician"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">作品：</label></td>
					<td class="width-35">
						<sys:fileUpload path="production"  value="${yybMusician.production}" type="file" uploadPath="/musician/yybMusician"/>
					</td>
					<td class="width-15 active"><label class="pull-right">备注信息：</label></td>
					<td class="width-35">
						<form:input path="remarks" htmlEscape="false"    class="form-control "/>
					</td>
				</tr>
		 	</tbody>
		</table>
		<div class="tabs-container">
            <ul class="nav nav-tabs">
				<li class="active"><a data-toggle="tab" href="#tab-1" aria-expanded="true">专辑：</a>
                </li>
            </ul>
            <div class="tab-content">
				<div id="tab-1" class="tab-pane fade in  active">
			<a class="btn btn-white btn-sm" onclick="addRow('#yybMusicianAlbumList', yybMusicianAlbumRowIdx, yybMusicianAlbumTpl);yybMusicianAlbumRowIdx = yybMusicianAlbumRowIdx + 1;" title="新增"><i class="fa fa-plus"></i> 新增</a>
			<table class="table table-striped table-bordered table-condensed">
				<thead>
					<tr>
						<th class="hide"></th>
						<th>专辑名</th>
						<th>封面</th>
						<th>备注信息</th>
						<th width="10">&nbsp;</th>
					</tr>
				</thead>
				<tbody id="yybMusicianAlbumList">
				</tbody>
			</table>
			<script type="text/template" id="yybMusicianAlbumTpl">//<!--
				<tr id="yybMusicianAlbumList{{idx}}">
					<td class="hide">
						<input id="yybMusicianAlbumList{{idx}}_id" name="yybMusicianAlbumList[{{idx}}].id" type="hidden" value="{{row.id}}"/>
						<input id="yybMusicianAlbumList{{idx}}_delFlag" name="yybMusicianAlbumList[{{idx}}].delFlag" type="hidden" value="0"/>
					</td>
					
					<td>
						<input id="yybMusicianAlbumList{{idx}}_name" name="yybMusicianAlbumList[{{idx}}].name" type="text" value="{{row.name}}"    class="form-control "/>
					</td>
					
					
					<td>
									<sys:fileUpload path="yybMusicianAlbumList{{idx}}_img"  value="{{row.img}}" type="file" uploadPath="/musician/yybMusician"/>
					</td>
					
					
					<td>
						<input id="yybMusicianAlbumList{{idx}}_remarks" name="yybMusicianAlbumList[{{idx}}].remarks" type="text" value="{{row.remarks}}"    class="form-control "/>
					</td>
					
					<td class="text-center" width="10">
						{{#delBtn}}<span class="close" onclick="delRow(this, '#yybMusicianAlbumList{{idx}}')" title="删除">&times;</span>{{/delBtn}}
					</td>
				</tr>//-->
			</script>
			<script type="text/javascript">
				var yybMusicianAlbumRowIdx = 0, yybMusicianAlbumTpl = $("#yybMusicianAlbumTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
				$(document).ready(function() {
					var data = ${fns:toJson(yybMusician.yybMusicianAlbumList)};
					for (var i=0; i<data.length; i++){
						addRow('#yybMusicianAlbumList', yybMusicianAlbumRowIdx, yybMusicianAlbumTpl, data[i]);
						yybMusicianAlbumRowIdx = yybMusicianAlbumRowIdx + 1;
					}
				});
			</script>
			</div>
		</div>
		</div>
		</form:form>
</body>
</html>