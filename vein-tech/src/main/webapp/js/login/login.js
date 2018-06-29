$(function(){
	//getAllCorporationInfo();
});
/**
 * Created by lijinduo on 2017/10/24.
 */
/*========================验证码开始=================================*/
var code;
function createCode()
{
    code = "";
    var codeLength = 6; //验证码的长度
    var checkCode = document.getElementById("checkCode");
    var codeChars = new Array(0, 1, 2, 3, 4, 5, 6, 7, 8, 9,
        'a','b','c','d','e','f','g','h','j','k','m','n',
        'o','p','q','r','s','t','u','v','w','x','y','z',
        'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'J', 'K', 
        'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V',
        'W', 'X', 'Y', 'Z'); //所有候选组成验证码的字符，当然也可以用中文的
    for(var i = 0; i < codeLength; i++)
    {
        var charNum = Math.floor(Math.random() *52);
        code += codeChars[charNum];
        checkCode.value = code;
    }
    if(checkCode)
    {
        checkCode.className = "code";
        checkCode.value= code;
    }
}
code = "";
var codeLength = 6; //验证码的长度
var checkCode = document.getElementById("checkCode");
var codeChars = new Array(0, 1, 2, 3, 4, 5, 6, 7, 8, 9,
    'a','b','c','d','e','f','g','h','j','k','m',
    'n','o','p','q','r','s','t','u','v','w','x','y','z',
    'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'J', 'K',
    'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V',
    'W', 'X', 'Y', 'Z'); //所有候选组成验证码的字符，当然也可以用中文的
for(var i = 0; i < codeLength; i++)
{
    var charNum = Math.floor(Math.random() *52);
    code += codeChars[charNum];
    checkCode.value = code;
}
/*==================================验证码结束========================================*/
var   number = $('#dia');
var paw = $('#psd');
var yz = $('#yz');
var checkCode = $('#checkCode')
function vawe() {
    number.val() == "";
    paw.val() == ''
    //判断用户名不能为空
    
    	
   var selectVal=$("#corporationselect").find("option:selected").val(); 
   
    if(selectVal=="0"){
    	$(".tishi").html("请选择所属公司");
    	return false;
    }


    number.focus(function () {
        $(".tishi").html("")
    })
   paw.focus(function () {
        $(".tishi").html("")
    })
    yz.focus(function () {
        $(".tishi").html("")
    });




    if(number.val()==""){
       $(".tishi").html("用户名不可以为空");
        return false;
    }

    //判断密码不能为空
    if(paw.val()==""){
        $(".tishi").html("密码不可以为空")
        return false;
    }
    //判断验证码输入不一致或不能为空
    var inputdata = yz.val().toLowerCase();//toLowerCase()将字符串转换成小写
    var autodata = checkCode.val().toLowerCase();

    if(inputdata != autodata || yz.val()=="" ){
        $(".tishi").html("验证码输入错误")
        return false;

    }else {
    return true;
    }
    return true;
}

function getAllCorporationInfo(){
	$.ajax({
		type : 'POST',
		url : 'login/corporationInfo',
		contentType: "application/json; charset=utf-8",
        timeout : 500,
        dataType : "json",
        success : function(data) {
        	if(data.accessType=='1'){
        		$.each(data.corporationList,function(index,item){
        			var corName = item.corName;
        			var corId = item.corId;
        			$('#corporationselect').append(
        					"<option style='color:#000' value="+corId+">"+corName+"</option>"
        			);
        		});
        	}
        },
        error : function(data) {
        }
    });
}
