'use strict';

(function(){

var configFunction = function($routeProvider){
console.log('provider');

    $routeProvider
        .when('/',{
            templateUrl :'static/html/templates/main.html',
            controller : 'mainController',
            controllerAs : 'mnCtrl'
        })
}

var app = angular.module('mainApp',['ngRoute']);

app.config(['$routeProvider',configFunction]);


})()