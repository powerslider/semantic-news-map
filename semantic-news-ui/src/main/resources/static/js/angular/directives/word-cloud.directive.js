class WordCloudDirective {

    constructor($window, $timeout, $state) {
        this.restrict = 'AE';
        this.template = '<div class="word-cloud-container"></div>';
        this.scope = {
            wordData: '=',
            wordSearchParams: '='
        };

        this.$window = $window;
        this.$timeout = $timeout;
        this.$state = $state;
    }

    link(scope, element, attrs) {
        let $state = this.$state;

        function drawWordCloud() {
            let width = Math.floor(window.innerWidth * 0.95),
                height = Math.floor(window.innerHeight) - 80;

            let svg = d3.select(".word-cloud-container")
                .insert("svg:svg", "h2")
                .attr("viewBox", "0 0 " + width / 2 + " " + height / 2)
                .attr("preserveAspectRatio", "xMidYMid meet");

            let color = d3.scale.category20();

            let layout = d3.layout.cloud()
                .size([width / 2, height / 2])
                .words(scope.wordData)
                .rotate(0)
                .font("Impact")
                .fontSize((d) => {
                    return d.size;
                })
                .on("end", draw);

            let vis = svg.append("g")
                .attr("transform", "translate(" + [width >> 1, height >> 1] + ")");

            update();

            window.onresize = (event) => {
                update();
            };

            function draw(data, bounds) {
                function getTranslatedX(x) {
                    return x - width / 4;
                }

                function getTranslatedY(y) {
                    return y - height / 4;
                }

                svg
                    .attr("width", width)
                    .attr("height", height);

                let scale = bounds ? Math.min(
                    width / Math.abs(bounds[1].x - width / 2),
                    width / Math.abs(bounds[0].x - width / 2),
                    height / Math.abs(bounds[1].y - height / 2),
                    height / Math.abs(bounds[0].y - height / 2)) / 2 : 1;

                let text = vis.selectAll("text")
                    .data(data, (d) => {
                        return d.text.toLowerCase();
                    });

                text
                    .transition()
                    .duration(1000)
                    .attr("transform", (d) => {
                        return "translate(" + [getTranslatedX(d.x), getTranslatedY(d.y)] + ")rotate(" + d.rotate + ")";
                    })
                    .style("font-size", (d) => {
                        return d.size + "px";
                    });


                text
                    .enter().append("text")
                    .attr("text-anchor", "middle")
                    .attr("transform", (d) => {
                        return "translate(" + [getTranslatedX(d.x), getTranslatedY(d.y)] + ")rotate(" + d.rotate + ")";
                    })
                    .style("font-size", (d) => {
                        return d.size + "px";
                    })
                    .on("click", (d) => {
                        let sp = angular.fromJson(scope.wordSearchParams);
                        let entityUri = encodeURIComponent(d.entityUri);
                        let detailsUrl = $state.href('news-details', {
                            uri: entityUri,
                            from: sp.from,
                            category: sp.category
                        });
                        window.open(detailsUrl, '_blank');
                    })
                    .style("opacity", 1e-6)
                    .transition()
                    .duration(200)
                    .style("opacity", 1)
                    .style("font-family", (d) => {
                        return d.font;
                    })
                    .style("fill", (d) => {
                        return color(d.text.toLowerCase());
                    })
                    .text((d) => {
                        return d.text;
                    });

                vis
                    .transition()
                    .attr("transform", "translate(" + [width >> 1, height >> 1] + ")scale(" + scale + ")");
            }

            function update() {
                if (scope.wordData.length) {
                    var wordData = scope.wordData;
                    layout.font('impact').spiral('rectangular');
                    let fontSize = d3.scale['sqrt']().range([10, 100]);
                    fontSize.domain([+wordData[wordData.length - 1].value || 1, +wordData[0].value]);
                }
                layout.stop().words(wordData).start();
            }
        }

        scope.$watch('wordData', () => {
            if (scope.wordData) {
                this.$timeout(drawWordCloud, 300);
            }
        });
    }
}

WordCloudDirective.$inject = ['$window', '$timeout', '$state'];
export default WordCloudDirective;