/**
 * The one and only controller used in this app.
 */
class HomeController {

    constructor(SplashService, $timeout) {
        this.title = "aeuaoeu";
        $timeout(() => {
            SplashService.open({
                title: 'Hi there!',
                message: "FUCK YEAHHHH"
            })
        }, 200);
    }

    changeState() {
        this.$state.go("home.init");
    }
}

HomeController.$inject = ['SplashService', '$timeout'];
export default HomeController;
