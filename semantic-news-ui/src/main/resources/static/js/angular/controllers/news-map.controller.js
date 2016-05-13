/**
 * The one and only controller used in this app.
 */
class NewsMapController {

    constructor($scope, $timeout, $mdBottomSheet, $mdDialog) {
        this.$scope = $scope;
        this.$timeout = $timeout;
        this.$mdBottomSheet = $mdBottomSheet;
        this.$mdDialog = $mdDialog;

        this.diagramOptions = {
            isOpen: false,
            count: 0
        };

        this.selectedTabIndex = 0;
//        this.showSearchCriteriaBottonSheet();
    }

    showSearchCriteriaDialog($event) {
        this.$mdDialog.show({
            templateUrl: 'views/search-criteria-dialog.tpl.html',
            controller: ['$mdDialog', 'MdAutocompleteService', 'NewsMapDataService', NewsMapSearchController],
            controllerAs: "searchCtrl",
            bindToController: true,
            clickOutsideToClose: true,
            targetEvent: $event
        });

        function NewsMapSearchController($mdDialog, MdAutocompleteService, NewsMapDataService) {
            this.currentDate = new Date();
            this.categories = MdAutocompleteService
                                    .loadAllItems("All, Science And Technology, Lifestyle, Business, Sports, International");
            this.querySearch = MdAutocompleteService.querySearch;
            this.cancel = ($event) => $mdDialog.cancel();
            this.finish = ($event) => $mdDialog.hide();
            this.loadNewsSearchResultData = loadNewsSearchResultData;

            function loadNewsSearchResultData() {
                let popularEntitiesSearchParams = {
                    from: this.currentDate,
                    type: "popular",
                    category: this.category,
                    relPop: this.relativePopularity
                };

                NewsMapDataService.getWordCloud(popularEntitiesSearchParams)
                    .success((response) => {
                        this.wordCloudDataPopular = response;
                    })
                    .error(() => {

                    });

                let hiddenEntitiesSearchParams = {
                    from: this.currentDate,
                    type: "hidden",
                    category: this.category,
                    relPop: this.relativePopularity
                };

                NewsMapDataService.getWordCloud(hiddenEntitiesSearchParams)
                    .success((response) => {
                        this.wordCloudDataHidden = response;
                    })
                    .error(() => {

                    });

                let popularAndHiddenEntitiesSearchParams = {
                    from: this.currentDate,
                    category: this.category,
                    relPop: this.relativePopularity
                };

                NewsMapDataService.getWordCloud(popularAndHiddenEntitiesSearchParams)
                    .success((response) => {
                        this.wordCloudDataPopularAndHidden = response;
                    })
                    .error(() => {

                    });

                NewsMapDataService.getWorldHeatMap(this.currentDate)
                    .success((response) => {
                        this.geoHeatMapData = response;
                    })
                    .error(() => {

                    });
            }
        }
    }

//    showSearchCriteriaBottonSheet($event) {
//        this.$mdBottomSheet.show({
//            templateUrl: 'views/bottom-sheet-search-criteria.tpl.html',
//            controller: [ '$mdBottomSheet', NewsMapSearchController],
//            controllerAs: "searchCtrl",
//            bindToController : true,
//            targetEvent: $event
//        });
//
//        function NewsMapSearchController($mdBottomSheet) {
//            this.categories = ["All", "Science And Technology", "Lifestyle", "Business", "Sports", "International"];
//            this.currentDate = new Date();
//        }
//    }


}

NewsMapController.$inject = ['$scope', '$timeout', '$mdBottomSheet', '$mdDialog'];
export default NewsMapController;
