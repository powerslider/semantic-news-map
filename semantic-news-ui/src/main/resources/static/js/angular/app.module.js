import 'angular-material/angular-material.css!';
import 'angular-material-icons/angular-material-icons.css!';
import 'angular-resizable/src/angular-resizable.css!';
import 'bootstrap/css/bootstrap.css!';
import 'font-awesome/css/font-awesome.css!';
import 'styles/splash.css!';
import 'styles/dynamic-tabs.css!';
import 'styles/app.css!';

import angular from 'angular';
import uiRouter from 'angular-ui-router';

import 'angular-material-icons';
import 'angular-resizable';

//import 'angular-animate';
import 'angular-ui-bootstrap';

import 'angular-material';
import 'angular-aria';
import 'angular-messages';

import 'lib/angular-pageslide-directive';

import 'jquery';
import 'lodash';

import { routing } from 'app/app.config';
import services from 'app/services/services.module';
import controllers from 'app/controllers/controllers.module';

export default angular
    .module('semNewsApp', [
        'ui.bootstrap',
        'ui.router',
        //'ngAnimate',
        'ngAria',
        'ngMaterial',
        'ngMdIcons',
        'angularResizable',
        'pageslide-directive',
        services.name,
        controllers.name
    ])
    .config(routing);
