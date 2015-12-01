'use strict';

angular.module('testApp')
	.controller('DDocumentDeleteController', function($scope, $modalInstance, entity, DDocument) {

        $scope.dDocument = entity;
        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            DDocument.delete({id: id},
                function () {
                    $modalInstance.close(true);
                });
        };

    });