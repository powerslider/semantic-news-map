routing.$inject = ['$urlRouterProvider', '$stateProvider', '$locationProvider'];
export function routing($urlRouterProvider, $stateProvider, $locationProvider) {
    $locationProvider.html5Mode(true);

    $urlRouterProvider.otherwise('/');

    $stateProvider
        .state('home', {
            url: '/',
            controller: 'HomeController as homeCtrl',
            template: '<div ui-view></div>'
        })
        .state('home.init', {
            templateUrl: 'views/home.tpl.html'
        });
}

