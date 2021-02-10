package com.rymcu.forest.web.api.v1.user;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rymcu.forest.core.result.GlobalResult;
import com.rymcu.forest.core.result.GlobalResultGenerator;
import com.rymcu.forest.core.service.log.annotation.VisitLogger;
import com.rymcu.forest.dto.ArticleDTO;
import com.rymcu.forest.dto.PortfolioDTO;
import com.rymcu.forest.dto.UserDTO;
import com.rymcu.forest.dto.result.Result;
import com.rymcu.forest.entity.UserExtend;
import com.rymcu.forest.service.ArticleService;
import com.rymcu.forest.service.FollowService;
import com.rymcu.forest.service.PortfolioService;
import com.rymcu.forest.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/** @author ronger */
@RestController
@RequestMapping("/api/v1/user")
public class UserController {

  @Resource private UserService userService;
  @Resource private ArticleService articleService;
  @Resource private PortfolioService portfolioService;
  @Resource private FollowService followService;

  @GetMapping("/{nickname}")
  @VisitLogger
  public GlobalResult detail(@PathVariable String nickname) {
    UserDTO userDTO = userService.findUserDTOByNickname(nickname);
    return GlobalResultGenerator.genSuccessResult(userDTO);
  }

  @GetMapping("/{nickname}/articles")
  public Result<?> userArticles(
      @RequestParam(defaultValue = "0") Integer page,
      @RequestParam(defaultValue = "12") Integer rows,
      @PathVariable String nickname) {
    UserDTO userDTO = userService.findUserDTOByNickname(nickname);
    if (userDTO == null) {
      return Result.error("用户不存在！");
    }
    List<ArticleDTO> list =
        articleService.findUserArticlesByIdUser(new Page<>(page, rows), userDTO.getIdUser());
    return Result.OK(list);
  }

  @GetMapping("/{nickname}/portfolios")
  public Result<?> userPortfolios(
      @RequestParam(defaultValue = "0") Integer page,
      @RequestParam(defaultValue = "12") Integer rows,
      @PathVariable String nickname) {
    UserDTO userDTO = userService.findUserDTOByNickname(nickname);
    if (userDTO == null) {
      return Result.error("用户不存在！");
    }
    List<PortfolioDTO> list =
        portfolioService.findUserPortfoliosByUser(new Page<>(page, rows), userDTO);
    return Result.OK(list);
  }

  @GetMapping("/{nickname}/followers")
  public Result<?> userFollowers(
      @RequestParam(defaultValue = "0") Integer page,
      @RequestParam(defaultValue = "12") Integer rows,
      @PathVariable String nickname) {
    UserDTO userDTO = userService.findUserDTOByNickname(nickname);
    if (userDTO == null) {
      return Result.error("用户不存在！");
    }
    List<UserDTO> list = followService.findUserFollowersByUser(new Page<>(page, rows), userDTO);
    return Result.OK(list);
  }

  @GetMapping("/{nickname}/followings")
  public Result<?> userFollowings(
      @RequestParam(defaultValue = "0") Integer page,
      @RequestParam(defaultValue = "12") Integer rows,
      @PathVariable String nickname) {
    UserDTO userDTO = userService.findUserDTOByNickname(nickname);
    if (userDTO == null) {
      return Result.error("用户不存在！");
    }
    List<UserDTO> list = followService.findUserFollowingsByUser(new Page<>(page, rows), userDTO);
    return Result.OK(list);
  }

  @GetMapping("/{nickname}/user-extend")
  public GlobalResult userExtend(@PathVariable String nickname) {
    UserExtend userExtend = userService.selectUserExtendByNickname(nickname);
    return GlobalResultGenerator.genSuccessResult(userExtend);
  }
}
