/**
 * The one and only controller used in this app.
 */
class NewsDetailsController {

    constructor($scope, $location, NewsMapDataService) {
        this.entityUri = $location.search().uri;

        let yasrNonRel = YASR(document.getElementById("yasr_results_non_rel"));
        let yasrRel = YASR(document.getElementById("yasr_results_rel"));

        let searchParams = {
            entityUri: this.entityUri,
            category: $location.search().category,
            from: $location.search().category.from
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

NewsDetailsController.$inject = ['$scope', '$location'];
export default NewsDetailsController;
