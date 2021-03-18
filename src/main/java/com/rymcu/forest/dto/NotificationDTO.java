package com.rymcu.forest.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.rymcu.forest.entity.Notification;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/** @author ronger */
@Data
@EqualsAndHashCode(callSuper = false)
public class NotificationDTO {

  /** 用户id */
  private Integer idUser;
  /** 数据类型 */
  private String dataType;
  /** 数据id */
  private Integer dataId;
  /** 数据摘要 */
  private String dataSummary;
  /** 是否已读 */
  private String hasRead;
  /** 是否已读 */
  @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private Date createdTime;

  private Integer idNotification;

  private String dataTitle;

  private String dataUrl;

  private Author author;
}
