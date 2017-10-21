"use strict";

(function(){

    var initializationServiceFunction = function($http) {

        this.submitCard = function (card) {
            var request = {
                method: 'post',
                url: 'card/playCard',
                data: card
            };

            return $http(request).then({
                function(result){
                    return result.data;
                }
            });
        };

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
        }
    };

    var app = angular.module('mainApp');

    app.service('mainService',['$http', initializationServiceFunction]);

})();