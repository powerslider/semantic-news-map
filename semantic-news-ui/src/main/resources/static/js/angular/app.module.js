import angular from 'angular';

import 'angular-ui-bootstrap';
import 'angular-ui-router';
import 'angular-material';

import 'jquery';
import 'lodash';

import routing from 'app/app.config';
import 'app/utils/register';
import 'app/services/services.module';
import 'app/controllers/controllers.module';

export default angular
    .module('semNewsApp', [
        'ui.bootstrap',
        'ui.router'
    ])
    .config(routing);