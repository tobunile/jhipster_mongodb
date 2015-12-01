'use strict';

angular.module('testApp').controller('DirectoryDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Directory',
        function($scope, $stateParams, $modalInstance, entity, Directory) {

        $scope.directory = entity;
        $scope.load = function(id) {
            Directory.get({id : id}, function(result) {
                $scope.directory = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('testApp:directoryUpdate', result);
            $modalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.directory.id != null) {
                Directory.update($scope.directory, onSaveSuccess, onSaveError);
            } else {
                Directory.save($scope.directory, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
