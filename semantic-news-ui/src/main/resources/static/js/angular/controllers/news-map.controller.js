/**
 * The one and only controller used in this app.
 */
class NewsMapController {

    constructor($scope, $timeout, $mdBottomSheet, $mdDialog, NewsMapDataService, MdAutocompleteService) {
        this.$scope = $scope;
        this.$timeout = $timeout;
        this.$mdBottomSheet = $mdBottomSheet;
        this.$mdDialog = $mdDialog;
        this.NewsMapDataService = NewsMapDataService;
        this.MdAutocompleteService = MdAutocompleteService;

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
            controller: ['$mdDialog', 'MdAutocompleteService', NewsMapSearchController],
            controllerAs: "searchCtrl",
            bindToController: true,
            clickOutsideToClose: true,
            targetEvent: $event
        });

        function NewsMapSearchController($mdDialog, MdAutocompleteService) {
            this.currentDate = new Date();
            this.categories = MdAutocompleteService
                                    .loadAllItems("All, Science And Technology, Lifestyle, Business, Sports, International");
            this.querySearch = MdAutocompleteService.querySearch;
            this.cancel = ($event) => $mdDialog.cancel();
            this.finish = ($event) => $mdDialog.hide();
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
