dojo.require("dojo.parser");
dojo.require("dojo.data.api.Read");
dojo.require("dijit.form.Form");
dojo.require("dijit.form.Button");
dojo.require("dijit.form.TextBox");
dojo.require("dijit.form.ValidationTextBox");
dojo.require("dijit.form.DateTextBox");
dojo.require("dijit.Dialog");
dojo.require("dijit.Tooltip");
dojo.require("dijit.Toolbar");
dojo.require("dijit.Menu");
dojo.require("dijit.MenuItem");
dojo.require("dijit.ProgressBar");
dojo.require("dojo.date.locale");
dojo.require("dijit.form.FilteringSelect");
dojo.require("dojox.validate");
dojo.require("dojox.validate.check");
dojo.require("dojox.validate.web");



var userRegisterURL = "register.html";
var userLoginURL = "index.html";
var resetPasswordURL = "resetPassword.html";
var showUserInfoURL = "showUserInfo.html";
var loginGotoURL = showUserInfoURL;
var headerImage = "images/uas.jpg";
var loginHeaderImage = "images/uas_s.jpg"

user = {
	_t_ : "com.cnebula.um.ejb.entity.usr.UMPrincipal"
};

String.prototype.trim   =   function(){   
	  return   this.replace(/(^\s*)|(\s*$)/g,"");   
} 

function getPamater( name ){
	  name = name.replace(/[\[]/,"\\\[").replace(/[\]]/,"\\\]");
	  var regexS = "[\\?&]"+name+"=([^&#]*)";
	  var regex = new RegExp( regexS );
	  var results = regex.exec( window.location.href );
	  return results ? decodeURIComponent(results[1]) : null;
}

//init userLoginURL & loginGotoURL
{	
	var tmpURL = getPamater("userLoginURL");
	if (tmpURL){
		userLoginURL = tmpURL;
	}
	tmpURL = getPamater("loginGotoURL");
	if (tmpURL){
		loginGotoURL = tmpURL;
	}
	tmpURL = getPamater("headerImage");
	if (tmpURL){
		headerImage = tmpURL;
	}
	tmpURL = getPamater("loginHeaderImage");
	if (tmpURL){
		loginHeaderImage = tmpURL;
	}
	
}


function handleOrgList(){
	var nodeQueryService = null;
	try{
		nodeQueryService = EasyServiceClient.lookup(null, null, "com.cnebula.uas.service.INodeQueryService");
	}catch(e){
		return;
	}
	var nodelist = null;
	try {
		nodelist = nodeQueryService.getNodeList();
	} catch (e) {
		return;
	}
	if(nodelist && nodelist.values.length > 0){
		var htmSrc = new Array();
		htmSrc.push("<span style=\"font-size: 14;font-weight: bold;color: red;\">请选择你所在的成员馆*:</span><select id='orgs'>");
		for(var i=0; i<nodelist.values.length; i++){
			var nodeName = nodelist.values[i].name;
			var nodeCode = nodelist.values[i].code;
			var isLocalMember = nodeQueryService.isLocalMember(nodeCode);
			if(!isLocalMember){
				htmSrc.push("<option value='" + nodeCode + "'>" + nodeName + "</option>");
			}
		}
		htmSrc.push("</select>");
		document.getElementById("orgsSelector").innerHTML = htmSrc.join('');
		document.getElementById("orgsSelector").style.visibility = "visible";	
	}		
}


// 更换人机区分验证码图片
function changeCaptchaPicture() {
	document.getElementById("captchapic").src = '/easycaptcha?' + new Date().getTime();
}
// 清空注册错误提示消息
function clearRegisterErrorMessage() {
	dojo.byId("nameError").innerHTML = "";
	dojo.byId("loginIdError").innerHTML = "";
	dojo.byId("birthdayError").innerHTML = "";
	dojo.byId("passwordError").innerHTML = "";
	dojo.byId("retypepasswordError").innerHTML = "";
	dojo.byId("emailError").innerHTML = "";
	dojo.byId("addIdError").innerHTML = "";
	dojo.byId("addIdError").innerHTML = "";
	dojo.byId("captchaCodeError").innerHTML = "";
	dojo.byId("phoneError").innerHTML = "";
	dojo.byId("mailAddrError").innerHTML = "";
	dojo.byId("msgCodeError").innerHTML = "";
}
// 注册
function register() {

	clearRegisterErrorMessage();
	var isOk = true;

	// 表单信息
	var loginId = dojo.byId("loginId").value;
	var password = dojo.byId("password").value;
	var retypepassword = dojo.byId("retypepassword").value;
	var name = dojo.byId("name").value;
	var sex = dijit.byId("sex").value;
	var birthday = dojo.date.locale.parse(dojo.byId("birthday").value, {
		selector : 'date',
		formatLength : 'full',
		datePattern : 'yyyy-MM-dd'
	});// 将生日的字符串转成Date类型
	var email = dojo.byId("email").value;
	var mailAddr = dojo.byId("mailAddr").value;
	var captchaCode = dojo.byId("captchaCode").value;
	var phone = dojo.byId("phone").value;
	var msgType = dijit.byId("msgType").value;
	var msgCode = dojo.byId("msgCode").value;
	var addIdType = dijit.byId("addType").value;
	var addId = dojo.byId("addId").value;
	var orgCode = null;
	if(document.getElementById("orgsSelector")){
		orgCode = document.getElementById("orgs").value;
	}

	// 校验
	if (!dijit.byId("name").isValid() && !dojox.validate.isText(name) && loginId.indexOf(':') >= 0 ) {
		dojo.byId("nameError").innerHTML = "姓名不能为空，或者您输入了非法字符!";
		isOk = false;
	}
	if(!dijit.byId("sex").isValid()){
		dojo.byId("sexError").innerHTML = "请正确输入您的性别";
		isOk = false;
	}
	if (!dijit.byId("loginId").isValid() && !dojox.validate.isText(loginId)) {
		dojo.byId("loginIdError").innerHTML = "登录名不能为空，或者您输入了非法字符!";
		isOk = false;
	}
	if (!dijit.byId("birthday").isValid()){
		dojo.byId("birthdayError").innerHTML = "您输入的生日日期格式有误，请按“2008-08-08”格式输入!";
		isOk = false;
	}
	if (password == "") {
		dojo.byId("passwordError").innerHTML = "密码不能为空，请您输入密码!";
		isOk = false;
	}
	if (retypepassword == "" || password != retypepassword) {
		dojo.byId("retypepasswordError").innerHTML = "您两次输入的密码不同!";
		isOk = false;
	}
	if (email == "") {
		dojo.byId("emailError").innerHTML = "Email地址不能为空!";
		isOk = false;
	}
	if (email != "" && !dijit.byId("email").isValid()) {
		dojo.byId("emailError").innerHTML += "您输入的不是一个正确的Email地址!";
		isOk = false;
	}
	if (!dijit.byId("addId").isValid()) {
		dojo.byId("addIdError").innerHTML = "有效证件号不能为空!";
		isOk = false;
	}
	if (!dijit.byId("captchaCode").isValid()) {
		dojo.byId("captchaCodeError").innerHTML = "请输入验证码!";
		isOk = false;
	}
	if (!isOk) {
		changeCaptchaPicture();
		return false;
	}
	var userManageService = EasyServiceClient.lookup(null, null,
			"com.cnebula.um.service.IUserManageService");
	var user = {
		_t_ : "com.cnebula.um.ejb.entity.usr.NewUserBean"
	};
	var aId = {
		_t_ : "com.cnebula.um.ejb.entity.usr.AdditionalId"
	};

	var aIds = new java.util.ArrayList();
	user.name = name;
	user.sex = sex;
	user.birthday = birthday;
	if(orgCode && orgCode.trim() != "")
		user.loginId = orgCode + ':' + loginId;
	else
		user.loginId = loginId;
	user.password = password;
	user.email = email;
	if (!mailAddr == "")
		user.mailAddr = mailAddr;
	if (!phone == "")
		user.phone = phone;
	user.msgType = msgType;
	if (!msgCode == "")
		user.msgCode = msgCode;
	if (!addId == "") {
		aId.type = addIdType;
		aId.code = addId;
		aIds.add(aId);
		user.additionalIds = aIds;
	}
	var errMap = new java.util.HashMap();
	try {
		errMap = userManageService.registerUser(user, captchaCode);
	} catch (e) {
		alert(e.message);
		changeCaptchaPicture();
		return false;
	}
	var errKeySet = errMap.keySet();
	if (errKeySet.length > 0) {
		var key;
		for (i = 0; i < errKeySet.length; i++) {
			key = errKeySet[i];
			if (key == "name") {
				dojo.byId("nameError").innerHTML = errMap.get(key);
			}
			if (key == "birthday") {
				dojo.byId("birthdayError").innerHTML = errMap.get(key);
			}
			if (key == "loginId") {
				dojo.byId("loginIdError").innerHTML = errMap.get(key);
			}
			if (key == "password") {
				dojo.byId("passwordError").innerHTML = errMap.get(key);
			}
			if (key == "email") {
				dojo.byId("emailError").innerHTML = errMap.get(key);
			}
			if (key == "mailAddr") {
				dojo.byId("mailAddrError").innerHTML = errMap.get(key);
			}
			if (key == "phone") {
				dojo.byId("phoneError").innerHTML = errMap.get(key);
			}
			if (key == "msgCode") {
				dojo.byId("msgCodeError").innerHTML = errMap.get(key);
			}
			if (key == "code") {
				dojo.byId("addIdError").innerHTML = errMap.get(key);
			}
			if (key == "$CaptchaCode.correctCheck") {
				dojo.byId("captchaCodeError").innerHTML = errMap.get(key);
			}
			if (key == "$LoginId.uniqueCheck") {
				dojo.byId("loginIdError").innerHTML += errMap.get(key);
			}
			if (key == "$Email.uniqueCheck") {
				dojo.byId("emailError").innerHTML += errMap.get(key);
			}
			if (key == "$ActiveMail.sendFailed") {
				alert(errMap.get(key));
			}
		}
		changeCaptchaPicture();
		return false;
	} else {
		window.location.href = userLoginURL;
	}
}
// 登录
function login() {
	var isOk = true;
	var loginService = EasyServiceClient.lookup(null, null,
			"com.cnebula.common.security.auth.ILoginService");
	var loginId = dojo.byId("loginId").value;
	var password = dojo.byId("password").value;
	if (loginId == "") {
		dojo.byId("loginIdError").innerHTML = "登录名不能为空";
		isOk = false;
	}
	if (password == "") {
		dojo.byId("passwordError").innerHTML = "密码不能为空";
		isOk = false;
	}
	if (!isOk)
		return false;
	var loginUser = {
		_t_ : "com.cnebula.um.ejb.entity.usr.UMPrincipal"
	};
	var err = null;
	try {
		loginUser = loginService.loginByNamePassword(loginId, password);
	} catch (e) {
		var errContent = "消息：<br>" + "<p> " + e.message + "!</p>";
		var faildDia = new dijit.Dialog( {
			title : "登录失败",
			content : errContent
		});
		dojo.body().appendChild(faildDia.domNode);
		dojo.style(faildDia.domNode, "width", "280px");
		dojo.style(faildDia.domNode, "height", "120px");
		faildDia.startup();
		faildDia.show();
		err = e.message;
		clearLoginErrorMessage();
	}
	if (err == null) {
		if(loginUser.loginId.indexOf(':')>0)
			loginUser.loginId = loginUser.loginId.substr(loginUser.loginId.indexOf(':')+1);
		setCookieLoginId(loginUser.loginId);
		window.location.href = loginGotoURL;
	}
}
// 保存用户longId，用于判断是否已经登录
function setCookieLoginId(loginId) {
	var expireDate = new Date();
	expireDate.setTime(expireDate.getTime() + 30 * 60 * 1000);// 半个小时cookie过期
	document.cookie = "loginId=" + escape(loginId) + ";expires="
			+ expireDate.toGMTString();
}
// 清空登录界面的错误提示
function clearLoginErrorMessage() {
	dojo.byId("loginIdError").innerHTML = "";
	dojo.byId("passwordError").innerHTML = "";
}
// 显示重置密码对话框
function showRestPasswordDialog() {
	dojo.byId("resetPasswordDV").style.visibility = "visible";
	dojo.byId("jsProgressDV").style.visibility = "hidden";
	dojo.byId("resetPasswordResultDIV").style.visibility = "hidden";
	dijit.byId("ResetPasswordDialog").show();
	clearResetPasswordErrorMessage();
	clearResetPasswordForm();
}
// 重置密码
function resetPassword() {
	clearResetPasswordErrorMessage();
	var isOk = true;
	var loginId = dojo.byId("resetLoginId").value;
	var email = dojo.byId("resetEmail").value;
	if (loginId == "") {
		dojo.byId("resetLoginIdError").innerHTML = "登录名不能为空!";
		isOk = false;
	}
	if (email == "") {
		dojo.byId("resetErrobox").innerHTML = "邮箱不能为空!";
		isOk = false;
	}
	if (email != "" && !dojox.validate.isEmailAddress(email)) {
		dojo.byId("resetErrobox").innerHTML = "您输入的不是一个正确的邮箱地址!";
		isOk = false;
	}
	if (!isOk) {
		return;
	}
	var userManageService = EasyServiceClient.lookup(null, null,
			"com.cnebula.um.service.IUserManageService");
	// 由于发邮件需要一点时间，碰到网络阻塞的时候，为给用户友好的交互
	// 添加一个进度条对话框。首先隐藏重置密码表单
	dojo.byId("resetPasswordDV").style.visibility = "hidden";
	dojo.byId("jsProgressDV").style.visibility = "visible";

	//调用后台服务进行密码重置
	try {
		var rst = userManageService.resetPassword(loginId, email);
	} catch (e) {
		alert(e.message);
		return;
	}
	var regexpSuccess = "^success$";
	content = null;
	if (rst.match(regexpSuccess)) {
		//成功
		content = "尊敬的用户" + loginId + ":<br>系统已经为您生成了随机密码，"
				+ "请您到您的邮箱中点击激活链接进行激活。" + "激活后再使用邮箱中的新密码登录，登录后请您及时修改您的密码!";
	} else {
		//失败
		content = "服务器正忙，发送邮件失败，请稍后再试！";
	}
	timeOut = setTimeout("setRestPasswordRestDialog()",500);
}

function setRestPasswordRestDialog(){
	dojo.byId("jsProgressDV").style.visibility = "hidden";
	dojo.byId("resetPasswordResultMessageBox").innerHTML = content;
	dojo.byId("resetPasswordResultDIV").style.visibility = "visible";
}

function closeResetPasswordDialog(){
	dijit.byId("ResetPasswordDialog").hide();
}

// 清除重置密码对话框错误消息
function clearResetPasswordErrorMessage() {
	dojo.byId("resetLoginIdError").innerHTML = "";
	dojo.byId("resetErrobox").innerHTML = "";
}
// 清除重置密码对话框表单信息
function clearResetPasswordForm() {
	dojo.byId("resetLoginId").value = "";
	dojo.byId("resetEmail").value = "";
}

// 从cookie中得到loginId
function getCookieLoginId() {
	var arr = document.cookie.match(new RegExp("(^| )loginId=([^;]*)(;|$)"));
	if (arr != null)
		return unescape(arr[2]);
	else
		return null;
}
// 检测用户是否已经登录
function isLogin() {
	var loginService = EasyServiceClient.lookup(null, null,
			"com.cnebula.um.service.IUserManageService");
	user = null;
	try {
		user = loginService.getUser();
	} catch (e) {
		alert("服务器中您的会话过期,请重新登录!");
		window.parent.window.location.href = userLoginURL;
		return false;
	}
	if(user.loginId.indexOf("$anonymous") >= 0){
		alert("会话过期，请重新登录！");
		return false;
	}
	// 从cookie中取出缓存的用户loginId;
	var cookieLoginId = getCookieLoginId();
	// 判断是否登录了
	if (cookieLoginId == null) {
		alert("cookie中您的会话过期,请重新登录!");
		window.parent.window.location.href = userLoginURL;
		return false;
	}
	if(user.loginId.indexOf(':')>0)
		user.loginId = user.loginId.substr(user.loginId.indexOf(':')+1);
	return user.loginId.match(new RegExp("^" + cookieLoginId + "$"));
}
// 显示用户信息的iframe
function showMyInfoFrame() {
	dojo.byId("mainBox").innerHTML = "";
	dojo.byId("mainBox").innerHTML = "<iframe id='myInfoFrame' class='myInfoFrame' "
			+ "src='_myInfoForm.html' frameborder='0' scrolling='no'></iframe>";
}
// 显示用户信息
function showMyInfo() {
	user = null;
	// 如果登录了，打印信息
	if (isLogin()) {
		if (user.name != null) {
			dojo.byId("name").innerHTML = user.name;
		}
		if (user.sex != null) {
			var sex = user.sex;
			if (sex.match(new RegExp("^m$")))
				dojo.byId("sex").innerHTML = "男";
			else if (sex.match(new RegExp("^f$")))
				dojo.byId("sex").innerHTML = "女";
			else
				dojo.byId("sex").innerHTML = "";
		}
		if (user.birthday != null) {
			var d = user.birthday.toLocaleString();
			d = d.replace(/\s[0-9]{1,2}:[0-9][0-9]:[0-9][0-9]$/, "");
			dojo.byId("birthday").innerHTML = d;
		}
		if (user.position != null) {
			dojo.byId("position").innerHTML = user.position;
		}
		if (user.institute != null) {
			dojo.byId("institute").innerHTML = user.institute;
		}
		if (user.loginId != null) {
			dojo.byId("loginId").innerHTML = user.loginId;
		}
		if (user.status != null) {
			var s = user.status;
			var intS = parseInt(s);
			switch (intS) {
			case 0:
				s = "注销";
				break;
			case 1:
				s = "停用";
				break;
			case 2:
				s = "启用";
				break;
			case 3:
				s = "待审核";
				break;
			}
			dojo.byId("status").innerHTML = s;
		}
		if (user.validDate != null) {
			var d = user.validDate.toLocaleString();
			d = d.replace(/\s[0-9]{1,2}:[0-9][0-9]:[0-9][0-9]$/, "");
			dojo.byId("validDate").innerHTML = d;
		}
		if (user.invalidDate != null) {
			var d = user.invalidDate.toLocaleString();
			d = d.replace(/\s[0-9]{1,2}:[0-9][0-9]:[0-9][0-9]$/, "");
			dojo.byId("invalidDate").innerHTML = d;
		}
		if (user.email != null) {
			dojo.byId("email").innerHTML = user.email;
		}
		if (user.phone != null) {
			dojo.byId("phone").innerHTML = user.phone;
		}
		if (user.msgType != null) {
			dojo.byId("msgType").innerHTML = user.msgType;
		}
		if (user.msgCode != null) {
			dojo.byId("msgCode").innerHTML = user.msgCode;
		}
		if (user.fax != null) {
			dojo.byId("fax").innerHTML = user.fax;
		}
		if (user.postalCode != null) {
			dojo.byId("postalCode").innerHTML = user.postalCode;
		}
		if (user.homeAddress != null) {
			dojo.byId("homeAddress").innerHTML = user.homeAddress;
		}
		if (user.officeAddr != null) {
			dojo.byId("officeAddr").innerHTML = user.officeAddr;
		}
		if (user.mailAddr != null) {
			dojo.byId("mailAddr").innerHTML = user.mailAddr;
		}
		if (user.additionalIds.size() > 0) {
			var codeType = user.additionalIds.get(0).type;
			var intCodeType = parseInt(codeType);
			switch (intCodeType) {
			case 2:
				codeType = "身份证";
				break;
			case 3:
				codeType = "军官证";
				break;
			case 4:
				codeType = "学生证";
				break;
			default:
				codeType = "未知类型";
				break;
			}
			dojo.byId("addId").innerHTML = user.additionalIds.get(0).code + "(" + codeType + ")";
		}
		parent.dojo.byId("userInfoBoxLoginId").innerHTML = user.loginId + ",您好!";
	} else {
//		alert("服务器会话中的LoginId与服务器会话中的LoginId不同,请重新登录!");
		window.parent.window.location.href = userLoginURL;
		return;
	}
}
// 显示更新用户信息的iframe
function showUpdateMyInfoFrame() {
	dojo.byId("mainBox").innerHTML = "";
	dojo.byId("mainBox").innerHTML = "<iframe id='updateInfoFrame' class='updateInfoFrame' "
			+ "src='_updateForm.html' frameborder='0' scrolling='no'></iframe>";
}
// 加载完毕后在作出判断，反馈用户现有的信息
function feedMyInfo() {
	user = null;
	parent.document.getElementById("updateInfoFrame").height = document.body.scrollHeight;
	if (isLogin()) {
		if (user.position != null) {
			dojo.byId("position").value = user.position;
		}
		if (user.institute != null) {
			dojo.byId("institute").value = user.institute;
		}
		if (user.email != null) {
			dojo.byId("email").value = user.email;
		}
		if (user.phone != null) {
			dojo.byId("phone").value = user.phone;
		}
		if (user.msgCode != null) {
			dojo.byId("msgCode").value = user.msgCode;
		}
		if (user.fax != null) {
			dojo.byId("fax").value = user.fax;
		}
		if (user.postalCode != null) {
			dojo.byId("postalCode").value = user.postalCode;
		}
		if (user.homeAddress != null) {
			dojo.byId("homeAddress").value = user.homeAddress;
		}
		if (user.officeAddr != null) {
			dojo.byId("officeAddr").value = user.officeAddr;
		}
		if (user.mailAddr != null) {
			dojo.byId("mailAddr").value = user.mailAddr;
		}
		if (user.msgType != null) {
			var value = user.msgType.toLowerCase();
			if (value == "qq")
				dijit.byId("msgType").setDisplayedValue("qq");
			else if (value == "msn")
				dijit.byId("msgType").setDisplayedValue("msn");
			else if (value == "gtalk")
				dijit.byId("msgType").setDisplayedValue("googleChat");
			else
				dijit.byId("msgType").setDisplayedValue(value);
		}
		return;
	} else {
//		alert("服务器会话中的LoginId与服务器会话中的LoginId不同,请重新登录!");
		window.parent.window.location.href = userLoginURL;
		return;
	}

}
// 显示用户更改密码的iframe
function showChangePasswordFrame() {
	dojo.byId("mainBox").innerHTML = "";
	dojo.byId("mainBox").innerHTML = "<iframe id='changePasswordFrame' class='changePasswordFrame' "
			+ "src='_changePasswordForm.html' frameborder='0' scrolling='no'></iframe>";
}
function resetChangePasswordFrameHeight() {
	user = null;
	parent.document.getElementById("changePasswordFrame").height = document.body.scrollHeight;
}
// 修改密码
function changePassword() {
	if (isLogin()) {
		var isOk = true;
		var oldPassword = dijit.byId("oldPassword").value;
		var newPassword = dijit.byId("newPassword").value;
		var conformPassword = dijit.byId("conformPassword").value;
		if (oldPassword == "") {
			dijit.byId("oldPassword").displayMessage();
			dijit.byId("oldPassword").displayMessage("旧密码不能为空!");
			isOk = false;
		}
		if (newPassword == "") {
			dijit.byId("newPassword").displayMessage();
			dijit.byId("newPassword").displayMessage("旧密码不能为空!");
			isOk = false;
		}
		if (conformPassword == "") {
			dijit.byId("conformPassword").displayMessage();
			dijit.byId("conformPassword").displayMessage("请确认新密码!");
			isOk = false;
		}
		if (newPassword != "" && conformPassword != ""
				&& newPassword != conformPassword) {
			dijit.byId("conformPassword").displayMessage();
			dijit.byId("conformPassword").displayMessage("您两次输入的密码不同，请重新输入!");
			isOk = false;
		}
		if (!isOk) {
			return;
		}
		var userManageService = EasyServiceClient.lookup(null, null,
				"com.cnebula.um.service.IUserManageService");
		try {
			var rst = userManageService
					.changePassword(oldPassword, newPassword);
		} catch (e) {
			alert(e.message);
		}
		if (rst.toLowerCase() == "success") {
			alert("修改密码成功!");
			window.parent.window.location.href = showUserInfoURL;
			return;
		} else {
			alert(rst);
			return;
		}
	} else {
		alert("服务器会话中的LoginId与服务器会话中的LoginId不同,请重新登录!");
		window.parent.window.location.href = userLoginURL;
		return;
	}
}
// 显示用户角色的iframe
function showMyRoleFrame() {
	dojo.byId("mainBox").innerHTML = "";
	dojo.byId("mainBox").innerHTML = "<iframe id='myRoleFrame' class='myRoleFrame' "
			+ "src='_myRoleForm.html' frameborder='0' scrolling='no'></iframe>";
}
// 得到用户的角色
function feedMyRole() {
	user = null;
	if (!isLogin()) {
//		alert("服务器会话中的LoginId与服务器会话中的LoginId不同,请重新登录!");
		window.parent.window.location.href = userLoginURL;
		return;
	}
	var role = {
		_t_ : "com.cnebula.um.ejb.entity.perm.Role"
	};
	var roles = new java.util.ArrayList();
	try {
		var authorizationService = EasyServiceClient.lookup(null, null,
				"com.cnebula.um.service.IUserManageService");
		user.extAttributeData = null;
		roles = authorizationService.getRoles();
	} catch (e) {
		alert(e.message);
		window.parent.window.location.href = showUserInfoURL;
	}
	var rf = document.getElementById("roleTable");
	var htmlsrc = new Array();
	htmlsrc
			.push("<form action='' onsubmit='return false;' method='post'><table>");
	htmlsrc
			.push("<tr><td align='left' valign='middle' bgcolor='#E8F3FD' colspan='3'>我的用户角色</td></tr>")
	for ( var i = 0; i < roles.size(); i++) {
		var roleName = roles.get(i).name;
		htmlsrc.push("<tr><th>" + roleName + "<th></tr>");
	}
	htmlsrc.push("</table></form>");
	var jSrc = htmlsrc.join(' ');
	rf.innerHTML = jSrc;
	parent.document.getElementById("myRoleFrame").height = document.body.scrollHeight;
	return;
}
// 显示附加表示iframe
function showMyAddId() {
	dojo.byId("mainBox").innerHTML = "";
	dojo.byId("mainBox").innerHTML = "<iframe id='myAddIdFrame' class='myAddIdFrame' "
			+ "src='_myAddIdForm.html' frameborder='0' scrolling='no'></iframe>";
}
// 显示用户的附加ID信息
function feedMyAddId() {
	user = null;
	parent.document.getElementById("myAddIdFrame").height = document.body.scrollHeight;
	if (isLogin()) {
		var addId = user.additionalIds.get(0);
		
		if (addId != null) {
			var codeType = user.additionalIds.get(0).type;
			var intCodeType = parseInt(codeType);
			switch (intCodeType) {
			case 2:
				codeType = "身份证";
				break;
			case 3:
				codeType = "军官证";
				break;
			case 4:
				codeType = "学生证";
				break;
			default:
				codeType = "未知类型";
				break;
			}
			dojo.byId("type").innerHTML = codeType;
		}
		
		dojo.byId("code").innerHTML = addId.code;
		
		if(addId.status != null){
			var s = addId.status;
			var intS = parseInt(s);
			switch (intS) {
			case 0:
				s = "注销";
				break;
			case 1:
				s = "停用";
				break;
			case 2:
				s = "启用";
				break;
			case 3:
				s = "待审核";
				break;
			}
			dojo.byId("status").innerHTML = s;
		}
		var intLoginType = parseInt(addId.loginType);
		var readableLoginType = "";
		switch (intLoginType) {
		case 0:
			readableLoginType = "不可登录(0)";
			break;
		case 1:
			readableLoginType = "可仅凭条码登录(1)";
			break;
		case 2:
			readableLoginType = "凭条码和密码登录(2)";
			break;

		}
		dojo.byId("loginType").innerHTML = readableLoginType;

		var intUpdateType = parseInt(addId.updateType);
		var readableUpdateType = "";
		switch (intUpdateType) {
		case 0:
			readableUpdateType = "不可见(0)";
			break;
		case 1:
			readableUpdateType = "只读(1)";
			break;
		case 2:
			readableUpdateType = "仅密码可更改(2)";
			break;
		case 3:
			readableUpdateType = "仅条码可更改(3)";
			break;
		case 4:
			readableUpdateType = "可更改(4)";
			break;
		}
		dojo.byId("updateType").innerHTML = readableUpdateType;

		var d = addId.validDate.toLocaleString();
		d = d.replace(/\s[0-9]{1,2}:[0-9][0-9]:[0-9][0-9]$/, "");
		dojo.byId("validDate").innerHTML = d;
	} else {
//		alert("服务器会话中的LoginId与服务器会话中的LoginId不同,请重新登录!");
		window.parent.window.location.href = userLoginURL;
		return;
	}
}

function processUpdateForm(){
	// Re-validate form fields
	var custForm = dijit.byId("updateUserInfoForm");
	var firstInvalidWidget = null;
	var FormValid = dojo.every(custForm.getDescendants(), function(widget){
		firstInvalidWidget = widget;
		return !widget.isValid || widget.isValid();
	});
	if (!FormValid) {
		// set focus to first field with an error
		if(firstInvalidWidget != null)
			firstInvalidWidget.focus();
	}else {
		updateMyInfoByFields();
	}
}

// 更新用户信息
function updateMyInfo() {
	user = null;
	if (isLogin()) {
		var position = dojo.byId("position").value;
		var institute = dojo.byId("institute").value;
		var email = dojo.byId("email").value;
		var phone = dojo.byId("phone").value;
		var msgType = dijit.byId("msgType").value;
		var msgCode = dojo.byId("msgCode").value;
		var fax = dojo.byId("fax").value;
		var postalCode = dojo.byId("postalCode").value;
		var homeAddress = dojo.byId("homeAddress").value;
		var officeAddr = dojo.byId("officeAddr").value;
		var mailAddr = dojo.byId("mailAddr").value;
		user.position = position;
		user.institute = institute;
		user.email = email;
		user.phone = phone;
		user.msgType = msgType;
		user.msgCode = msgCode;
		user.fax = fax;
		user.postalCode = postalCode;
		user.homeAddress = homeAddress;
		user.officeAddr = officeAddr;
		user.mailAddr = mailAddr;
		var userManageService = EasyServiceClient.lookup(null, null,
				"com.cnebula.um.service.IUserManageService");
		try {
			var rst = userManageService.updateUser(user);
			alert("更新成功!");
			window.parent.location.href='showUserInfo.html';
		} catch (e) {
			alert(e.message);
		}
	} else {
//		alert("服务器会话中的LoginId与服务器会话中的LoginId不同,请重新登录!");
		window.parent.window.location.href = userLoginURL;
		return;
	}
}

// 通过键值对更新用户信息
function updateMyInfoByFields() {
	if (isLogin()) {
		var fields = new java.util.HashMap();
		var fieldNames = ["position", "institute", "email", "phone", "msgType","msgCode", "fax", "postalCode", "homeAddress", "officeAddr", "mailAddr"];
		for (var name in fieldNames) {
			fields.put(fieldNames[name], dojo.byId(fieldNames[name]).value);
		}

		var userManageService = EasyServiceClient.lookup(null, null,
		"com.cnebula.um.service.IUserManageService");
		try {
			var rst = userManageService.updateUserByFields(fields);
			alert("更新成功!");
			window.parent.location.href='showUserInfo.html';
		} catch (e) {
			alert(e.message);
		}
	} else {
//		alert("服务器会话中的LoginId与服务器会话中的LoginId不同,请重新登录!");
		window.parent.window.location.href = userLoginURL;
		return;
	}
}

//登出
function logout(){
	var loginService = EasyServiceClient.lookup(null, null,
				"com.cnebula.common.security.auth.ILoginService");
	try{
		loginService.logout();
	}catch(e){
		alert(e.message)
	}
	delCookie();
	window.location.href = userLoginURL;
}

//删除cookie
function delCookie(){
	var expireDate = new Date();
	expireDate.setTime(expireDate.getTime() - 1);
	document.cookie = "loginId=" + ";expires=" + expireDate.toGMTString();
}