package com.shousi.web.manager.auth;

import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.json.JSONUtil;
import com.shousi.web.manager.auth.model.SpaceUserAuthConfig;
import com.shousi.web.manager.auth.model.SpaceUserRole;
import com.shousi.web.model.entity.Space;
import com.shousi.web.model.entity.SpaceUser;
import com.shousi.web.model.entity.User;
import com.shousi.web.model.eums.SpaceRoleEnum;
import com.shousi.web.model.eums.SpaceTypeEnum;
import com.shousi.web.service.SpaceUserService;
import com.shousi.web.service.UserService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 根据角色获取权限列表
 */
@Component
public class SpaceUserAuthManager {

    @Resource
    private UserService userService;

    @Resource
    private SpaceUserService spaceUserService;

    public static final SpaceUserAuthConfig SPACE_USER_AUTH_CONFIG;

    static {
        String json = ResourceUtil.readUtf8Str("biz/spaceUserAuthConfig.json");
        SPACE_USER_AUTH_CONFIG = JSONUtil.toBean(json, SpaceUserAuthConfig.class);
    }

    /**
     * 根据角色获取权限列表
     */
    public List<String> getPermissionListByRole(String role) {
        if (role == null) {
            return new ArrayList<>();
        }
        SpaceUserRole spaceUserRole = SPACE_USER_AUTH_CONFIG.getRoles()
                .stream()
                .filter(r -> r.getKey().equals(role))
                .findFirst()
                .orElse(null);
        if (spaceUserRole == null) {
            return new ArrayList<>();
        }
        return spaceUserRole.getPermissions();
    }

    /**
     * 获取权限列表
     * @param space
     * @param loginUser
     * @return
     */
    public List<String> getPermissionList(Space space, User loginUser) {
        if (loginUser == null) {
            return new ArrayList<>();
        }
        // 管理员权限
        List<String> ADMIN_PERMISSIONS = getPermissionListByRole(SpaceRoleEnum.ADMIN.getValue());
        // 公共图库
        if (space == null) {
            // 如果是管理员，则返回所有权限
            if (userService.isAdmin(loginUser)) {
                return ADMIN_PERMISSIONS;
            }
            return new ArrayList<>();
        }
        SpaceTypeEnum spaceTypeEnum = SpaceTypeEnum.getEnumByValue(space.getSpaceType());
        if (spaceTypeEnum == null) {
            return new ArrayList<>();
        }
        // 根据空间获取对应的权限
        switch (spaceTypeEnum) {
            case PRIVATE:
                // 私有空间，仅本人或管理员有所有权限
                if (space.getUserId().equals(loginUser.getId()) || userService.isAdmin(loginUser)) {
                    return ADMIN_PERMISSIONS;
                } else {
                    return new ArrayList<>();
                }
            case TEAM:
                // 团队空间，查询 SpaceUser 并获取角色和权限
                SpaceUser spaceUser = spaceUserService.lambdaQuery()
                        .eq(SpaceUser::getSpaceId, space.getId())
                        .eq(SpaceUser::getUserId, loginUser.getId())
                        .one();
                if (spaceUser == null) {
                    return new ArrayList<>();
                } else {
                    return getPermissionListByRole(spaceUser.getSpaceRole());
                }
        }
        return new ArrayList<>();
    }

}
