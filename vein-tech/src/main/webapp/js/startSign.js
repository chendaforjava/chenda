var signMap = new Map();
var corId;
var ws;
var target;
var currentDeptId;
$(function(){
	var corName = localStorage.getItem("corName");
	$('#coporationicon').text(corName);
	//localStorage.clear();
	currentDeptId = GetQueryString("deptId");
	getCorId();
	getDeptInfo();
	getUserInfo();
	webSocketConnect();
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

function getDeptInfo(){
	$.ajax({
		type : 'POST',
		async: false,
		url : 'dept/getAllDept',
		contentType: "application/json; charset=utf-8",
        timeout : 500,
        dataType : "json",
        success : function(data) {
            if(data!=null && data!=undefined){
            	if (data.accessType == '1') {
					$.each(data.deptList, function(index, item) {
						var deptName = item.deptName;
						var deptId = item.deptId;
						sessionStorage.setItem(deptId, deptName);
					});
				}
            }
        },
        error : function(data) {
        }
    });
}

function getUserInfo(){
	var params = {"deptId":currentDeptId};
	$.ajax({
		type : 'POST',
		url : 'sign/getSignUserInfo',
		contentType: "application/json; charset=utf-8",
        timeout : 500,
        async: false,
        data:JSON.stringify(params),
        dataType : "json",
        success : function(data) {
            if(data!=null && data!=undefined){
            	var allUserSize = data.allUserSize;
            	var signUserSize = data.signUserSize;
            	var notSignUser = allUserSize - signUserSize;
            	$('#notSigned').text(notSignUser);
            	$('#signedUser').text(signUserSize);
            	$('#leftTable tr').not(":first").remove();
            	$('#rightTable tr').not(":first").remove();
            	$.each(data.signUserList,function(index,item){
            		$.each(item.signList,function(a,b){
            			if(b.signId!=null && b.signId !=undefined){
            				localStorage.setItem(b.uId, b.uId);
            			}
            		});
            	})
            	$.each(data.allUserList,function(index,item){
            		var ujobNum = item.ujobNum;
            		var uId = item.uId;
            		var name = item.uName;
            		var dept = sessionStorage.getItem(item.deptId.toString());
            		var fingerCount = item.veinFingerList.length;
            		if(fingerCount==1){
            			$.each(item.veinFingerList,function(index,childitem){
            				if(childitem.veinId==null || childitem.veinId==undefined){
            					fingerCount = 0;
            				}
            			});
            		}
            		
            		var flag = localStorage.getItem(uId);
            		//alert(flag);
            		if(flag == uId){
            			//说明该用户已签到
            			var rightUid = "right"+ujobNum;
            			$('#rightTable').append(
            					"<tr id="+rightUid+">"
		          					  +"<td id="+ujobNum+">"+ujobNum+"</td>"
		          					  +"<td>"+name+"</td>"
		          					  +"<td>"+dept+"</td>"
		          					  +"<td>"+fingerCount+"</td>"
		          				+"</tr>"
            			);
            		}else{
            			//没签到
            			$('#leftTable').append(
                				"<tr>"
            						  +"<td id="+ujobNum+">"+ujobNum+"</td>"
                					  +"<td>"+name+"</td>"
                					  +"<td>"+dept+"</td>"
                					  +"<td>"+fingerCount+"</td>"
                				+"</tr>"
                		);
            		}
            	});
            }
        },
        error : function(data) {
        }
    });
}

function changeSignStatus(uId){
	var rightUid = "right"+uId;
	var userSignDom = $('#'+rightUid);
	if(userSignDom.html() ==null || userSignDom.html() ==undefined){
		var tempHtml = "<tr id="+rightUid+">"+ $('#'+uId).parent().html()+"</tr>";
		$('#'+uId).parent().remove();
		$('#rightTable').append(
				tempHtml
		);
		var num1 = $('#notSigned').text();
		num1 = parseInt(num1)-1;
		$('#notSigned').text(num1);
		var num2 = $('#signedUser').text();
		num2 = parseInt(num2)+1;
		$('#signedUser').text(num2);
	}
}

function GetQueryString(name){
    var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if(r!=null)return  unescape(r[2]); return null;
}

function selectChange(){
	var startSelectValue = $("#sblx option:selected").val();
	var optionArray = $('#sblx2 option');
	$.each(optionArray,function(index,item){
		var nowTimeValue = parseInt($(item).val());
		if(nowTimeValue<parseInt(startSelectValue)){
			$(item).attr('disabled','desabled');
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
        changeSignStatus(uId);
    }
}
function subSend(){
    var msg = document.getElementById("msg").value;
    ws.send(msg);
    document.getElementById("msg").value="";
}

window.onunload = function() {
    ws.close();
}
