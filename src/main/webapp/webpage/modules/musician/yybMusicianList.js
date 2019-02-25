<%@ page contentType="text/html;charset=UTF-8" %>
<script>
var typeFlag = "1";
$(document).ready(function() {
	$('#yybMusicianTable').bootstrapTable({
		 
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
               url: "${ctx}/musician/yybMusician/data",
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
                        jp.confirm('确认要删除该音乐人记录吗？', function(){
                       	jp.loading();
                       	jp.get("${ctx}/musician/yybMusician/delete?id="+row.id, function(data){
                   	  		if(data.success){
                   	  			$('#yybMusicianTable').bootstrapTable('refresh');
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
		        field: 'name',
		        title: '姓名',
		        sortable: true,
		        sortName: 'name'
		        ,formatter:function(value, row , index){
		        	value = jp.unescapeHTML(value);
				   <c:choose>
					   <c:when test="${fns:hasPermission('musician:yybMusician:edit')}">
					      return "<a href='javascript:edit(\""+row.id+"\")'>"+value+"</a>";
				      </c:when>
					  <c:when test="${fns:hasPermission('musician:yybMusician:view')}">
					      return "<a href='javascript:view(\""+row.id+"\")'>"+value+"</a>";
				      </c:when>
					  <c:otherwise>
					      return value;
				      </c:otherwise>
				   </c:choose>
		         }
		       
		    }
		    ,{
		        field: 'headPhoto',
		        title: '头像',
		        sortable: true,
		        sortName: 'headPhoto',
		        formatter:function(value, row , index){
		        	if (value == undefined || value == "" || value == null) {return "";}; var valueArray = value.split("|");
		        	var labelArray = [];
		        	for(var i =0 ; i<valueArray.length; i++){
		        		if(!/\.(gif|jpg|jpeg|png|GIF|JPG|PNG)$/.test(valueArray[i]))
		        		{
		        			labelArray[i] = "<a href=\""+valueArray[i]+"\" url=\""+valueArray[i]+"\" target=\"_blank\">"+decodeURIComponent(valueArray[i].substring(valueArray[i].lastIndexOf("/")+1))+"</a>"
		        		}else{
		        			labelArray[i] = '<img   onclick="jp.showPic(\''+valueArray[i]+'\')"'+' height="50px" src="'+valueArray[i]+'">';
		        		}
		        	}
		        	return labelArray.join(" ");
		        }
		       
		    }
			,{
		        field: 'stageName',
		        title: '艺名',
		        sortable: true,
		        sortName: 'stageName'
		       
		    }
			,{
		        field: 'address',
		        title: '地址',
		        sortable: true,
		        sortName: 'address'
		       
		    }
			,{
		        field: 'phone',
		        title: '手机',
		        sortable: true,
		        sortName: 'phone'
		       
		    }
			,{
		        field: 'mail',
		        title: '邮箱',
		        sortable: true,
		        sortName: 'mail'
		       
		    }
			,{
		        field: 'idCard',
		        title: '身份证号',
		        sortable: true,
		        sortName: 'idCard'
		       
		    }
		    ,{
		        field: 'idCardAttach',
		        title: '身份证附件',
		        sortable: true,
		        sortName: 'idCardAttach',
		        formatter:function(value, row , index){
		        	if (value == undefined || value == "" || value == null) {return "";}; var valueArray = value.split("|");
		        	var labelArray = [];
		        	for(var i =0 ; i<valueArray.length; i++){
		        		if(!/\.(gif|jpg|jpeg|png|GIF|JPG|PNG)$/.test(valueArray[i]))
		        		{
		        			labelArray[i] = "<a href=\""+valueArray[i]+"\" url=\""+valueArray[i]+"\" target=\"_blank\">"+decodeURIComponent(valueArray[i].substring(valueArray[i].lastIndexOf("/")+1))+"</a>"
		        		}else{
		        			labelArray[i] = '<img   onclick="jp.showPic(\''+valueArray[i]+'\')"'+' height="50px" src="'+valueArray[i]+'">';
		        		}
		        	}
		        	return labelArray.join(" ");
		        }
		       
		    }
		    ,{
		        field: 'production',
		        title: '作品',
		        sortable: true,
		        sortName: 'production',
		        formatter:function(value, row , index){
		        	if (value == undefined || value == "" || value == null) {return "";}; var valueArray = value.split("|");
		        	var labelArray = [];
		        	for(var i =0 ; i<valueArray.length; i++){
		        		if(!/\.(gif|jpg|jpeg|png|GIF|JPG|PNG)$/.test(valueArray[i]))
		        		{
		        			labelArray[i] = "<a href=\""+valueArray[i]+"\" url=\""+valueArray[i]+"\" target=\"_blank\">"+decodeURIComponent(valueArray[i].substring(valueArray[i].lastIndexOf("/")+1))+"</a>"
		        		}else{
		        			labelArray[i] = '<img   onclick="jp.showPic(\''+valueArray[i]+'\')"'+' height="50px" src="'+valueArray[i]+'">';
		        		}
		        	}
		        	return labelArray.join(" ");
		        }
		       
		    }
			,{
		        field: 'remarks',
		        title: '备注信息',
		        sortable: true,
		        sortName: 'remarks'
		       
		    }
			,{
		        field: 'type',
		        title: '类型',
		        sortable: true,
		        sortName: 'type',
		        formatter:function(value, row , index){
                	<c:if test="${fns:getUser().getLoginName()!='admin'}">
                        if (value == "1"){
                            typeFlag = "1";

                        } if (value == "2"){
                        	typeFlag = "2";
                        $("#add").attr("disabled",true);
                        $("#remove").attr("disabled",true);
                    	}
					</c:if>

                    return jp.getDictLabel(${fns:toJson(fns:getDictList('musician_type'))}, value, "-");
		        }
		       
		    }
			,{
		        field: 'companyName',
		        title: '归属公司',
		        sortable: true,
		        sortName: 'companyName'
		       
		    }
			,{
		        field: 'status',
		        title: '音乐人状态',
		        sortable: true,
		        sortName: 'status',
		        formatter:function(value, row , index){
                    if ("通过" == jp.getDictLabel(${fns:toJson(fns:getDictList('musician_status'))}, value, "-")){
                        return "通过";

                    }if ("拒绝" == jp.getDictLabel(${fns:toJson(fns:getDictList('musician_status'))}, value, "-")){
                        return "拒绝";

                    } if ("待审核" == jp.getDictLabel(${fns:toJson(fns:getDictList('musician_status'))}, value, "-")){
                    	var html = "<a onclick='pass(\""+row.id+"\")'>通过</a>  <a onclick='noPass(\""+row.id+"\")'>拒绝</a>"
                        return "待审核 " + html;

                    }
                }
		       
		    }
		     ]
		
		});
		
		  
	  if(navigator.userAgent.match(/(iPhone|iPod|Android|ios)/i)){//如果是移动端

		 
		  $('#yybMusicianTable').bootstrapTable("toggleView");
		}
	  
	  $('#yybMusicianTable').on('check.bs.table uncheck.bs.table load-success.bs.table ' +
                'check-all.bs.table uncheck-all.bs.table', function () {
            $('#remove').prop('disabled', ! $('#yybMusicianTable').bootstrapTable('getSelections').length);
            $('#view,#edit').prop('disabled', $('#yybMusicianTable').bootstrapTable('getSelections').length!=1);
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
					  jp.downloadFile('${ctx}/musician/yybMusician/import/template');
				  },
			    btn2: function(index, layero){
						var iframeWin = layero.find('iframe')[0]; //得到iframe页的窗口对象，执行iframe页的方法：iframeWin.method();
						iframeWin.contentWindow.importExcel('${ctx}/musician/yybMusician/import', function (data) {
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
            var sortName = $('#yybMusicianTable').bootstrapTable("getOptions", "none").sortName;
            var sortOrder = $('#yybMusicianTable').bootstrapTable("getOptions", "none").sortOrder;
            var values = "";
            for(var key in searchParam){
                values = values + key + "=" + searchParam[key] + "&";
            }
            if(sortName != undefined && sortOrder != undefined){
                values = values + "orderBy=" + sortName + " "+sortOrder;
            }

			jp.downloadFile('${ctx}/musician/yybMusician/export?'+values);
	  })
	  $("#search").click("click", function() {// 绑定查询按扭
		  $('#yybMusicianTable').bootstrapTable('refresh');
		});
	 
	 $("#reset").click("click", function() {// 绑定查询按扭
		  $("#searchForm  input").val("");
		  $("#searchForm  select").val("");
		   $("#searchForm  .select-item").html("");
		  $('#yybMusicianTable').bootstrapTable('refresh');
		});
		
		
	});

  function getIdSelections() {
        return $.map($("#yybMusicianTable").bootstrapTable('getSelections'), function (row) {
            return row.id
        });
    }
  
  function deleteAll(){
      if (typeFlag == "2") {
          jp.error("您是独立音乐人， 不能进行新增")
		  return;
      }
		jp.confirm('确认要删除该音乐人记录吗？', function(){
			jp.loading();  	
			jp.get("${ctx}/musician/yybMusician/deleteAll?ids=" + getIdSelections(), function(data){
         	  		if(data.success){
         	  			$('#yybMusicianTable').bootstrapTable('refresh');
         	  			jp.success(data.msg);
         	  		}else{
         	  			jp.error(data.msg);
         	  		}
         	  	})
          	   
		})
  }
  
    //刷新列表
  function refresh() {
      $('#yybMusicianTable').bootstrapTable('refresh');
  }
  function add(){
  		if (typeFlag == "2") {
            jp.error("您是独立音乐人， 不能进行新增操作")
			return;
		}
	  jp.openSaveDialog('新增音乐人', "${ctx}/musician/yybMusician/form",'800px', '500px');
  }
  
   function edit(id){//没有权限时，不显示确定按钮

       if(id == undefined){
	      id = getIdSelections();
	}
	jp.openSaveDialog('编辑音乐人', "${ctx}/musician/yybMusician/form?id=" + id, '800px', '500px');
  }

  
 function view(id){//没有权限时，不显示确定按钮
      if(id == undefined){
             id = getIdSelections();
      }
        jp.openViewDialog('查看音乐人', "${ctx}/musician/yybMusician/form?id=" + id, '800px', '500px');
 }
  
  
  
  
		   
  function detailFormatter(index, row) {
	  var htmltpl =  $("#yybMusicianChildrenTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
	  var html = Mustache.render(htmltpl, {
			idx:row.id
		});
	  $.get("${ctx}/musician/yybMusician/detail?id="+row.id, function(yybMusician){
    	var yybMusicianChild1RowIdx = 0, yybMusicianChild1Tpl = $("#yybMusicianChild1Tpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
		var data1 =  yybMusician.yybMusicianAlbumList;
		for (var i=0; i<data1.length; i++){
			data1[i].dict = {};
			addRow('#yybMusicianChild-'+row.id+'-1-List', yybMusicianChild1RowIdx, yybMusicianChild1Tpl, data1[i]);
			yybMusicianChild1RowIdx = yybMusicianChild1RowIdx + 1;
		}
				
      	  			
      })
     
        return html;
    }
  
	function addRow(list, idx, tpl, row){
		$(list).append(Mustache.render(tpl, {
			idx: idx, delBtn: true, row: row
		}));
	}

    function pass(id){
  		var apiHost = window.location.protocol+"//"+window.location.host+"/api";
        jp.confirm('确认要通过音乐人记录吗？', function(){
            jp.loading();
            jp.get(apiHost + "/musician/pass?id=" + id, function(data){
                if(data.success){
                    $('#yybMusicianTable').bootstrapTable('refresh');
                    jp.alert(data.msg);
                }else{
                    jp.error(data.msg);
                }
            })

        })
	}

    function noPass(id){
        var apiHost = window.location.protocol+"//"+window.location.host+"/api";
        jp.confirm('确认要通过音乐人记录吗？', function(){
            jp.loading();
            jp.get(apiHost + "/musician/noPass?id=" + id, function(data){
                if(data.success){
                    $('#yybMusicianTable').bootstrapTable('refresh');
                    jp.success(data.msg);
                }else{
                    jp.error(data.msg);
                }
            })

        })
	}


</script>
<script type="text/template" id="yybMusicianChildrenTpl">//<!--
	<div class="tabs-container">
		<ul class="nav nav-tabs">
				<li class="active"><a data-toggle="tab" href="#tab-{{idx}}-1" aria-expanded="true">专辑</a></li>
		</ul>
		<div class="tab-content">
				 <div id="tab-{{idx}}-1" class="tab-pane fade in active">
						<table class="ani table">
						<thead>
							<tr>
								<th>音乐人</th>
								<th>专辑名</th>
								<th>封面</th>
								<th>备注信息</th>
							</tr>
						</thead>
						<tbody id="yybMusicianChild-{{idx}}-1-List">
						</tbody>
					</table>
				</div>
		</div>//-->
	</script>
	<script type="text/template" id="yybMusicianChild1Tpl">//<!--
				<tr>
					<td>
						{{row.yybMusician.name}}
					</td>
					<td>
						{{row.name}}
					</td>
					<td>
						<img   onclick="jp.showPic('{{row.img}}')" height="50px" src="{{row.img}}">
					</td>
					<td>
						{{row.remarks}}
					</td>
				</tr>//-->
	</script>
