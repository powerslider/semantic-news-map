/**
 * The one and only controller used in this app.
 */
class NewsMapController {

    constructor($scope, $state, $compile, $timeout) {
        this.$scope = $scope;
        this.$state = $state;
        this.$compile = $compile;
        this.$timeout = $timeout;
        this.isVizOptionsSidePanelOpen = true;
        this.tabs = [
            {title: 'Most popular', content: '<div word-cloud></div>'},
            {title: 'Most popular + Hidden', content: '<div word-cloud></div>'},
            {title: 'Hidden Champions', content: '<div word-cloud></div>'},
            {title: 'Heat Map', content: '<div word-cloud></div>'}
        ];


        this.categories = ["All", "Science And Technology", "Lifestyle", "Business", "Sports", "International"];
        this.selectedIndex = 0;
        this.currentDate = new Date();
    }

    openMenu($mdOpenMenu, event) {
        $mdOpenMenu(event);
    }

    toggleVizOptionsSidePanel() {
        this.isVizOptionsSidePanelOpen = !this.isVizOptionsSidePanelOpen;
    }

    closeVizOptionsSidePanel() {
        this.isVizOptionsSidePanelOpen = false;
    }

//    addTab(title, view) {
//        view = view || title + " Content View";
//        this.tabs.push({ title: title, content: view, disabled: false});
//    }
//
//    removeTab(tab) {
//        let index = this.tabs.indexOf(tab);
//        this.tabs.splice(index, 1);
//    }
}

NewsMapController.$inject = ['$scope', '$state', '$compile', '$timeout'];
export default NewsMapController;
