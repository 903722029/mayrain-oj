import router from "@/router";
import store from "@/store";
import AuthorityEnum from "@/authority/authorityEnum";
import checkAuthority from "@/authority/checkAuthority";

router.beforeEach(async (to, from, next) => {
  console.log("用户身份", store.state.user.loginUser);
  let loginUser = store.state.user.loginUser;
  if (!loginUser || !loginUser.userRole) {
    // 如果用户没登录，自动登录
    await store.dispatch("user/getLoginUser");
    loginUser = store.state.user.loginUser;
  }
  const needAuthority = (to.meta?.access as string) ?? AuthorityEnum.NOT_LOGIN;
  // 如果跳转页面必须要登录
  if (needAuthority !== AuthorityEnum.NOT_LOGIN) {
    // 如果用户未登录，跳转到登录页面
    if (
      !loginUser ||
      !loginUser.userRole ||
      loginUser.userRole === AuthorityEnum.NOT_LOGIN
    ) {
      next(`user/login?redirect=${to.fullPath}`);
      return;
    }
    // 如果用户已经登录，判断权限
    if (!checkAuthority(loginUser, needAuthority)) {
      next("noAuth");
      return;
    }
  }
  next();
});
