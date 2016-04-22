import services from 'app/services/services.module';
import HomeController from 'app/controllers/home.controller';


export default angular
    .module('semNewsApp.controllers', [services.name])
    .controller('HomeController', HomeController);