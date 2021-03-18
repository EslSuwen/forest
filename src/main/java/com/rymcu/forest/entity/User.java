package com.rymcu.forest.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/** @author ronger */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@TableName("forest_user")
public class User implements Serializable, Cloneable {
  @TableId(value = "id", type = IdType.AUTO)
  private Integer idUser;
  /** 登录账号 */
  private String account;
  /** 密码 */
  @JSONField(serialize = false)
  private String password;
  /** 昵称 */
  private String nickname;
  /** 真实姓名 */
  private String realName;
  /** 性别 1:男性 2:女性 */
  private String sex;
  /** 头像文件类型 */
  private String avatarType;
  /** 头像路径 */
  private String avatarUrl;
  /** 邮箱地址 */
  private String email;
  /** 手机号码 */
  private String phone;
  /** 签名 */
  private String signature;
  /** 状态 */
  private String status;
  /** 最后登录时间 */
  @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private Date lastLoginTime;
  /** 创建时间 */
  @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private Date createdTime;
  /** 创建时间 */
  @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private Date updatedTime;
}
