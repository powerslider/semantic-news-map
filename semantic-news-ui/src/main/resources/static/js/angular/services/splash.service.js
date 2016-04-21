/**
 * Provides access to the JSON endpoint which contains the data about the items.
 */
class SplashService {

    constructor($uibModal, $rootScope) {
        this.$uibModal = $uibModal;
        this.$rootScope = $rootScope;
    }

    open(attrs, opts) {
        var scope = $rootScope.$new();
        angular.extend(scope, attrs);
        opts = angular.extend(opts || {}, {
            backdrop: false,
            scope: scope,
            templateUrl: 'views/splash-content.tpl.html',
            windowTemplateUrl: 'views/splash.tpl.html'
        });
        return $uibModal.open(opts);
    }
}

SplashService.$inject = ['$uibModal', '$rootScope'];
export default SplashService;