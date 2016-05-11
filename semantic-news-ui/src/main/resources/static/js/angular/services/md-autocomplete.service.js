/**
 * Service for opening a splash screen
 */
class MdAutocompleteService {

    constructor() {

    }
    
    loadAllItems(items) {
        return items.split(/, +/g).map((item) => {
            return {
                value: item.toLowerCase(),
                display: item
            };
        });
    }

    querySearch(query, items) {
        function createFilterFor(query) {
            let lowercaseQuery = angular.lowercase(query);
            return function filterFn(item) {
                return (item.value.indexOf(lowercaseQuery) === 0);
            };
        }

        return query ? items.filter(createFilterFor(query)) : items;
    }
}

MdAutocompleteService.$inject = [];
export default MdAutocompleteService;