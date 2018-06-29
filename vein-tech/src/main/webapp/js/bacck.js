function req(){
    var bumen = $('#depar');
    /*姓名*/
    var namex= $('#name');
    /*编号*/
    var numbers = $("#number");
    var userId = numbers.val();
    /*手机*/
    var phones = $('#phone');
   
    /**/

    /*部门*/

    bumen.focus(function () {
        bumen.css({"border":"1px #c0c0c0 solid"})
    })
    namex.focus(function () {
        namex.css({"border":"1px #c0c0c0 solid"})
    })
    numbers.focus(function () {
        numbers.css({"border":"1px #c0c0c0 solid"})
    })
    phones.focus(function () {
        numbers.css({"border":"1px #c0c0c0 solid"})
    })

    if(bumen.val()===''){

        bumen.css({"border-color":"#fa7278"})
        return false;
    }

    if(namex.val()===''){
        namex.css({"border-color":"#fa7278"})
        return false;
    }
    
    if(namex.val().trim().length==0){
    	 namex.css({"border-color":"#fa7278"})
         return false;
    }
    
    if(namex.val().trim().length>12){
    	$('#prompt').text('姓名12个字符以内');
   	 	namex.css({"border-color":"#fa7278"})
        return false;
   }

    if(numbers.val()===''){
        numbers.css({"border-color":"#fa7278"})
        return false;
    }


    /*if(phones.val()===''){
        phones.css({"border-color":"#fa7278"})
        return false;
    }*/
    
   /* if(!checkPhoneNumReg(phones.val())){
    	phones.css({"border-color":"#fa7278"})
        return false;
    }*/
    
    
    if(userId.length>20){
    	$('#prompt').text('编号长度过长');
    	return false;
    }
    
  
    
    //验证userId是否为数字和字母
    var reg =  new RegExp(/^[0-9A-Za-z]+$/);
    if (!reg.test(userId)){
    	$('#prompt').text('编号只能是数字或字母组合');
    	return false;
    }
    
    //判断该用户是否已经注册
    if(!verityUserId(userId)){
    	$('#prompt').text('该编号已存在');
    	return false;
    }
    
    return true;
}


function verityUserId(userId){
	var parame = {"userId":userId};
	var flag = true;
	$.ajax({
		type : 'POST',
		url : 'user/verityUserId',
		async : false,
		contentType : "application/json; charset=utf-8",
		timeout : 500,
		data : JSON.stringify(parame),
		dataType : "json",
		success : function(data) {
			if (data != null && data != undefined) {
				if (data.accessType == '1') {
					if(data.userNum =="1"){
						flag = false;
					}
				}
			}
		},
		error : function(data) {
		}
	});
	return flag;
}


function xiugai(){

    var bumen1 = $('#depar1');
    /*姓名*/
    var namex1= $('#name1');
    /*编号*/
    var numbers1 = $("#number1");
    /*手机*/
    var phones1 = $('#phone1')
    /**/

    /*部门*/

    bumen1.focus(function () {
        bumen.css({"border":"1px #c0c0c0 solid"})
    })
    namex1.focus(function () {
        namex.css({"border":"1px #c0c0c0 solid"})
    })
    numbers1.focus(function () {
        numbers.css({"border":"1px #c0c0c0 solid"})
    })
    phones1.focus(function () {
        numbers.css({"border":"1px #c0c0c0 solid"})
    })

    if(bumen1.val()===''){

        bumen1.css({"border-color":"#fa7278"})
        return false;
    }

    if(namex1.val()===''){
        namex1.css({"border-color":"#fa7278"})
        return false;
    }

    if(numbers1.val()===''){
        numbers1.css({"border-color":"#fa7278"})
        return false;
    }


    if(phones1.val()===''){
        phones1.css({"border-color":"#fa7278"})
        return false;
    }

    return true;


}

function xiugai2(){
    /*姓名*/
    var namex1= $('#name1');
    var phones1 = $('#phone1')
    var phone = phones1.val();
    var flag = checkPhoneNumReg(phone);
    if(namex1.val().trim()===''){
        namex1.css({"border-color":"#fa7278"})
        return false;
    }
    
    if((phone.length>0 && !flag)){
    	phones1.css({"border-color":"#fa7278"})
    	return false;
    }
    
    return true;
}




function  bumen(){

    var deptName  = $("#inputPassword2").val();
    if(deptName.length>18){
    	$('#inputPassword2').val('');
    	return;
    }
   if(deptName.trim().length!=0  && deptName!=''){
	   
	   //添加新部门
	   var tempDetpNo = localStorage.getItem(deptName);
	   if(tempDetpNo!=null && tempDetpNo!=undefined){
		   $("#deptAddId").text("部门已存在！");
		   return ;
	   }
	   deptName = deptName.trim();
	   params = {"deptName":deptName};
	   $.ajax({
	 		type : 'POST',
	 		url : 'vein/adddept',
	 		async:true,
	 		contentType: "application/json; charset=utf-8",
	         timeout : 500,
	         dataType : "json",
	         data:JSON.stringify(params),
	         success : function(data) {
	             if(data!=null && data!=undefined){
	             	if(data.accessType=='1'){
	             		var deptno = data.deptNo;
	             		localStorage.setItem(deptName, deptno);
	             		$("#bumen").append("<option value='"+deptno+"'>"+deptName+"</option>");
	             		$("#departmentId").append("<option value='"+deptno+"'>"+deptName+"</option>");
	             		$("#bumenselect").append("<option value='"+deptno+"'>"+deptName+"</option>");
	             		$('<tr style="width:60%">'+'<td style="margin-left:20px;">'+'<span class="span-class">'+deptName+'</span>'+'</td>'+'<td><span style="margin-left: 30px; color:red; cursor: pointer;"  onclick="shan(this)">删除</span></td>'+'</tr>').appendTo(".tabless");
	             	}
	             }
	         },
	         error : function(data) {
	         	alert('fail');
	         }
	     });
	   $('#inputPassword2').val('');
   }
}


 function shan(obj){
	 var con;
	 con=confirm("该部门下的人员将被清空！"); //在页面上弹出对话框
	 if(con==true){
		 var deptName = $(obj).parent().prev().children("span:first-child").text();
		 var deptNo = localStorage.getItem(deptName);
		 params = {"deptNo":deptNo};
		 $.ajax({
		 		type : 'POST',
		 		url : 'vein/deldept',
		 		async:true,
		 		contentType: "application/json; charset=utf-8",
		         timeout : 500,
		         dataType : "json",
		         data:JSON.stringify(params),
		         success : function(data) {
		             if(data!=null && data!=undefined){
		             	if(data.accessType=='1'){
		             		localStorage.removeItem(deptName);
		             		$.each(data.users,function(index,item){
		             			var userId = item.userId;
		             			sessionStorage.removeItem(userId);
		             			$("#"+deptNo).parent().remove();
		             		});
		             		$(obj).parents('tr').remove();
		             		$("#bumen").find("option[value="+deptNo+"]").remove();
		             		$("#departmentId").find("option[value="+deptNo+"]").remove();
		             		$("#bumenselect").find("option[value="+deptNo+"]").remove();
		             	}
		             }
		         },
		         error : function(data) {
		         	alert('fail');
		         }
		     });
	}else{
		window.location.reload();
	}
 }

 function checkPhoneNumReg(phone){
 	var pho1 = /^1[3|4|5|7|8][0-9]{9}$/;
 	var flag = pho1.test(phone);
 	return flag;
 }

$(function (){
    var an = $('.ones');
    var shu =$('.luru')
    var input = $('#deper')
    console.log(input)
    var btn = $('.button')


    an.click(function () {
        shu.toggle()


    });


    input.keydown(function () {
        $('btn').attr('disabled',"true");
    })

    var tou = $(".tou");
    var xiala = $(".xiala");


    tou.click(function () {
        xiala.toggle();



    })





});

 