"use strict";

(function(){

    var gameFunction = function(searchService,  $http){

        var self = this;

        self.listOfGames = {};

        self.Search = function(){
            searchService.discover().then(
                function(success){
                    console.log(success);

                    self.discoverDone = true;
                },function(fail){
                    console.log(fail);
                }
            )
        };

        self.Create = function(){
            searchService.startVote('LeaderSelect').then(
                function(success){
                    console.log(success);
                },function(fail){
                    console.log(fail);
                }
            );
        };
    };

    var app = angular.module('mainApp');

    app.controller('searchController',['searchService', '$http', gameFunction]);

})();