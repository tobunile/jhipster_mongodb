'use strict';

angular.module('testApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('directory', {
                parent: 'entity',
                url: '/directorys',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Directorys'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/directory/directorys.html',
                        controller: 'DirectoryController'
                    }
                },
                resolve: {
                }
            })
            .state('directory.detail', {
                parent: 'entity',
                url: '/directory/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Directory'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/directory/directory-detail.html',
                        controller: 'DirectoryDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'Directory', function($stateParams, Directory) {
                        return Directory.get({id : $stateParams.id});
                    }]
                }
            })
            .state('directory.new', {
                parent: 'directory',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/directory/directory-dialog.html',
                        controller: 'DirectoryDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    name: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('directory', null, { reload: true });
                    }, function() {
                        $state.go('directory');
                    })
                }]
            })
            .state('directory.edit', {
                parent: 'directory',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/directory/directory-dialog.html',
                        controller: 'DirectoryDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Directory', function(Directory) {
                                return Directory.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('directory', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('directory.delete', {
                parent: 'directory',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/directory/directory-delete-dialog.html',
                        controller: 'DirectoryDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Directory', function(Directory) {
                                return Directory.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('directory', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
