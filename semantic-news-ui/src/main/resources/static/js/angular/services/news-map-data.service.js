class NewsMapDataService {

    constructor($http) {
        this.$http = $http;
    }

    getWordCloud(from, category, hidden, relativePopularity) {
        return this.$http.get('rest/semnews/word-cloud', {
                        params: {
                            from: from,
                            category: category,
                            hidden: hidden,
                            relPop: relativePopularity
                        }
                    });
    }

    getWorldHeatMap(from) {
        return this.$http.get('rest/semnews/world-heat-map', {
                        params: {
                            from: from
                        }
                   });
    }

    getNewsEntityDetails(from, entityUri, category) {
        return this.$http.get('rest/semnews/news-details', {
                        params: {
                            from: from,
                            uri: entityUri,
                            category: category
                        }
                    });
    }
}

NewsMapDataService.$inject = ['$http'];
export default NewsMapDataService;