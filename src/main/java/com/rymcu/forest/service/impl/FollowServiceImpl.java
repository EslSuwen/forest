package com.rymcu.forest.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rymcu.forest.core.constant.NotificationConstant;
import com.rymcu.forest.dto.UserDTO;
import com.rymcu.forest.entity.Follow;
import com.rymcu.forest.entity.User;
import com.rymcu.forest.mapper.FollowMapper;
import com.rymcu.forest.service.FollowService;
import com.rymcu.forest.util.NotificationUtils;
import com.rymcu.forest.util.UserUtils;
import com.rymcu.forest.web.api.v1.exception.BaseApiException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/** @author ronger */
@Service
public class FollowServiceImpl extends ServiceImpl<FollowMapper, Follow> implements FollowService {

  @Resource private FollowMapper followMapper;

  @Override
  public Boolean isFollow(Integer followingId, String followingType) throws BaseApiException {
    User tokenUser = UserUtils.getCurrentUserByToken();
    Boolean b = followMapper.isFollow(followingId, tokenUser.getIdUser(), followingType);
    return b;
  }

  @Override
  public Boolean follow(Follow follow) throws BaseApiException {
    User tokenUser = UserUtils.getCurrentUserByToken();
    follow.setFollowerId(tokenUser.getIdUser());
    if (save(follow)) {
      NotificationUtils.saveNotification(
          follow.getFollowingId(),
          follow.getIdFollow(),
          NotificationConstant.Follow,
          tokenUser.getNickname() + " 关注了你!");
    }
    return true;
  }

  @Override
  public Boolean cancelFollow(Follow follow) throws BaseApiException {
    User tokenUser = UserUtils.getCurrentUserByToken();
    follow.setFollowerId(tokenUser.getIdUser());
    // TODO 测试删除逻辑
    int result =
        followMapper.delete(
            new LambdaQueryWrapper<Follow>().eq(Follow::getFollowerId, tokenUser.getIdUser()));
    return result == 0;
  }

  @Override
  public List<Follow> findByFollowingId(String followType, Integer followingId) {
    return list(
        new LambdaQueryWrapper<Follow>()
            .eq(Follow::getFollowingType, followType)
            .eq(Follow::getFollowingId, followingId));
  }

  @Override
  public List<UserDTO> findUserFollowersByUser(Page<?> page, UserDTO userDTO) {
    return followMapper.selectUserFollowersByUser(page, userDTO.getIdUser());
  }

  @Override
  public List<UserDTO> findUserFollowingsByUser(Page<?> page, UserDTO userDTO) {
    return followMapper.selectUserFollowingsByUser(userDTO.getIdUser());
  }
}
