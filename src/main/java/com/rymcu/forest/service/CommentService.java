package com.rymcu.forest.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rymcu.forest.dto.CommentDTO;
import com.rymcu.forest.entity.Comment;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/** @author ronger */
public interface CommentService extends IService<Comment> {

  List<CommentDTO> getArticleComments(Integer idArticle);

  Map postComment(Comment comment, HttpServletRequest request);
}
