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
@TableName("forest_wx_user")
public class WxUser {

  @TableId(value = "id", type = IdType.AUTO)
  private Integer idWxUser;

  private Boolean subscribe;

  private String openId;

  private String nickname;

  private String sexDesc;

  private Integer sex;

  private String language;

  private String city;

  private String province;

  private String country;

  private String headImgUrl;

  private Long subscribeTime;

  private String unionId;

  private String appId;

  private String actToken;
}
