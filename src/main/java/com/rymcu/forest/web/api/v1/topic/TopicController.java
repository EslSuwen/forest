package com.rymcu.forest.web.api.v1.topic;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rymcu.forest.core.service.log.annotation.VisitLogger;
import com.rymcu.forest.dto.ArticleDTO;
import com.rymcu.forest.dto.result.Result;
import com.rymcu.forest.entity.Topic;
import com.rymcu.forest.service.ArticleService;
import com.rymcu.forest.service.TopicService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/** @author ronger */
@RestController
@RequestMapping("/api/topic")
public class TopicController {
  @Resource private ArticleService articleService;
  @Resource private TopicService topicService;

  @GetMapping("/topic-nav")
  public Result<List<Topic>> topicNav() {
    return Result.OK(topicService.findTopicNav());
  }

  @GetMapping("/{name}")
  @VisitLogger
  public Result<IPage<ArticleDTO>> articles(
      @RequestParam(defaultValue = "0") Integer page,
      @RequestParam(defaultValue = "10") Integer rows,
      @PathVariable String name) {
    IPage<ArticleDTO> list = articleService.findArticlesByTopicUri(new Page<>(page, rows), name);
    return Result.OK(list);
  }
}
