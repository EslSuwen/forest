package com.rymcu.forest.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rymcu.forest.entity.UserExtend;
import org.apache.ibatis.annotations.Param;

/** @author ronger */
public interface UserExtendMapper extends BaseMapper<UserExtend> {
  /**
   * 获取用户扩展信息
   *
   * @param nickname
   * @return
   */
  UserExtend selectUserExtendByNickname(@Param("nickname") String nickname);
}
