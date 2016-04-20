import mainModule from 'app/app.module';

angular.element(document).ready(() => {
    angular.bootstrap(document, [mainModule.name], {
        strictDi: true
    });
});