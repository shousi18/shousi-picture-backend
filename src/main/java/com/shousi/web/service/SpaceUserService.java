package com.shousi.web.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.shousi.web.model.dto.spaceuser.HandleInvitationRequest;
import com.shousi.web.model.dto.spaceuser.SpaceUserAddRequest;
import com.shousi.web.model.dto.spaceuser.SpaceUserQueryRequest;
import com.shousi.web.model.entity.SpaceUser;
import com.baomidou.mybatisplus.extension.service.IService;
import com.shousi.web.model.entity.User;
import com.shousi.web.model.vo.SpaceUserVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author 86172
 * @description 针对表【space_user(空间用户关联)】的数据库操作Service
 * @createDate 2025-04-08 19:46:54
 */
public interface SpaceUserService extends IService<SpaceUser> {

    /**
     * 创建空间成员
     *
     * @param spaceUserAddRequest
     * @return
     */
    long addSpaceUser(SpaceUserAddRequest spaceUserAddRequest);

    /**
     * 校验空间成员
     *
     * @param spaceUser
     * @param add       是否为创建时检验
     */
    void validSpaceUser(SpaceUser spaceUser, boolean add);

    /**
     * 获取空间成员包装类（单条）
     *
     * @param spaceUser
     * @return
     */
    SpaceUserVO getSpaceUserVO(SpaceUser spaceUser);

    /**
     * 获取空间成员包装类（列表）
     *
     * @param spaceUserList
     * @return
     */
    List<SpaceUserVO> getSpaceUserVOList(List<SpaceUser> spaceUserList);

    /**
     * 获取查询对象
     *
     * @param spaceUserQueryRequest
     * @return
     */
    QueryWrapper<SpaceUser> getQueryWrapper(SpaceUserQueryRequest spaceUserQueryRequest);

    /**
     * 获取待处理的邀请
     * @param loginUser
     * @return
     */
    List<SpaceUserVO> getPendingInvitations(User loginUser);

    /**
     * 处理团队空间邀请
     * @param handleInvitationRequest
     * @param loginUser
     * @return
     */
    Boolean handleInvitation(HandleInvitationRequest handleInvitationRequest, User loginUser);
}
