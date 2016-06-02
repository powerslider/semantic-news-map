class YasrDirective {

    constructor($timeout) {
        this.restrict = 'AE';
        this.template = '<div id="yasr_data"></div>';
        this.scope = {
            yasrData: '='
        }

        this.$timeout = $timeout;

        YASR.defaults.outputPlugins = ["table", "error", "boolean"];
//        YASR.defaults.outputPlugins = ["table", "error", "boolean", "pivot", "gchart"];
    }

    link(scope, element, attrs) {
        scope.$watch('yasrData', () => {
            if (scope.yasrData) {
                this.$timeout(() => {
                    let yasrRes = YASR(document.getElementById("yasr_data"));
                    yasrRes.setResponse(scope.yasrData);
                }, 50);
            }
        })
    }
}

YasrDirective.$inject = ['$timeout'];
export default YasrDirective;