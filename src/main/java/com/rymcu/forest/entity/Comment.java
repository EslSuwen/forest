package com.rymcu.forest.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
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
@TableName("forest_comment")
public class Comment implements Serializable, Cloneable {
  /** 主键 */
  @TableId(value = "id", type = IdType.AUTO)
  private Integer idComment;
  /** 评论内容 */
  private String commentContent;
  /** 作者 id */
  private Integer commentAuthorId;
  /** 文章 id */
  private Integer commentArticleId;
  /** 锚点 url */
  private String commentSharpUrl;
  /** 父评论 id */
  private Integer commentOriginalCommentId;
  /** 状态 */
  private String commentStatus;
  /** 评论 IP */
  @TableField(value = "comment_ip", select = false)
  private String commentIP;
  /** User-Agent */
  @TableField(value = "comment_ua", select = false)
  private String commentUA;
  /** 0：公开回帖，1：匿名回帖 */
  private String commentAnonymous;
  /** 回帖计数 */
  private Integer commentReplyCount;
  /** 0：所有人可见，1：仅楼主和自己可见 */
  private String commentVisible;
  /** 创建时间 */
  @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private Date createdTime;
}
