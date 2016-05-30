routing.$inject = ['$stateProvider', '$urlRouterProvider', '$locationProvider', 'localStorageServiceProvider'];
export function routing($stateProvider, $urlRouterProvider, $locationProvider, localStorageServiceProvider) {
    $stateProvider
        .state('home', {
            url: '/',
            controller: 'SplashController as splashCtrl',
            template: '<div ui-view></div>'
        })
        .state('news-map', {
            url: '/news-map',
            controller: 'NewsMapController as mapCtrl',
            templateUrl: 'views/news-map.tpl.html',
            reloadOnSearch: true
        })
        .state('news-details', {
            url: '/news-details?uri&from&category',
            controller: 'NewsDetailsController as detailsCtrl',
            templateUrl: 'views/news-details.tpl.html'
        });

    $locationProvider.html5Mode(true);
    $urlRouterProvider.otherwise('/');

    localStorageServiceProvider
       .setStorageType('localStorage')
       .setNotify(true, true);
}

