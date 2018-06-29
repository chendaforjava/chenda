var deptMap = new VeinMap();
var deptIdMap = new VeinMap();
var pageCount;
$(function() {
	localStorage.clear();
	// 获取当前登录用户的所属公司和用户名
	ManagerInfo();
	var tou = $(".tou");
	var xiala = $(".xiala");
	tou.click(function() {
		xiala.toggle();
	});
});

function ManagerInfo() {

	$.ajax({
		type : 'POST',
		url : 'dept/getManagerInfo',
		contentType : "application/json; charset=utf-8",
		timeout : 500,
		dataType : "json",
		success : function(data) {
			if (data.accessType == '1') {
				$('#corporationName').text(data.corporation.corName);
				$.each(data.corporation.managerList, function(index, item) {
					$('#administraterName').text(item.mid);
					localStorage.setItem("adminName", item.mid);
				});
				localStorage.setItem("corName", data.corporation.corName);
				localStorage.setItem("corId", data.corporation.corId);
				fillDept("1");
				pageShow(pageCount);
			}
		},
		error : function(data) {
		}
	});

}


function fillDept(page) {
	var deptName = $('#InquireNameId').val();
	params = {
		"page" : page,
		"deptName" : deptName
	};
	
	$.ajax({
				type : 'POST',
				url : 'dept/getDeptByPage',
				contentType : "application/json; charset=utf-8",
				timeout : 500,
				async : false,
				dataType : "json",
				data : JSON.stringify(params),
				success : function(data) {
					if (data.accessType == '1') {
						console.log(data);
						$('#deptstable tr').not(':first').remove();
						$.each(data.deptList,function(index, item) {
							var deptName = item.deptName;
							deptMap.put(deptName.toString(),deptName.toString());
							deptIdMap.put(deptName.toString()+ "#", item.deptId.toString());
							fillPage(deptName);
						});
						pageCount = data.totalCount;
					}
				},
				error : function(data) {
				}
			});
}


function pageShow(pageCount){
	$('#paginationId').show();
	$('#paginationNameId').hide();
	var pageIndex = 0;
	$('#paginationId').pagination({
		pageCount : pageCount,
		jump : true,
		coping : true,
		current_page : pageIndex, // 当前页索引
		callback : function(current_page) {
			fillDept(current_page);
		}
	});
}


function pageShow2(pageCount){
	$('#paginationId').hide();
	$('#paginationNameId').show();
	var pageIndex = 0;
	$('#paginationNameId').pagination({
		pageCount : pageCount,
		jump : true,
		coping : true,
		current_page : pageIndex, // 当前页索引
		callback : function(current_page) {
			fillDept(current_page);
		}
	});
}


function fillPage(deptName){
	$('#deptstable').append(
			"<tr>"
					+ "<td>"
						+ deptName
					+ "</td>"
					+ " <td>"
						+ "<button type='button' class='btn btn-primary' data-toggle='modal' data-target='.bs-example-modal-sm2' onclick='fillText(this);'>编辑</button>"
						+ " <button type='button' onclick='deleteDept(this)' class='btn btn-default'>删除</button>"
					+ "</td>"
				+ "</tr>");
}


function fillText(obj) {
	var t = $(obj).parent().prev().text();
	$("#exampleInputAmount").val(t);
	var deptId = deptIdMap.get(t + "#");
	$('#hidespan').val(deptId);
}

function bumen() {
	var flag = true;
	var deptName = $('#departmentInput').val();
	if (deptName.length == 0 || deptName.trim().length == 0) {
		$('#deptAddId').text('请输入部门名称!');
		flag = false
	}

	var reg = /^\s|\s#"/;
	if (reg.test(deptName)) {
		$('#deptAddId').text('不能以空格开头或结尾!');
		flag = false;
	}

	if (deptName.length > 10) {
		$('#deptAddId').text('部门名称过长!');
		flag = false;
	}

	if (deptMap.get(deptName.toString()) != null
			&& deptMap.get(deptName.toString()).length > 0) {
		$('#deptAddId').text('该部门已存在!');
		flag = false;
	}

	if (flag) {
		params = {
			"deptName" : deptName
		};
		$
				.ajax({
					type : 'POST',
					url : 'dept/saveDept',
					contentType : "application/json; charset=utf-8",
					timeout : 500,
					dataType : "json",
					data : JSON.stringify(params),
					success : function(data) {
						if (data.accessType == "1") {
							deptMap.put(deptName.toString(), deptName
									.toString());
							deptIdMap.put(deptName.toString()+ "#",data.dept.deptId);
							window.location.reload();
						} else {
							$('#deptAddId').text('添加部门失败!');
						}
					},
					error : function(data) {
						$('#deptAddId').text('添加部门失败!');
					}
				});
	}
}

function deleteDept(obj) {
	var con;
	con = confirm("该部门下的人员将被清空！"); // 在页面上弹出对话框
	if (con == true) {
		var deptName = $(obj).parent().prev().text();
		params = {
			"deptName" : deptName
		};
		$.ajax({
			type : 'POST',
			url : 'dept/delDept',
			contentType : "application/json; charset=utf-8",
			timeout : 500,
			dataType : "json",
			data : JSON.stringify(params),
			success : function(data) {
				if (data.accessType == "1") {
					$(obj).parent().parent().remove();
					deptMap.remove(deptName.toString());
					deptIdMap.remove(deptName.toString() + "#");
					window.location.reload();
				} else {
				}
			},
			error : function(data) {
			}
		});
	}else{
		window.location.reload();
	}

}

function InquireDept() {
	var page = "1";
	fillDept(page);
	pageShow2(pageCount);
}

function clearText() {
	$('#departmentInput').val('');
	$('#deptAddId').text('');
}

function VeinMap() {
	var obj = {};

	// put方法
	this.put = function(key, value) {
		obj[key] = value;
	}

	// 对象大小
	this.size = function() {
		var num = 0;
		for ( var arr in b = obj) {
			num++;
		}
		return num;
	}

	// 通过key获得值
	this.get = function(key) {
		if (obj[key] || obj[key] === 0 || obj[key] == false) {
			return obj[key];
		} else {
			return null;
		}
	}

	// 移除key
	this.remove = function(key) {
		delete obj[key];
	}

	// 遍历对象里的key
	this.eachMap = function(fn) {
		for ( var arr in obj) {
			fn(arr, obj[arr]);
		}
	}

}
