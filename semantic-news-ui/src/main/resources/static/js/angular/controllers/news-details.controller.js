class NewsDetailsController {

    constructor($scope, $state, NewsMapDataService) {
        this.entityUri = $state.params.uri;

        YASR.defaults.outputPlugins = ["table", "error", "boolean", "pivot", "gchart"];

        let yasrNonRel = YASR(document.getElementById("yasr_results_non_rel"));
        let yasrRel = YASR(document.getElementById("yasr_results_rel"));

        let searchParams = {
            entityUri: decodeURIComponent(this.entityUri),
            category: $state.params.category,
            from: $state.params.from
        };

        NewsMapDataService.getNewsEntityDetails(searchParams)
            .success((response) => {
                yasrNonRel.setResponse(response.nonRelevant);
                yasrRel.setResponse(response.relevant);
            })
            .error(() => {

            });
    }

}

NewsDetailsController.$inject = ['$scope', '$state', 'NewsMapDataService'];
export default NewsDetailsController;
