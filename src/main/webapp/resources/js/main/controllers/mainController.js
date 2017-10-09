"use strict";

(function(){

var mainFunction = function(mainService, $scope, $http, $location){
    var self = this;

    self.card = 0;

    self.submit = function(){
        mainService.submit(self.card).then(
            function(success){

            },
            function(error){
                console.log(error)
            }
        );
    }
}

var app = angular.module('mainApp');

app.controller('mainController',['mainService', '$scope' ,'$http', '$location', mainFunction]);

})()