import 'angular-material/angular-material.css!';
import 'styles/splash.css!';
import 'styles/app.css!';

import angular from 'angular';
import 'angular-ui-router';

import 'angular-animate';
import 'angular-ui-bootstrap';

import 'angular-material';
import 'angular-aria';
import 'angular-messages';

import 'jquery';
import 'lodash';

import { routing } from 'app/app.config';
import 'app/utils/register';
import services from 'app/services/services.module';
import controllers from 'app/controllers/controllers.module';

export default angular
    .module('semNewsApp', [
        'ui.bootstrap',
        'ui.router',
        'ngAnimate',
        'ngAria',
        'ngMaterial',
        services.name,
        controllers.name
    ])
    .config(routing);