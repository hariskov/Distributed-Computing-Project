"use strict";

(function(){

var mainFunction = function(mainService, $scope, $http, $location){
    console.log('function controller');

}

var app = angular.module('mainApp');

app.controller('mainController',['loginService', '$scope' ,'$http', '$location', mainFunction]);

})()