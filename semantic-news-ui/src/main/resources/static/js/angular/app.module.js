import angular from 'webjars/angularjs/1.5.2/angular.min';

import uiBootstrap from 'webjars/angular-ui-bootstrap/1.5.2/ui-bootstrap-tpls.min';
import uiRouter from 'webjars/angular-ui-router/0.2.18/angular-ui-router.min';
import angularMaterial from 'webjars/angular-material/0.7.1/angular.material.min';

import { routing } from './app.config';
import { services } from './services/services.module';
import { controllers } from './controllers/controllers.module';

import 'webjars/angularjs/1.5.2/angular-animate.min';
import 'webjars/angularjs/1.5.2/angular-cookies.min';
import 'webjars/angular-toastr/1.5.0/angular-toastr.min';
import 'webjars/jquery/2.1.4/jquery.min';
import 'webjars/bootstrap/4.0.0-alpha.2/js/bootstrap.min';

export default angular.module('semNewsApp', [
        uiBootstrap,
        uiRouter,
        angularMaterial,
        services,
        controllers
    ])
    .config(routing)
    .name;

angular.element(document).ready(() => {
    angular.bootstrap(document, ['semNewsApp']);
});

