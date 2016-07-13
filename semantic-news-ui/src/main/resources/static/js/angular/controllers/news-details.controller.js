class NewsDetailsController {

    constructor($scope, $state, NewsMapDataService) {
        this.entityUri = $state.params.uri;

        let searchParams = {
            entityUri: decodeURIComponent(this.entityUri),
            category: $state.params.category,
            from: $state.params.from
        };

        this.selectedTabIndex = 0;
        this.yasrNonRelDataExists = false;
        this.yasrRelDataExists = false;

        NewsMapDataService.getNewsEntityDetails(searchParams)
            .success((response) => {
                this.yasrNonRelData = response.nonRelevant;
                this.yasrRelData = response.relevant;
                this.yasrNonRelDataExists = this.isResponseEmpty(this.yasrNonRelData);
                console.log(this.yasrNonRelDataExists);
                this.yasrRelDataExists = this.isResponseEmpty(this.yasrRelData);
                console.log(this.yasrRelDataExists);
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
        let data = response.results.bindings;
        return response ? $.isEmptyObject(data[data.length - 1]) : true;
    }
}

NewsDetailsController.$inject = ['$scope', '$state', 'NewsMapDataService'];
export default NewsDetailsController;
