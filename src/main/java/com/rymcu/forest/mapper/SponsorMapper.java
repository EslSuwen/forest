package com.rymcu.forest.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rymcu.forest.entity.Sponsor;
import org.apache.ibatis.annotations.Param;

/** @author ronger */
public interface SponsorMapper extends BaseMapper<Sponsor> {
  /**
   * 更新文章赞赏数
   *
   * @param idArticle
   * @return
   */
  Integer updateArticleSponsorCount(@Param("idArticle") Integer idArticle);
}
