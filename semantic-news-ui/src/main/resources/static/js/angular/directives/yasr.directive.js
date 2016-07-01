class YasrDirective {

    constructor($timeout) {
        this.restrict = 'AE';
        this.template = '<div id="yasr_data"></div>';
        this.scope = {
            yasrData: '=',
            yasrPostProcess: '&'
        };

        this.$timeout = $timeout;

        YASR.defaults.outputPlugins = ["table", "error", "boolean"];
    }

    link(scope, element, attrs) {
        scope.$watch('yasrData', () => {
            if (scope.yasrData) {
                this.$timeout(() => {
                    let yasrRes = YASR(document.getElementById("yasr_data"));
                    yasrRes.setResponse(scope.yasrData);
                    scope.yasrPostProcess();
                }, 50);
            }
        })
    }
}

YasrDirective.$inject = ['$timeout'];
export default YasrDirective;