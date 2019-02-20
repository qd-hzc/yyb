<%@ page contentType="text/html;charset=UTF-8" %>
<script>
	    var $yybTagCategoryTreeTable=null;  
		$(document).ready(function() {
			$yybTagCategoryTreeTable=$('#yybTagCategoryTreeTable').treeTable({  
		    	   theme:'vsStyle',	           
					expandLevel : 2,
					column:0,
					checkbox: true,
		            url:'${ctx}/tagcatetory/yybTagCategory/getChildren?parentId=',  
		            callback:function(item) { 
		            	 var treeTableTpl= $("#yybTagCategoryTreeTableTpl").html();
		            	 item.dict = {};

		            	 var result = laytpl(treeTableTpl).render({
								  row: item
							});
		                return result;                   
		            },  
		            beforeClick: function($yybTagCategoryTreeTable, id) { 
		                //异步获取数据 这里模拟替换处理  
		                $yybTagCategoryTreeTable.refreshPoint(id);  
		            },  
		            beforeExpand : function($yybTagCategoryTreeTable, id) {   
		            },  
		            afterExpand : function($yybTagCategoryTreeTable, id) {  
		            },  
		            beforeClose : function($yybTagCategoryTreeTable, id) {    
		            	
		            }  
		        });
		        
		        $yybTagCategoryTreeTable.initParents('${parentIds}', "0");//在保存编辑时定位展开当前节点

    $.each($(".tree_table tr"), function(i, value) {
    if ($(value).attr("pid") != undefined) {
    $(value).find(".btn-group").remove()
}
})


});
		
		function del(con,id){  
			jp.confirm('确认要删除标签分类吗？', function(){
				jp.loading();
	       	  	$.get("${ctx}/tagcatetory/yybTagCategory/delete?id="+id, function(data){
	       	  		if(data.success){
	       	  			$yybTagCategoryTreeTable.del(id);
	       	  			jp.success(data.msg);
	       	  		}else{
	       	  			jp.error(data.msg);
	       	  		}
	       	  	})
	       	   
       		});
	
		} 
		
		function refreshNode(data) {//刷新节点
            var current_id = data.body.yybTagCategory.id;
			var target = $yybTagCategoryTreeTable.get(current_id);
			var old_parent_id = target.attr("pid") == undefined?'1':target.attr("pid");
			var current_parent_id = data.body.yybTagCategory.parentId;
			var current_parent_ids = data.body.yybTagCategory.parentIds;
			if(old_parent_id == current_parent_id){
				if(current_parent_id == '0'){
					$yybTagCategoryTreeTable.refreshPoint(-1);
				}else{
					$yybTagCategoryTreeTable.refreshPoint(current_parent_id);
				}
			}else{
				$yybTagCategoryTreeTable.del(current_id);//刷新删除旧节点
				$yybTagCategoryTreeTable.initParents(current_parent_ids, "0");
			}

    $.each($(".tree_table tr"), function(i, value) {
    if ($(value).attr("pid") != undefined) {
    $(value).find(".btn-group").remove()
}
})
        }
		function refresh(){//刷新
			var index = jp.loading("正在加载，请稍等...");
			$yybTagCategoryTreeTable.refresh();
			jp.close(index);

    $.each($(".tree_table tr"), function(i, value) {
    if ($(value).attr("pid") != undefined) {
    $(value).find(".btn-group").remove()
}
})
		}
</script>
<script type="text/html" id="yybTagCategoryTreeTableTpl">
			<td>
			<c:choose>
			      <c:when test="${fns:hasPermission('tagcatetory:yybTagCategory:edit')}">
				    <a  href="#" onclick="jp.openSaveDialog('编辑标签分类', '${ctx}/tagcatetory/yybTagCategory/form?id={{d.row.id}}','800px', '500px')">
							{{d.row.name === undefined ? "": d.row.name}}
					</a>
			      </c:when>
			      <c:when test="${fns:hasPermission('tagcatetory:yybTagCategory:view')}">
				    <a  href="#" onclick="jp.openViewDialog('查看标签分类', '${ctx}/tagcatetory/yybTagCategory/form?id={{d.row.id}}','800px', '500px')">
							{{d.row.name === undefined ? "": d.row.name}}
					</a>
			      </c:when>
			      <c:otherwise>
							{{d.row.name === undefined ? "": d.row.name}}
			      </c:otherwise>
			</c:choose>
			</td>
			<td>
							{{d.row.createDate === undefined ? "": d.row.createDate}}
			</td>
			<td>
							{{d.row.remarks === undefined ? "": d.row.remarks}}
			</td>
			<td>
				<div class="btn-group">
			 		<button type="button" class="btn  btn-primary btn-xs dropdown-toggle" data-toggle="dropdown">
						<i class="fa fa-cog"></i>
						<span class="fa fa-chevron-down"></span>
					</button>
				  <ul class="dropdown-menu" role="menu">
					<shiro:hasPermission name="tagcatetory:yybTagCategory:view">
						<li><a href="#" onclick="jp.openViewDialog('查看标签分类', '${ctx}/tagcatetory/yybTagCategory/form?id={{d.row.id}}','800px', '500px')"><i class="fa fa-search-plus"></i> 查看</a></li>
					</shiro:hasPermission>
					<shiro:hasPermission name="tagcatetory:yybTagCategory:edit">
						<li><a href="#" onclick="jp.openSaveDialog('修改标签分类', '${ctx}/tagcatetory/yybTagCategory/form?id={{d.row.id}}','800px', '500px')"><i class="fa fa-edit"></i> 修改</a></li>
		   			</shiro:hasPermission>
		   			<shiro:hasPermission name="tagcatetory:yybTagCategory:del">
		   				<li><a  onclick="return del(this, '{{d.row.id}}')"><i class="fa fa-trash"></i> 删除</a></li>
					</shiro:hasPermission>
		   			<shiro:hasPermission name="tagcatetory:yybTagCategory:add">
						<li><a href="#" onclick="jp.openSaveDialog('添加下级标签分类', '${ctx}/tagcatetory/yybTagCategory/form?parent.id={{d.row.id}}','800px', '500px')"><i class="fa fa-plus"></i> 添加下级标签分类</a></li>
					</shiro:hasPermission>
				  </ul>
				</div>
			</td>
	</script>