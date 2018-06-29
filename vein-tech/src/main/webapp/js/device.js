var deviceId;
$(function(){
	var corName = localStorage.getItem("corName");
	$('#corporationName').text(corName);
	var adminName = localStorage.getItem("adminName");
	
	$('#managereName').text(adminName);
	//加载设备信息
	getDeviceStatus();
	
});



function getDeviceStatus(){
	$('#deviceStatusTable tr').not(":first").remove();
	$.ajax({
		type : 'POST',
		url : 'device/getDeviceStatus',
		contentType: "application/json; charset=utf-8",
        timeout : 500,
        async: false, 
        dataType : "json",
        success : function(data) {
        	console.log(data);
            if(data!=null && data!=undefined){
            	if(data.accessType=='1'){
            		$.each(data.deviceList,function(index,item){
            			var deviceId = item.deviceId;
            			var deviceName = item.deviceName;
            			var status = item.status;
            			var statusText = '';
            			var deviceHtml = '';
            			if(status=='0'){
            				statusText = '在线';
            				deviceHtml = "<td><span class='glyphicon glyphicon-hdd' style='color:green'></span></td>";
            			}else{
            				statusText = '离线';
            				deviceHtml = "<td><span class='glyphicon glyphicon-hdd' style='color:red'></span></td>";
            			}
            			$('#deviceStatusTable').append(
            					"<tr>"
            						+deviceHtml
            						+"<td>"+deviceName+"</td>"
            						+"<td id="+deviceId+">"+deviceId+"</td>"
            						+"<td>"+statusText+"</td>"
            						+"<td>"
            							+"<button type='button' onclick='modifyDeviceName(this);' class='btn btn-primary' data-target='.bs-example-modal-sm1'>编辑</button>"
            							+"<button type='button' class='btn btn-default' onclick='delDevice(this);'>删除</button>"
            						+"</td>"
            					+"</tr> "
            			);
            			
            		});
            	}
            }else{
            }
        },
        error : function(data) {
        }
    });
}



function delDevice(obj){
	var con;
	con = confirm("该设备的所有信息将被清空！"); // 在页面上弹出对话框
	
	if(con==true){
		deviceId = $(obj).parent().parent().children(':eq(2)').text();
		var params = {"deviceId":deviceId};
		$.ajax({
			type : 'POST',
			url : 'device/delDevice',
			contentType : "application/json; charset=utf-8",
			timeout : 500,
			dataType : "json",
			data : JSON.stringify(params),
			success : function(data) {
				if (data != null && data != undefined) {
					if (data.accessType == 1) {
						$(obj).parent().parent().remove();
					}
				}
			},
			error : function(data) {
				alert('fail');
			}
		});
	}
	
}




function modifyDeviceName(obj){
	$('#modifyDeviceDiv').css({"display":"block"});
	deviceId =  $(obj).parent().parent().children(':eq(2)').text();
}

function modifydeviceName(){
	var flag = true;
	//$("#"+deviceId).parent()
	var newName = $('#exampleInputAmount').val();
	var reg = /^\s|\s#"/;
	if(reg.test(newName)){
		$('#prompt').text('不能以空格开头或结尾!');
		flag = false;
	}
	
	if(newName.length>20){
		$('#prompt').text('名字过长!');
		flag = false;
	}
	
	var params ={"deviceId":deviceId,"name":newName};
	if(flag){
		$.ajax({
			type : 'POST',
			url : 'device/changeDeviceName',
			contentType : "application/json; charset=utf-8",
			timeout : 500,
			dataType : "json",
			data : JSON.stringify(params),
			success : function(data) {
				if (data != null && data != undefined) {
					if (data.accessType == 1) {
						$("#"+deviceId).prev().text(newName);
						$('#modifyDeviceDiv').css({"display":"none"});
					}
				}
			},
			error : function(data) {
				alert('fail');
			}
		});
	}
	
}

function colse(){

	$('#modifyDeviceDiv').css({"display":"none"});
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