import controllers from 'app/controllers/controllers.module';
import WordCloudDirective from 'app/directives/word-cloud.directive';
import GeoHeatMapDirective from 'app/directives/geo-heatmap.directive';
import { register } from 'app/utils/register';


let directives = angular
    .module('semNewsApp.directives', [controllers.name]);
export default directives;

register(directives.name)
    .directive('wordCloud', WordCloudDirective)
    .directive('geoHeatMap', GeoHeatMapDirective);
