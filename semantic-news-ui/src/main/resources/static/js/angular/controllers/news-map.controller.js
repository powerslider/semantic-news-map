/**
 * The one and only controller used in this app.
 */
class NewsMapController {

    constructor($scope, $timeout, $mdBottomSheet, $mdDialog, NewsMapDataService) {
        this.$scope = $scope;
        this.$timeout = $timeout;
        this.$mdBottomSheet = $mdBottomSheet;
        this.$mdDialog = $mdDialog;
        this.NewsMapDataService = NewsMapDataService;

        this.wholeResponse = this.NewsMapDataService.getWholeResponse();

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
                let wholeResponse = {};

                let popularEntitiesSearchParams = {
                    from: this.currentDate,
                    type: "popular",
                    category: this.category,
                    relPop: this.relativePopularity
                };

                NewsMapDataService.getWordCloud(popularEntitiesSearchParams)
                    .success((response) => {
                        wholeResponse.popularEntities = response;
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
                        wholeResponse.hiddenEntities = response;
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
                        wholeResponse.popularAndHiddenEntities = response;
                    })
                    .error(() => {

                    });

                NewsMapDataService.getWorldHeatMap(this.currentDate)
                    .success((response) => {
                        wholeResponse.geoHeatMapData = response;
                    })
                    .error(() => {

                    });

                NewsMapDataService.setWholeResponse(wholeResponse);
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

NewsMapController.$inject = ['$scope', '$timeout', '$mdBottomSheet', '$mdDialog', 'NewsMapDataService'];
export default NewsMapController;
