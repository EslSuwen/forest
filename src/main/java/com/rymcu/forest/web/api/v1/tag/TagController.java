package com.rymcu.forest.web.api.v1.tag;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rymcu.forest.dto.ArticleDTO;
import com.rymcu.forest.dto.result.Result;
import com.rymcu.forest.service.ArticleService;
import com.rymcu.forest.service.TagService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/** @author ronger */
@RestController
@RequestMapping("/api/tag")
public class TagController {

  @Resource private ArticleService articleService;
  @Resource private TagService tagService;

  @GetMapping("/{name}")
  public Result<List<ArticleDTO>> articles(
      @RequestParam(defaultValue = "0") Integer page,
      @RequestParam(defaultValue = "10") Integer rows,
      @PathVariable String name) {
    List<ArticleDTO> list = articleService.findArticlesByTagName(new Page<>(page, rows), name);
    return Result.OK(list);
  }

  @GetMapping("/tags")
  public Result<?> tags() {
    return Result.OK(tagService.findTagLabels());
  }
}
