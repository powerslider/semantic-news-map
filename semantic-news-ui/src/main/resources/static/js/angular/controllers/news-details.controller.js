class NewsDetailsController {

    constructor($scope, $state, NewsMapDataService) {
        this.entityUri = $state.params.uri;

        let searchParams = {
            entityUri: decodeURIComponent(this.entityUri),
            category: $state.params.category,
            from: $state.params.from
        };

        this.selectedTabIndex = 0;
        NewsMapDataService.getNewsEntityDetails(searchParams)
            .success((response) => {
                this.yasrNonRelData = response.nonRelevant;
                this.yasrRelData = response.relevant;
//
//                this.forcedTabIndex = this.selectedTabIndex;
//
//                this.nonRelevantDataExists = this.isResponseEmpty(this.yasrNonRelData.results.bindings);
//                this.relevantDataExists = this.isResponseEmpty(this.yasrRelData.results.bindings);
//
//                if (this.nonRelevantDataExists ^ this.relevantDataExists == 1) {
//                    this.forcedTabIndex = 0;
//                }
            })
            .error(() => {

            });

    }

//    isResponseEmpty(arr) {
//        return arr ? arr.length == 0 || (arr.length == 1 && $.isEmptyObject(arr[0])) : true;
//    }
}

NewsDetailsController.$inject = ['$scope', '$state', 'NewsMapDataService'];
export default NewsDetailsController;
