package com.mayrain.mayrainbackendserviceclient.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mayrain.mayrainbackendcommon.common.ErrorCode;
import com.mayrain.mayrainbackendcommon.exception.BusinessException;
import com.mayrain.mayrainbackendmodel.dto.user.UserQueryRequest;
import com.mayrain.mayrainbackendmodel.entity.User;
import com.mayrain.mayrainbackendmodel.enums.UserRoleEnum;
import com.mayrain.mayrainbackendmodel.vo.LoginUserVO;
import com.mayrain.mayrainbackendmodel.vo.UserVO;
import org.springframework.beans.BeanUtils;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.List;

import static com.mayrain.mayrainbackendcommon.constant.UserConstant.USER_LOGIN_STATE;

/**
 * 用户服务
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
 */
@FeignClient(name = "mayrain-backend-user-service", path = "/api/user/inner")
public interface UserFeignClient {
    /**
     * 根据 id 获取用户信息
     * @param userId
     * @return
     */
    @GetMapping("/get/id")
    User getById(@RequestParam("userId") Long userId);

    /**
     * 根据 ids 获取用户列表
     * @param ids
     * @return
     */
    @GetMapping("/get/ids")
    List<User> listByIds(@RequestParam("idList") Collection<Long> idList);
    /**
     * 获取当前登录用户
     *
     * @param request
     * @return
     */
     default User getLoginUser(HttpServletRequest request) {
         // 先判断是否已登录
         Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
         User currentUser = (User) userObj;
         if (currentUser == null || currentUser.getId() == null) {
             throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
         }
         return currentUser;
     }

    /**
     * 是否为管理员
     *
     * @param user
     * @return
     */
    default boolean isAdmin(User user) {
        return user != null && UserRoleEnum.ADMIN.getValue().equals(user.getUserRole());
    }

    /**
     * 获取脱敏的用户信息
     *
     * @param user
     * @return
     */
    default UserVO getUserVO(User user) {
        if (user == null) {
            return null;
        }
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        return userVO;
    }

}
