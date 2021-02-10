package com.rymcu.forest.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/** @author ronger */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@TableName("forest_tag")
public class Tag implements Serializable, Cloneable {
  /** 主键 */
  @TableId(value = "id", type = IdType.AUTO)
  private Integer idTag;
  /** 标签名 */
  private String tagTitle;
  /** 标签图标 */
  private String tagIconPath;
  /** 标签uri */
  private String tagUri;
  /** 描述 */
  private String tagDescription;
  /** 浏览量 */
  private Integer tagViewCount;
  /** 关联文章总数 */
  private Integer tagArticleCount;
  /** 标签广告 */
  private String tagAd;
  /** 是否显示全站侧边栏广告 */
  private String tagShowSideAd;
  /** 标签状态 */
  private String tagStatus;
  /** 创建时间 */
  private Date createdTime;
  /** 更新时间 */
  private Date updatedTime;
  /** 保留标签 */
  private String tagReservation;
  /** 描述 */
  private String tagDescriptionHtml;
}
