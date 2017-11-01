"use strict";

(function(){

    var mainFunction = function(gameService, $scope, $http, $window , $location){


        var self = this;
        var stompClient = null;

        self.card = {
            cardSign:"3",
            cardValue:"2"
        };

        self.discoverDone=false;
        self.canPlay=false;

        self.submit = function(){
            gameService.submitCard(self.card).then(
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

                // self.discoverDone=true;

                self.canPlay=resultMap;

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


        $window.onbeforeunload = function (e) {

            gameService.exit().then(
                function(success){
                    console.log(success);
                },function(fail){
                    console.log(fail);
                }
            )

        };

        self.enterFunction = function () {
            gameService.enter().then(
                function(success){
                    console.log(success);
                },function(fail){
                    console.log(fail);
                }

            )
        };

    };

    var app = angular.module('mainApp');

    app.controller('gameController',['gameService', '$scope' ,'$http', '$window', '$location', mainFunction]);

})()
