function getDatePart(date) {
    if (date) {
        return date.substring(0, 10);
    }
}

class NewsMapDataService {

    constructor($http) {
        this.$http = $http;
    }

    getWordCloud(searchParams) {
        return this.$http.get('rest/semnews/word-cloud', {
            params: {
                type: searchParams.type,
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

    checkNewsEntityDetails(searchParams) {
        return this.$http.head('rest/semnews/news-details', {
            params: {
                from: getDatePart(searchParams.from),
                uri: searchParams.entityUri,
                category: searchParams.category
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

    getNewsMentioningCountry(searchParams) {
        return this.$http.get('rest/semnews/news-mentioning-country', {
            params: {
                from: getDatePart(searchParams.from),
                countryCode: searchParams.countryCode
            }
        })
    }
}

NewsMapDataService.$inject = ['$http'];
export default NewsMapDataService;