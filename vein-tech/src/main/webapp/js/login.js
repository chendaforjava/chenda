$(function(){
	/*getAllCorporationInfo();*/
});


function getAllCorporationInfo(){
	$.ajax({
		type : 'POST',
		url : 'login/corporationInfo',
		contentType: "application/json; charset=utf-8",
        timeout : 500,
        dataType : "json",
        success : function(data) {
        	if(data.accessType=='1'){
        		$.each(data.corporationList,function(index,item){
        			var corName = item.corName;
        			var corId = item.corId;
        			$('#corporationselect').append(
        					"<option style='color:#000' value="+corId+">"+corName+"</option>"
        			);
        		});
        	}
        },
        error : function(data) {
        }
    });
}