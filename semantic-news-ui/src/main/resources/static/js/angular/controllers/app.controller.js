/**
 * The one and only controller used in this app.
 */
class AppController {

    /*@ngInject*/
    constructor($scope) {
        this.title = "FUCK YEAHHHH!!!!!!!"
        this.$inject = ['$scope'];
    }
}

register('semNewsApp.controllers')
    .controller('AppController', AppController);
