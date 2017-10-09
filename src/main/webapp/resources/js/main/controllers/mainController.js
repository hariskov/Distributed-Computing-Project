"use strict";

(function(){

var mainFunction = function(mainService, $scope, $http, $location){
    var self = this;

    self.card = {
        cardSign:"3",
        cardValue:"2"
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