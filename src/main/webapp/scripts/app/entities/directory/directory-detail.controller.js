'use strict';

angular.module('testApp')
    .controller('DirectoryDetailController', function ($scope, $rootScope, $stateParams, entity, Directory) {
        $scope.directory = entity;
        $scope.load = function (id) {
            Directory.get({id: id}, function(result) {
                $scope.directory = result;
            });
        };
        var unsubscribe = $rootScope.$on('testApp:directoryUpdate', function(event, result) {
            $scope.directory = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
