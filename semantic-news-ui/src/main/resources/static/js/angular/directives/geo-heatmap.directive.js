class GeoHeatMapDirective {

    constructor($rootScope) {
        this.restrict = 'AE';
        this.template = '<div class="geo-heatmap-holder"></div>';
        this.scope = {
            geoData: '=',
            geoCountry: '='
        }

        this.$rootScope = $rootScope;
    }

    link(scope, element, attrs) {
        let $rootScope = this.$rootScope;

        let format = (d) => {
            return Math.floor(d);
        };

        let map = d3.geomap.choropleth()
            .geofile('lib/d3-geomap/countries.json')
            .colors(colorbrewer.YlGnBu[9])
            .column('frequency')
            .format(format)
            .legend(true)
            .unitId('country_code')
            .onClick(onClick);

        function onClick(d) {
            $rootScope.$broadcast('countryClicked', d);
        }

        scope.$watch('geoData', () => {
            if (scope.geoData) {
                d3.select('.geo-heatmap-holder')
                    .datum(d3.csv.parse(scope.geoData))
                    .call(map.draw, map);
            }
        });
    }
}

GeoHeatMapDirective.$inject = ['$rootScope'];
export default GeoHeatMapDirective;