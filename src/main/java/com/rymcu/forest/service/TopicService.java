package com.rymcu.forest.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rymcu.forest.dto.admin.TopicTagDTO;
import com.rymcu.forest.dto.result.Result;
import com.rymcu.forest.entity.Tag;
import com.rymcu.forest.entity.Topic;

import java.util.List;

/** @author ronger */
public interface TopicService extends IService<Topic> {

  /**
   * 获取导航主题数据
   *
   * @return
   */
  List<Topic> findTopicNav();

  /**
   * 根据 topicUri 获取主题信息及旗下标签数据
   *
   * @param topicUri 主题 URI
   * @return
   */
  Topic findTopicByTopicUri(String topicUri);

  /**
   * 新增/更新主题信息
   *
   * @param topic 主题信息
   * @return
   */
  Result<?> saveTopic(Topic topic);

  /**
   * 查询未绑定标签
   *
   *
   * @param page
   * @param idTopic
   * @param tagTitle
   * @return
   */
  IPage<Tag> findUnbindTagsById(Page<?> page, Integer idTopic, String tagTitle);

  /**
   * 绑定标签
   *
   * @param topicTag
   * @return
   */
  Result<?> bindTopicTag(TopicTagDTO topicTag);

  /**
   * 取消绑定标签
   *
   * @param topicTag
   * @return
   */
  Result<?> unbindTopicTag(TopicTagDTO topicTag);

  /**
   * 获取主题下标签列表
   *
   * @param page
   * @param topicUri
   * @return
   */
  Result<?> findTagsByTopicUri(Page<?> page, String topicUri);
}
