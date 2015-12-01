'use strict';

angular.module('testApp').controller('DDocumentDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'DDocument',
        function($scope, $stateParams, $modalInstance, entity, DDocument) {

        $scope.dDocument = entity;
        $scope.load = function(id) {
            DDocument.get({id : id}, function(result) {
                $scope.dDocument = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('testApp:dDocumentUpdate', result);
            $modalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.dDocument.id != null) {
                DDocument.update($scope.dDocument, onSaveSuccess, onSaveError);
            } else {
                DDocument.save($scope.dDocument, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
