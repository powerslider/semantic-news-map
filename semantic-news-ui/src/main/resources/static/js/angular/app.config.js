routing.$inject = ['$urlRouterProvider', '$stateProvider', '$locationProvider'];
export function routing($urlRouterProvider, $stateProvider, $locationProvider) {
    $locationProvider.html5Mode(true);

    $urlRouterProvider.otherwise('/');

    $stateProvider
        .state('home', {
            url: '/',
            templateUrl: 'views/app.tpl.html',
            controller: 'AppController'
        });
}

