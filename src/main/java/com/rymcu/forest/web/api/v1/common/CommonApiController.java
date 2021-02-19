package com.rymcu.forest.web.api.v1.common;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rymcu.forest.core.service.log.annotation.VisitLogger;
import com.rymcu.forest.dto.*;
import com.rymcu.forest.dto.result.Result;
import com.rymcu.forest.entity.User;
import com.rymcu.forest.service.*;
import com.rymcu.forest.util.UserUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.mail.MessagingException;

/** @author ronger */
@RestController
@RequestMapping("/api/v1/console")
public class CommonApiController {

  @Resource private JavaMailService javaMailService;
  @Resource private UserService userService;
  @Resource private ArticleService articleService;
  @Resource private PortfolioService portfolioService;
  @Resource private SearchService searchService;

  @GetMapping("/get-email-code")
  public Result<?> getEmailCode(@RequestParam("email") String email) throws MessagingException {
    User user = userService.findByAccount(email);
    if (user != null) {
      return Result.error("该邮箱已被注册！");
    }
    Integer result = javaMailService.sendEmailCode(email);
    return result != 0 ? Result.OK("验证码已发送至邮箱！") : Result.error("发送失败，请稍后再试！");
  }

  @GetMapping("/get-forget-password-email")
  public Result<?> getForgetPasswordEmail(@RequestParam("email") String email)
      throws MessagingException {
    User user = userService.findByAccount(email);
    if (user == null) {
      return Result.error("该邮箱未注册！");
    }
    Integer result = javaMailService.sendForgetPasswordEmail(email);
    return result != 0 ? Result.OK("验证码已发送至邮箱！") : Result.error("发送失败，请稍后再试！");
  }

  @PostMapping("/register")
  public Result<?> register(@RequestBody UserRegisterInfoDTO registerInfo) {
    return userService.register(
        registerInfo.getEmail(), registerInfo.getPassword(), registerInfo.getCode());
  }

  @PostMapping("/login")
  public Result<?> login(@RequestBody User user) {
    return userService.login(user.getAccount(), user.getPassword());
  }

  @GetMapping("/heartbeat")
  public Result<String> heartbeat() {
    return Result.OK("heartbeat");
  }

  @GetMapping("/articles")
  @VisitLogger
  public Result<IPage<ArticleDTO>> articles(
      @RequestParam(defaultValue = "0") Integer page,
      @RequestParam(defaultValue = "10") Integer rows,
      ArticleSearchDTO searchDTO) {
    IPage<ArticleDTO> list = articleService.findArticles(new Page<>(page, rows), searchDTO);
    return Result.OK(list);
  }

  @GetMapping("/article/{id}")
  @VisitLogger
  public Result<?> article(@PathVariable Integer id) {
    return Result.OK(articleService.findArticleDTOById(id, 1));
  }

  @GetMapping("/token/{token}")
  public Result<?> token(@PathVariable String token) {
    return Result.OK(UserUtils.getTokenUser(token));
  }

  @PatchMapping("/forget-password")
  public Result<?> forgetPassword(@RequestBody ForgetPasswordDTO forgetPassword) {
    return userService.forgetPassword(forgetPassword.getCode(), forgetPassword.getPassword());
  }

  @GetMapping("/portfolio/{id}")
  @VisitLogger
  public Result<PortfolioDTO> portfolio(@PathVariable Integer id) {
    return Result.OK(portfolioService.findPortfolioDTOById(id, 1));
  }

  @GetMapping("/portfolio/{id}/articles")
  public Result<IPage<ArticleDTO>> articles(
      @RequestParam(defaultValue = "0") Integer page,
      @RequestParam(defaultValue = "10") Integer rows,
      @PathVariable Integer id) {
    return Result.OK(articleService.findArticlesByIdPortfolio(new Page<>(page, rows), id));
  }
}
