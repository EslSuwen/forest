package com.rymcu.forest.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rymcu.forest.dto.UserDTO;
import com.rymcu.forest.entity.Follow;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/** @author ronger */
public interface FollowMapper extends BaseMapper<Follow> {
  /**
   * 判断是否关注
   *
   * @param followingId
   * @param followerId
   * @param followingType
   * @return
   */
  Boolean isFollow(
      @Param("followingId") Integer followingId,
      @Param("followerId") Integer followerId,
      @Param("followingType") String followingType);

  /**
   * 查询用户粉丝
   *
   * @param idUser
   * @return
   */
  IPage<UserDTO> selectUserFollowersByUser(Page<?> page, @Param("idUser") Integer idUser);

  /**
   * 查询用户关注用户
   *
   * @param idUser
   * @return
   */
  IPage<UserDTO> selectUserFollowingsByUser(IPage<?> page, @Param("idUser") Integer idUser);
}
