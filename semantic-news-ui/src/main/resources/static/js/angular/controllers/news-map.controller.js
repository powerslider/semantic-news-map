const GEO_HEAT_MAP_SIDE_PANEL = "geo-heat-map-side-panel";

class NewsMapController {

    constructor($scope, $rootScope, $timeout, $mdBottomSheet, $mdToast, $mdDialog, $window, $state, NewsMapDataService, MdAutocompleteService, MdToastService, localStorageService) {
        this.$scope = $scope;
        this.$timeout = $timeout;
        this.$mdBottomSheet = $mdBottomSheet;
        this.$mdToast = $mdToast;
        this.$mdDialog = $mdDialog;
        this.NewsMapDataService = NewsMapDataService;
        this.MdAutocompleteService = MdAutocompleteService;
        this.MdToastService = MdToastService;
        this.localStorageService = localStorageService;

        this.selectedTabIndex = 0;

        this.searchParams = this.localStorageService.get("searchParams");
        if (this.searchParams) {
            this.searchParams = angular.fromJson(this.searchParams);
            this.loadNewsSearchResultData(this.searchParams);
        }

        this.showGeoHeatMapSidePanel = false;

//        this.categories = ["Business", "International", "Lifestyle", "Science And Technology", "Sports"];
        this.categories = MdAutocompleteService
            .loadAllItems("Business, International, Lifestyle, Science And Technology, Sports");
        this.querySearch = MdAutocompleteService.querySearch;

        $rootScope.$on('wordClicked', (event, entityUri, text) => {
            this.searchParams.entityUri = entityUri;
            this.NewsMapDataService.checkNewsEntityDetails(this.searchParams)
                .success((response, status) => {
                    if (status === 204) {
                        this.MdToastService
                            .showToast('No other news data mentioning "' + text +'" or other entities related to it.');
                    } else {
                        let uri = encodeURIComponent(entityUri);
                        let detailsUrl = $state.href('news-details', {
                            uri: uri,
                            from: this.searchParams.from,
                            category: this.searchParams.category
                        });
                        $window.open(detailsUrl, '_blank');
                    }
                })
                .error((response) => {
                     this.MdToastService
                          .showToast('Error getting other news data mentioning "' + text +'" or other entities related to it.');
                });
        });

        $rootScope.$on('countryClicked', (event, country) => {
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
                            this.$mdToast
                                .position('top right')
                                .showSimple('Error requesting news mentioning ' + country.properties.name);
                        });
                    this.selectedCountry = country;
                    this.showGeoHeatMapSidePanel = true;
                });
            }
        });

        $scope.$watch('selectedCategory', angular.bind(this, (categoryIndex) => {
            let scrollCategory = Math.floor(this.topIndex / 6);
            if (scrollCategory !== categoryIndex) {
                this.topIndex = categoryIndex * 6;
            }
        }));

        $scope.$watch('topIndex', () => {
            let scrollCategory = Math.floor(this.topIndex / 6);
            this.selectedCategory = scrollCategory;
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

//        function showToast(msg) {
//            let toast = this.$mdToast
//                    .simple()
//                    .position('top right')
//                    .textContent(msg);
//            this.$mdToast.show(toast);
//        }

        this.NewsMapDataService.getWordCloud(popularEntitiesSearchParams)
            .success((response) => {
                console.log("POPULAR");
                this.wordCloudPopular = response;
            })
            .error(() => {
                this.MdToastService.showToast('Error requesting POPULAR entities word cloud data');
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
                this.MdToastService.showToast('Error requesting HIDDEN CHAMPIONS entities word cloud data');
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
                this.MdToastService.showToast('Error requesting POPULAR & HIDDEN entities word cloud data');
            });

        this.NewsMapDataService.getWorldHeatMap(searchParams.from)
            .success((response) => {
                console.log("HEAT MAP");
                this.geoHeatMap = response;
            })
            .error(() => {
                this.MdToastService.showToast('Error requesting countries GEO HEAT MAP data');
            });
    }
}

NewsMapController.$inject = ['$scope', '$rootScope', '$timeout', '$mdBottomSheet', '$mdToast', '$mdDialog', '$window', '$state', 'NewsMapDataService', 'MdAutocompleteService', 'MdToastService', 'localStorageService'];
export default NewsMapController;
