/**
 * Created by lijinduo on 2017/11/30.
 */
function tianjia() {
	/*var kaishi = $('#name1').val();
     var d = new Date(kaishi);
     var end = $()
    //获取添加时间的初始时间
    var time= $("#name1").val();
    var s = new Date(time)
      var a =  d.getTime();
      var b = s.getTime();
      var tishi = $("#tishi")
      if(b<a){
          tishi.html("起止时间与上次时间冲突").css({"color":"red"})
      }else {
          tishi.html("")var kaishi =$('.lastdate').eq(-1).text();
   
      }*/
	var lastTime = $('#mainSchedule tr:last').children('td').eq(3).text();
	alert(lastTime);
}