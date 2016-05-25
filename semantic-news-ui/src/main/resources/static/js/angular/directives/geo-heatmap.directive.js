class GeoHeatMapDirective {

    constructor() {
        this.restrict = 'AE';
        this.template = '<div class="geo-heatmap-holder"></div>';
        this.scope = {
            geoData: '='
        }
    }

    link(scope, element, attrs) {
        let format = function (d) {
            return d % 1 == 0 ? d : d3.format(',.02f')(d);
        };

        let map = d3.geomap.choropleth()
            .geofile('lib/d3-geomap/countries.json')
            .colors(colorbrewer.YlGnBu[9])
            .column('frequency')
            .format(format)
            .legend(true)
            .unitId('country_code');

        scope.$watch('geoData', () => {
            if (scope.geoData) {
                d3.select('.geo-heatmap-holder')
                    .datum(d3.csv.parse(scope.geoData))
                    .call(map.draw, map);
            }
        });
    }
}

GeoHeatMapDirective.$inject = [];
export default GeoHeatMapDirective;