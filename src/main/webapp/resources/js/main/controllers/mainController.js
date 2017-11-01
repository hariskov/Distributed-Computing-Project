"use strict";

(function(){

var gameFunction = function(mainService,  $location){

    var self = this;
    console.log('asd');

    self.playerName = "";

    self.enterPlayStage = function (playerName) {
        // save the name of the player somewhere
        console.log('asd');
        $location.path('/search')
    };
};

var app = angular.module('mainApp');

app.controller('mainController',['mainService', '$location', gameFunction]);

})();