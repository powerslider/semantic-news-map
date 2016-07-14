class SplashController {

    constructor(SplashService) {
        SplashService.open({
            title: 'Semantic News Map',
            message: "Discover a world of meaning"
        });
    }
}

SplashController.$inject = ['SplashService'];
export default SplashController;
