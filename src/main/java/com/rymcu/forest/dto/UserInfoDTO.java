package com.rymcu.forest.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/** @author ronger */
@Data
public class UserInfoDTO implements Serializable {

  private Integer idUser;

  private String account;

  private String avatarType;

  private String avatarUrl;

  private String nickname;

  private String email;

  private String phone;

  private String status;

  private String roleIds;

  private String sex;

  private String signature;

  @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private Date lastLoginTime;
}
