jQuery.extend(jQuery.validator.messages, {
    required: "必填项",
	remote: "请修正该项",
	email: "请输入正确格式的电子邮件",
	url: "请输入合法的网址",
	date: "请输入合法的日期",
	dateISO: "请输入合法的日期 (ISO).",
	number: "请输入合法的数字",
	digits: "请输入整数",
	creditcard: "请输入合法的信用卡号",
	equalTo: "请再次输入相同的值",
	accept: "请输入拥有合法后缀名的字符串",
	maxlength: jQuery.validator.format("请输入一个 长度最多是 {0} 的字符串"),
	minlength: jQuery.validator.format("请输入一个 长度最少是 {0} 的字符串"),
	rangelength: jQuery.validator.format("请输入 一个长度介于 {0} 和 {1} 之间的字符串"),
	range: jQuery.validator.format("请输入一个介于 {0} 和 {1} 之间的值"),
	max: jQuery.validator.format("请输入一个最大为{0} 的值"),
	min: jQuery.validator.format("请输入一个最小为{0} 的值")
});

jQuery.validator.setDefaults({
	errorPlacement: function(error, element) {
		var spanId = '#' + element.attr('id') + '-tips';
		if ($(spanId).length > 0) {
	    	error.appendTo($(spanId));
	    } else {
	    	error.appendTo(element.parent());
	    }
	}
});

//特殊值:数字、字母、下划线
jQuery.validator.addMethod("special", function(value) {
	if(!/^[\A-Za-z0-9\_]+$/.test(value)){
		return false;
	};
	return true;
	}, '请输入数字、字母、下划线');

//中文和字母验证
jQuery.validator.addMethod("chineseAndEnglish", function(value) {
	if(/[\d| |_|\\]/g.test(value)){
		return false;
	}
	if(!/^[a-zA-z\u4E00-\u9FA5]*$/.test(value)){
		return false;
	}
	return true;
}, '请输入中文和英文字母');

//NULL验证
jQuery.validator.addMethod("isNotNULL", function(value) {
	if("NULL" == value.toUpperCase()){
		return false;
	}
	return true;
}, '不能输入null');

//中文
jQuery.validator.addMethod("chinese", function(value) {
	if(/[\u4e00-\u9fa5]/g.test(value)){
		return true;
	}
	return false;
}, '请输入中文');

//是否含有全角符号的函数
jQuery.validator.addMethod("isFullwidthChar", function(value) {
	if(/[\uff00-\uffff]/g.test(value)){
		return true;
	}
	return false;
}, '不能含有全角符号的函数');

//是否含有半角符号的函数
jQuery.validator.addMethod("isFullwidthChar", function(value) {
	if(/[\u0000-\u00ff]/g.test(value)){
		return true;
	}
	return false;
}, '不能含有半角符号的函数');

//常用:中文、字母、顿号、·
jQuery.validator.addMethod("allNameValid", function(value) {
	if(!/^[a-zA-z\u4E00-\u9FA5\、\·]+$/.test(value)){
		return false;
	}
	if(/^[\、\-\\\_]+$/.test(value)){
		return false;
	}
	if(/^[\``]+$/.test(value)){
		return false;
	}
	return true;
}, '姓名格式不合规范');

//正整数（不含0）
jQuery.validator.addMethod("numberNoInclude0", function(value) {
	if(/^([1-9][0-9]*)+$/.test(value)){
		return true;
	}
	return false;
}, '请输入正整数');

//正整数+正小数（如:价格、面积）
jQuery.validator.addMethod("intOrFloat", function(value) {
	//不是数字直接返回
	if(!((/^([1-9][0-9]*)+$/.test(value)) || (/^([1-9][0-9]*\.{1}[0-9]{1,2})+$/.test(value)) || (/^([0]\.{1}[0-9]{1,2})+$/.test(value)))){
		return false;
	}
	//排除0.00
	if(/^0\.0*$/.test(value)){
		return false;
	}
	return true;
}, '请输入正整数或两位小数');

//手机号验证
jQuery.validator.addMethod("telePhone", function(value) {
	if(!/(^1\d{10}$)/g.test(value)){
		return false;
	}
	return true;
}, '请输入正确的手机号码');

//验证日期是否比今天大(由于修改的时候不能调动jquery validate的方法，改用传统验证方式onchange)
function afterToday(obj){
	var buyOrSellflag;
	if(null != (obj.id.match("buy"))){
		buyOrSellflag = "buy";
	} else if(null != (obj.id.match("sell"))){
		buyOrSellflag = "sell";
	}
	var value;
	if(obj.type == "text"){
		value = obj.value;
	} else if(obj.type == "submit"){
		value = $("#"+buyOrSellflag+"EndDate").val();
	}
	if(afterToday2(value)){
		if(obj.type == "submit"){
			$("#"+buyOrSellflag+"-form").submit();
		}
		$("#"+buyOrSellflag+"EndDate-msg").hide();
	} else {
		$("#"+buyOrSellflag+"EndDate-msg").show();
	}
}

function afterToday2(value){
	var dateNow = new Date();
	var arr = value.split("-");
	var cur_Year = dateNow.getFullYear();
	var cur_Month = dateNow.getMonth()+1;
	if(cur_Month.toString().length == 1){
		cur_Month="0"+cur_Month;
	}
	var cur_Day = dateNow.getDate();
	if(cur_Day.toString().length == 1){
		cur_Day="0"+cur_Day;
	}
	if(arr[0] > cur_Year){
		return true;
	} else if(arr[0] < cur_Year){
		return false;
	} else if(arr[0] == cur_Year){
		if(arr[1] > cur_Month){
			return true;
		} else if(arr[1] < cur_Month){
			return false;
		} else if(arr[1] == cur_Month){
			if(arr[2] > cur_Day){
				return true;
			} else if(arr[2] <= cur_Day){
				return false;
			}
		}
	}
	return false;
}

//根据要求验证电子邮箱
jQuery.validator.addMethod("emailValid", function(value) {
	if(!/^([\.a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(\.[a-zA-Z0-9_-])+/.test(value)){
		return false;
	}
	return true;
}, '邮箱格式有误');	

//根据要求验证权证号 权证号格式:（2013）字第008821号
jQuery.validator.addMethod("certificateIdValid", function(value) {
	if(/^[\u4e00-\u9fa5\d\(\)\（\）]+$/.test(value)){
		return true;
	}
	return false;
}, '请输入包含中文、数字和括号');	

//根据要求验证规划用途
jQuery.validator.addMethod("purposeValid", function(value) {
	if(/^[ \%\&\$\@\#\！\~]+$/.test(value)){
		return false;
	}
	return true;
}, '不能包含无效字符如%&$@#！~');	
//常用:数字、字母、下划线、中文、括号
jQuery.validator.addMethod("commonChar", function(value) {
	if(!/^[\u4e00-\u9fa5\w\d\_\:]+$/.test(value)){
		return false;
	}
	return true;
}, '请输入中英文、数字、冒号和下划线');

//密码
jQuery.validator.addMethod("passwordValid", function(value) {
	if("" == value || "undefined" == value){
		return false;
	}
	var arr = value.split(" ");
	if(arr.length != 1){
		return false;
	}	
	if(!/^[\w\d\_]+$/.test(value)){
		return false;
	}
	return true;
}, '请输入数字、字母和下划线');
//用户名
jQuery.validator.addMethod("userNameValid", function(value) {
	if("" == value || "undefined" == value){
		return false;
	}
	if("NULL" == value.toUpperCase()){
		return false;
	}
	return true;
}, '不能为空，不能为null');
