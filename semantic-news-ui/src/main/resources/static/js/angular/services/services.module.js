import { register } from 'app/utils/register';

import SplashService from 'app/services/splash.service';
import NewsMapDataService from 'app/services/news-map-data.service';
import MdAutocompleteService from 'app/services/md-autocomplete.service';
import MdToastService from 'app/services/md-toast.service';


let services = angular
    .module('semNewsApp.services', []);
export default services;

register(services.name)
    .service('SplashService', SplashService)
    .service('NewsMapDataService', NewsMapDataService)
    .service('MdAutocompleteService', MdAutocompleteService)
    .service('MdToastService', MdToastService);
