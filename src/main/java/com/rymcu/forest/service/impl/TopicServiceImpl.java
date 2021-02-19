package com.rymcu.forest.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rymcu.forest.dto.admin.TopicDTO;
import com.rymcu.forest.dto.admin.TopicTagDTO;
import com.rymcu.forest.dto.result.Result;
import com.rymcu.forest.entity.Tag;
import com.rymcu.forest.entity.Topic;
import com.rymcu.forest.mapper.TopicMapper;
import com.rymcu.forest.service.TopicService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/** @author ronger */
@Service
public class TopicServiceImpl extends ServiceImpl<TopicMapper, Topic> implements TopicService {

  @Resource private TopicMapper topicMapper;

  @Override
  public List<Topic> findTopicNav() {
    return topicMapper.selectTopicNav();
  }

  @Override
  public Topic findTopicByTopicUri(String topicUri) {
    return getOne(new LambdaQueryWrapper<Topic>().eq(Topic::getTopicUri, topicUri));
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public Result<?> saveTopic(Topic topic) {
    if (topic.getIdTopic() == null) {
      if (StringUtils.isBlank(topic.getTopicTitle())) {
        return Result.error("标签名不能为空!");
      } else {
        if (!list(new LambdaQueryWrapper<Topic>().eq(Topic::getTopicTitle, topic.getTopicTitle()))
            .isEmpty()) {
          return Result.error("专题 '" + topic.getTopicTitle() + "' 已存在!");
        }
      }
      Topic newTopic = new Topic();
      newTopic.setTopicTitle(topic.getTopicTitle());
      newTopic.setTopicUri(topic.getTopicUri());
      newTopic.setTopicIconPath(topic.getTopicIconPath());
      newTopic.setTopicNva(topic.getTopicNva());
      newTopic.setTopicStatus(topic.getTopicStatus());
      newTopic.setTopicSort(topic.getTopicSort());
      newTopic.setTopicDescription(topic.getTopicDescription());
      newTopic.setTopicDescriptionHtml(topic.getTopicDescriptionHtml());
      newTopic.setCreatedTime(new Date());
      newTopic.setUpdatedTime(topic.getCreatedTime());
      save(newTopic);
    } else {
      topic.setCreatedTime(new Date());
      topicMapper.update(
          topic.getIdTopic(),
          topic.getTopicTitle(),
          topic.getTopicUri(),
          topic.getTopicIconPath(),
          topic.getTopicNva(),
          topic.getTopicStatus(),
          topic.getTopicSort(),
          topic.getTopicDescription(),
          topic.getTopicDescriptionHtml());
    }
    return Result.OK(topic);
  }

  @Override
  public IPage<Tag> findUnbindTagsById(Page<?> page, Integer idTopic, String tagTitle) {
    if (StringUtils.isBlank(tagTitle)) {
      tagTitle = "";
    }
    return baseMapper.selectUnbindTagsById(page, idTopic, tagTitle);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public Result<?> bindTopicTag(TopicTagDTO topicTag) {
    Integer result = baseMapper.insertTopicTag(topicTag.getIdTopic(), topicTag.getIdTag());
    return result != 0 ? Result.OK(topicTag) : Result.error("操作失败!");
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public Result<?> unbindTopicTag(TopicTagDTO topicTag) {
    Integer result = baseMapper.deleteTopicTag(topicTag.getIdTopic(), topicTag.getIdTag());
    return result != 0 ? Result.OK(topicTag) : Result.error("操作失败!");
  }

  @Override
  public Result<?> findTagsByTopicUri(Page<?> page, String topicUri) {
    TopicDTO topic = baseMapper.selectTopicByTopicUri(topicUri);
    if (topic == null) {
      return Result.error("专题不存在！");
    }
    return Result.OK(baseMapper.selectTopicTag(page, topic.getIdTopic()));
  }
}
