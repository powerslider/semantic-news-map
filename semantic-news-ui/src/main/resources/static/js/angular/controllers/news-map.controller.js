const GEO_HEAT_MAP_SIDE_PANEL = "geo-heat-map-side-panel";

class NewsMapController {

    constructor($scope, $rootScope, $timeout, $mdBottomSheet, $mdDialog, NewsMapDataService, MdAutocompleteService, localStorageService) {
        this.$scope = $scope;
        this.$timeout = $timeout;
        this.$mdBottomSheet = $mdBottomSheet;
        this.$mdDialog = $mdDialog;
        this.NewsMapDataService = NewsMapDataService;
        this.MdAutocompleteService = MdAutocompleteService;
        this.localStorageService = localStorageService;
        this.$rootScope = $rootScope;

        this.selectedTabIndex = 0;

        this.searchParams = this.localStorageService.get("searchParams");
        if (this.searchParams) {
            this.searchParams = angular.fromJson(this.searchParams)
            this.loadNewsSearchResultData(this.searchParams);
        }

        this.showGeoHeatMapSidePanel = false;

//        this.categories = ["Business", "International", "Lifestyle", "Science And Technology", "Sports"];
        this.categories = MdAutocompleteService
            .loadAllItems("Business, International, Lifestyle, Science And Technology, Sports");
        this.querySearch = MdAutocompleteService.querySearch;


        this.$rootScope.$on('countryClicked', (event, country) => {
            if ($.isArray(country)) {
                this.$scope.$apply(() => {
                    this.showGeoHeatMapSidePanel = false;
                });
            } else {
                this.$scope.$apply(() => {
                    let newsMentioningCountryParams = {
                        from: this.searchParams.from,
                        countryCode: country.id
                    };
                    this.NewsMapDataService.getNewsMentioningCountry(newsMentioningCountryParams)
                        .success((response) => {
                            country.newsCount = response.length;
                            this.newsMentioningCountry = response;
                        })
                        .error(() => {

                        });
                    this.selectedCountry = country;
                    this.showGeoHeatMapSidePanel = true;
                });
            }
        });

        $scope.$watch('selectedCategory', () => {
            var scrollYear = Math.floor(this.topIndex / 13);
            if (scrollYear !== yearIndex) {
                this.topIndex = yearIndex * 13;
            }
        });

        $scope.$watch('topIndex', () => {
            var scrollYear = Math.floor(this.topIndex / 13);
            this.selectedYear = scrollYear;
        });
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

        function NewsMapSearchController($mdDialog, $rootScope, MdAutocompleteService, localStorageService, $state) {
            this.currentDate = new Date();
            this.categories = MdAutocompleteService
                .loadAllItems("All, Business, International, Lifestyle, Science And Technology, Sports");
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

                localStorageService.set("searchParams", angular.toJson(searchParams));
                $state.go('news-map', {}, {reload: true});
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
}

NewsMapController.$inject = ['$scope', '$rootScope', '$timeout', '$mdBottomSheet', '$mdDialog', 'NewsMapDataService', 'MdAutocompleteService', 'localStorageService'];
export default NewsMapController;
