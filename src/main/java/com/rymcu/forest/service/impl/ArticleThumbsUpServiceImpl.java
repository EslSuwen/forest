package com.rymcu.forest.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rymcu.forest.dto.result.Result;
import com.rymcu.forest.entity.Article;
import com.rymcu.forest.entity.ArticleThumbsUp;
import com.rymcu.forest.entity.User;
import com.rymcu.forest.mapper.ArticleThumbsUpMapper;
import com.rymcu.forest.service.ArticleService;
import com.rymcu.forest.service.ArticleThumbsUpService;
import com.rymcu.forest.util.UserUtils;
import com.rymcu.forest.web.api.v1.exception.BaseApiException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Objects;

/** @author ronger */
@Service
public class ArticleThumbsUpServiceImpl extends ServiceImpl<ArticleThumbsUpMapper, ArticleThumbsUp>
    implements ArticleThumbsUpService {

  @Resource private ArticleThumbsUpMapper articleThumbsUpMapper;
  @Resource private ArticleService articleService;

  @Override
  @Transactional(rollbackFor = Exception.class)
  public Result<?> thumbsUp(ArticleThumbsUp articleThumbsUp) throws BaseApiException {
    if (Objects.isNull(articleThumbsUp) || Objects.isNull(articleThumbsUp.getIdArticle())) {
      return Result.error("数据异常,文章不存在!");
    } else {
      int thumbsUpNumber = 1;
      Article article = articleService.getById(String.valueOf(articleThumbsUp.getIdArticle()));
      if (Objects.isNull(article)) {
        return Result.error("数据异常,文章不存在!");
      } else {
        User user = UserUtils.getCurrentUserByToken();
        articleThumbsUp.setIdUser(user.getIdUser());
        ArticleThumbsUp thumbsUp = getById(articleThumbsUp);
        if (Objects.isNull(thumbsUp)) {
          articleThumbsUp.setThumbsUpTime(new Date());
          save(articleThumbsUp);
          // 更新文章点赞数
        } else {
          removeById(thumbsUp.getIdArticleThumbsUp());
          // 更新文章点赞数
          thumbsUpNumber = -1;
        }
        articleThumbsUpMapper.updateArticleThumbsUpNumber(
            articleThumbsUp.getIdArticle(), thumbsUpNumber);
        return thumbsUpNumber > 0
            ? Result.OK("点赞成功", thumbsUpNumber)
            : Result.OK("已取消点赞", thumbsUpNumber);
      }
    }
  }
}
