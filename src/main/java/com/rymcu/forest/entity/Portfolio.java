package com.rymcu.forest.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/** @author ronger */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@TableName("forest_portfolio")
public class Portfolio {
  /** 主键 */
  @TableId(value = "id", type = IdType.AUTO)
  private Integer idPortfolio;
  /** 作品集头像 */
  @TableField("portfolio_head_img_url")
  private String headImgUrl;
  /** 作品集名称 */
  private String portfolioTitle;
  /** 作品集作者 */
  private Integer portfolioAuthorId;
  /** 作品集介绍 */
  private String portfolioDescription;
  /** 作品集介绍 Html */
  private String portfolioDescriptionHtml;
  /** 创建时间 */
  private Date createdTime;
  /** 更新时间 */
  private Date updatedTime;

  @TableField(exist = false)
  private String headImgType;
}
