package com.rymcu.forest.web.api.v1.comment;

import com.rymcu.forest.dto.result.Result;
import com.rymcu.forest.entity.Comment;
import com.rymcu.forest.service.CommentService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/** @author ronger */
@RestController
@RequestMapping("/api/comment")
public class CommentController {

  @Resource private CommentService commentService;

  @PostMapping("/post")
  public Result<?> postComment(@RequestBody Comment comment, HttpServletRequest request) {
    return commentService.postComment(comment, request);
  }
}
