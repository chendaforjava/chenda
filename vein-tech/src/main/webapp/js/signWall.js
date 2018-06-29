/**
 * Created by lijinduo on 2017/11/8.
 */
var deptMap = new Map();
var userMessageMap = new Map();
var userPageMap = new Map();

var corId;
var ws;

var imageURL = "http:\/\/120.77.84.143:8877\/image/vein\/";

$(function(){
    //加载部门信息以及第一个部门的人员信息
   // getManagerInfo();
    
	getCorId();
	
    //加载会议信息
    getMeetingMessage();
    
    webSocketConnect();
    //触发第一个会议的点击事件
    var firstMessage = $('#scheduleUl').children('li:first-child');
    firstMessage.trigger('click');
});


function getCorId(){
	$.ajax({
		type : 'POST',
		async: false,
		url : 'sign/getCorId',
		contentType: "application/json; charset=utf-8",
        timeout : 500,
        dataType : "json",
        success : function(data) {
            if(data!=null && data!=undefined){
            	var corId = data.corId;
            	 target ="ws://192.168.1.111:8080/vein-tech/websocket?corId="+corId;
            }
        },
        error : function(data) {
        }
    });
}



function webSocketConnect() {
    if ('WebSocket' in window) {
        ws = new WebSocket(target);
    } else if ('MozWebSocket' in window) {
    	ws = new MozWebSocket(target);
    } else {
        alert('WebSocket is not supported by this browser.');
        return;
    }
    
    ws.onmessage = function(event){
        var uId = event.data;
        var obj = $('#'+uId);
    	showUserDetil(obj,"device");
    }
}


function  getManagerInfo(){
	//获取部门信息和管理员信息
	$.ajax({
    		type : 'POST',
    		url : 'vein/getdeptandmanage',
    		contentType: "application/json; charset=utf-8",
            timeout : 500,
            async:false,
            dataType : "json",
            success : function(data) {
                if(data!=null && data!=undefined){
                	$.each(data.dept,function(index,item){
                		var deptName = item.deptName;
                		var deptno = item.deptNo;
                		deptMap.set(deptno.toString(),deptName.toString());
                	});
                }
            },
            error : function(data) {
            }
        });
}


function getMeetingMessage(){
	$('#scheduleUl').empty();
	$.ajax({
		type : 'POST',
		url : 'signWall/loadSchedule',
		contentType: "application/json; charset=utf-8",
		timeout : 500,
		dataType : "json",
		async:false,
		success : function(data) {
         if(data!=null && data!=undefined){
        	if(data.accessType=='1'){
        		$.each(data.scheduleList,function(index,item){
        			var scheduleName = item.scheduleName;
        			var scheduleId = item.scheduleId;
        			$('#scheduleUl').append(
        					"<li onclick='getScheduleUsers(this)' id="+scheduleId+">"
        						+"<span>"+scheduleName+"</span>"
        						+"<p>"+scheduleName+"</p>"
        						+"<a href='#'>"
        							+"<img src='img/anniu.png'>"
        						+"</a>"
        					+"</li>"
        			);
        		});
        	}else{
        		
        	}
         }else{
         }
     },
     error : function(data) {
     }
 });
}


function getScheduleUsers(obj){
	var signedUserMap = new Map();
	$(obj).find('img').attr({'src':'img/anniu2.png'});
	$(obj).siblings().find('img').attr({'src':'img/anniu.png'})
   
	$('#userListShowDiv').empty();
	/*$('#userListShowDiv').empty();
	localStorage.clear();
	sessionStorage.clear();*/
	var scheduleId = $(obj).attr('id');
	var params ={'scheduleId':scheduleId};
	//加载会议信息
	$.ajax({
		type : 'POST',
		url : 'signWall/getScheduleUsers',
		contentType: "application/json; charset=utf-8",
		timeout : 500,
		dataType : "json",
		async:false,
		data:JSON.stringify(params),
		success : function(data) {
         if(data!=null && data!=undefined){
        	if(data.accessType=='1'){
        		//已签到人员ID
        		$.each(data.signedUser,function(index,item){
        			signedUserMap.set(item.ujobNum.toString(),item.ujobNum.toString());
        		});
        		
        		//判断当前会议共有几页，每页的人数是28个
        		var userListArray = data.userList;
        		var userNum = userListArray.length;
        		//判断当前会议需要几页
        		var pageTotal =  Math.ceil(userNum/33);
        		var userListShowDiv = $("#userListShowDiv");
        		for(var i=0;i<pageTotal;i++){
        			var idNum ="userdiv"+i;
        			userListShowDiv.append(
        					"<div class='swiper-slide' id="+idNum+">"
        						+"<ul class='ull'>"
        						+"</ul>"
        					+"</div>"
        			);
        		}
        		
        		$.each(data.userList,function(index,item){
        			var name = item.uName;
        			var sex = item.uSex;
        			var userId = item.ujobNum;
        			var deptno = item.deptId;
        			var photoUrl = item.uPhotoUrl;
        			var deptName = deptMap.get(deptno.toString());
        			var message = name+"|"+deptName+"|"+sex;
        			userMessageMap.set(userId.toString(),message.toString());
        			if(photoUrl!=null && photoUrl!=undefined){
             			photoUrl = photoUrl.replace(/\\/g, "/");
             			photoUrl = imageURL+photoUrl;
             		}else{
             			if (sex == '男') {
             				photoUrl = "img/boy.jpg";
						} else {
							photoUrl = "img/girl.png";
						}
             		}
        			//找出当前人属于第几页
        			index = index+1;
        			var pageNum = Math.ceil(index/33)-1;
        			userPageMap.set(userId.toString(),pageNum);
        			var divObj = $('#'+'userdiv'+pageNum);
        			var ulObj = divObj.children('ul');
        			ulObj.append(
        					"<li onclick='showUserDetil(this)' id="+userId+">"
        						+"<img src="+photoUrl+" />"
        					+"</li>"
        			);
        			
        			var signedUserId =  signedUserMap.get(userId.toString());
        			if(signedUserId!=null && signedUserId!=undefined){
        				//说明已经签到了
        				$("#"+userId).css({"background": "url('./img/you.png') no-repeat center center"," background-size": "100% ;"});
        				$("#"+userId).find('img').css({ "opacity": 1});
        			}
        			
        		});
        		scrollPage();
        	}else{
        		
        	}
         }else{
         }
     },
     error : function(data) {
     }
 });
}


function showUserDetil(obj){
	var userId = $(obj).attr('id');
	var pageNum = userPageMap.get(userId.toString());
	var swiper = $('.swiper-pagination-bullet').get(pageNum);
	var swiperDom = $(swiper);
	swiperDom.trigger('click');
	var imgUrl = $(obj).children('img').attr('src');
	var userMessage = userMessageMap.get(userId.toString());
	var messageArray = userMessage.split("|");
	var name = messageArray[0];
	var deptNo = messageArray[1];
	var deptName =deptMap.get(deptNo.toString());
	var sex = messageArray[2];
	
	$('#nameId').text(name);
	$('#sexId').text(sex);
	$('#departmentId').text(deptName);
	$('#userId').text(userId);
	$('#alertImage').attr('src',imgUrl);
	
	alterMessage(obj);
}


function alterMessage(obj,flag){
	var li =$('.ull li');
    var img = li.find('img');
    var tan =$('.xiala');
    var hei = $('.hei');
    tan.show();
    var self = $(obj);
    setTimeout(function () {
        $(self).find(hei).css({"display": "none"});
        $(self).css({"background": "url('./img/you.png') no-repeat center center"," background-size": "100% ;"});
        $(self).find('img').css({ "opacity": 1});
        tan.hide();
    },2000);
}


function scrollPage(){
	
	var swiper = new Swiper('.swiper-container',{
        direction: 'vertical',
        slidesPerView: 1,
        spaceBetween: 30,
        mousewheel: true,
        pagination: {
            el: '.swiper-pagination',
            clickable: true
        }
    });
}
