'use strict';

angular.module('testApp')
    .controller('DDocumentController', function ($scope, $state, $modal, DDocument) {
      
        $scope.dDocuments = [];
        $scope.loadAll = function() {
            DDocument.query(function(result) {
               $scope.dDocuments = result;
            });
        };
        $scope.loadAll();


        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.dDocument = {
                name: null,
                directories: null,
                id: null
            };
        };
    });
