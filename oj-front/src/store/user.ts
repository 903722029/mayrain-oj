import { StoreOptions } from "vuex";
import authorityEnum from "@/authority/authorityEnum";
import { UserControllerService } from "../../generated";

export default {
  namespaced: true,
  state: () => ({
    loginUser: {},
  }),
  mutations: {
    updateUser(state, payload) {
      state.loginUser = payload;
    },
  },
  actions: {
    async getLoginUser({ commit, state }, payload) {
      const res = await UserControllerService.getLoginUserUsingGet();
      if (res.code === 0) {
        commit("updateUser", res.data);
      } else {
        commit("updateUser", {
          ...state.loginUser,
          userRole: authorityEnum.NOT_LOGIN,
        });
      }
    },
  },
} as StoreOptions<any>;
