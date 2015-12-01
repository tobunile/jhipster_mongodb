'use strict';

angular.module('testApp')
	.controller('DirectoryDeleteController', function($scope, $modalInstance, entity, Directory) {

        $scope.directory = entity;
        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Directory.delete({id: id},
                function () {
                    $modalInstance.close(true);
                });
        };

    });