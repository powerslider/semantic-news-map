/**
 * The one and only controller used in this app.
 */
class NewsMapController {

    constructor() {
        this.isVizOptionsSidePanelOpen = true;
        this.tabs = [
                  { title: 'Most popular', content: "Tabs will become paginated if there isn't enough room for them."},
                  { title: 'Most popular + Hidden', content: "You can swipe left and right on a mobile device to change tabs."},
                  { title: 'Hidden Champions', content: "You can bind the selected tab via the selected attribute on the md-tabs element."},
                  { title: 'Heat Map', content: "You can bind the selected tab via the selected attribute on the md-tabs element."}
                ];

        this.menu = [
            {
              title: 'Category',
              icon: 'local_offer'
            },
            {
              link : '',
              title: 'Friends',
              icon: 'group'
            },
            {
              link : '',
              title: 'Messages',
              icon: 'message'
            }
        ];
        this.categories = ["All", "Science And Technology", "Lifestyle", "Business", "Sports", "International"];
        this.selectedIndex = 2;

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

//            selected = null,
//            previous = null;
//        $scope.tabs = tabs;
//        $scope.$watch('selectedIndex', function(current, old){
//          previous = selected;
//          selected = tabs[current];
//          if ( old + 1 && (old != current)) $log.debug('Goodbye ' + previous.title + '!');
//          if ( current + 1 )                $log.debug('Hello ' + selected.title + '!');
//        });
    addTab(title, view) {
        view = view || title + " Content View";
        this.tabs.push({ title: title, content: view, disabled: false});
    }

    removeTab(tab) {
        let index = this.tabs.indexOf(tab);
        this.tabs.splice(index, 1);
    }
}

NewsMapController.$inject = [];
export default NewsMapController;