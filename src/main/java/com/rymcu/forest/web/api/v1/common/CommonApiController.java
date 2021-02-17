package com.rymcu.forest.web.api.v1.common;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rymcu.forest.core.result.GlobalResult;
import com.rymcu.forest.core.result.GlobalResultGenerator;
import com.rymcu.forest.core.result.GlobalResultMessage;
import com.rymcu.forest.core.service.log.annotation.VisitLogger;
import com.rymcu.forest.dto.*;
import com.rymcu.forest.dto.result.Result;
import com.rymcu.forest.entity.User;
import com.rymcu.forest.service.*;
import com.rymcu.forest.util.UserUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** @author ronger */
@RestController
@RequestMapping("/api/v1/console")
public class CommonApiController {

  @Resource private JavaMailService javaMailService;
  @Resource private UserService userService;
  @Resource private ArticleService articleService;
  @Resource private PortfolioService portfolioService;
  @Resource private SearchService SearchService;

  @GetMapping("/get-email-code")
  public GlobalResult<Map<String, String>> getEmailCode(@RequestParam("email") String email)
      throws MessagingException {
    Map<String, String> map = new HashMap<>(1);
    map.put("message", GlobalResultMessage.SEND_SUCCESS.getMessage());
    User user = userService.findByAccount(email);
    if (user != null) {
      map.put("message", "该邮箱已被注册！");
    } else {
      Integer result = javaMailService.sendEmailCode(email);
      if (result == 0) {
        map.put("message", GlobalResultMessage.SEND_FAIL.getMessage());
      }
    }
    return GlobalResultGenerator.genSuccessResult(map);
  }

  @GetMapping("/get-forget-password-email")
  public GlobalResult<Map<Object, Object>> getForgetPasswordEmail(
      @RequestParam("email") String email) throws MessagingException {
    Map<Object, Object> map = new HashMap<>(1);
    map.put("message", GlobalResultMessage.SEND_SUCCESS.getMessage());
    User user = userService.findByAccount(email);
    if (user != null) {
      Integer result = javaMailService.sendForgetPasswordEmail(email);
      if (result == 0) {
        map.put("message", GlobalResultMessage.SEND_FAIL.getMessage());
      }
    } else {
      map.put("message", "该邮箱未注册！");
    }
    return GlobalResultGenerator.genSuccessResult(map);
  }

  @PostMapping("/register")
  public GlobalResult<Map> register(@RequestBody UserRegisterInfoDTO registerInfo) {
    Map map =
        userService.register(
            registerInfo.getEmail(), registerInfo.getPassword(), registerInfo.getCode());
    return GlobalResultGenerator.genSuccessResult(map);
  }

  @PostMapping("/login")
  public Result<?> login(@RequestBody User user) {
    return userService.login(user.getAccount(), user.getPassword());
  }

  @GetMapping("/heartbeat")
  public GlobalResult heartbeat() {
    return GlobalResultGenerator.genSuccessResult("heartbeat");
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
  public GlobalResult<Map<String, Object>> article(@PathVariable Integer id) {
    ArticleDTO articleDTO = articleService.findArticleDTOById(id, 1);
    Map<String, Object> map = new HashMap<>(1);
    map.put("article", articleDTO);
    return GlobalResultGenerator.genSuccessResult(map);
  }

  @GetMapping("/token/{token}")
  public GlobalResult<TokenUser> token(@PathVariable String token) {
    TokenUser tokenUser = UserUtils.getTokenUser(token);
    return GlobalResultGenerator.genSuccessResult(tokenUser);
  }

  @PatchMapping("/forget-password")
  public GlobalResult<Map> forgetPassword(@RequestBody ForgetPasswordDTO forgetPassword) {
    Map map = userService.forgetPassword(forgetPassword.getCode(), forgetPassword.getPassword());
    return GlobalResultGenerator.genSuccessResult(map);
  }

  @GetMapping("/portfolio/{id}")
  @VisitLogger
  public GlobalResult<Map<String, Object>> portfolio(@PathVariable Integer id) {
    PortfolioDTO portfolioDTO = portfolioService.findPortfolioDTOById(id, 1);
    Map<String, Object> map = new HashMap<>(1);
    map.put("portfolio", portfolioDTO);
    return GlobalResultGenerator.genSuccessResult(map);
  }

  @GetMapping("/portfolio/{id}/articles")
  public Result<List<ArticleDTO>> articles(
      @RequestParam(defaultValue = "0") Integer page,
      @RequestParam(defaultValue = "10") Integer rows,
      @PathVariable Integer id) {
    List<ArticleDTO> list = articleService.findArticlesByIdPortfolio(new Page<>(page, rows), id);
    return Result.OK(list);
  }

  @GetMapping("/initial-search")
  public GlobalResult initialSearch() {
    List<SearchModel> list = SearchService.initialSearch();
    return GlobalResultGenerator.genSuccessResult(list);
  }
}
