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
            controller: ['$mdDialog', NewsMapSearchController],
            controllerAs: "searchCtrl",
            bindToController: true,
            clickOutsideToClose: true,
            targetEvent: $event
        });

        function NewsMapSearchController($mdDialog) {
            this.currentDate = new Date();
            this.categories = loadAllCategories();
            this.querySearch = querySearch;
            this.cancel = ($event) => $mdDialog.cancel();
            this.finish = ($event) => $mdDialog.hide();

            function loadAllCategories() {
                let categories = "All, Science And Technology, Lifestyle, Business, Sports, International";
                return categories.split(/, +/g).map((category) => {
                    return {
                        value: category.toLowerCase(),
                        display: category
                    };
                });
            }

            function createFilterFor(query) {
                let lowercaseQuery = angular.lowercase(query);
                return function filterFn(category) {
                    return (category.value.indexOf(lowercaseQuery) === 0);
                };
            }

            function querySearch(query) {
                return query ? this.categories.filter(createFilterFor(query)) : this.categories;
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
