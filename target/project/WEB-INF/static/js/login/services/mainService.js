"use strict";

(function(){

    var initializationServiceFunction = function($http){

    }

    var app = angular.module('mainApp');

    app.service('loginService',['$http', initializationServiceFunction]);

})()