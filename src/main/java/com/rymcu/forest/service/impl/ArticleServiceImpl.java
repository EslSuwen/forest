package com.rymcu.forest.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rymcu.forest.core.constant.NotificationConstant;
import com.rymcu.forest.core.constant.ProjectConstant;
import com.rymcu.forest.dto.*;
import com.rymcu.forest.entity.Article;
import com.rymcu.forest.entity.ArticleContent;
import com.rymcu.forest.entity.Tag;
import com.rymcu.forest.entity.User;
import com.rymcu.forest.mapper.ArticleMapper;
import com.rymcu.forest.service.ArticleService;
import com.rymcu.forest.service.CommentService;
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
import java.util.*;

/** @author ronger */
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article>
    implements ArticleService {

  @Resource private ArticleMapper articleMapper;
  @Resource private TagService tagService;
  @Resource private UserService userService;
  @Resource private CommentService commentService;

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
    articlePage
        .getRecords()
        .forEach(
            article -> {
              genArticle(article, 0);
            });
    return articlePage;
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
    articleIPage.getRecords().forEach(
        articleDTO -> {
          genArticle(articleDTO, 0);
        });
    return articleIPage;
  }

  @Override
  public List<ArticleDTO> findArticlesByTagName(Page<?> page, String name) {
    List<ArticleDTO> articleDTOS = articleMapper.selectArticlesByTagName(page, name);
    return articleDTOS;
  }

  @Override
  public List<ArticleDTO> findUserArticlesByIdUser(Page<?> page, Integer idUser) {
    List<ArticleDTO> list = articleMapper.selectUserArticles(page, idUser);
    list.forEach(
        article -> {
          genArticle(article, 0);
        });
    return list;
  }

  @Override
  @Transactional(rollbackFor = {UnsupportedEncodingException.class, BaseApiException.class})
  public Map postArticle(ArticleDTO article, HttpServletRequest request)
      throws UnsupportedEncodingException, BaseApiException {
    Map map = new HashMap(1);
    if (StringUtils.isBlank(article.getArticleTitle())) {
      map.put("message", "标题不能为空！");
      return map;
    }
    if (StringUtils.isBlank(article.getArticleContent())) {
      map.put("message", "正文不能为空！");
      return map;
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
        map.put("message", StringEscapeUtils.unescapeJava(reservedTag) + "标签为系统保留标签!");
        return map;
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
      if (!user.getIdUser().equals(newArticle.getArticleAuthorId())) {
        map.put("message", "非法访问！");
        return map;
      }
      newArticle.setArticleTitle(articleTitle);
      newArticle.setArticleTags(articleTags);
      newArticle.setArticleStatus(article.getArticleStatus());
      newArticle.setUpdatedTime(new Date());
      articleMapper.updateArticleContent(
          newArticle.getIdArticle(), articleContent, articleContentHtml);
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

    map.put("id", newArticle.getIdArticle());
    return map;
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
  public Map delete(Integer id) {
    Map<String, String> map = new HashMap(1);
    // 判断是否有评论
    boolean isHavComment = articleMapper.existsCommentWithPrimaryKey(id);
    if (isHavComment) {
      map.put("message", "已有评论的文章不允许删除!");
    } else {
      // 删除关联数据(作品集关联关系,标签关联关系)
      deleteLinkedData(id);
      // 删除文章
      if (!removeById(id)) {
        map.put("message", "删除失败!");
      }
    }
    return map;
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
  public Map share(Integer id) throws BaseApiException {
    Article article = getById(id);
    User user = UserUtils.getCurrentUserByToken();
    StringBuilder shareUrl = new StringBuilder(article.getArticlePermalink());
    shareUrl.append("?s=").append(user.getNickname());
    Map map = new HashMap(1);
    map.put("shareUrl", shareUrl);
    return map;
  }

  @Override
  public List<ArticleDTO> findDrafts(Page<?> page) throws BaseApiException {
    User user = UserUtils.getCurrentUserByToken();
    List<ArticleDTO> list = articleMapper.selectDrafts(page, user.getIdUser());
    list.forEach(
        article -> {
          genArticle(article, 0);
        });
    return list;
  }

  @Override
  public List<ArticleDTO> findArticlesByIdPortfolio(Page<?> page, Integer idPortfolio) {
    List<ArticleDTO> list = articleMapper.selectArticlesByIdPortfolio(page, idPortfolio);
    list.forEach(
        article -> {
          genArticle(article, 0);
        });
    return list;
  }

  @Override
  public List<ArticleDTO> selectUnbindArticles(
      Integer idPortfolio, String searchText, Integer idUser) {
    List<ArticleDTO> list =
        articleMapper.selectUnbindArticlesByIdPortfolio(idPortfolio, searchText, idUser);
    list.forEach(
        article -> {
          genArticle(article, 0);
        });
    return list;
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public Map updateTags(Integer idArticle, String tags)
      throws UnsupportedEncodingException, BaseApiException {
    Map map = new HashMap(2);
    Article article = getById(idArticle);
    if (Objects.nonNull(article)) {
      article.setArticleTags(tags);
      articleMapper.updateArticleTags(idArticle, tags);
      tagService.saveTagArticle(article, "");
      map.put("success", true);
    } else {
      map.put("success", false);
      map.put("message", "更新失败,文章不存在!");
    }
    return map;
  }

  @Override
  public Map updatePerfect(Integer idArticle, String articlePerfect) {
    Map map = new HashMap(2);
    int result = articleMapper.updatePerfect(idArticle, articlePerfect);
    if (result == 0) {
      map.put("success", false);
      map.put("message", "设置优选文章失败!");
    } else {
      map.put("success", true);
    }
    return map;
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
