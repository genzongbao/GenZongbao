$(function(){
	
	/**修改手机号验证*/
	$("#update_phone").validate({
		rules:{
			mobile : {
				required : true,
				telePhone:true,number:true,
				remote:{
					url: sys.contextPath+"/admin/checkMobile",     //后台处理程序 
					type: "post",               //数据发送方式
					dataType: "json",           //接受数据格式    
					data: {                     //要传递的数据
						newMobile: function() {
							return $("#newMobile").val();
						}
			  		}
		  		}
			},
			phoneCode : {
				required : true,
				remote:{
					url: sys.contextPath+"/admin/checkValidcode",     //后台处理程序 
					type: "post",               //数据发送方式
					dataType: "json",           //接受数据格式    
					data: {                     //要传递的数据
						validcode: function() {
							return $("#phoneCode").val();
						},
						mobile: function() {
							return $("#newMobile").val();
						}
			  		}
		  		}
			}
		},
		messages:{
			mobile : {required : "新手机号不能为空",telePhone:"手机号不正确",number:"请输入数字", remote:"手机号已存在"},
			phoneCode : {required : "验证码不能为空",remote:"验证码不正确"}
		},
		submitHandler : function(form) {
			form.submit(); //提交表单   
		},
		invalidHandler : function() {
			return false;
		}
	});
	//修改手机号 获取验证码
	$('#get_validcode').bind( "click",function() {
		var newMobile = $("#newMobile").val();
		var ispass=true;
		if($("#update_phone").validate().element($("#newMobile"))){
			$.ajax({
				type:"POST",
				url:sys.contextPath+"/admin/checkMobile?mobile="+newMobile,
				dataType:"json",
				success:function(data){
					if(data){
						$.post(sys.contextPath+"/admin/sendPhonecode",{mobile:$.trim($("#newMobile").val())}, function(result) {
							//这里的判断上环境后来改
							if (result == '000000') {
								countdown(2*60);
							} else {
								alert("验证码发送失败:"+result);
							}
						});
					}else{
						$("#newMobile-error").html("该手机号已存在！");
						$("#newMobile-error").show();
					}
				}
			});
		}
	});
})
//倒计时
function countdown(count) {
	var obj = $('#get_validcode');
	if (count > 0) {
		obj.html(count + 's后可发送');
		obj.attr('disabled', "true");
		obj.css("background-color", "#BBB");
		setTimeout('countdown(' + (count - 1) + ')', 1000);
	} else {
		obj.html('获取验证码');
		obj.css("background-color", "#81c0fd");
		obj.removeAttr('disabled');
	}
}