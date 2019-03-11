<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>订单管理</title>
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
                jp.post("${ctx}/order/yybOrder/save",$('#inputForm').serialize(),function(data){
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
		<form:form id="inputForm" modelAttribute="yybOrder" action="${ctx}/order/yybOrder/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<table class="table table-bordered">
		   <tbody>
				<tr>
					<td class="width-15 active"><label class="pull-right">订单号：</label></td>
					<td class="width-35">
						<form:input path="orderNo" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">交易流水号：</label></td>
					<td class="width-35">
						<form:input path="tradeNo" htmlEscape="false"    class="form-control "/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">状态：</label></td>
					<td class="width-35">
						<form:select path="status" class="form-control ">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('order_status')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</td>
					<td class="width-15 active"><label class="pull-right">下单时间：</label></td>
					<td class="width-35">
						<form:input path="orderTime" htmlEscape="false"    class="form-control "/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">支付时间：</label></td>
					<td class="width-35">
						<form:input path="payTime" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">支付类型：</label></td>
					<td class="width-35">
						<form:select path="payType" class="form-control ">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('pay_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">订单金额：</label></td>
					<td class="width-35">
						<form:input path="orderAmount" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">支付金额：</label></td>
					<td class="width-35">
						<form:input path="payAmount" htmlEscape="false"    class="form-control "/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">购买人：</label></td>
					<td class="width-35">
						<form:input path="memberName" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">用户类型：</label></td>
					<td class="width-35">
						<form:select path="memberType" class="form-control ">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('user_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">手机号：</label></td>
					<td class="width-35">
						<form:input path="phone" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">性别：</label></td>
					<td class="width-35">
						<form:input path="memberSex" htmlEscape="false"    class="form-control "/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">地址：</label></td>
					<td class="width-35">
						<form:input path="memberAddress" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">身份证：</label></td>
					<td class="width-35">
						<form:input path="idCard" htmlEscape="false"    class="form-control "/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">身份证附件：</label></td>
					<td class="width-35">
						<sys:fileUpload path="idCardAttach"  value="${yybOrder.idCardAttach}" type="file" uploadPath="/order/yybOrder"/>
					</td>
					<td class="width-15 active"><label class="pull-right">组织机构：</label></td>
					<td class="width-35">
						<form:input path="orgCode" htmlEscape="false"    class="form-control "/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">组织机构附件：</label></td>
					<td class="width-35">
						<sys:fileUpload path="orgCodeAttach"  value="${yybOrder.orgCodeAttach}" type="file" uploadPath="/order/yybOrder"/>
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
				<li class="active"><a data-toggle="tab" href="#tab-1" aria-expanded="true">订单详情：</a>
                </li>
            </ul>
            <div class="tab-content">
				<div id="tab-1" class="tab-pane fade in  active">
			<a class="btn btn-white btn-sm" onclick="addRow('#yybOrderDeatilList', yybOrderDeatilRowIdx, yybOrderDeatilTpl);yybOrderDeatilRowIdx = yybOrderDeatilRowIdx + 1;" title="新增"><i class="fa fa-plus"></i> 新增</a>
			<table class="table table-striped table-bordered table-condensed">
				<thead>
					<tr>
						<th class="hide"></th>
						<th>音乐名称</th>
						<th>音乐单价</th>
						<th>音乐人</th>
						<th>专辑</th>
						<th>公司</th>
						<th>总价</th>
						<th>权利</th>
						<th>用途</th>
						<th>备注信息</th>
						<th width="10">&nbsp;</th>
					</tr>
				</thead>
				<tbody id="yybOrderDeatilList">
				</tbody>
			</table>
			<script type="text/template" id="yybOrderDeatilTpl">//<!--
				<tr id="yybOrderDeatilList{{idx}}">
					<td class="hide">
						<input id="yybOrderDeatilList{{idx}}_id" name="yybOrderDeatilList[{{idx}}].id" type="hidden" value="{{row.id}}"/>
						<input id="yybOrderDeatilList{{idx}}_delFlag" name="yybOrderDeatilList[{{idx}}].delFlag" type="hidden" value="0"/>
					</td>
					
					<td>
						<input id="yybOrderDeatilList{{idx}}_musicTitle" name="yybOrderDeatilList[{{idx}}].musicTitle" type="text" value="{{row.musicTitle}}"    class="form-control "/>
					</td>
					
					
					<td>
						<input id="yybOrderDeatilList{{idx}}_musicPrice" name="yybOrderDeatilList[{{idx}}].musicPrice" type="text" value="{{row.musicPrice}}"    class="form-control "/>
					</td>
					
					
					<td>
						<input id="yybOrderDeatilList{{idx}}_musicianName" name="yybOrderDeatilList[{{idx}}].musicianName" type="text" value="{{row.musicianName}}"    class="form-control "/>
					</td>
					
					
					<td>
						<input id="yybOrderDeatilList{{idx}}_albumName" name="yybOrderDeatilList[{{idx}}].albumName" type="text" value="{{row.albumName}}"    class="form-control "/>
					</td>
					
					
					<td>
						<input id="yybOrderDeatilList{{idx}}_companyName" name="yybOrderDeatilList[{{idx}}].companyName" type="text" value="{{row.companyName}}"    class="form-control "/>
					</td>
					
					
					<td>
						<input id="yybOrderDeatilList{{idx}}_musicTotal" name="yybOrderDeatilList[{{idx}}].musicTotal" type="text" value="{{row.musicTotal}}"    class="form-control "/>
					</td>
					
					
					<td>
						<input id="yybOrderDeatilList{{idx}}_rightSelectName" name="yybOrderDeatilList[{{idx}}].rightSelectName" type="text" value="{{row.rightSelectName}}"    class="form-control "/>
					</td>
					
					
					<td>
						<input id="yybOrderDeatilList{{idx}}_usageSelectName" name="yybOrderDeatilList[{{idx}}].usageSelectName" type="text" value="{{row.usageSelectName}}"    class="form-control "/>
					</td>
					
					
					<td>
						<input id="yybOrderDeatilList{{idx}}_remarks" name="yybOrderDeatilList[{{idx}}].remarks" type="text" value="{{row.remarks}}"    class="form-control "/>
					</td>
					
					<td class="text-center" width="10">
						{{#delBtn}}<span class="close" onclick="delRow(this, '#yybOrderDeatilList{{idx}}')" title="删除">&times;</span>{{/delBtn}}
					</td>
				</tr>//-->
			</script>
			<script type="text/javascript">
				var yybOrderDeatilRowIdx = 0, yybOrderDeatilTpl = $("#yybOrderDeatilTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
				$(document).ready(function() {
					var data = ${fns:toJson(yybOrder.yybOrderDeatilList)};
					for (var i=0; i<data.length; i++){
						addRow('#yybOrderDeatilList', yybOrderDeatilRowIdx, yybOrderDeatilTpl, data[i]);
						yybOrderDeatilRowIdx = yybOrderDeatilRowIdx + 1;
					}
				});
			</script>
			</div>
		</div>
		</div>
		</form:form>
</body>
</html>