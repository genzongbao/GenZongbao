function submitForm(){
	if(checkLogin()){
		$.ajax({
			type: "POST",
			url:sys.contextPath+"/auth/app-login?num="+Math.random(),
			data:$('#form').serialize(),
			dataType:"json",
			success: function(data) {
				if(data && data.error){
					$.alert(data.msg,function(){
						$(".weui-mask").remove();
						$(".weui-dialog").remove();
					});
				} else if(data && data.data){
					var obj = eval(data.data);
					window.android.login(JSON.stringify(obj));
				}
			}
		});
	}
}

function closeWindow() {
    var timestamp;
    var nonceStr;
    var signature;
    $.get("/auth/getJSTicket", function (data) {
        timestamp = data.timestamp;
        nonceStr = data.nonceStr;
        signature = data.signature;
    }, "json");
    wx.config({
        debug: false,
        appId: 'wxc7e55232740f1077',
        timestamp: timestamp,
        nonceStr: nonceStr,
        signature: signature,
        jsApiList: [
            'closeWindow',
        ]
    });
    wx.error(function (res) {
        $.alert(res.errMsg,function(){
			$(".weui-mask").remove();
			$(".weui-dialog").remove();
		});
    });
}

function submitCompanyForm(){
	$.post(sys.contextPath+"/auth/companyLogin?num"+Math.random(),$('#companyForm').serialize(),function(data){
		if(data.alertMsg){
			window.android.error(data.alertMsg);
		} else {
			var obj = eval(data.customer);
			window.android.login(JSON.stringify(obj));
		}
	})
}
function submitRegister(){
	var regFlag=false;
	if($("[name='validcode']").val().length==4){//验证码4位
		 $.post(sys.contextPath+"/auth/checkValidcode",$('#form').serialize(),function(data){
			if(data){
				regFlag=true;
			}else{
				$.alert("验证码不正确",function(){
					$(".weui-mask").remove();
					$(".weui-dialog").remove();
				});
				regFlag=false;
			}
		},"json");
	}else{
		$.alert("验证码不正确",function(){
			$(".weui-mask").remove();
			$(".weui-dialog").remove();
		});
		return;
	}
	if(regFlag){
		$.ajax({
			type: "POST",
			url:sys.contextPath+"/auth/app-register?num="+Math.random(),
			data:$('#form').serialize(),
			dataType:"json",
			success: function(data) {
				if(data.alertMsg){
	        		window.android.error(data.alertMsg);
	        	} else {
	        		var obj = eval(data.customer);
					window.android.login(JSON.stringify(obj));
	        	}
			}
		});
	}
	
}
/**
 * 登录验证
 */
function checkLogin(){
	var cusPhone=$("[name='cusPhone']").val();
	var cusPassword=$("[name='cusPassword']").val();
	if(cusPhone=='' || $.trim(cusPhone)==null){
		$.alert("请输入用户名",function(){
			$(".weui-mask").remove();
			$(".weui-dialog").remove();
		});
		return false;
	}
	if(cusPassword=='' || $.trim(cusPassword)==null){
		$.alert("请输入密码",function(){
			$(".weui-mask").remove();
			$(".weui-dialog").remove();
		});
		return false;
	}
	return true;
}
/**
 * 注册验证
 */
function checkRegister(){
	var regFlag=false;
	var cusLogName=$("[name='cusLogName']").val();
	var cusPhone=$("[name='cusPhone']").val();
	if($.trim(cusLogName)=='' || cusLogName==null){
		$.alert("请输入用户名",function(){
			$(".weui-mask").remove();
			$(".weui-dialog").remove();
		});
		return regFlag;
	}
	if($.trim(cusPhone)=='' || cusPhone==null){
		$.alert("请输入电话号码",function(){
			$(".weui-mask").remove();
			$(".weui-dialog").remove();
		});
		return regFlag;
	}
	if($.trim($("[name='cusPassword']").val())==''||$.trim($("[name='reCusPassword']").val())==''){
		$.alert("请输入密码",function(){
			$(".weui-mask").remove();
			$(".weui-dialog").remove();
		});
		return regFlag;
	}
	if($("[name='cusPassword']").val()!=$("[name='reCusPassword']").val()){
		$.alert("密码不一致",function(){
			$(".weui-mask").remove();
			$(".weui-dialog").remove();
		});
		return regFlag;
	}
	$.post(sys.contextPath+"/auth/checkLogName",{"cusLogName":cusLogName},function(data){
		if(!data){
			$.alert("用户名已存在",function(){
				$(".weui-mask").remove();
				$(".weui-dialog").remove();
			});
			regFlag=false;
		}else{
			$.post(sys.contextPath+"/auth/checkPhone",{"cusPhone":cusPhone},function(data){
				if(!data){
					$.alert("电话号码已经注册",function(){
						$(".weui-mask").remove();
						$(".weui-dialog").remove();
					});
					regFlag=false;
				}else{
					regFlag=true;
				}
			},"json");
		}
	},"json");
	return regFlag;
}


/**
 * 获取验证码
 */
function getCode(button){
	if(valid()){
		if($("#cusPhone").val()!="" && $("#cusPhone").val().length==11){
			if(checkRegister()){
				$.ajax({
					type: "POST",
					url:sys.contextPath+"/auth/sendValidcode?num="+Math.random()+"&cusPhone="+$("#cusPhone").val(),
					dataType:"text",
					success: function(data) {
						$.alert("获取验证码成功",function(){
							$(".weui-mask").remove();
							$(".weui-dialog").remove();
						});
						time(button,60);
					}
				});
			}
		}else{
			$.alert("手机号码不正确",function(){
				$(".weui-mask").remove();
				$(".weui-dialog").remove();
			});
		}
			
	} else {
		$.alert("请填写完整信息",function(){
			$(".weui-mask").remove();
			$(".weui-dialog").remove();
		});
	}
}
/**
 * 计时锁定
 * @param el a标签
 * @param wait 时间：秒
 */
function time(el,wait) {
	var button=$(el);
    if (wait<1) {
    	button.attr("onclick",method); 
    	button.html("获取验证码");
    	button.css("color","#21a9f5");
    	return;
    } else {
    	if(wait==60){
	    	button.removeAttr("onclick");         
	    	button.css("color","#CDCDCD");
    	}
    	button.html(wait + "秒后重新获取");
        wait--;
        setTimeout(function() {
            time(el,wait)
        },1000);
    }
}

function valid(){
	if($("#cusPhone").val()==""
		|| $("#_name").val()==""
		|| $("#_pwd").val()==""
		|| $("#rePwd").val()==""){
		return false
	}
	return true;
}
/**
 * 无账号体验
 */
function NoPwdTest(){
	window.android.loginNoPwd();
}



/*===================忘记密码========================*/

/**
 * 获取验证码
 */
function getCode_forget(button){
		if($("#cusPhone").val()!="" && $("#cusPhone").val().length==11){
			$.ajax({
				type: "POST",
				url:sys.contextPath+"/auth/sendValidcode-forget?num="+Math.random()+"&cusPhone="+$("#cusPhone").val(),
				dataType:"json",
				success: function(data) {
					if(data && data.error){
						$.alert(data["msg"],function(){
							$(".weui-mask").remove();
							$(".weui-dialog").remove();
						});
					}else{
						$.alert(data["msg"],function(){
							$(".weui-mask").remove();
							$(".weui-dialog").remove();
						});
						time(button,60);
					}
				}
			});
		}else{
			$.alert("手机号码不正确",function(){
				$(".weui-mask").remove();
				$(".weui-dialog").remove();
			});
		}
}
/**
 * 修改密码
 */
function editPwd(){
	if($("[name='firstPwd']").val()==""){
		$.alert("请填写规范",function(){
			$(".weui-mask").remove();
			$(".weui-dialog").remove();
		});
		return;
	}
	if($("[name='secondPwd']").val()!=$("[name='firstPwd']").val()){
		$.alert("两次密码不一致",function(){
			$(".weui-mask").remove();
			$(".weui-dialog").remove();
		});
		return;
	}
	$.post(sys.contextPath+"/auth/editPassword-service",$("#form_editPwd").serialize(),function(data){
		if(data && data.error){
			$.alert(data.msg,function(){
				$(".weui-mask").remove();
				$(".weui-dialog").remove();
			});
		}else{
			$.alert(data.msg,function(){
				$(".weui-mask").remove();
				$(".weui-dialog").remove();
				window.location=sys.contextPath+"/auth/login";
			});
		}
	},"json");
}