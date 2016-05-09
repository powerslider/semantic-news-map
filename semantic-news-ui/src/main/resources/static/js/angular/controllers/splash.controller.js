class SplashController {

    constructor(SplashService) {
        SplashService.open({
            title: 'Semantic News Map',
            message: "Read news like a champ"
        });
    }
}

SplashController.$inject = ['SplashService'];
export default SplashController;
