import authorityEnum from "@/authority/authorityEnum";

const checkAuthority = (user: any, needAuthority = authorityEnum.NOT_LOGIN) => {
  const userAuthority = user?.userRole ?? authorityEnum.NOT_LOGIN;
  if (needAuthority === authorityEnum.NOT_LOGIN) {
    return true;
  }
  if (needAuthority === authorityEnum.USER) {
    if (userAuthority === authorityEnum.NOT_LOGIN) {
      return false;
    }
  }
  if (needAuthority === authorityEnum.ADMIN) {
    if (userAuthority !== authorityEnum.ADMIN) {
      return false;
    }
  }
  return true;
};
export default checkAuthority;
