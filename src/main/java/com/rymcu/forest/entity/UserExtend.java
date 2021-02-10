package com.rymcu.forest.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/** @author ronger */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@TableName("forest_user_extend")
public class UserExtend {
  @TableId(type = IdType.INPUT)
  private Integer idUser;
  private String github;
  private String weibo;
  private String weixin;
  private String qq;
  private String blog;
}
