routing.$inject = ['$urlRouterProvider', '$stateProvider', '$locationProvider'];
export function routing($urlRouterProvider, $stateProvider, $locationProvider) {
    $locationProvider.html5Mode(true);

    $urlRouterProvider.otherwise('/');

    $stateProvider
        .state('home', {
            url: '/',
            controller: 'HomeController as homeCtrl',
        })
        .state('home.init', {
//            url: 'аоеуитнеуоеутноеутнщ',
            templateUrl: 'views/home.tpl.html'
//            template: 'уаоеуоеауоеу'
        });
}

