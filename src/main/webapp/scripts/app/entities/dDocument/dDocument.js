'use strict';

angular.module('testApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('dDocument', {
                parent: 'entity',
                url: '/dDocuments',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'DDocuments'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/dDocument/dDocuments.html',
                        controller: 'DDocumentController'
                    }
                },
                resolve: {
                }
            })
            .state('dDocument.detail', {
                parent: 'entity',
                url: '/dDocument/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'DDocument'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/dDocument/dDocument-detail.html',
                        controller: 'DDocumentDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'DDocument', function($stateParams, DDocument) {
                        return DDocument.get({id : $stateParams.id});
                    }]
                }
            })
            .state('dDocument.new', {
                parent: 'dDocument',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/dDocument/dDocument-dialog.html',
                        controller: 'DDocumentDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    name: null,
                                    directories: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('dDocument', null, { reload: true });
                    }, function() {
                        $state.go('dDocument');
                    })
                }]
            })
            .state('dDocument.edit', {
                parent: 'dDocument',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/dDocument/dDocument-dialog.html',
                        controller: 'DDocumentDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['DDocument', function(DDocument) {
                                return DDocument.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('dDocument', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('dDocument.delete', {
                parent: 'dDocument',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/dDocument/dDocument-delete-dialog.html',
                        controller: 'DDocumentDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['DDocument', function(DDocument) {
                                return DDocument.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('dDocument', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
