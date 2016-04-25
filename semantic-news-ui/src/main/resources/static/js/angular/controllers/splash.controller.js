class SplashController {

    constructor(SplashService) {
        SplashService.open({
            title: 'Hi there!',
            message: "FUCK YEAHHHH"
        });
    }
}

SplashController.$inject = ['SplashService'];
export default SplashController;
