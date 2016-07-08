/**
 * Service for opening a splash screen
 */
class MdToastService {

   constructor($mdToast) {
       this.$mdToast = $mdToast;
   }

   showToast(msg) {
       let toast = this.$mdToast
               .simple()
               .position('top right')
               .textContent(msg);
       this.$mdToast.show(toast);
   }
}

MdToastService.$inject = ['$mdToast'];
export default MdToastService;