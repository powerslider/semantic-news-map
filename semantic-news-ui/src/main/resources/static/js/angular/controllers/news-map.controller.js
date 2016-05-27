/**
 * The one and only controller used in this app.
 */
class NewsMapController {

    constructor($scope, $timeout, $mdBottomSheet, $mdDialog, NewsMapDataService, localStorageService) {
        this.$scope = $scope;
        this.$timeout = $timeout;
        this.$mdBottomSheet = $mdBottomSheet;
        this.$mdDialog = $mdDialog;
        this.NewsMapDataService = NewsMapDataService;
        this.localStorageService = localStorageService;

        this.selectedTabIndex = 0;

        this.searchParams = this.localStorageService.get("searchParams");
        if (this.searchParams) {
            this.loadNewsSearchResultData(angular.fromJson(this.searchParams));
        }
//        this.showSearchCriteriaBottonSheet();
    }

    showSearchCriteriaDialog($event) {
        this.$mdDialog.show({
            templateUrl: 'views/search-criteria-dialog.tpl.html',
            controller: ['$mdDialog', '$rootScope', 'MdAutocompleteService', 'localStorageService', '$state', NewsMapSearchController],
            controllerAs: "searchCtrl",
            bindToController: true,
            clickOutsideToClose: true,
            targetEvent: $event
        });

        function NewsMapSearchController($mdDialog, $rootScope,  MdAutocompleteService, localStorageService, $state) {
            this.currentDate = new Date();
            this.categories = MdAutocompleteService
                                    .loadAllItems("All, Science And Technology, Lifestyle, Business, Sports, International");
            this.querySearch = MdAutocompleteService.querySearch;
            this.cancel = ($event) => $mdDialog.cancel();
            this.finish = ($event) => $mdDialog.hide();
            this.sendSearchParams = sendSearchParams;

            function sendSearchParams() {
                let searchParams = {
                    from: this.currentDate,
                    category: this.category,
                    relPop: this.relativePopularity
                };

                localStorageService.set("searchParams", JSON.stringify(searchParams));
                $state.go('tabs', {}, {reload: true});
            }
        }
    }

    loadNewsSearchResultData(searchParams) {
        let popularEntitiesSearchParams = {
            from: searchParams.from,
            type: "popular",
            category: searchParams.category,
            relPop: searchParams.relPop
        };

        this.NewsMapDataService.getWordCloud(popularEntitiesSearchParams)
            .success((response) => {
                console.log("POPULAR");
                this.wordCloudPopular = response;
            })
            .error(() => {

            });

        let hiddenEntitiesSearchParams = {
            from: searchParams.from,
            type: "hidden",
            category: searchParams.category,
            relPop: searchParams.relPop
        };

        this.NewsMapDataService.getWordCloud(hiddenEntitiesSearchParams)
            .success((response) => {
                console.log("HIDDEN");
                this.wordCloudHidden = response;
            })
            .error(() => {

            });

        let popularAndHiddenEntitiesSearchParams = {
            from: searchParams.from,
            category: searchParams.category,
            relPop: searchParams.relPop
        };

        this.NewsMapDataService.getWordCloud(popularAndHiddenEntitiesSearchParams)
            .success((response) => {
                console.log("POPULAR & HIDDEN");
                this.wordCloudPopularAndHidden = response;
            })
            .error(() => {

            });

        this.NewsMapDataService.getWorldHeatMap(searchParams.from)
            .success((response) => {
                console.log("HEAT MAP");
                this.geoHeatMap = response;
            })
            .error(() => {

            });
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

NewsMapController.$inject = ['$scope', '$timeout', '$mdBottomSheet', '$mdDialog', 'NewsMapDataService', 'localStorageService'];
export default NewsMapController;
