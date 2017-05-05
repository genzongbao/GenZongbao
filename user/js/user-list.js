//时间选择框
dateArray = ["minRegisterTime","maxRegisterTime","minLastLoginTime","maxLastLoginTime"];
//添加管理员
$(function() {
	//身份证号码验证 
	jQuery.validator.addMethod("isIdCardNo", function(value, element) { 
	  return this.optional(element) || idCardNoUtil.checkIdCardNo(value);     
	}, "请正确输入您的身份证号码"); 
	//数据验证
	$("#user-form").validate({
		rules:{
			loginName:{
				required:true,
				minlength:3,
				remote:{
					url: "check-log-name",     //后台处理程序 
					type: "post",               //数据发送方式
					dataType: "json",           //接受数据格式    
					data: {                     //要传递的数据
						loginName: function() {
							return $("#loginName").val();
						}
			  		}
			  	}	
			},
			password:{
				required:true,
				minlength:5,
				maxlength:18
			},
			repassword:{
				equalTo:"#password"
			},
			realName:"required",
			idCardNo:{
				required:true,
				minlength:16,
				maxlength:24,
				remote:{
					url: "check-idCard",     //后台处理程序 
					type: "post",               //数据发送方式
					dataType: "json",           //接受数据格式    
					data: {                     //要传递的数据
						idCardNo: function() {
							return $("#idCardNo").val();
						}
			  		}
			  	},
			  	isIdCardNo:true
			},
			mobile:{
				required:true,
				minlength:10,
				maxlength:11,
				remote:{
					url: "check-mobile",     //后台处理程序 
					type: "post",               //数据发送方式
					dataType: "json",           //接受数据格式    
					data: {                     //要传递的数据
						mobile: function() {
							return $("#mobile").val();
						}
			  		}
			  	}	
			}
		},
		messages:{
			loginName:{
				required:"登录账号不能为空",
				minlength:"登录账号不能小于3个字符",
				remote:"登录账号已被使用！"
			},
			password:{required:"请输入密码",minlength:"密码不能小于5位",maxlength:"密码不能大于24位"},
			repassword:{equalTo:"两次密码不一致"},
			realName:{required:"请输入真实姓名"},
			idCardNo:{required:"请输入身份证号",minlength:"身份证长度不能小于16位",maxlength:"身份证长度不能大于18位",remote:"身份证号已存在",isIdCardNo:"身份证号不正确"},
			mobile:{required:"请输入手机号",minlength:"手机号长度不能小于10位",maxlength:"手机号长度不能大于11位",remote:"该手机号已存在用户"}
		},
		submitHandler:function(form){
			if(confirm("确定添加吗？")) {
				form.submit();
			}
		},
		invalidHandler:function(){
			return false;
		}
	})
});
//提交表单
function userAdd(){
	$("#user-form").submit();
}
//修改添加用户的modal的样式
function updateUserModal(){
	$("#userTable").css("display","block");
	$("#addUserModal").modal("show");
}
/**
 * 修改状态
 */
function editState(userId,state) {
	var alertMsg;
	if(state == "VALID") {
		alertMsg = "确定解冻吗？";
	}else if(state == "FREEZED") {
		alertMsg = "确定冻结吗？";
	}else{
		alertMsg = "确定注销吗？";
	}
	if(confirm(alertMsg)) {
		window.location.href="edit-state?state="+state+"&userId="+userId;
	}
}
//恢复
function recovery(userId,state) {
	if(confirm("确定恢复吗？")) {
		window.location.href="edit-state?state="+state+"&userId="+userId;
	}
}
/**
 * 打开修改角色对话框
 */
function editPermissionShow(userId,roles) {
	$("#userId").val(userId);
	$("#roleTable").find(":checkbox").each(function() {
		$(this).attr("checked",false);
	});
	$("#roleTable").show();
	var roleArray = roles.split(",");
	for(var i=0;i<roleArray.length;i++) {
		$("#roleTable").find(":checkbox").each(function() {
			if(roleArray[i] == $(this).val()) {
				$(this).attr("checked",true);
			}
		});
	}
	window.scrollTo(0,0);
	$("#roleModal").modal({});
}
function editPermission() {
	if(confirm("确定授权吗？")) {
		var roles = "";
		$("#roleTable").find(":checkbox").each(function() {
			if($(this)[0].checked) {
				if(roles == "") {
					roles = $(this).val();
				}else{
					roles = roles+","+$(this).val();
				}
			}
		});
		$("input[name=roleIds]").val(roles);
		$("#edit-form").submit();
	}
}
//
function checkedTd(roleId){
	$("#roleTable").find(":checkbox").each(function() {
		if(roleId == $(this).val()) {
			if($(this).prop('checked')){
				$(this).prop('checked',false);
			}else{
				$(this).prop('checked',true);
			}
		}
	});
}
/*判断结束时间是否小于或者等于开始时间*/
function valiLastLoginTime(){
	if($("#maxLastLoginTime").val()=="" && $("#minLastLoginTime").val()==""){
    	return true;
    }
   else if($("#maxLastLoginTime").val()<=$("#minLastLoginTime").val()){
    	alert("开始时间不能在结束时间之前或者当天");
    	return false;
    }else{
    	return true;
    }
}
function valiRegisterTime(){
	if($("#maxRegisterTime").val()=="" && $("#minRegisterTime").val()==""){
    	return true;
    }
   else if($("#maxRegisterTime").val()<=$("#minRegisterTime").val()){
    	alert("开始时间不能在结束时间之前或者当天");
    	return false;
    }else{
    	return true;
    }
}
//验证时间
function valiTime(){
	if(valiLastLoginTime() == true && valiRegisterTime() == true) {
		return true;
	}else{
		return false;
	}
}
