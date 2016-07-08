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
                this.yasrNonRelDataExists = this.isResponseEmpty(this.yasrNonRelData);
                this.yasrRelDataExists = this.isResponseEmpty(this.yasrRelData);
            })
            .error(() => {

            });

    }

    redirectNewsToNOW() {
        $('table tr td:nth-child(4) a.uri').each(function () {
            let href = $(this).attr('href');
            $(this).attr('href', "http://now.ontotext.com/#document?uri=" + encodeURIComponent(href));
        });
    }

    isResponseEmpty(response) {
        console.log(response);
        return response ? $.isEmptyObject(response.results.bindings[0]) : true;
    }
}

NewsDetailsController.$inject = ['$scope', '$state', 'NewsMapDataService'];
export default NewsDetailsController;
