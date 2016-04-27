/**
 * The one and only controller used in this app.
 */
class NewsMapController {

    constructor($scope, $state) {
        this.$scope = $scope;
        this.$state = $state;
        this.isVizOptionsSidePanelOpen = true;
        this.tabs = [
                  { title: 'Most popular', content: '<div word-cloud></div>' },
                  { title: 'Most popular + Hidden', content: '<div word-cloud></div>' },
                  { title: 'Hidden Champions', content: '<div word-cloud></div>' },
                  { title: 'Heat Map', content: '<div word-cloud></div>' }
                ];

        this.categories = ["All", "Science And Technology", "Lifestyle", "Business", "Sports", "International"];
        this.selectedIndex = 0;
        this.currentDate = new Date();
        this.$scope.$watch('selectedIndex', this.changeTabContent);
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

    changeTabContent(current, old) {
        switch(current) {
            case 0: this.$state.go("tab1"); break;
            case 1: this.$state.go("tab2"); break;
        }
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

NewsMapController.$inject = ['$scope', '$state'];
export default NewsMapController;
