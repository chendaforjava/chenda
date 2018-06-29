var confirmbutton = document.getElementById('addNewSchedule');
var cancelbutton = document.getElementById('canceladd');
$(function(){
	sessionStorage.clear();
	$('#coporationName').text(localStorage.getItem("corName"));
	$('#administraterName').text(localStorage.getItem("adminName"));
	
	//获取部门信息
	getAllDept();
	
	

	//getManagerInfo();
	//加载所有人员信息
	getAllUsers();
	//下拉选改变事件
	selectChange();
	//加载行程表
	loadSchdule();
	//给特定按钮绑定事件
	confirmbutton.addEventListener( "click",addSchedule,false );
	cancelbutton.addEventListener( "click",canceladd,false );
});



function getAllDept(){
	var corId = localStorage.getItem("corId");
	params = {"corId":corId};
	$.ajax({
		type : 'POST',
		url : 'dept/getAllDept',
		contentType: "application/json; charset=utf-8",
        timeout : 500,
        dataType : "json",
        data:JSON.stringify(params),
        success : function(data) {
        	if(data.accessType=='1'){
        		$.each(data.deptList,function(index,item){
        			var deptId = item.deptId;
            		var deptName = item.deptName;
            		$("#bumen").append("<option value='"+deptId+"'>"+deptName+"</option>");
        		});
        	}
        },
        error : function(data) {
        }
    });
}







function  getManagerInfo(){
	//获取部门信息和管理员信息
	$.ajax({
    		type : 'POST',
    		url : 'vein/getdeptandmanage',
    		contentType: "application/json; charset=utf-8",
            timeout : 500,
            dataType : "json",
            success : function(data) {
                if(data!=null && data!=undefined){
                	$.each(data.dept,function(index,item){
                		var deptno = item.deptNo;
                		var deptName = item.deptName;
                		$("#bumen").append("<option value='"+deptno+"'>"+deptName+"</option>");
                		//$("#departmentId").append("<option value='"+deptno+"'>"+deptName+"</option>");
                	});
                	if(data.managerMessage!=null){
                		$('#administraterName').text(data.managerMessage);
                	}
                }else{
                }
            },
            error : function(data) {
            }
        });
}


function getAllUsers(){
	var deptId = $("#bumen option:selected").val();
	var params = {"deptId":deptId};
	//加载人员信息
	$.ajax({
		type : 'POST',
		url : 'meeting/getDeptUsers',
		contentType: "application/json; charset=utf-8",
		timeout : 500,
		dataType : "json",
		data : JSON.stringify(params),
		success : function(data) {
         if(data!=null && data!=undefined){
        	$('#scheduleUsers tr').not(':first').remove();
         	$.each(data.userList,function(index,item){
         		var name = item.uName;
         		var ujobNum = item.ujobNum;
         		$('#scheduleUsers').append(
         				"<tr>"
         					+"<td><input type='checkbox' name='dui'></td>"
         					+"<td>"+name+"</td>"
         					+"<td>"+ujobNum+"</td>"
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

function selectChange(){
	$("#bumen").bind('change',function () {
		var optionVal = $(this).val();
		if(optionVal==null || optionVal==""){
			getAllUsers();
		}else{
			//加载人员信息
			var params ={'deptId':optionVal};
			$.ajax({
				type : 'POST',
				url : 'meeting/getDeptUsers',
				contentType: "application/json; charset=utf-8",
				timeout : 500,
				data:JSON.stringify(params),
				dataType : "json",
				success : function(data) {
		         if(data!=null && data!=undefined){
		        	 $('#scheduleUsers tr').not(':first').remove();
		        	 $.each(data.userList,function(index,item){
		          		var name = item.uName;
		          		var ujobNum = item.ujobNum;
		          		$('#scheduleUsers').append(
		          				"<tr>"
		          					+"<td><input type='checkbox' name='dui'></td>"
		          					+"<td>"+name+"</td>"
		          					+"<td>"+ujobNum+"</td>"
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
	});
}


function addScheduleUser(){
	var name;
	var userId;
	$('input[name="dui"]:checked').each(function(index,item){
		var firsttd =  $(item).parent().attr('id');
		var nameAndId = $(item).parent().nextAll();
		if(firsttd!='selectalluser'){
			$.each(nameAndId,function(index,item){
				if(index==0){
					//姓名
					name = $(this).text();
				}
				if(index==1){
					//编号
					userId = $(this).text();
				}		
			});
			sessionStorage.setItem(userId, name);
		}
		//$(this)[0].checked=false;
	});
	$('input[name="dui"]:checked').prop('checked',false);
	//遍历sessionStorage
	$('#rightUsers').empty();
	for(i=0;i<sessionStorage.length;i++){
		var userId = sessionStorage.key(i);
		var name = sessionStorage.getItem(userId);
		$('#rightUsers').append(
				"<tr>"
					+"<td>"+name+"</td>"
					+"<td class='userIdToServer'>"+userId+"</td>"
					+"<td onclick='delrightuser(this);'><a style='color:red'>删除</a></td>"
				+"</tr>"
		);
	}
}


function delrightuser(obj){
	var userId = $(obj).prev().text();
	sessionStorage.removeItem(userId);
	$(obj).parent().remove();
}


function   formatDate()   {
	var now = new Date();
    var   year=now.getFullYear();   
    var   month=now.getMonth()+1;    
    var   date=now.getDate();     
    var   hour=now.getHours()<10?"0"+now.getHours():now.getHours();
    var   minute=now.getMinutes()<10?"0"+now.getMinutes():now.getMinutes();
    var   second=now.getSeconds()<10?"0"+now.getSeconds():now.getSeconds();
    return   year+"-"+month+"-"+date+"   "+hour+":"+minute+":"+second;     
}     


function timeStamp2Date(obj){
	var   year=obj.getFullYear();   
    var   month=obj.getMonth()+1;    
    var   date=obj.getDate();  
    var   hour=obj.getHours()<10? "0" + obj.getHours():obj.getHours();
    var   minute=obj.getMinutes()<10?"0"+ obj.getMinutes():obj.getMinutes();
    var   second=obj.getSeconds()<10?"0"+ obj.getSeconds():obj.getSeconds(); 
    return   year+"-"+month+"-"+date+"   "+hour+":"+minute+":"+second;  
}



function addSchedule() {
	$('#tishi').text('');
	var lastEndTime = $('#mainSchedule tr:last').children('td').eq(4).text();
	var scheduleName = $('#name').val();
	var startTime = $('#name1').val();
	var endTime = $('#endTime').val();
	var address = $('#name2').val();
	var  returnArray = new Array();
	var userNum =  sessionStorage.length;
	if(scheduleName.trim()==''||startTime.length==0||endTime.length==0||address.trim()==''||userNum==0){
		$('#tishi').text('请填写完整信息后提交！');
		return;
	}
	
	if(scheduleName.length>20){
		$('#tishi').text('会议名称过长！');
		return;
	}
	
	if(address.length>20){
		$('#tishi').text('地点名称过长！');
		return;
	}
	
	
	
	var lastEndDate  = new Date(lastEndTime);
	var startDate = new Date(startTime);
	var endDate = new Date(endTime);
	
	var lastEndDateStamp = lastEndDate.getTime();
	var startDateStamp = startDate.getTime();
	var endDateStamp = endDate.getTime();
	/*if(startDateStamp<lastEndDateStamp){
		$('#tishi').text('开始日期不能小于上次结束日期！');
		return;
	}*/
	
	if(endDateStamp<startDateStamp){
		$('#tishi').text('开始日期不能大于结束日期！');
		return;
	}
	
	//获取添加的人员信息
	var jobNumArrayTmp = $('.userIdToServer');
	var jobNumberArray = new Array();
	$.each(jobNumArrayTmp,function(index,item){
		jobNumberArray[index] = $(item).text();
	});
	
	var jobNumberText = JSON.stringify(jobNumberArray);
	var params ={"scheduleName":scheduleName,"startTime":startTime,"endTime":endTime,"address":address,"jobNumberText":jobNumberText};
	confirmbutton.removeEventListener( "click",addSchedule,false );
	$.ajax({
		type : 'POST',
		url : 'meeting/addSchedule',
		contentType: "application/json; charset=utf-8",
		timeout : 500,
		data:JSON.stringify(params),
		dataType : "json",
		success : function(data) {
         if(data!=null && data!=undefined){
        	if(data.accessType=='1'){
        		window.location.href="meetingManage.html";
        	}else if(data.accessType=='7'){
        		$.each(data.userList,function(index,item){
        			var name = item.uName;
        			returnArray[index] = name;
        		});
        		$('#tishi').text(JSON.stringify(returnArray)+",没有录指静脉");
        		confirmbutton.addEventListener( "click",addSchedule,false );
        	}else{
        		$('#tishi').text('添加日程失败!');
        	}
         }else{
         }
     },
     error : function(data) {
     }
 });
}
 

function loadSchdule(){
	$.ajax({
		type : 'POST',
		url : 'meeting/loadSchedule',
		contentType: "application/json; charset=utf-8",
		timeout : 500,
		dataType : "json",
		success : function(data) {
         if(data!=null && data!=undefined){
        	if(data.accessType=='1'){
        		$.each(data.scheduleList,function(index,item){
        			var status;
        			var scheduleName = item.scheduleName;
        			var address = item.address;
        			var startTime = item.startTime;
        			var startDate = new Date(startTime);
        			var startTimeText = timeStamp2Date(startDate);
        			var endTime = item.endTime;
        			var endDate = new Date(endTime);
        			var endTimeText = timeStamp2Date(endDate);
        			var participantNum = item.participantNum;
        			//获取当前时间
        			var nowTime= Date.parse(new Date());
        			var scheduleId = item.scheduleId;
        			
        			if(nowTime>endTime){
        				status = '已过期';
        			}else if(nowTime<endTime && nowTime>startTime){
        				status = '进行中';
        			}else{
        				status='未进行';
        			}
        			$('#mainSchedule').append(
        					"<tr>"
        						+"<td>"+scheduleName+"</td>"
        						+"<td>"+scheduleId+"</td>"
        						+"<td>"+address+"</td>"
        						+"<td>"+startTimeText+"</td>"
        						+"<td class='lastdate'>"+endTimeText+"</td>"
        						+"<td class='lastdate'>"+participantNum+"</td>"
        						+"<td class='lastdate'>"+status+"</td>"
        						+"<td>"
        							+"<button type='button' class='btn btn-primary' data-toggle='modal' data-target='.b-example-modal' onclick='showscheduleuser(this);'>查看人员</button>"
        							+"<button type='button' class='btn btn-success' data-toggle='modal' data-target='.bss-example-modals' style='margin:0 6px 0 6px' onclick='showScheduleDetil(this);'>查看会议</button>"
        							+"<button type='button' class='btn btn-default' style='color:red;' onclick='delScheduler(this);'>删除</button>"
        						+"</td>"
        					+"</tr>"
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

function showscheduleuser(obj){
	localStorage.clear();
	$('#signTableDetil tr').not(':first').remove();
	var $_scheduleId = $(obj).parent().parent().children('td').eq(1);
	var scheduleId = $_scheduleId.text();
	var scheduleStartTime = $(obj).parent().parent().children('td').eq(3).text();
	var scheduleEndTime = $(obj).parent().parent().children('td').eq(4).text();
	
	var scheduleStartTimeStamp =  Date.parse(new Date(scheduleStartTime));
	var scheduleEndTimeStamp = Date.parse(new Date(scheduleEndTime));
	
	
	var params = {"scheduleId":scheduleId};
	
	$.ajax({
		type : 'POST',
		url : 'meeting/getScheduleUser',
		contentType: "application/json; charset=utf-8",
		timeout : 500,
		dataType : "json",
		data:JSON.stringify(params),
		success : function(data) {
         if(data!=null && data!=undefined){
        	if(data.accessType=='1'){
        		//已签到的人员
        		$.each(data.userListSign,function(index,item){
        			var status='';
        			var name = item.uName;
        			var signTimeText ='';
        			var signOutText='';
        			var signOutStamp;
        			var sigTimeStamp;
        			var userId = item.uId;
        			localStorage.setItem(userId, userId);
        			
        			$.each(item.signList,function(index,item){
        				//签到时间
        				sigTimeStamp = item.signTime;
        				var signTimeDate = new Date(sigTimeStamp);
        				signTimeText = timeStamp2Date(signTimeDate);
        				
        				
        				//签出时间
        				signOutStamp = item.signOut;
        				if(signOutStamp!=null && signOutStamp!=undefined){
        					var signOutDate = new Date(signOutStamp);
        					signOutText = timeStamp2Date(signOutDate);
        				}
        			});
        			
        			
        			status ='已签到';
        			/*if(sigTimeStamp>=scheduleStartTimeStamp && sigTimeStamp<= scheduleEndTimeStamp){
        			 * status ='已签到';
        			}else{
        				status ='未签到';
        			}*/
        			$('#signTableDetil').append(
        					"<tr>"
        						+"<td>"+name+"</td>"
        						+"<td>"+status+"</td>"
        						+"<td>"+signTimeText+"</td>"
        					+"</tr>"
        			);
        		});
        		
        		
        		//将没有签到的人员添加上
        		$.each(data.userList,function(index,item){
        			var status='未签到';
        			var name = item.uName;
        			var userId = item.uId;
        			var signTimeText ='';
        			var signOutText='';
        			
        			if(localStorage.getItem(userId)==null ||localStorage.getItem(userId)==undefined){
        				$('#signTableDetil').append(
            					"<tr>"
            						+"<td>"+name+"</td>"
            						+"<td>"+status+"</td>"
            						+"<td>"+signTimeText+"</td>"
            						+"<td>"+signOutText+"</td>"
            					+"</tr>"
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

function showScheduleDetil(obj){
	$('#checkusertable tr').not(':first').remove();
	$('#ben').val('');
	$('#strtim').val('');
	$('#enTim').val('');
	$('#ben2').val('');
	var scheduleName = $(obj).parent().parent().children('td').eq(0).text();
	var scheduleId = $(obj).parent().parent().children('td').eq(1).text();
	var address = $(obj).parent().parent().children('td').eq(2).text();
	var startTimeText = $(obj).parent().parent().children('td').eq(3).text();
	var endTimeText = $(obj).parent().parent().children('td').eq(4).text();
	
	
	//获取当前日程参与人员
	var params ={"scheduleId":scheduleId};
	$.ajax({
		type : 'POST',
		url : 'meeting/getScheduleUser',
		contentType: "application/json; charset=utf-8",
		timeout : 500,
		data:JSON.stringify(params),
		dataType : "json",
		success : function(data) {
         if(data!=null && data!=undefined){
        	if(data.accessType=='1'){
        		$.each(data.userList,function(index,item){
        			var name = item.uName;
        			var userId = item.ujobNum;
        			$('#checkusertable').append(
        					"<tr>"
        						+"<td>"+userId+"</td>"
        						+"<td>"+name+"</td>"
        					+"</tr>"
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
	
	$('#ben').val(scheduleName);
	$('#strtim').val(startTimeText);
	$('#enTim').val(endTimeText);
	$('#ben2').val(address);
}

function delScheduler(obj){
	var scheduleId = $(obj).parent().parent().children('td').eq(1).text();
	//删除当前的日程
	var params ={"scheduleId":scheduleId};
	$.ajax({
		type : 'POST',
		url : 'meeting/delSchedule',
		contentType: "application/json; charset=utf-8",
		timeout : 500,
		data:JSON.stringify(params),
		dataType : "json",
		success : function(data) {
         if(data!=null && data!=undefined){
        	if(data.accessType=='1'){
        		window.location.href="meetingManage.html";
        	}else{
        	}
         }else{
         }
     },
     error : function(data) {
     }
 });
}

function modifypsw(){
	var origPassword = $('#txtName').val();
	var newPassword1 = $('#txtPwd').val();
	var newPassword2 = $('#txtPwd2').val();
	var flag = checkParameter(origPassword,newPassword1,newPassword2);
	if(flag==true){
		//修改密码
    	params = {"password":origPassword,"newpassword":newPassword1};
    	$.ajax({
    		type : 'POST',
    		url : 'vein/changepwd',
    		async:true,
    		contentType: "application/json; charset=utf-8",
            timeout : 500,
            dataType : "json",
            data:JSON.stringify(params),
            success : function(data) {
                if(data!=null && data!=undefined){
                	if(data.accessType=='1'){
                		window.location.href="login.html";
                	}else if(data.accessType=='4'){
                		$('#warn').show();
                	}
                }
            },
            error : function(data) {
            	alert('fail');
            }
        });
	}
}

function checkParameter(parm1,param2,param3){
	if(parm1.trim().length==0){
		$('#warn').show();
		return false;
	}else{
		$('#warn').hide();
	}
	if(param2===param3 && param2.trim().length>0 && param3.trim().length>0){
		$('#warn').hide();
		$('#warn2').hide();
		$('#warn3').hide();
		return true;
	}else{
		$('#warn2').show();
		$('#warn3').show();
		return false;
	}
	return true;
}


function canceladd(){
	confirmbutton.addEventListener( "click",addSchedule,false );
}

function selectallusers(){
	var checkFlag = $('#selectalluser').children(':first').is(':checked');
	if(checkFlag){
		$(":checkbox").prop("checked",true); 
	}else{
		$(":checkbox").prop("checked",false); 
	}
}