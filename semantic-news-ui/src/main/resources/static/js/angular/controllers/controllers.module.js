import { register } from 'app/utils/register';

import services from 'app/services/services.module';

import SplashController from 'app/controllers/splash.controller';
import NewsMapController from 'app/controllers/news-map.controller';


let controllers = angular
    .module('semNewsApp.controllers', [services.name]);
export default controllers;

register(controllers.name)
    .controller('NewsMapController', NewsMapController)
    .controller('SplashController', SplashController);