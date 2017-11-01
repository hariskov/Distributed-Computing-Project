'use strict';

(function(){

var configFunction = function($routeProvider){
console.log('provider');

    $routeProvider
        .when('/',{
            templateUrl :'resources/html/templates/main.html',
            controller : 'mainController',
            controllerAs : 'mnCtrl'
        })
        .when('/search',{
            templateUrl :'resources/html/templates/search.html',
            controller : 'searchController',
            controllerAs : 'scCtrl'
        })
        .when('/play',{
            templateUrl :'resources/html/templates/game.html',
            controller : 'gameController',
            controllerAs : 'gmCtrl'
        })
};

var app = angular.module('mainApp',['ngRoute']);

app.config(['$routeProvider',configFunction]);


})();