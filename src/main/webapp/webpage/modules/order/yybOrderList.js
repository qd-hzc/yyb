<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function() {
	$('#yybOrderTable').bootstrapTable({
		 
		  //请求方法
               method: 'post',
               //类型json
               dataType: "json",
               contentType: "application/x-www-form-urlencoded",
               //显示检索按钮
	       showSearch: true,
               //显示刷新按钮
               showRefresh: true,
               //显示切换手机试图按钮
               showToggle: true,
               //显示 内容列下拉框
    	       showColumns: true,
    	       //显示到处按钮
    	       showExport: true,
    	       //显示切换分页按钮
    	       showPaginationSwitch: true,
    	       //显示详情按钮
    	       detailView: true,
    	       	//显示详细内容函数
	           detailFormatter: "detailFormatter",
    	       //最低显示2行
    	       minimumCountColumns: 2,
               //是否显示行间隔色
               striped: true,
               //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）     
               cache: false,    
               //是否显示分页（*）  
               pagination: true,   
                //排序方式 
               sortOrder: "asc",  
               //初始化加载第一页，默认第一页
               pageNumber:1,   
               //每页的记录行数（*）   
               pageSize: 10,  
               //可供选择的每页的行数（*）    
               pageList: [10, 25, 50, 100],
               //这个接口需要处理bootstrap table传递的固定参数,并返回特定格式的json数据  
               url: "${ctx}/order/yybOrder/data",
               //默认值为 'limit',传给服务端的参数为：limit, offset, search, sort, order Else
               //queryParamsType:'',   
               ////查询参数,每次调用是会带上这个参数，可自定义                         
               queryParams : function(params) {
               	var searchParam = $("#searchForm").serializeJSON();
               	searchParam.pageNo = params.limit === undefined? "1" :params.offset/params.limit+1;
               	searchParam.pageSize = params.limit === undefined? -1 : params.limit;
               	searchParam.orderBy = params.sort === undefined? "" : params.sort+ " "+  params.order;
                   return searchParam;
               },
               //分页方式：client客户端分页，server服务端分页（*）
               sidePagination: "server",
               contextMenuTrigger:"right",//pc端 按右键弹出菜单
               contextMenuTriggerMobile:"press",//手机端 弹出菜单，click：单击， press：长按。
               contextMenu: '#context-menu',
               onContextMenuItem: function(row, $el){
                   if($el.data("item") == "edit"){
                   		edit(row.id);
                   }else if($el.data("item") == "view"){
                       view(row.id);
                   } else if($el.data("item") == "delete"){
                        jp.confirm('确认要删除该订单记录吗？', function(){
                       	jp.loading();
                       	jp.get("${ctx}/order/yybOrder/delete?id="+row.id, function(data){
                   	  		if(data.success){
                   	  			$('#yybOrderTable').bootstrapTable('refresh');
                   	  			jp.success(data.msg);
                   	  		}else{
                   	  			jp.error(data.msg);
                   	  		}
                   	  	})
                   	   
                   	});
                      
                   } 
               },
              
               onClickRow: function(row, $el){
               },
               	onShowSearch: function () {
			$("#search-collapse").slideToggle();
		},
               columns: [{
		        checkbox: true
		       
		    }
			,{
		        field: 'orderNo',
		        title: '订单号',
		        sortable: true,
		        sortName: 'orderNo'
		        ,formatter:function(value, row , index){
		        	value = jp.unescapeHTML(value);
				   <c:choose>
					   <c:when test="${fns:hasPermission('order:yybOrder:edit')}">
					      return "<a href='javascript:edit(\""+row.id+"\")'>"+value+"</a>";
				      </c:when>
					  <c:when test="${fns:hasPermission('order:yybOrder:view')}">
					      return "<a href='javascript:view(\""+row.id+"\")'>"+value+"</a>";
				      </c:when>
					  <c:otherwise>
					      return value;
				      </c:otherwise>
				   </c:choose>
		         }
		       
		    }
			,{
		        field: 'tradeNo',
		        title: '交易流水号',
		        sortable: true,
		        sortName: 'tradeNo'
		       
		    }
			,{
		        field: 'status',
		        title: '状态',
		        sortable: true,
		        sortName: 'status',
		        formatter:function(value, row , index){
		        	return jp.getDictLabel(${fns:toJson(fns:getDictList('order_status'))}, value, "-");
		        }
		       
		    }
			,{
		        field: 'orderTime',
		        title: '下单时间',
		        sortable: true,
		        sortName: 'orderTime'
		       
		    }
			,{
		        field: 'payTime',
		        title: '支付时间',
		        sortable: true,
		        sortName: 'payTime'
		       
		    }
			,{
		        field: 'payType',
		        title: '支付类型',
		        sortable: true,
		        sortName: 'payType',
		        formatter:function(value, row , index){
		        	return jp.getDictLabel(${fns:toJson(fns:getDictList('pay_type'))}, value, "-");
		        }
		       
		    }
			,{
		        field: 'orderAmount',
		        title: '订单金额',
		        sortable: true,
		        sortName: 'orderAmount'
		       
		    }
			,{
		        field: 'payAmount',
		        title: '支付金额',
		        sortable: true,
		        sortName: 'payAmount'
		       
		    }
			,{
		        field: 'memberId',
		        title: '会员',
		        sortable: true,
		        sortName: 'memberId'
		       
		    }
			,{
		        field: 'memberName',
		        title: '购买人',
		        sortable: true,
		        sortName: 'memberName'
		       
		    }
			,{
		        field: 'memberAddress',
		        title: '地址',
		        sortable: true,
		        sortName: 'memberAddress'
		       
		    }
			,{
		        field: 'idCard',
		        title: '身份证',
		        sortable: true,
		        sortName: 'idCard'
		       
		    }
			,{
		        field: 'idCardAttach',
		        title: '身份证附件',
		        sortable: true,
		        sortName: 'idCardAttach'
		       
		    }
			,{
		        field: 'orgCode',
		        title: '组织机构',
		        sortable: true,
		        sortName: 'orgCode'
		       
		    }
			,{
		        field: 'orgCodeAttach',
		        title: '组织机构附件',
		        sortable: true,
		        sortName: 'orgCodeAttach'
		       
		    }
			,{
		        field: 'memberType',
		        title: '用户类型',
		        sortable: true,
		        sortName: 'memberType',
		        formatter:function(value, row , index){
		        	return jp.getDictLabel(${fns:toJson(fns:getDictList('user_type'))}, value, "-");
		        }
		       
		    }
			,{
		        field: 'phone',
		        title: '手机号',
		        sortable: true,
		        sortName: 'phone'
		       
		    }
			,{
		        field: 'remarks',
		        title: '备注信息',
		        sortable: true,
		        sortName: 'remarks'
		       
		    }
		     ]
		
		});
		
		  
	  if(navigator.userAgent.match(/(iPhone|iPod|Android|ios)/i)){//如果是移动端

		 
		  $('#yybOrderTable').bootstrapTable("toggleView");
		}
	  
	  $('#yybOrderTable').on('check.bs.table uncheck.bs.table load-success.bs.table ' +
                'check-all.bs.table uncheck-all.bs.table', function () {
            $('#remove').prop('disabled', ! $('#yybOrderTable').bootstrapTable('getSelections').length);
            $('#view,#edit').prop('disabled', $('#yybOrderTable').bootstrapTable('getSelections').length!=1);
        });
		  
		$("#btnImport").click(function(){
			jp.open({
			    type: 2,
                area: [500, 200],
                auto: true,
			    title:"导入数据",
			    content: "${ctx}/tag/importExcel" ,
			    btn: ['下载模板','确定', '关闭'],
				btn1: function(index, layero){
					  jp.downloadFile('${ctx}/order/yybOrder/import/template');
				  },
			    btn2: function(index, layero){
						var iframeWin = layero.find('iframe')[0]; //得到iframe页的窗口对象，执行iframe页的方法：iframeWin.method();
						iframeWin.contentWindow.importExcel('${ctx}/order/yybOrder/import', function (data) {
							if(data.success){
								jp.success(data.msg);
								refresh();
							}else{
								jp.error(data.msg);
							}
						   jp.close(index);
						});//调用保存事件
						return false;
				  },
				 
				  btn3: function(index){ 
					  jp.close(index);
	    	       }
			}); 
		});
	  $("#export").click(function(){//导出Excel文件
	        var searchParam = $("#searchForm").serializeJSON();
	        searchParam.pageNo = 1;
	        searchParam.pageSize = -1;
            var sortName = $('#yybOrderTable').bootstrapTable("getOptions", "none").sortName;
            var sortOrder = $('#yybOrderTable').bootstrapTable("getOptions", "none").sortOrder;
            var values = "";
            for(var key in searchParam){
                values = values + key + "=" + searchParam[key] + "&";
            }
            if(sortName != undefined && sortOrder != undefined){
                values = values + "orderBy=" + sortName + " "+sortOrder;
            }

			jp.downloadFile('${ctx}/order/yybOrder/export?'+values);
	  })
	  $("#search").click("click", function() {// 绑定查询按扭
		  $('#yybOrderTable').bootstrapTable('refresh');
		});
	 
	 $("#reset").click("click", function() {// 绑定查询按扭
		  $("#searchForm  input").val("");
		  $("#searchForm  select").val("");
		   $("#searchForm  .select-item").html("");
		  $('#yybOrderTable').bootstrapTable('refresh');
		});
		
		
	});
		
  function getIdSelections() {
        return $.map($("#yybOrderTable").bootstrapTable('getSelections'), function (row) {
            return row.id
        });
    }
  
  function deleteAll(){

		jp.confirm('确认要删除该订单记录吗？', function(){
			jp.loading();  	
			jp.get("${ctx}/order/yybOrder/deleteAll?ids=" + getIdSelections(), function(data){
         	  		if(data.success){
         	  			$('#yybOrderTable').bootstrapTable('refresh');
         	  			jp.success(data.msg);
         	  		}else{
         	  			jp.error(data.msg);
         	  		}
         	  	})
          	   
		})
  }
  
    //刷新列表
  function refresh() {
      $('#yybOrderTable').bootstrapTable('refresh');
  }
  function add(){
	  jp.openSaveDialog('新增订单', "${ctx}/order/yybOrder/form",'800px', '500px');
  }
  
   function edit(id){//没有权限时，不显示确定按钮
       if(id == undefined){
	      id = getIdSelections();
	}
	jp.openSaveDialog('编辑订单', "${ctx}/order/yybOrder/form?id=" + id, '800px', '500px');
  }

  
 function view(id){//没有权限时，不显示确定按钮
      if(id == undefined){
             id = getIdSelections();
      }
        jp.openViewDialog('查看订单', "${ctx}/order/yybOrder/form?id=" + id, '800px', '500px');
 }
  
  
  
  
		   
  function detailFormatter(index, row) {
	  var htmltpl =  $("#yybOrderChildrenTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
	  var html = Mustache.render(htmltpl, {
			idx:row.id
		});
	  $.get("${ctx}/order/yybOrder/detail?id="+row.id, function(yybOrder){
    	var yybOrderChild1RowIdx = 0, yybOrderChild1Tpl = $("#yybOrderChild1Tpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
		var data1 =  yybOrder.yybOrderDeatilList;
		for (var i=0; i<data1.length; i++){
			data1[i].dict = {};
			addRow('#yybOrderChild-'+row.id+'-1-List', yybOrderChild1RowIdx, yybOrderChild1Tpl, data1[i]);
			yybOrderChild1RowIdx = yybOrderChild1RowIdx + 1;
		}
				
      	  			
      })
     
        return html;
    }
  
	function addRow(list, idx, tpl, row){
		$(list).append(Mustache.render(tpl, {
			idx: idx, delBtn: true, row: row
		}));
	}
			
</script>
<script type="text/template" id="yybOrderChildrenTpl">//<!--
	<div class="tabs-container">
		<ul class="nav nav-tabs">
				<li class="active"><a data-toggle="tab" href="#tab-{{idx}}-1" aria-expanded="true">订单详情</a></li>
		</ul>
		<div class="tab-content">
				 <div id="tab-{{idx}}-1" class="tab-pane fade in active">
						<table class="ani table">
						<thead>
							<tr>
								<th>订单</th>
								<th>购物车</th>
								<th>音乐</th>
								<th>音乐名称</th>
								<th>音乐单价</th>
								<th>用途</th>
								<th>权利</th>
								<th>总价</th>
								<th>备注信息</th>
							</tr>
						</thead>
						<tbody id="yybOrderChild-{{idx}}-1-List">
						</tbody>
					</table>
				</div>
		</div>//-->
	</script>
	<script type="text/template" id="yybOrderChild1Tpl">//<!--
				<tr>
					<td>
						{{row.yybOrder.id}}
					</td>
					<td>
						{{row.yybShopcart.id}}
					</td>
					<td>
						{{row.musicId}}
					</td>
					<td>
						{{row.musicTitle}}
					</td>
					<td>
						{{row.musicPrice}}
					</td>
					<td>
						{{row.usageSelect}}
					</td>
					<td>
						{{row.rightSelect}}
					</td>
					<td>
						{{row.musicTotal}}
					</td>
					<td>
						{{row.remarks}}
					</td>
				</tr>//-->
	</script>
