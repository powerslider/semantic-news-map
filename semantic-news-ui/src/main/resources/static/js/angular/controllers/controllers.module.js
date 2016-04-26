import services from 'app/services/services.module';
import SplashController from 'app/controllers/splash.controller';
import NewsMapController from 'app/controllers/news-map.controller';


export default angular
    .module('semNewsApp.controllers', [services.name])
    .controller('NewsMapController', NewsMapController)
    .controller('SplashController', SplashController);