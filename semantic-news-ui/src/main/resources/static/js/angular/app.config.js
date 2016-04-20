routing.$inject = ['$urlRouterProvider', '$stateProvider'];
export default function routing($urlRouterProvider, $stateProvider) {

    $urlRouterProvider.otherwise('/');
    console.log("routing");
    $stateProvider
        .state('home', {
            url: '/',
            templateUrl: 'views/app.tpl.html',
            controller: 'AppController'
        });
}