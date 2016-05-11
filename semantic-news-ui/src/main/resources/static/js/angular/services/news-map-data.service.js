class NewsMapDataService {

    constructor($http) {
        this.$http = $http;
    }

    getWordCloud(searchParams) {
        return this.$http.get('rest/semnews/word-cloud', {
                        params: {
                            from: searchParams.from,
                            category: searchParams.category,
                            hidden: searchParams.hidden,
                            relPop: searchParams.relativePopularity
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

    getNewsEntityDetails(searchParams) {
        return this.$http.get('rest/semnews/news-details', {
                        params: {
                            from: searchParams.from,
                            uri: searchParams.entityUri,
                            category: searchParams.category
                        }
                    });
    }
}

NewsMapDataService.$inject = ['$http'];
export default NewsMapDataService;