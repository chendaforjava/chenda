var userIdGloble;
var userIdMap = new Map();
var deptMap = new Map();
var pageCount = 0;

var imageURL = "http:\/\/120.77.84.143:8877\/image/vein\/";
$(function() {
	
	var adminName = localStorage.getItem("adminName");
	var corName = localStorage.getItem("corName");
	$('#coporationicon').text(corName);
	$("#administraterName").text(adminName);
	loadDept();
	initUsers();

});

function initUsers() {
	var deptId = $("#bumenselect option:selected").val();
	var userName = $("#InquireNameId").val();
	if (userName.length == 0) {
		userName = '';
	}
	var parame = {
		"page" : "1",
		"deptId" : deptId,
		"userName" : userName
	};
	$.ajax({
				type : 'POST',
				url : 'user/sysuserlist',
				async : true,
				contentType : "application/json; charset=utf-8",
				timeout : 500,
				data : JSON.stringify(parame),
				dataType : "json",
				success : function(data) {
					if (data != null && data != undefined) {
						if (data.accessType == '1') {
							pageCount = data.totalCount; // 总页数
							inserUsers(data.sysuserList);
							initPage(pageCount);
						}
					}
				},
				error : function(data) {
				}
			});
}

function initPage(pageCount) {
	$('#paginationId').show();
	$('#paginationDeptId').hide();
	$('#paginationNameId').hide();
	var pageIndex = 0;
	$('#paginationId').pagination({
		pageCount : pageCount,
		jump : true,
		coping : true,
		current_page : pageIndex, // 当前页索引
		callback : function(current_page) {
			fillingUser(current_page);
		}
	});
}

function initDeptPage(pageCount) {
	$('#paginationDeptId').show();
	$('#paginationId').hide();
	$('#paginationNameId').hide();
	var pageIndex = 0;
	$('#paginationDeptId').pagination({
		pageCount : pageCount,
		jump : true,
	
		coping:true,			//首页和尾页
		homePage:'首页',			//首页节点内容
		endPage:'末页',	
		current_page : pageIndex, // 当前页索引
		callback : function(current_page) {
			fillingUser(current_page);
		}
	});
}

function initNamePage(pageCount){
	$('#paginationDeptId').hide();
	$('#paginationId').hide();
	$('#paginationNameId').show();
	var pageIndex = 0;
	$('#paginationNameId').pagination({
		pageCount : pageCount,
		jump : true,
		coping : true,
		current_page : pageIndex, // 当前页索引
		callback : function(current_page) {
			fillingUser(current_page);
		}
	});
}


function fillingUser(page) {
	if (page == null || page == undefined || page==0) {
		page = 1;
	}
	var deptId = $("#bumenselect option:selected").val();
	var userName = $("#InquireNameId").val();
	if (userName.length == 0) {
		userName = '';
	}
	var parame = {
		"page" : page,
		"deptId" : deptId,
		"userName" : userName
	};
	$.ajax({
				type : 'POST',
				url : 'user/sysuserlist',
				async : true,
				contentType : "application/json; charset=utf-8",
				timeout : 500,
				data : JSON.stringify(parame),
				dataType : "json",
				success : function(data) {
					if (data != null && data != undefined) {
						if (data.accessType == '1') {
							pageCount = data.totalCount; // 总页数
							inserUsers(data.sysuserList);
						}
					}
				},
				error : function(data) {
				}
			});

}

$("#bumenselect").change(
		function(){
			var deptId = $("#bumenselect option:selected").val();
			var userName = $("#InquireNameId").val();
			if (userName.length == 0) {
				userName = '';
			}
			var page = "1";
			var parame = {
				"page" : page,
				"deptId" : deptId,
				"userName" : userName
			};
			$.ajax({
				type : 'POST',
				url : 'user/sysuserlist',
				async : true,
				contentType : "application/json; charset=utf-8",
				timeout : 500,
				data : JSON.stringify(parame),
				dataType : "json",
				success : function(data) {
					if (data != null && data != undefined) {
						if (data.accessType == '1') {
							pageCount = data.totalCount; // 总页数
							inserUsers(data.sysuserList);
							initDeptPage(pageCount);
						}
					}
				},
				error : function(data) {
				}
			});
			
		}
);

function InquireUser() {
	var inquireName = $('#InquireNameId').val();
	var deptNo = $("#bumenselect option:selected").val();
	var page = "1";
	var params = {
		"page":page,
		"userName" : inquireName,
		"deptId" : deptNo
	};
	
	$.ajax({
		type : 'POST',
		url : 'user/sysuserlist',
		async : true,
		contentType : "application/json; charset=utf-8",
		timeout : 500,
		data : JSON.stringify(params),
		dataType : "json",
		success : function(data) {
			if (data != null && data != undefined) {
				if (data.accessType == '1') {
					pageCount = data.totalCount; // 总页数
					inserUsers(data.sysuserList);
					initNamePage(pageCount);
				}
			}
		},
		error : function(data) {
		}
	});
}


function loadDept() {
	/*var corId = localStorage.getItem("corId");
	var params = {
		"corId" : corId
	};*/
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
						$("#bumenselect").append(
									"<option value='" + deptId + "'>" + deptName+ "</option>"
								);
						$("#departmentId").append(
								"<option value='" + deptId + "'>" + deptName+ "</option>"
							);
						
						$('#bumen').append(
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


function modifyInfo() {
	var deptno = $("#departmentId option:selected").val();
	var name = $('#name1').val();
	var phone = $('#phone1').val();
	if (phone.length == 0 || phone == undefined || phone == null) {
		phone = "";
	}
	var flag = checkPhoneNumReg(phone);
	if (!flag) {
		$('#phonespan').show();
		return;
	}
	if (name.trim().length == 0) {
		$('#namespan').show();
		return;
	}
	params = {
		"dptno" : deptno,
		"name" : name,
		"phone" : phone,
		"userId" : userIdGloble
	};
	$.ajax({
		type : 'POST',
		url : 'vein/modifyuser',
		contentType : "application/json; charset=utf-8",
		timeout : 500,
		dataType : "json",
		data : JSON.stringify(params),
		success : function(data) {
			if (data != null && data != undefined) {
				if (data.accessType == 1) {
					window.location.href = "userManage.html";
				}
			}
		},
		error : function(data) {
			alert('fail');
		}
	});
}
function checkPhoneNumReg(phone) {
	var pho1 = /^1[3|4|5|7|8][0-9]{9}$/;
	var flag = false;
	if (phone.length == 0) {
		flag = true;
	} else {
		flag = pho1.test(phone);
	}
	return flag;
}
function getManagerInfo() {
	// 获取部门信息和管理员信息
	$
			.ajax({
				type : 'POST',
				url : 'vein/getdeptandmanage',
				contentType : "application/json; charset=utf-8",
				timeout : 500,
				dataType : "json",
				success : function(data) {
					if (data != null && data != undefined) {
						$
								.each(
										data.dept,
										function(index, item) {
											var deptno = item.deptNo;
											var deptName = item.deptName;
											localStorage.setItem(deptName,
													deptno);
											$("#bumen").append(
													"<option value='" + deptno
															+ "'>" + deptName
															+ "</option>");
											$("#departmentId").append(
													"<option value='" + deptno
															+ "'>" + deptName
															+ "</option>");
											$("#bumenselect").append(
													"<option value='" + deptno
															+ "'>" + deptName
															+ "</option>");
											$(
													'<tr style="width:60%">'
															+ '<td style="margin-left:20px;">'
															+ '<span class="span-class">'
															+ deptName
															+ '</span>'
															+ '</td>'
															+ '<td><span style="margin-left: 30px; color:red; cursor: pointer;"  onclick="shan(this)">删除</span></td>'
															+ '</tr>')
													.appendTo(".tabless");
										});

						$.each(data.managerMessage, function(index, item) {
							$('#administraterName').text(item);
							localStorage.setItem("adminName", item);
						});

					} else {
					}
				},
				error : function(data) {
				}
			});
}

function inserUsers(obj){
	$('#userstable tr').not(':first').remove();
	$.each(obj,function(index, item) {
						var name = item.uName;
						var uSex = item.uSex;
						var jobNum = item.ujobNum;
						var uPhone = item.uPhone;
						var deptId = item.deptId;
						if(uPhone==null || uPhone==undefined){
							uPhone = "";
						}
						var fingerCount = 0;
						var veinFingerList = item.veinFingerList;
						$.each(veinFingerList,function(index,item){
	             			if(item.veinId!=null && item.veinId!=undefined){
	             				fingerCount = index+1;
	             			}else{
	             				fingerCount = 0;
	             			}
	             		});
						var uPhotoUrl = item.uPhotoUrl;
						if (uPhotoUrl == null
								|| uPhotoUrl == undefined ||uPhotoUrl=="") {
							if (uSex == '男') {
								uPhotoUrl = "img/boy.jpg";
							} else {
								uPhotoUrl = "img/girl.png";
							}
						} else {
							uPhotoUrl =imageURL+uPhotoUrl;
						}
						$("#userstable").append(
								"<tr>" 
									+ "<td>"+ name+ "</td>"
									+ "<td>"+ uSex+ "</td>"
									+ "<td>"+ jobNum+ "</td>"
									+ "<td>"+ uPhone+ "</td>"
									+ "<td>"+ fingerCount+ "</td>"
									+ "<td>"
										+ "<img src="+ uPhotoUrl+ " width='45' height='45' onclick='imgClick(this)'>"
									+ "</td>"
									+ "<td>"
										+"<button type='button' class='btn btn-primary chakan' onclick='getInfoDetil(this);' id="+deptId+">编辑</button>"
										+"<button type='button' class='btn' onclick='delUser(this);'>删除</button>"
									+ " </td>"
								+ "</tr>"
							);
					});
}


function imgClick(obj){
	
	$('#imageblock').show();
	
	var imageURL =$(obj).attr('src');
	
	$('#showBigImage').attr('src',imageURL);
}


function imageListener(){
	$('#imageblock').hide();
}


function getInfoDetil(obj) {
	
	var deptName = $(obj).attr('id');
	$("#departmentId").find("option[value = '"+deptName+"']").attr("selected","selected");
	
	var name = $(obj).parent().parent().children(':eq(0)').text();
	var phone = $(obj).parent().parent().children(':eq(3)').text();
	var photoUrl =  $(obj).parent().parent().children(':eq(5)').children(':eq(0)').attr('src');
	userIdGloble = $(obj).parent().parent().children(':eq(2)').text();
	
	/*$("#departmentId").find('option[value="' + dptno + '"]').attr("selected",
			true);*/
	$("#userIddd").val(userIdGloble);
	$('#detilImage').attr('src', photoUrl);
	//$('#depar1').val(deptName);
	$('#name1').val(name);
	$('#phone1').val(phone);

	var chakan = $(".chakan");
	// 获取弹出层
	var ruens = $(".tan");

	// 弹出层的关闭按钮
	var tguan = $('.tanguan');

	ruens.toggle();

	tguan.click(function() {
		ruens.hide();
	})
}

function delUser(obj) {
	var con;
	con = confirm("该人员的所有信息将被清空！"); // 在页面上弹出对话框
	
	if(con==true){
		var jobNum = $(obj).parent().parent().children(':eq(2)').text();
		params = {
			'jobNum':jobNum
		};
		$.ajax({
			type : 'POST',
			url : 'user/delUser',
			async : true,
			contentType : "application/json; charset=utf-8",
			timeout : 500,
			dataType : "json",
			data : JSON.stringify(params),
			success : function(data) {
				if (data != null && data != undefined) {
					if (data.accessType == '1') {
						window.location.reload();
					}else{
						alert('删除失败');
					}
				}
			},
			error : function(data) {
				window.location.reload();
			}
		});
	}
	
	
}