routing.$inject = ['$urlRouterProvider', '$stateProvider', '$locationProvider'];
export function routing($urlRouterProvider, $stateProvider, $locationProvider) {
    $locationProvider.html5Mode(true);

    $urlRouterProvider.otherwise('/');

    $stateProvider
        .state('home', {
            url: '/',
            controller: 'SplashController as splashCtrl',
            template: '<div ui-view></div>'
        })
        .state('home.init', {
            controller: 'NewsMapController as nmCtrl',
            templateUrl: 'views/news-map.tpl.html'
        })
        .state('tab1', {
            url: '/tab1',
            template: '<word-cloud></word-cloud>'
        })
        .state('tab2', {
            url: '/tab2',
            template: '<word-cloud></word-cloud>'
        });
}

