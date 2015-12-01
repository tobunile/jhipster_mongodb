'use strict';

angular.module('testApp')
    .controller('DirectoryController', function ($scope, $state, $modal, Directory) {
      
        $scope.directorys = [];
        $scope.loadAll = function() {
            Directory.query(function(result) {
               $scope.directorys = result;
            });
        };
        $scope.loadAll();


        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.directory = {
                name: null,
                id: null
            };
        };
    });
