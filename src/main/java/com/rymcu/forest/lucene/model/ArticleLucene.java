package com.rymcu.forest.lucene.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * ArticleLucene
 *
 * @author suwen
 * @date 2021/2/3 09:57
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class ArticleLucene {

  /** 文章编号 */
  @TableId(value = "id", type = IdType.INPUT)
  private String idArticle;

  /** 文章标题 */
  private String articleTitle;

  /** 文章内容 */
  private String articleContent;

  /** 相关度评分 */
  private String score;
}
