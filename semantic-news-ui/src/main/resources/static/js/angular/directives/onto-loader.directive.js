class OntoLoaderDirective {

    constructor($window, $timeout) {
        this.restrict = 'AE';
        this.template = (elem, attr) => { return '<object width="' + attr.size + '" height="' + attr.size + '"data="img/ot-loader.svg">Loading...</object>' }
    }
}

OntoLoaderDirective.$inject = [];
export default OntoLoaderDirective;