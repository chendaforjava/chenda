/**
 * Created by lijinduo on 2017/11/17.
 */
$(function ($) {
    //弹出登录
    $("#example").hover(function () {
        $(this).stop().animate({
            opacity: '1'
        }, 600);
    }, function () {
        $(this).stop().animate({
            opacity: '0.6'
        }, 1000);
    }).on('click', function () {
        $("body").append("<div id='mask'></div>");
        $("#mask").addClass("mask").fadeIn("slow");
        $("#LoginBox").fadeIn("slow");
    });
    //
    //按钮的透明度
    $("#loginbtn").hover(function () {
        $(this).stop().animate({
            opacity: '1'
        }, 600);
    }, function () {
        $(this).stop().animate({
            opacity: '0.8'
        }, 1000);
    });
    //文本框不允许为空---按钮触发

    //文本框不允许为空---单个文本触发

    //关闭
    $(".close_btn").hover(function () { $(this).css({ color: 'black' }) }, function () { $(this).css({ color: '#999' }) }).on('click', function () {
        $("#LoginBox").fadeOut("fast");
        $("#mask").css({ display: 'none' });
    });
});

function ckoe() {
    /*原始密码*/
    var txtName = $("#txtName").val();
    /*第一次输入的密码*/
    var txtPwd = $("#txtPwd").val();
    /*第二次输入的密码*/
    var txtPwd2 = $("#txtPwd2").val();

    /*原始密码提示*/
    var yti= $("#warn")
    /*密码提示*/
    var mti = $("#warn2")
    /*第二次密码提示*/
    var erti = $("#warn3")

    /*---------原始密码为空--------------*/
    if(txtName==='' || txtName.length<6){
        yti.css({"display":"block","color":"red"})
        return false;
    }else if(txtPwd==="" || txtPwd.length<6){
        mti.css({"display":"block","color":"red"})
        return false;
    }else if(txtPwd!=txtPwd2){
        erti.css({"display":"block","color":"red"})
        return false;
    }else {

    }
    return true;

}