package com.rymcu.forest.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rymcu.forest.dto.ArticleTagDTO;
import com.rymcu.forest.dto.LabelModel;
import com.rymcu.forest.dto.baidu.TagNlpDTO;
import com.rymcu.forest.entity.Article;
import com.rymcu.forest.entity.Tag;
import com.rymcu.forest.entity.User;
import com.rymcu.forest.mapper.ArticleMapper;
import com.rymcu.forest.mapper.TagMapper;
import com.rymcu.forest.service.TagService;
import com.rymcu.forest.util.BaiDuAipUtils;
import com.rymcu.forest.util.UserUtils;
import com.rymcu.forest.web.api.v1.exception.BaseApiException;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;
import java.util.stream.Collectors;

/** @author ronger */
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {

  @Resource private ArticleMapper articleMapper;

  @Override
  @Transactional(rollbackFor = {UnsupportedEncodingException.class, BaseApiException.class})
  public Integer saveTagArticle(Article article, String articleContentHtml)
      throws UnsupportedEncodingException, BaseApiException {
    User user = UserUtils.getCurrentUserByToken();
    String articleTags = article.getArticleTags();
    if (StringUtils.isNotBlank(articleTags)) {
      String[] tags = articleTags.split(",");
      List<ArticleTagDTO> articleTagDTOList = articleMapper.selectTags(article.getIdArticle());
      for (String s : tags) {
        boolean addTagArticle = false;
        boolean addUserTag = false;
        Tag tag = getOne(new LambdaQueryWrapper<Tag>().eq(Tag::getTagTitle, s));
        if (tag == null) {
          tag = new Tag();
          tag.setTagTitle(s);
          tag.setTagUri(URLEncoder.encode(tag.getTagTitle(), "UTF-8"));
          tag.setCreatedTime(new Date());
          tag.setUpdatedTime(tag.getCreatedTime());
          tag.setTagArticleCount(1);
          save(tag);
          addTagArticle = true;
          addUserTag = true;
        } else {
          int n = articleTagDTOList.size();
          for (int m = 0; m < n; m++) {
            ArticleTagDTO articleTag = articleTagDTOList.get(m);
            if (articleTag.getIdTag().equals(tag.getIdTag())) {
              articleTagDTOList.remove(articleTag);
              m--;
              n--;
            }
          }
          Integer count =
              baseMapper.selectCountTagArticleById(tag.getIdTag(), article.getIdArticle());
          if (count == 0) {
            tag.setTagArticleCount(tag.getTagArticleCount() + 1);
            updateById(tag);
            addTagArticle = true;
          }
          Integer countUserTag =
              baseMapper.selectCountUserTagById(user.getIdUser(), tag.getIdTag());
          if (countUserTag == 0) {
            addUserTag = true;
          }
        }
        articleTagDTOList.forEach(
            articleTagDTO -> {
              articleMapper.deleteUnusedArticleTag(articleTagDTO.getIdArticleTag());
            });
        if (addTagArticle) {
          baseMapper.insertTagArticle(tag.getIdTag(), article.getIdArticle());
        }
        if (addUserTag) {
          baseMapper.insertUserTag(tag.getIdTag(), user.getIdUser(), 1);
        }
      }
      return 1;
    } else {
      if (StringUtils.isNotBlank(articleContentHtml)) {
        String tags =
            Objects.requireNonNull(
                    BaiDuAipUtils.getKeywords(article.getArticleTitle(), articleContentHtml))
                .stream()
                .map(TagNlpDTO::getTag)
                .collect(Collectors.joining(","));
        if (tags.length() > 0) {
          article.setArticleTags(tags);
        } else {
          article.setArticleTags("待分类");
        }
        saveTagArticle(article, articleContentHtml);
      }
    }
    return 0;
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public Map cleanUnusedTag() {
    Map map = new HashMap(1);
    baseMapper.deleteUnusedTag();
    return map;
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public Map saveTag(Tag tag) {
    Map map = new HashMap(1);
    if (tag.getIdTag() == null) {
      if (StringUtils.isBlank(tag.getTagTitle())) {
        map.put("message", "标签名不能为空!");
        return map;
      } else {
        if (!list(new LambdaQueryWrapper<Tag>().eq(Tag::getTagTitle, tag.getTagTitle()))
            .isEmpty()) {
          map.put("message", "标签 '" + tag.getTagTitle() + "' 已存在!");
          return map;
        }
      }
      Tag newTag = new Tag();
      newTag.setTagTitle(tag.getTagTitle());
      newTag.setTagUri(tag.getTagUri());
      newTag.setTagIconPath(tag.getTagIconPath());
      newTag.setTagStatus(tag.getTagStatus());
      newTag.setTagDescription(tag.getTagDescription());
      newTag.setTagReservation(tag.getTagReservation());
      newTag.setCreatedTime(new Date());
      newTag.setUpdatedTime(tag.getCreatedTime());
      save(newTag);
    } else {
      tag.setUpdatedTime(new Date());
      baseMapper.update(
          tag.getIdTag(),
          tag.getTagUri(),
          tag.getTagIconPath(),
          tag.getTagStatus(),
          tag.getTagDescription(),
          tag.getTagReservation());
    }
    map.put("tag", tag);
    return map;
  }

  @Override
  public List<LabelModel> findTagLabels() {
    return baseMapper.selectTagLabels();
  }
}
