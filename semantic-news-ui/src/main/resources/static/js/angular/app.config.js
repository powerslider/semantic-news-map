routing.$inject = ['$urlRouterProvider', '$stateProvider', '$locationProvider', 'localStorageServiceProvider'];
export function routing($urlRouterProvider, $stateProvider, $locationProvider, localStorageServiceProvider) {
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
        .state('home.data', {
            controller: 'NewsMapController as nmCtrl',
            templateUrl: 'views/news-map.tpl.html'
        });

    localStorageServiceProvider
       .setStorageType('localStorage')
       .setNotify(true, true);
}

