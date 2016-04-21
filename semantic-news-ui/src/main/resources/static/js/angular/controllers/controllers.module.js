import services from 'app/services/services.module';
import AppController from 'app/controllers/app.controller';


export default angular
    .module('semNewsApp.controllers', [services.name])
    .controller('AppController', AppController);