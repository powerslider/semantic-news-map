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
        .state('news-map', {
            url: '/news-map',
            controller: 'NewsMapController as mapCtrl',
            templateUrl: 'views/news-map.tpl.html'
        })
        .state('news-details', {
            url: '/news-details?uri&from&category',
            controller: 'NewsDetailsController as detailsCtrl',
            templateUrl: 'views/news-details.tpl.html'
        });

    localStorageServiceProvider
       .setStorageType('localStorage')
       .setNotify(true, true);
}

