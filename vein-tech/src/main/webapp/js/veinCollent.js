var deptNoMap = new Map();
var imageURL = "http:\/\/120.77.84.143:8877\/image/vein\/";
$(function(){
     $('#coporationName').text(localStorage.getItem("corName"));
	 $('#adminstratroId').text(localStorage.getItem("adminName"));
	 getAllDept();
	 getUserInfoDe();
});

function getUserInfoDe(){
	//加载人员信息
		$.ajax({
 		type : 'POST',
 		url : 'feature/getUserList',
 		contentType: "application/json; charset=utf-8",
         timeout : 500,
         dataType : "json",
         success : function(data) {
             if(data!=null && data!=undefined){
            	$.each(data.userList,function(index,item){
             		var deptNo = item.deptId;
             		var deptName = sessionStorage.getItem(deptNo);
             		var name = item.uName;
             		var phone = item.uPhone;
             		if(phone==null || phone==undefined ||phone=="null"){
             			phone ="";
             		}
             		var uPhotoUrl =item.uPhotoUrl;
             		var uSex = item.uSex;
             		if (uPhotoUrl == null
							|| uPhotoUrl == undefined) {
						if (uSex == '男') {
							uPhotoUrl = "img/boy.jpg";
						} else {
							uPhotoUrl = "img/girl.png";
						}
					} else {
						uPhotoUrl =imageURL+uPhotoUrl;
					}
             		var ujobNum = item.ujobNum;
             		$('#collentTable').append(
             				"<tr>"
             					+"<td>"+deptName+"</td>"
             					+"<td>"+name+"</td>"
             					+"<td>"+uSex+"</td>"
             					+"<td>"+ujobNum+"</td>"
             					+"<td>"+phone+"</td>"
             					+"<td class='zhao'><img src="+uPhotoUrl+" width='45'></td>"
             					+"<td>"
             						+"<button type='button' class='btn btn-primary chakan2' onclick='checkUserVein(this);'>"
             							+"采集"
             						+"</button>"
             					+"</td>"
             				+"</tr>"
             		);
            	});
             }else{
             }
         },
         error : function(data) {
         }
     });
}

function InquireUser(){
	var inquireName = $('#InquireNameId').val();
	var params = {"name":inquireName};
	$.ajax({
		type : 'POST',
		url : 'feature/findUserByName',
		async:true,
		contentType: "application/json; charset=utf-8",
        timeout : 500,
        dataType : "json",
        data:JSON.stringify(params),
        success:function(data) {
            if(data!=null && data!=undefined){
            	$('#collentTable tr').not(':first').remove();
            	$.each(data.userList,function(index,item){
             		var deptNo = item.deptId;
             		var deptName = sessionStorage.getItem(deptNo);
             		var name = item.uName;
             		var phone = item.uPhone;
             		if(phone==null || phone==undefined ||phone=="null"){
             			phone ="";
             		}
             		var uPhotoUrl =item.uPhotoUrl;
             		var uSex = item.uSex;
             		if (uPhotoUrl == null
							|| uPhotoUrl == undefined) {
						if (uSex == '男') {
							uPhotoUrl = "img/boy.jpg";
						} else {
							uPhotoUrl = "img/girl.png";
						}
					} else {
						uPhotoUrl =imageURL+uPhotoUrl;
					}
             		var userId = item.uId;
             		$('#collentTable').append(
             				"<tr>"
             					+"<td>"+deptName+"</td>"
             					+"<td>"+name+"</td>"
             					+"<td>"+uSex+"</td>"
             					+"<td>"+userId+"</td>"
             					+"<td>"+phone+"</td>"
             					+"<td class='zhao'><img src="+uPhotoUrl+" width='45'></td>"
             					+"<td>"
             						+"<button type='button' class='btn btn-primary chakan2' onclick='checkUserVein(this);'>"
             							+"采集"
             						+"</button>"
             					+"</td>"
             				+"</tr>"
             		);
            	});
            }
        },
        error:function(data) {
        	alert('fail');
        }
    });
}

function checkUserVein(obj){
	$('#VeinData1').text('');
	var photoUrl = $(obj).parent().parent().children().eq(5).children('img').attr('src');
	var deptName = $(obj).parent().parent().children().eq(0).text();
	var name = $(obj).parent().parent().children().eq(1).text();
	var phone = $(obj).parent().parent().children().eq(4).text();
	var userId = $(obj).parent().parent().children().eq(3).text();
	var isShow = $('#veinCollentPage').css('display');
	if(isShow=='none'){
		$('#veinCollentPage').css('display','block');
		$('.img-thumbnail').attr('src',photoUrl);
		$('#depar1').val(deptName);
		$('#name1').val(name);
		$('#number1').val(userId);
		$('#phone1').val(phone);
	}else{
		$('#veinCollentPage').css('display','none');
	}
}

function clockVeinPage(){
	$('#alertMessage').text('');
	$('#VeinData1').text('');
	$('#veinCollentPage').css('display','none');
}

function FetchTemplate1(){
	var ret = WedoneOcx.WDOCX_FetchVeinTemplateEx(3);
    var obj = document.getElementById("VeinData1");
    obj.value = ret;
}

function submitVeinData(){
	var feature = $('#VeinData1').val();
	var veinPosition = $('#hideInput').val();
	var jobNumber = $('#number1').val();
	var veinCount = "3";
	var params = {"feature":feature,"veinPosition":veinPosition,"veinCount":veinCount,"jobNumber":jobNumber};
	if(feature.length==3072){
		$.ajax({
			type : 'POST',
			url : 'feature/pageVeinVollent',
			async:true,
			contentType: "application/json; charset=utf-8",
	        timeout : 50000,
	        dataType : "json",
	        data:JSON.stringify(params),
	        success:function(data) {
	        	if(data!=null && data!=undefined){
	        		$('#VeinData1').text('');
	        		if(data.accessType=='3'){
	        			$('#alertMessage').text('该人员不存在！');
	        		}
	        		if(data.accessType=='2'){
	        			$('#alertMessage').text('采集指静脉失败！');
	        		}
	        		if(data.accessType=='1'){
	        			$('#alertMessage').text('指静脉采集成功！');
	        		}
	        		
	        		if(data.accessType=='10'){
	        			$('#alertMessage').text('同一个手指不能注册多个人！');
	        		}
	        		
	        		var t=setTimeout(function(){
		        		$('#alertMessage').text('');
		        		window.clearTimeout(t);
		        	},5000);
	        	}
	        },
	        error:function(data) {
	        	$('#alertMessage').text('采集指静脉失败！');
	        }
	    });
	}else{
		$('#alertMessage').text('指静脉数据格式不对！');
	}
}


function getAllDept(){
	$.ajax({
		type : 'POST',
		async:false,
		url : 'device/getAllDept',
		contentType: "application/json; charset=utf-8",
        timeout : 500,
        dataType : "json",
        success : function(data) {
        	if(data.accessType=='1'){
        		$.each(data.deptList,function(index,item){
        			sessionStorage.setItem(item.deptId, item.deptName);
        		});
        	}
        },
        error : function(data) {
        }
    });
}
