//var deptMap = new Map();
$(function(){
	$('#coporationName').text(localStorage.getItem("corName"));
	$('#adminstratroId').text(localStorage.getItem("adminName"));
	//加载部门信息
	getDeptInfo();
	//加载人员信息
	getUserInfo();
	$('#department').change(function(){
		getUserInfo();
	});
});

function getDeptInfo(){
	$.ajax({
		type : 'POST',
		url : 'dept/getAllDept',
		async : true,
		contentType : "application/json; charset=utf-8",
		timeout : 500,
		dataType : "json",
		success : function(data) {
			if (data != null && data != undefined) {
				if (data.accessType == '1') {
					$.each(data.deptList, function(index, item) {
						var deptName = item.deptName;
						var deptId = item.deptId;
						sessionStorage.setItem(deptId, deptName);
						$("#department").append(
									"<option value='" + deptId + "'>" + deptName+ "</option>"
								);
					});
				}
			}
		},
		error : function(data) {
		}
	});
}


function getUserInfo(){
	var deptId = $('#department').find("option:selected").val();
	var params = {"deptId":deptId};
	$.ajax({
		type : 'POST',
		url : 'sign/getUserList',
		contentType: "application/json; charset=utf-8",
        timeout : 500,
        data:JSON.stringify(params),
        dataType : "json",
        success : function(data) {
            if(data!=null && data!=undefined){
            	if(data.accessType=="1"){
            		$('#leftTable tr').not(":first").remove();
                	$.each(data.userList,function(index,item){
                		var jobNum = item.ujobNum;
                		var name = item.uName;
                		var deptId = item.deptId;
                		var deptName = sessionStorage.getItem(deptId);
                		var fingerCount = 0;
						var veinFingerList = item.veinFingerList;
						$.each(veinFingerList,function(index,item){
	             			if(item.veinId!=null && item.veinId!=undefined){
	             				fingerCount = index+1;
	             			}else{
	             				fingerCount = 0;
	             			}
	             		});
						
						$('#leftTable').append(
	            				" <tr>"
	            					  +"<td>"+jobNum+"</td>"
	            					  +"<td>"+name+"</td>"
	            					  +"<td>"+deptName+"</td>"
	            					  +"<td>"+fingerCount+"</td>"
	            				+"</tr>"
	            		);
						
                	});
            	}
            }
        },
        error : function(data) {
        }
    });
}

function startSign(){
	var deptId = $('#department').find("option:selected").val();
	window.location.href="signStart.html?deptId="+deptId;
}