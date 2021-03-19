package com.rymcu.forest.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rymcu.forest.core.constant.NotificationConstant;
import com.rymcu.forest.core.constant.ProjectConstant;
import com.rymcu.forest.dto.*;
import com.rymcu.forest.dto.result.Result;
import com.rymcu.forest.entity.Article;
import com.rymcu.forest.entity.ArticleContent;
import com.rymcu.forest.entity.Tag;
import com.rymcu.forest.entity.User;
import com.rymcu.forest.lucene.service.LuceneService;
import com.rymcu.forest.mapper.ArticleMapper;
import com.rymcu.forest.service.ArticleService;
import com.rymcu.forest.service.TagService;
import com.rymcu.forest.service.UserService;
import com.rymcu.forest.util.*;
import com.rymcu.forest.web.api.v1.exception.BaseApiException;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/** @author ronger */
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article>
    implements ArticleService {

  @Resource private ArticleMapper articleMapper;
  @Resource private TagService tagService;
  @Resource private UserService userService;
  @Resource private LuceneService luceneService;

  @Value("${resource.domain}")
  private String domain;

  @Value("${env}")
  private String env;

  private static final int MAX_PREVIEW = 200;
  private static final String defaultStatus = "0";
  private static final String defaultTopicUri = "news";

  @Override
  public IPage<ArticleDTO> findArticles(Page<?> page, ArticleSearchDTO searchDTO) {
    IPage<ArticleDTO> articlePage;
    if (StringUtils.isNotBlank(searchDTO.getTopicUri())
        && !defaultTopicUri.equals(searchDTO.getTopicUri())) {
      articlePage = articleMapper.selectArticlesByTopicUri(page, searchDTO.getTopicUri());
    } else {
      articlePage =
          articleMapper.selectArticles(
              page, searchDTO.getSearchText(), searchDTO.getTag(), searchDTO.getTopicUri());
    }
    return articlePage;
  }

  @Override
  public IPage<ArticleListDTO> getArticleList(Page<?> page) {
    return baseMapper.selectArticleList(page);
  }

  @Override
  public ArticleDTO findArticleDTOById(Integer id, Integer type) {
    ArticleDTO articleDTO = articleMapper.selectArticleDTOById(id, type);
    if (articleDTO == null) {
      return null;
    }
    articleDTO = genArticle(articleDTO, type);
    return articleDTO;
  }

  @Override
  public IPage<ArticleDTO> findArticlesByTopicUri(Page<?> page, String name) {
    IPage<ArticleDTO> articleIPage = articleMapper.selectArticlesByTopicUri(page, name);
    articleIPage.getRecords().forEach(articleDTO -> genArticle(articleDTO, 0));
    return articleIPage;
  }

  @Override
  public List<ArticleDTO> findArticlesByTagName(Page<?> page, String name) {
    List<ArticleDTO> articleDTOS = articleMapper.selectArticlesByTagName(page, name);
    return articleDTOS;
  }

  @Override
  public IPage<ArticleDTO> findUserArticlesByIdUser(Page<?> page, Integer idUser) {
    return articleMapper.selectUserArticles(page, idUser);
  }

  @Override
  @Transactional(rollbackFor = {UnsupportedEncodingException.class, BaseApiException.class})
  public Result<?> postArticle(ArticleDTO article, HttpServletRequest request)
      throws UnsupportedEncodingException, BaseApiException {
    if (StringUtils.isBlank(article.getArticleTitle())) {
      Result.error("标题不能为空！");
    }
    if (StringUtils.isBlank(article.getArticleContent())) {
      Result.error("正文不能为空！");
    }
    boolean isUpdate = false;
    String articleTitle = article.getArticleTitle();
    String articleTags = article.getArticleTags();
    String articleContent = article.getArticleContent();
    String articleContentHtml = article.getArticleContentHtml();
    User user = UserUtils.getCurrentUserByToken();
    String reservedTag = checkTags(articleTags);
    boolean notification = false;
    if (StringUtils.isNotBlank(reservedTag)) {
      Integer roleWeights = userService.findRoleWeightsByUser(user.getIdUser());
      if (roleWeights > 2) {
        return Result.error(StringEscapeUtils.unescapeJava(reservedTag) + "标签为系统保留标签!");
      } else {
        notification = true;
      }
    }
    Article newArticle;
    if (article.getIdArticle() == null || article.getIdArticle() == 0) {
      newArticle = new Article();
      newArticle.setArticleTitle(articleTitle);
      newArticle.setArticleAuthorId(user.getIdUser());
      newArticle.setArticleTags(articleTags);
      newArticle.setCreatedTime(new Date());
      newArticle.setUpdatedTime(newArticle.getCreatedTime());
      newArticle.setArticleStatus(article.getArticleStatus());
      save(newArticle);
      articleMapper.insertArticleContent(
          newArticle.getIdArticle(), articleContent, articleContentHtml);
    } else {
      newArticle = getById(article.getIdArticle());
      // 如果文章之前状态为草稿则应视为新发布文章
      isUpdate = defaultStatus.equals(newArticle.getArticleStatus());
      if (userService.findRoleWeightsByUser(user.getIdUser()) <= 2
          || user.getIdUser().equals(newArticle.getArticleAuthorId())) {
        newArticle.setArticleTitle(articleTitle);
        newArticle.setArticleTags(articleTags);
        newArticle.setArticleStatus(article.getArticleStatus());
        newArticle.setUpdatedTime(new Date());
        articleMapper.updateArticleContent(
            newArticle.getIdArticle(), articleContent, articleContentHtml);
      } else {
        return Result.error("非法访问！");
      }
    }

    // 发送相关通知
    if (defaultStatus.equals(newArticle.getArticleStatus())) {
      // 发送系统通知
      if (notification) {
        NotificationUtils.sendAnnouncement(
            newArticle.getIdArticle(), NotificationConstant.Article, newArticle.getArticleTitle());
      } else {
        // 发送关注通知
        StringBuilder dataSummary = new StringBuilder();
        if (isUpdate) {
          dataSummary
              .append(user.getNickname())
              .append("更新了文章: ")
              .append(newArticle.getArticleTitle());
          NotificationUtils.sendArticlePush(
              newArticle.getIdArticle(),
              NotificationConstant.UpdateArticle,
              dataSummary.toString(),
              newArticle.getArticleAuthorId());
        } else {
          dataSummary
              .append(user.getNickname())
              .append("发布了文章: ")
              .append(newArticle.getArticleTitle());
          NotificationUtils.sendArticlePush(
              newArticle.getIdArticle(),
              NotificationConstant.PostArticle,
              dataSummary.toString(),
              newArticle.getArticleAuthorId());
        }
      }
    }

    tagService.saveTagArticle(newArticle, articleContentHtml);

    if (defaultStatus.equals(newArticle.getArticleStatus())) {
      newArticle.setArticlePermalink(domain + "/article/" + newArticle.getIdArticle());
      newArticle.setArticleLink("/article/" + newArticle.getIdArticle());
    } else {
      newArticle.setArticlePermalink(domain + "/draft/" + newArticle.getIdArticle());
      newArticle.setArticleLink("/draft/" + newArticle.getIdArticle());
    }

    if (StringUtils.isNotBlank(articleContentHtml)) {
      String previewContent =
          BaiDuAipUtils.getNewsSummary(
              newArticle.getArticleTitle(), articleContentHtml, MAX_PREVIEW);
      if (previewContent.length() > MAX_PREVIEW) {
        previewContent = previewContent.substring(0, MAX_PREVIEW);
      }
      newArticle.setArticlePreviewContent(previewContent);
    }
    updateById(newArticle);

    // 推送百度 SEO
    if (!ProjectConstant.ENV.equals(env)
        && defaultStatus.equals(newArticle.getArticleStatus())
        && articleContent.length() >= MAX_PREVIEW) {
      if (isUpdate) {
        BaiDuUtils.sendUpdateSEOData(newArticle.getArticlePermalink());
      } else {
        BaiDuUtils.sendSEOData(newArticle.getArticlePermalink());
      }
    }
    // 草稿不更新索引
    if ("0".equals(article.getArticleStatus())) {
      System.out.println("开始增加索引");
      if (isUpdate) {
        luceneService.writeArticle(newArticle.getIdArticle().toString());
      } else {
        luceneService.updateArticle(newArticle.getIdArticle().toString());
      }
    }
    return Result.OK(newArticle.getIdArticle());
  }

  private String checkTags(String articleTags) {
    // 判断文章是否有标签
    if (StringUtils.isBlank(articleTags)) {
      return "";
    }
    // 判断是否存在系统配置的保留标签词
    List<Tag> tags = tagService.list(new LambdaQueryWrapper<Tag>().eq(Tag::getTagReservation, "1"));
    if (tags.isEmpty()) {
      return "";
    } else {
      String[] articleTagArr = articleTags.split(",");
      for (Tag tag : tags) {
        if (StringUtils.isBlank(tag.getTagTitle())) {
          continue;
        }
        for (String articleTag : articleTagArr) {
          if (StringUtils.isBlank(articleTag)) {
            continue;
          }
          if (articleTag.equals(tag.getTagTitle())) {
            return tag.getTagTitle();
          }
        }
      }
    }

    return "";
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public Result<?> delete(Integer id) {
    // 删除关联数据(作品集关联关系,标签关联关系)
    // TODO 开发中暂不删除关联数据 deleteLinkedData(id);
    removeById(id);
    luceneService.deleteArticle(id.toString());
    return Result.OK();
  }

  private void deleteLinkedData(Integer id) {
    // 删除关联作品集
    articleMapper.deleteLinkedPortfolioData(id);
    // 删除引用标签记录
    articleMapper.deleteTagArticle(id);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void incrementArticleViewCount(Integer id) {
    Article article = getById(id);
    Integer articleViewCount = article.getArticleViewCount() + 1;
    articleMapper.updateArticleViewCount(article.getIdArticle(), articleViewCount);
  }

  @Override
  public String share(Integer id) throws BaseApiException {
    Article article = getById(id);
    User user = UserUtils.getCurrentUserByToken();
    StringBuilder shareUrl = new StringBuilder(article.getArticlePermalink());
    shareUrl.append("?s=").append(user.getNickname());
    return shareUrl.toString();
  }

  @Override
  public IPage<ArticleDTO> findDrafts(Page<?> page) throws BaseApiException {
    User user = UserUtils.getCurrentUserByToken();
    IPage<ArticleDTO> list = articleMapper.selectDrafts(page, user.getIdUser());
    list.getRecords().forEach(article -> genArticle(article, 0));
    return list;
  }

  @Override
  public IPage<ArticleDTO> findArticlesByIdPortfolio(Page<?> page, Integer idPortfolio) {
    IPage<ArticleDTO> list = articleMapper.selectArticlesByIdPortfolio(page, idPortfolio);
    list.getRecords().forEach(article -> genArticle(article, 0));
    return list;
  }

  @Override
  public IPage<ArticleDTO> selectUnbindArticles(
      Page<?> page, Integer idPortfolio, String searchText, Integer idUser) {
    IPage<ArticleDTO> list =
        articleMapper.selectUnbindArticlesByIdPortfolio(page, idPortfolio, searchText, idUser);
    list.getRecords().forEach(article -> genArticle(article, 0));
    return list;
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public Result<?> updateTags(Integer idArticle, String tags)
      throws UnsupportedEncodingException, BaseApiException {
    Article article = getById(idArticle);
    if (Objects.nonNull(article)) {
      article.setArticleTags(tags);
      articleMapper.updateArticleTags(idArticle, tags);
      tagService.saveTagArticle(article, "");
      return Result.OK("更新成功");
    } else {
      return Result.error("更新失败,文章不存在!");
    }
  }

  @Override
  public Result<?> updatePerfect(Integer idArticle, String articlePerfect) {
    int result = articleMapper.updatePerfect(idArticle, articlePerfect);
    return result != 0 ? Result.OK() : Result.error("设置优选文章失败!");
  }

  @Override
  public Result<?> revokeDelete(Integer idArticle) {
    return articleMapper.revokeDelete(idArticle) == 1 ? Result.OK() : Result.error("撤销删除失败！");
  }

  private ArticleDTO genArticle(ArticleDTO article, Integer type) {
    Integer articleList = 0;
    Integer articleView = 1;
    Integer articleEdit = 2;
    Author author = genAuthor(article);
    article.setArticleAuthor(author);
    article.setTimeAgo(Utils.getTimeAgo(article.getUpdatedTime()));
    List<ArticleTagDTO> tags = articleMapper.selectTags(article.getIdArticle());
    article.setTags(tags);
    if (!type.equals(articleList)) {
      ArticleContent articleContent = articleMapper.selectArticleContent(article.getIdArticle());
      if (type.equals(articleView)) {
        article.setArticleContent(articleContent.getArticleContentHtml());
        // 获取所属作品集列表数据
        List<PortfolioArticleDTO> portfolioArticleDTOList =
            articleMapper.selectPortfolioArticles(article.getIdArticle());
        portfolioArticleDTOList.forEach(this::genPortfolioArticles);
        article.setPortfolios(portfolioArticleDTOList);
      } else if (type.equals(articleEdit)) {
        article.setArticleContent(articleContent.getArticleContent());
      } else {
        article.setArticleContent(articleContent.getArticleContentHtml());
      }
    }
    return article;
  }

  private PortfolioArticleDTO genPortfolioArticles(PortfolioArticleDTO portfolioArticleDTO) {
    List<ArticleDTO> articles =
        articleMapper.selectPortfolioArticlesByIdPortfolioAndSortNo(
            portfolioArticleDTO.getIdPortfolio(), portfolioArticleDTO.getSortNo());
    portfolioArticleDTO.setArticles(articles);
    return portfolioArticleDTO;
  }

  private Author genAuthor(ArticleDTO article) {
    Author author = new Author();
    author.setUserNickname(article.getArticleAuthorName());
    author.setUserAvatarURL(article.getArticleAuthorAvatarUrl());
    author.setIdUser(article.getArticleAuthorId());
    return author;
  }
}
