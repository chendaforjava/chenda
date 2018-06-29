var timeout = -1;
var FingerDetectCnt = 0;
var RegisterCounter = 0;
$(function(){
	 CheckWedoneOcx();
});
function CheckWedoneOcx() {
	try {
		var obj = new ActiveXObject("WEDONEOCX.WedoneOcxCtrl.1");
	} catch (e) {
		alert("尚未安装控件，请下载安装后重试");//可以在这边弹出下载提示框
	}
}

function CheckFingerLeave() {
	clearTimeout(timeout);

	var ret = WedoneOcx.WDOCX_FingerDetect(0);
	if (0 != ret) {
		if (20 < FingerDetectCnt) {
			FingerDetectCnt = 0;
			obj = document.getElementById("ReadVerifyMsg");
			obj.value = "已经超时";
			return;
		}
		timeout = window.setTimeout(CheckFingerLeave, 500);
		obj = document.getElementById("ReadVerifyMsg");
		obj.value = "读取完成请移开手指，倒计时:" + parseInt(10.5 - FingerDetectCnt / 2);
		FingerDetectCnt = FingerDetectCnt + 1;
		return;
	}

	FingerDetectCnt = 0;
	obj = document.getElementById("ReadVerifyMsg");
	obj.value = "";

	RegisterCounter = RegisterCounter + 1;
	if (3 > RegisterCounter) {
		clearTimeout(timeout);
		timeout = window.setTimeout(FetchTemplate2, 100);
		return;
	}

}

function FetchTemplate1()// 添加注册模板
{
	var ret = WedoneOcx.WDOCX_FetchVeinTemplateEx(5);
	var obj = document.getElementById("VeinData1");
	obj.value = ret;
}
function FetchTemplateFor2Times() {
	RegisterCounter = 0;
	FetchTemplate2();
}
function FetchTemplate2()// 获取验证模板
{
	var ret, obj;
	clearTimeout(timeout);

	ret = WedoneOcx.WDOCX_FingerDetect(0);
	if (3 != ret) {
		if (20 < FingerDetectCnt) {
			FingerDetectCnt = 0;
			obj = document.getElementById("ReadVerifyMsg");
			obj.value = "已经超时";
			return;
		}
		timeout = window.setTimeout(FetchTemplate2, 500);
		obj = document.getElementById("ReadVerifyMsg");
		obj.value = "已经采集" + RegisterCounter + "次，请放手指，倒计时:"
				+ parseInt(10.5 - FingerDetectCnt / 2);
		FingerDetectCnt = FingerDetectCnt + 1;
		return;
	}

	FingerDetectCnt = 0;
	obj = document.getElementById("ReadVerifyMsg");
	obj.value = "";

	ret = WedoneOcx.WDOCX_FetchVeinTemplate(30000);

	obj = document.getElementById("VeinData2");
	if (0 < RegisterCounter) {
		if ((2 < obj.value.length) && (2 < ret.length)) {
			//用WDOCX_SameFingerCheck接口判断两个模板是否属于同一根手指，
			//避免注册过程中，用户故意用不同的手指进行注册，影响验证时候的通过率
			var ret2 = WedoneOcx.WDOCX_SameFingerCheck(ret, obj.value, 3);
			var msg;
			if (0 == ret2) {
				msg = "与前一次属于同一根手指";
			} else {
				msg = "与前一次不属于同一根手指";
				alert(msg);
				return;
			}
		}
	}
	obj.value = ret;
	CheckFingerLeave(); //检查手指是否移开，确保下次采集手指移开后重新放的。
}

function MatchTemplates()// 比对模板1和模板2
{
	var obj1 = document.getElementById("VeinData1");
	var obj2 = document.getElementById("VeinData2");

	if (1024 > obj1.value.length) {
		alert("尚无有效注册模板数据，请读取注册模板后再进行比对操作！");
		return;
	}

	if (1024 > obj2.value.length) {
		alert("尚无有效验证模板数据，请读取验证模板后再进行比对操作！");
		return;
	}

	var ret = WedoneOcx.WDOCX_MatchTemplates(obj2.value, obj1.value,
			obj1.value.length / 1024, 3);
	var msg;
	if (0 == ret) {
		WedoneOcx.WDOCX_OutputWaveSound(6, 0);
		msg = "指静脉验证通过";
	} else {
		WedoneOcx.WDOCX_OutputWaveSound(7, 0);
		msg = "指静脉验证失败，错误码=" + ret;
	}
	alert(msg);
}
function CaptureVeinImage()// 获取验证模板
{
	var ret = WedoneOcx.WDOCX_CaptureVeinImage(0);
	var obj = document.getElementById("VeinImage");
	obj.value = ret;
}
function GetSerialNUmber() {
	var ret = WedoneOcx.WDOCX_GetSerialNumber(0);
	var obj = document.getElementById("SerialNumber");
	obj.value = ret;
}
function GetVersion() {
	var ret = WedoneOcx.WDOCX_GetVersion();
	var obj = document.getElementById("OcxVersion");
	obj.value = ret;
}
function OutputWaveSound(soundIndex) {
	WedoneOcx.WDOCX_OutputWaveSound(soundIndex, 0);
}
function SetSoundNotice(bEnableSound) {
	WedoneOcx.WDOCX_SetSoundNotice(bEnableSound);
}