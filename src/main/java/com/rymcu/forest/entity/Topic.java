package com.rymcu.forest.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/** @author ronger */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@TableName("forest_topic")
public class Topic {
  /** 主键 */
  @TableId(value = "id", type = IdType.AUTO)
  private Integer idTopic;
  /** 专题标题 */
  private String topicTitle;
  /** 专题路径 */
  private String topicUri;
  /** 专题描述 */
  private String topicDescription;
  /** 专题类型 */
  private String topicType;
  /** 专题序号;10 */
  private Integer topicSort;
  /** 专题图片路径 */
  private String topicIconPath;
  /** 0：作为导航1：不作为导航;0 */
  private String topicNva;
  /** 专题下标签总数;0 */
  private Integer topicTagCount;
  /** 0：正常1：禁用;0 */
  private String topicStatus;
  /** 创建时间 */
  @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private Date createdTime;
  /** 更新时间 */
  @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private Date updatedTime;
  /** 专题描述 Html */
  private String topicDescriptionHtml;
}
