'use strict';

angular.module('testApp')
    .controller('DDocumentDetailController', function ($scope, $rootScope, $stateParams, entity, DDocument) {
        $scope.dDocument = entity;
        $scope.load = function (id) {
            DDocument.get({id: id}, function(result) {
                $scope.dDocument = result;
            });
        };
        var unsubscribe = $rootScope.$on('testApp:dDocumentUpdate', function(event, result) {
            $scope.dDocument = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
