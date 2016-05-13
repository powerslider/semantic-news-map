function getDatePart(date) {
    return date.toISOString().substring(0, 10);
}

class NewsMapDataService {

    constructor($http) {
        this.$http = $http;
    }

    getWordCloud(searchParams) {
        return this.$http.get('rest/semnews/word-cloud', {
                        params: {
                            from: getDatePart(searchParams.from),
                            category: searchParams.category,
                            hidden: searchParams.hidden,
                            relPop: searchParams.relativePopularity
                        }
                    });
    }

    getWorldHeatMap(from) {
        return this.$http.get('rest/semnews/world-heat-map', {
                        params: {
                            from: getDatePart(from)
                        }
                   });
    }

    getNewsEntityDetails(searchParams) {
        return this.$http.get('rest/semnews/news-details', {
                        params: {
                            from: getDatePart(searchParams.from),
                            uri: searchParams.entityUri,
                            category: searchParams.category
                        }
                    });
    }
}

NewsMapDataService.$inject = ['$http'];
export default NewsMapDataService;