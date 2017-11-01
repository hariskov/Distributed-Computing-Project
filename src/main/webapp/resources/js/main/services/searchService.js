"use strict";

(function(){

    var initializationServiceFunction = function($http) {


        this.discover = function(){
            var request = {
                method : 'get',
                url:'echo/discovery'
            };

            return $http(request).then({
                function(result){
                    return result.data;
                }
            });
        };

        this.startVote = function(leaderSelect){
            var request = {
                method : 'post',
                url:'voting/startVote',
                data:leaderSelect
            };

            return $http(request).then({
                function(result){
                    return result.data;
                }
            });
        };

        this.exit = function(){
            var request = {
                method : 'put',
                url:'echo/exit'
            };

            return $http(request).then({
                function(result){
                    return result.data;
                }
            });
        };

        this.enter = function(){
            var request = {
                method : 'put',
                url:'echo/enter'
            };

            return $http(request).then({
                function(result){
                    return result.data;
                }
            });
        };
    };

    var app = angular.module('mainApp');

    app.service('searchService',['$http', initializationServiceFunction]);

})();