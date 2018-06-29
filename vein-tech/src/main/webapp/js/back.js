/**
 * Created by lijinduo on 2017/11/15.
 */
/**
 * Created by lijinduo on 2017/11/8.
 */
var app = angular.module('myApp', []);
app.controller('myCtrl', function($scope) {
    $scope.myVar = true;
    $scope.toggle = function() {
        $scope.myVar = !$scope.myVar;
    }
});


$(function (){
    var an = $('.ones');
    var shu =$('.luru')
    var input = $('#deper')
    console.log(input)
    var btn = $('.button')


   an.click(function () {
       shu.toggle()


   })


    input.keydown(function () {
        $('btn').attr('disabled',"true");
    })



})


