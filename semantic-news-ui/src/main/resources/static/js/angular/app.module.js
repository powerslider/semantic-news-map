import 'angular-material/angular-material.css!';
import 'angular-material-icons/angular-material-icons.css!';
//import 'angular-resizable/src/angular-resizable.css!';
import 'bootstrap/css/bootstrap.css!';
import 'font-awesome/css/font-awesome.css!';

import 'lib/d3-geomap/d3.geomap.css!';

import 'styles/splash.css!';
import 'styles/dynamic-tabs.css!';
import 'styles/app.css!';

import angular from 'angular';
import 'angular-ui-router';

import 'angular-material-icons';
import 'angular-local-storage';
//import 'angular-resizable';

import 'angular-ui-bootstrap';

import 'angular-material';
import 'angular-aria';
import 'angular-messages';

//import 'lib/angular-pageslide-directive';

import 'jquery';
import 'lodash';

import { routing } from 'app/app.config';
import services from 'app/services/services.module';
import controllers from 'app/controllers/controllers.module';
import directives from 'app/directives/directives.module';

export default angular
    .module('semNewsApp', [
        'ui.bootstrap',
        'ui.router',
        'ngAria',
        'ngMaterial',
        'ngMdIcons',
//        'angularResizable',
//        'pageslide-directive',
        'LocalStorageModule',
        services.name,
        controllers.name,
        directives.name
    ])
    .config(routing);
