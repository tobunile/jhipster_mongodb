'use strict';

angular.module('testApp')
    .factory('DDocument', function ($resource, DateUtils) {
        return $resource('api/dDocuments/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
