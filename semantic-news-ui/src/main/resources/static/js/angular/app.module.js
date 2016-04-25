import 'angular-material/angular-material.css!';
import 'bootstrap/css/bootstrap.css!';
import 'font-awesome/css/font-awesome.css!';
import 'styles/splash.css!';
import 'styles/app.css!';

import angular from 'angular';
import uiRouter from 'angular-ui-router';

import 'lib/angular-material-icons.min';

import 'angular-animate';
import 'angular-ui-bootstrap';

import 'angular-material';
import 'angular-aria';
import 'angular-messages';

import 'jquery';
import 'lodash';

import { routing } from 'app/app.config';
import services from 'app/services/services.module';
import controllers from 'app/controllers/controllers.module';

export default angular
    .module('semNewsApp', [
        'ui.bootstrap',
        'ui.router',
        'ngAnimate',
        'ngAria',
        'ngMaterial',
        'ngMdIcons',
        services.name,
        controllers.name
    ])
    .config(routing);
