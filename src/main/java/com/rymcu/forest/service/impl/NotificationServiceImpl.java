package com.rymcu.forest.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rymcu.forest.dto.ArticleDTO;
import com.rymcu.forest.dto.Author;
import com.rymcu.forest.dto.NotificationDTO;
import com.rymcu.forest.entity.Comment;
import com.rymcu.forest.entity.Follow;
import com.rymcu.forest.entity.Notification;
import com.rymcu.forest.entity.User;
import com.rymcu.forest.mapper.NotificationMapper;
import com.rymcu.forest.service.*;
import com.rymcu.forest.util.BeanCopierUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/** @author ronger */
@Service
public class NotificationServiceImpl extends ServiceImpl<NotificationMapper, Notification>
    implements NotificationService {

  @Resource private NotificationMapper notificationMapper;
  @Resource private ArticleService articleService;
  @Resource private CommentService commentService;
  @Resource private UserService userService;
  @Resource private FollowService followService;

  @Value("${resource.domain:http://10.26.201.17:32240/static}")
  private String domain;

  private static final String unRead = "0";

  @Override
  public IPage<Notification> findUnreadNotifications(Page<?> page, Integer idUser) {
    return notificationMapper.selectUnreadNotifications(page, idUser);
  }

  @Override
  public IPage<NotificationDTO> findNotifications(Page<?> page, Integer idUser) {
    IPage<Notification> list = notificationMapper.selectNotifications(page, idUser);
    List<NotificationDTO> notifications = new ArrayList<>();
    list.getRecords()
        .forEach(
            notification -> {
              NotificationDTO notificationDTO = genNotification(notification);
              // 判断关联数据是否已删除
              if (Objects.nonNull(notificationDTO.getAuthor())) {
                notifications.add(notificationDTO);
              } else {
                // 关联数据已删除,且未读
                if (unRead.equals(notification.getHasRead())) {
                  notificationMapper.readNotification(notification.getIdNotification());
                }
                NotificationDTO dto = new NotificationDTO();
                dto.setDataSummary("该消息已被撤销!");
                dto.setDataType("-1");
                dto.setHasRead("1");
                dto.setCreatedTime(notification.getCreatedTime());
                notifications.add(dto);
              }
            });
    Page<NotificationDTO> pageResult = new Page<>(page.getCurrent(), page.getSize());
    pageResult.setRecords(notifications);
    return pageResult;
  }

  private NotificationDTO genNotification(Notification notification) {
    NotificationDTO notificationDTO = new NotificationDTO();
    BeanCopierUtil.copy(notification, notificationDTO);
    ArticleDTO article;
    Comment comment;
    User user;
    Follow follow;
    switch (notification.getDataType()) {
      case "0":
        // 系统公告/帖子
        article = articleService.findArticleDTOById(notification.getDataId(), 0);
        if (Objects.nonNull(article)) {
          notificationDTO.setDataTitle("系统公告");
          notificationDTO.setDataUrl(article.getArticlePermalink());
          user = userService.getById(article.getArticleAuthorId().toString());
          notificationDTO.setAuthor(genAuthor(user));
        }
        break;
      case "1":
        // 关注
        follow = followService.getById(notification.getDataId().toString());
        notificationDTO.setDataTitle("关注提醒");
        if (Objects.nonNull(follow)) {
          user = userService.getById(follow.getFollowerId().toString());
          notificationDTO.setDataUrl(getFollowLink(follow.getFollowingType(), user.getNickname()));
          notificationDTO.setAuthor(genAuthor(user));
        }
        break;
      case "2":
        // 回帖
        comment = commentService.getById(notification.getDataId().toString());
        article = articleService.findArticleDTOById(comment.getCommentArticleId(), 0);
        if (Objects.nonNull(article)) {
          notificationDTO.setDataTitle(article.getArticleTitle());
          notificationDTO.setDataUrl(comment.getCommentSharpUrl());
          user = userService.getById(comment.getCommentAuthorId().toString());
          notificationDTO.setAuthor(genAuthor(user));
        }
        break;
      case "3":
        // 关注用户发布文章
      case "4":
        // 关注文章更新
        article = articleService.findArticleDTOById(notification.getDataId(), 0);
        if (Objects.nonNull(article)) {
          notificationDTO.setDataTitle("关注通知");
          notificationDTO.setDataUrl(article.getArticlePermalink());
          user = userService.getById(article.getArticleAuthorId().toString());
          notificationDTO.setAuthor(genAuthor(user));
        }
        break;
      default:
        break;
    }
    return notificationDTO;
  }

  private String getFollowLink(String followingType, String id) {
    StringBuilder url = new StringBuilder();
    url.append(domain);
    switch (followingType) {
      case "0":
        url.append("/user/").append(id);
        break;
      default:
        url.append("/notification");
    }
    return url.toString();
  }

  private Author genAuthor(User user) {
    Author author = new Author();
    author.setUserNickname(user.getNickname());
    author.setUserAvatarURL(user.getAvatarUrl());
    author.setIdUser(user.getIdUser());
    return author;
  }

  @Override
  public Notification findNotification(Integer idUser, Integer dataId, String dataType) {
    return notificationMapper.selectNotification(idUser, dataId, dataType);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public Integer save(Integer idUser, Integer dataId, String dataType, String dataSummary) {
    return notificationMapper.insertNotification(idUser, dataId, dataType, dataSummary);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public Integer readNotification(Integer id) {
    return notificationMapper.readNotification(id);
  }
}
