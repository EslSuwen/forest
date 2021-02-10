package com.rymcu.forest.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rymcu.forest.entity.ArticleThumbsUp;
import com.rymcu.forest.web.api.v1.exception.BaseApiException;

import java.util.Map;

/** @author ronger */
public interface ArticleThumbsUpService extends IService<ArticleThumbsUp> {
  /**
   * 点赞
   *
   * @param articleThumbsUp
   * @throws BaseApiException
   * @return
   */
  Map thumbsUp(ArticleThumbsUp articleThumbsUp) throws BaseApiException;
}
