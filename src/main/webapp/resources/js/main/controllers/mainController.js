"use strict";

(function(){

var mainFunction = function(mainService, $scope, $http, $location){
    var self = this;

    self.card = {
        cardSign:"3",
        cardValue:"2"
    };

    self.discoverDone=false;


    self.discover = function(){
        mainService.discover().then(
            function(success){
                console.log(success);
                self.discoverDone = true;
            },function(fail){
                console.log(fail);
            }
        )
    };

    self.submit = function(){
        mainService.submitCard(self.card).then(
            function(success){
                console.log(success)
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