import services from 'app/services/services.module';
import SplashController from 'app/controllers/splash.controller';
import HomeController from 'app/controllers/home.controller';


export default angular
    .module('semNewsApp.controllers', [services.name])
    .controller('SplashController', SplashController)
    .controller('HomeController', HomeController);