/**
 * The one and only controller used in this app.
 */
class HomeController {

    constructor(SplashService, $timeout, $mdSidenav) {
        this.$mdSidenav = $mdSidenav;
        $timeout(() => {
            SplashService.open({
                title: 'Hi there!',
                message: "FUCK YEAHHHH"
            })
        }, 200);
    }

    toggleLeft() {
        return () => {
            this.$mdSidenav("left").toggle();
        }
    }

    close() {
        return () => {
            this.$mdSidenav("left").close();
        }
    }
}


HomeController.$inject = ['SplashService', '$timeout'];
export default HomeController;
