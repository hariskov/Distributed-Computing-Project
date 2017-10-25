"use strict";

(function(){

var mainFunction = function(mainService, $scope, $http, $location){
    var self = this;
    var stompClient = null;

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
    };

    var socket = new SockJS('http://localhost:8080/project/project/p2-websocket');

    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log('attempt subscribe started');
        stompClient.subscribe('/broker/turn', function (graphResult) {
            console.log('subscribe succeeded');
            var resultMap = JSON.parse(graphResult.body);

            console.log('result ' + resultMap);

            $scope.$apply();
        });

        stompClient.subscribe('/broker/discoveryInProgress', function (graphResult) {
            console.log('subscribe succeeded');
            var resultMap = JSON.parse(graphResult.body);
            self.discoverDone = true;

            $scope.$apply();
        });

        stompClient.send("project/hello", {}, "");

    }, function (message) {
        // disconnect();
        // noServersPopUp();
        console.log(message);
    });

};


var app = angular.module('mainApp');

app.controller('mainController',['mainService', '$scope' ,'$http', '$location', mainFunction]);

})()