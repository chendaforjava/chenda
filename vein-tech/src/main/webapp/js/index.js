/**
 * Created by lijinduo on 2017/11/8.
 */
var deptMap = new Map();

$(function(){
	//加载部门信息
	getManagerInfo();
	//加载会议信息
	$.ajax({
		type : 'POST',
		url : 'vein/loadschedule',
		contentType: "application/json; charset=utf-8",
		timeout : 500,
		dataType : "json",
		async:false,
		success : function(data) {
         if(data!=null && data!=undefined){
        	if(data.accessType=='1'){
        		$.each(data.schedulerList,function(index,item){
        			var scheduleName = item.scheduleName;
        			var scheduleId = item.scheduleId;
        			$('.zhong').append(
        					"<li style='overflow: hidden' onclick='getScheduleUsers(this)' id="+scheduleId+">"+scheduleName+"</li>"
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
	
	
	webSocketConnect();
})
	



function getScheduleUsers(obj){
	$('#userListShowDiv').empty();
	localStorage.clear();
	sessionStorage.clear();
	var scheduleId = $(obj).attr('id');
	var params ={'scheduleId':scheduleId};
	//加载会议信息
	$.ajax({
		type : 'POST',
		url : 'vein/getscheduleuserbyid',
		contentType: "application/json; charset=utf-8",
		timeout : 500,
		dataType : "json",
		async:false,
		data:JSON.stringify(params),
		success : function(data) {
         if(data!=null && data!=undefined){
        	if(data.accessType=='1'){
        		$.each(data.singUserId,function(index,item){
        			localStorage.setItem(item,item);
        		});
        		
        		$.each(data.userList,function(index,item){
        			var name = item.name;
        			var sex = item.sex;
        			if(sex==1){
        				sex='男'
        			}else{
        				sex='女'
        			}
        			var userId = item.userId;
        			var deptNo = item.deptNo;
        			var message = name+"|"+deptNo+"|"+sex;
        			sessionStorage.setItem(userId, message);
        			var photoUrl = item.photoUrl;
        			if(photoUrl!=null && photoUrl!=undefined){
             			photoUrl = photoUrl.replace(/\\/g, "/");
             		}
        			var signedUserId =  localStorage.getItem(userId);
        			if(signedUserId!=null && signedUserId!=undefined){
        				$('#userListShowDiv').append(
        						"<li onclick='showPicDetil(this)' id="+userId+">"
        							+"<p class='zhao' style=' background: url("+photoUrl+") no-repeat center;background-size:100%'></p>"
        							+"<p class='hei' style='display:none'></p>"
        						+"</li>"
            					/*"<li onclick='showPicDetil(this)' id="+userId+">"
            						+" <img src="+photoUrl+" />"
            						+" <p class='hei' style='display:none'></p>"
            					+"</li>"*/
            			);
        			}else{
        				$('#userListShowDiv').append(
        						"<li onclick='showPicDetil(this)' id="+userId+">"
    								+"<p class='zhao' style=' background: url("+photoUrl+") no-repeat center;background-size:100%'></p>"
    								+"<p class='hei'></p>"
    								+"</li>"
            			);
        			}
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


function alertUserImg(){
	var li =$('.ull li');
    /*弹出框*/
    var tan =$('.xiala');
    var hei = $('.hei');
        li.click(function () {
            tan.show()

            var self = $(this)
            var imgSrc = self.find('img').attr('src');
            setTimeout(function () {
                $(self).find(hei).css({"display": "none"})
                $(self).css({"background": "url('./img/youkuang.png')"})
                tan.hide()

            },2000)

        });


    
}


function showPicDetil(obj){
	var li =$('.ull li');
	 var self = $(obj)
    /*弹出框*/
    var tan =$('.xiala');
    var hei = $('.hei');
	//var imgUrl = $(obj).children('img').attr('src');
    var imgUrl = $(obj).children('.zhao').css('background-image');
    imgUrl = imgUrl.split("\"")[1];
	var userId = $(obj).attr('id');
	var message = sessionStorage.getItem(userId);
	var messageArray = message.split("|");
	var name = messageArray[0];
	var deptNo = messageArray[1];
	var deptName =deptMap.get(deptNo.toString());
	var sex = messageArray[2];
	tan.show();
	$('.xiala2').append(
			"<span class='xinxi word'>姓名:"+name+"<br>性别:"+sex+"<br>部门:"+deptName+"<br> 编号:"+userId+"</span>"
			+"<span class='touxiang'>"
				+"<img src="+imgUrl+" width='100%'/>"
			+"</span>"
	);
	 setTimeout(function () {
         $(self).find(hei).css({"display": "none"})
         $(self).css({"background": "url('./img/youkuang.png')"})
         tan.hide()
         $('.xiala2').empty();
     },2000)
     
     
     
    /*获取日程按钮*/
    var span = $(".zhong li");
    span.click(function () {
        console.log(span);
        $(this).addClass('s-one').siblings().removeClass('s-one')
    })
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
                		var deptno = item.deptNo;
                		var deptName = item.deptName;
                		deptMap.set(deptno.toString(),deptName.toString());
                	});
                	
                }else{
                }
            },
            error : function(data) {
            }
        });
}