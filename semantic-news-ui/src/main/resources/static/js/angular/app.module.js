import angular from 'webjars/angularjs/1.5.2/angular.min'
import ui-bootstrap from 'webjars/angular-ui-bootstrap/1.5.2/ui-bootstrap-tpls.min'
import ui-router from 'webjars/angular-ui-router/0.2.18/angular-ui-router.min'

import 'webjars/angularjs/1.5.2/angular-animate.min'
import 'webjars/angularjs/1.5.2/angular-cookies.min'
import 'webjars/angular-toastr/1.5.0/angular-toastr.min'
import 'webjars/jquery/2.1.4/jquery.min'
import 'webjars/bootstrap/4.0.0-alpha.2/js/bootstrap.min'

angular
    .module('semanticNewsMapApp', [
        ui-bootstrap,
        ui-router
    ]);

