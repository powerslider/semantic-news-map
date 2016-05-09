class GeoHeatMapDirective {

    constructor() {
        this.restrict = 'AE';
        this.template = '<div class="geo-heatmap-holder"></div>';

    }

    link(scope, element, attrs) {
        let format = function (d) {
            d = d / 1000000;
            return d3.format(',.02f')(d) + 'M';
        };

        let map = d3.geomap.choropleth()
            .geofile('lib/d3-geomap/countries.json')
            .colors(colorbrewer.YlGnBu[9])
            .column('YR2010')
            .format(format)
            .legend(true)
            .unitId('Country Code');

        d3.csv('/data/sp.pop.totl.csv', function (error, data) {
            d3.select('.geo-heatmap-holder')
                .datum(data)
                .call(map.draw, map);
        });
    }

}

GeoHeatMapDirective.$inject = [];
export default GeoHeatMapDirective;