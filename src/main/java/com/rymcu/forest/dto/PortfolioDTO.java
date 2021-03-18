package com.rymcu.forest.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/** @author ronger */
@Data
public class PortfolioDTO {

  private Integer idPortfolio;
  /** 作品集头像 */
  private String headImgUrl;
  /** 作品集作者 */
  private Integer portfolioAuthorId;
  /** 作品集作者 */
  private String portfolioAuthorName;
  /** 作品集作者头像 */
  private String portfolioAuthorAvatarUrl;
  /** 作品集名称 */
  private String portfolioTitle;
  /** 作品集介绍 */
  private String portfolioDescription;
  /** 更新时间 */
  @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private Date updatedTime;
  /** 过去时长 */
  private String timeAgo;

  private Author portfolioAuthor;

  private Integer articleNumber;
}
