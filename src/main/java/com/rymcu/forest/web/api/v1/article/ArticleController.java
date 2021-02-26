package com.rymcu.forest.web.api.v1.article;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rymcu.forest.dto.ArticleDTO;
import com.rymcu.forest.dto.result.Result;
import com.rymcu.forest.entity.Article;
import com.rymcu.forest.entity.ArticleThumbsUp;
import com.rymcu.forest.entity.Sponsor;
import com.rymcu.forest.service.ArticleService;
import com.rymcu.forest.service.ArticleThumbsUpService;
import com.rymcu.forest.service.CommentService;
import com.rymcu.forest.service.SponsorService;
import com.rymcu.forest.web.api.v1.exception.BaseApiException;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

/** @author ronger */
@RestController
@RequestMapping("/api/v1/article")
public class ArticleController {

  @Resource private ArticleService articleService;
  @Resource private CommentService commentService;
  @Resource private ArticleThumbsUpService articleThumbsUpService;
  @Resource private SponsorService sponsorService;

  @GetMapping("/list")
  public Result<?> listArticle(
      @RequestParam(defaultValue = "1") Integer pageNum,
      @RequestParam(defaultValue = "10") Integer pageSize) {
    return Result.OK(articleService.getArticleList(new Page<>(pageNum, pageSize)));
  }

  @GetMapping("/detail/{id}")
  public Result<ArticleDTO> detailArticle(
      @PathVariable Integer id, @RequestParam(defaultValue = "2") Integer type) {
    return Result.OK(articleService.findArticleDTOById(id, type));
  }

  @PutMapping("/update")
  public Result<Boolean> updateArticle(@RequestBody Article article) {
    return Result.OK(articleService.updateById(article));
  }

  @RequestMapping(
      value = "/post",
      method = {RequestMethod.POST, RequestMethod.PUT})
  public Result<?> postArticle(@RequestBody ArticleDTO article, HttpServletRequest request)
      throws BaseApiException, UnsupportedEncodingException {
    return articleService.postArticle(article, request);
  }

  @DeleteMapping("/delete/{id}")
  public Result<?> delete(@PathVariable Integer id) {
    return articleService.delete(id);
  }

  @GetMapping("/{id}/comments")
  public Result<?> commons(@PathVariable Integer id) {
    return Result.OK(commentService.getArticleComments(id));
  }

  @GetMapping("/drafts")
  public Result<IPage<ArticleDTO>> drafts(
      @RequestParam(defaultValue = "0") Integer page,
      @RequestParam(defaultValue = "10") Integer rows)
      throws BaseApiException {
    return Result.OK(articleService.findDrafts(new Page<>(page, rows)));
  }

  @GetMapping("/{id}/share")
  public Result<String> share(@PathVariable Integer id) throws BaseApiException {
    return Result.OK(articleService.share(id));
  }

  @PostMapping("/{id}/update-tags")
  public Result<?> updateTags(@PathVariable Integer id, @RequestBody Article article)
      throws BaseApiException, UnsupportedEncodingException {
    return articleService.updateTags(id, article.getArticleTags());
  }

  @PatchMapping("/update-perfect")
  public Result<?> updatePerfect(@RequestBody Article article) {
    return articleService.updatePerfect(article.getIdArticle(), article.getArticlePerfect());
  }

  @PostMapping("/thumbs-up")
  public Result<?> thumbsUp(@RequestBody ArticleThumbsUp articleThumbsUp) throws BaseApiException {
    return articleThumbsUpService.thumbsUp(articleThumbsUp);
  }

  @PostMapping("/sponsor")
  public Result<?> sponsor(@RequestBody Sponsor sponsor) throws Exception {
    return sponsorService.sponsorship(sponsor);
  }
}
