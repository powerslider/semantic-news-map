/**
 * The one and only controller used in this app.
 */
class AppController {

    constructor(SplashService) {
        this.SplashService = SplashService;
    }

    openSplash() {
        SplashService.open({
            title: 'Hi there!',
            message: "This sure is a fine modal, isn't it?"
        });
    }
}

AppController.$inject = ['SplashService'];
export default AppController;
