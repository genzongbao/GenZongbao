
$(function(){
	$("#roleAdd").validate({
		rules:{
			roleName:{required:true,
				remote:{
					url:sys.contextPath+"/admin/security/check-roleName",     //后台处理程序 
					type: "post",               //数据发送方式
					dataType: "json",           //接受数据格式    
					data: {                     //要传递的数据
						roleId: function() {
							return $("#roleId").val();
						},
						roleName: function() {
							return $("#roleName").val();
						},
						num: function() {
							return Math.random();
						}
					}
				}
			
			},
			roleDesc:{required:true}
		},
		messages:{
			roleName:{required:"请输入角色名",remote:"角色名已存在"},
			roleDesc:{required:"请输入角色描述"}
		}
	});
});

function newsEdit(newsId) {
	if(confirm("确定修改吗")) {
		$("#newsPublish").submit();
	}
}