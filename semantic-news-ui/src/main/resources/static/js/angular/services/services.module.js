import { register } from 'app/utils/register';

import SplashService from 'app/services/splash.service';


let services = angular
    .module('semNewsApp.services', []);
export default services;

register(services.name)
    .service('SplashService', SplashService);