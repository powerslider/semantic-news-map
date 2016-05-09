class WordCloudDirective {

    constructor($window, $timeout) {
        this.restrict = 'AE';
        this.template = '<div class="word-cloud-holder" style="text-align:center;"></div>';
        this.$window = $window;
        this.$timeout = $timeout;
    }

    link(scope, element, attrs) {
        this.$timeout(() => {
            let tags = [{"text": "study", "size": 40}, {"text": "motion", "size": 15}, {
                "text": "forces",
                "size": 10
            }, {"text": "electricity", "size": 15}, {"text": "movement", "size": 10}, {
                "text": "relation",
                "size": 5
            }, {"text": "things", "size": 10}, {"text": "force", "size": 5}, {
                "text": "ad",
                "size": 5
            }, {"text": "energy", "size": 85}, {"text": "living", "size": 5}, {
                "text": "nonliving",
                "size": 5
            }, {"text": "laws", "size": 15}, {"text": "speed", "size": 45}, {
                "text": "velocity",
                "size": 30
            }, {"text": "define", "size": 5}, {"text": "constraints", "size": 5}, {
                "text": "universe",
                "size": 10
            }, {"text": "physics", "size": 120}, {"text": "describing", "size": 5}, {
                "text": "matter",
                "size": 90
            }, {"text": "physics-the", "size": 5}, {"text": "world", "size": 10}, {
                "text": "works",
                "size": 10
            }, {"text": "science", "size": 70}, {"text": "interactions", "size": 30}, {
                "text": "studies",
                "size": 5
            }, {"text": "properties", "size": 45}, {"text": "nature", "size": 40}, {
                "text": "branch",
                "size": 30
            }, {"text": "concerned", "size": 25}, {"text": "source", "size": 40}, {
                "text": "google",
                "size": 10
            }, {"text": "defintions", "size": 5}, {"text": "two", "size": 15}, {
                "text": "grouped",
                "size": 15
            }, {"text": "traditional", "size": 15}, {"text": "fields", "size": 15}, {
                "text": "acoustics",
                "size": 15
            }, {"text": "optics", "size": 15}, {"text": "mechanics", "size": 20}, {
                "text": "thermodynamics",
                "size": 15
            }, {"text": "electromagnetism", "size": 15}, {"text": "modern", "size": 15}, {
                "text": "extensions",
                "size": 15
            }, {"text": "thefreedictionary", "size": 15}, {"text": "interaction", "size": 15}, {
                "text": "org",
                "size": 25
            }, {"text": "answers", "size": 5}, {"text": "natural", "size": 15}, {
                "text": "objects",
                "size": 5
            }, {"text": "treats", "size": 10}, {"text": "acting", "size": 5}, {
                "text": "department",
                "size": 5
            }, {"text": "gravitation", "size": 5}, {"text": "heat", "size": 10}, {
                "text": "light",
                "size": 10
            }, {"text": "magnetism", "size": 10}, {"text": "modify", "size": 5}, {
                "text": "general",
                "size": 10
            }, {"text": "bodies", "size": 5}, {"text": "philosophy", "size": 5}, {
                "text": "brainyquote",
                "size": 5
            }, {"text": "words", "size": 5}, {"text": "ph", "size": 5}, {"text": "html", "size": 5}, {
                "text": "lrl",
                "size": 5
            }, {"text": "zgzmeylfwuy", "size": 5}, {"text": "subject", "size": 5}, {
                "text": "distinguished",
                "size": 5
            }, {"text": "chemistry", "size": 5}, {"text": "biology", "size": 5}, {
                "text": "includes",
                "size": 5
            }, {"text": "radiation", "size": 5}, {"text": "sound", "size": 5}, {
                "text": "structure",
                "size": 5
            }, {"text": "atoms", "size": 5}, {"text": "including", "size": 10}, {
                "text": "atomic",
                "size": 10
            }, {"text": "nuclear", "size": 10}, {"text": "cryogenics", "size": 10}, {
                "text": "solid-state",
                "size": 10
            }, {"text": "particle", "size": 10}, {"text": "plasma", "size": 10}, {
                "text": "deals",
                "size": 5
            }, {"text": "merriam-webster", "size": 5}, {"text": "dictionary", "size": 10}, {
                "text": "analysis",
                "size": 5
            }, {"text": "conducted", "size": 5}, {"text": "order", "size": 5}, {
                "text": "understand",
                "size": 5
            }, {"text": "behaves", "size": 5}, {"text": "en", "size": 5}, {
                "text": "wikipedia",
                "size": 5
            }, {"text": "wiki", "size": 5}, {"text": "physics-", "size": 5}, {
                "text": "physical",
                "size": 5
            }, {"text": "behaviour", "size": 5}, {"text": "collinsdictionary", "size": 5}, {
                "text": "english",
                "size": 5
            }, {"text": "time", "size": 35}, {"text": "distance", "size": 35}, {
                "text": "wheels",
                "size": 5
            }, {"text": "revelations", "size": 5}, {"text": "minute", "size": 5}, {
                "text": "acceleration",
                "size": 20
            }, {"text": "torque", "size": 5}, {"text": "wheel", "size": 5}, {
                "text": "rotations",
                "size": 5
            }, {"text": "resistance", "size": 5}, {"text": "momentum", "size": 5}, {
                "text": "measure",
                "size": 10
            }, {"text": "direction", "size": 10}, {"text": "car", "size": 5}, {
                "text": "add",
                "size": 5
            }, {"text": "traveled", "size": 5}, {"text": "weight", "size": 5}, {
                "text": "electrical",
                "size": 5
            }, {"text": "power", "size": 5}];

            let width = Math.floor(this.$window.innerWidth * 0.95),
                height = Math.floor(this.$window.innerHeight) - 80;

            let svg = d3.select(".word-cloud-holder")
                .insert("svg:svg", "h2")
                .attr("viewBox", "0 0 " + width / 2 + " " + height / 2)
                .attr("preserveAspectRatio", "xMidYMid meet");

            let color = d3.scale.category20();

            let layout = d3.layout.cloud()
                .size([width / 2, height / 2])
                .words(tags)
                .rotate(0)
                .font("Impact")
                .fontSize((d) => {
                    return d.size;
                })
                .on("end", draw);

            let vis = svg.append("g")
                .attr("transform", "translate(" + [width >> 1, height >> 1] + ")");

            update();

            this.$window.onresize = (event) => {
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

                text.enter().append("text")
                    .attr("text-anchor", "middle")
                    .attr("transform", (d) => {
                        return "translate(" + [getTranslatedX(d.x), getTranslatedY(d.y)] + ")rotate(" + d.rotate + ")";
                    })
                    .style("font-size", (d) => {
                        return d.size + "px";
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
                layout.font('impact').spiral('rectangular');
                let fontSize = d3.scale['sqrt']().range([10, 100]);
                if (tags.length) {
                    fontSize.domain([+tags[tags.length - 1].value || 1, +tags[0].value]);
                }
                layout.stop().words(tags).start();
            }
        }, 50);
    }
}

WordCloudDirective.$inject = ['$window', '$timeout'];
export default WordCloudDirective;