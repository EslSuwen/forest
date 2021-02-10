package com.rymcu.forest.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.rymcu.forest.entity.Notification;
import lombok.Data;
import lombok.EqualsAndHashCode;

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
  @JSONField(format = "yyyy-MM-dd HH:mm:ss")
  private Date createdTime;

  private Integer idNotification;

  private String dataTitle;

  private String dataUrl;

  private Author author;
}
