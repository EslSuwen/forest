package com.rymcu.forest.web.api.v1.common;

import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.rymcu.forest.dto.TokenUser;
import com.rymcu.forest.entity.Role;
import com.rymcu.forest.entity.User;
import com.rymcu.forest.jwt.service.TokenManager;
import com.rymcu.forest.mapper.RoleMapper;
import com.rymcu.forest.mapper.UserMapper;
import com.rymcu.forest.service.UserService;
import com.rymcu.forest.util.BeanCopierUtil;
import com.rymcu.forest.util.GitLabSource;
import com.rymcu.forest.util.Utils;
import com.xkcoding.justauth.AuthRequestFactory;
import lombok.extern.slf4j.Slf4j;
import me.zhyd.oauth.enums.AuthResponseStatus;
import me.zhyd.oauth.enums.AuthUserGender;
import me.zhyd.oauth.model.AuthCallback;
import me.zhyd.oauth.model.AuthResponse;
import me.zhyd.oauth.model.AuthToken;
import me.zhyd.oauth.model.AuthUser;
import me.zhyd.oauth.request.AuthRequest;
import me.zhyd.oauth.utils.AuthStateUtils;
import me.zhyd.oauth.utils.UrlBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * ThirdLoginController 第三方登录控制器
 *
 * @author suwen
 * @date 2021/2/23 14:05
 */
@Controller
@RequestMapping("/api/thirdLogin")
@Slf4j
public class ThirdLoginController {

  @Resource private AuthRequestFactory factory;
  @Resource private UserService userService;
  @Resource private UserMapper userMapper;
  @Resource private RoleMapper roleMapper;
  @Resource private TokenManager tokenManager;

  @Value("${third.loginCallBackUrl}")
  private String loginCallBackUrl;

  @RequestMapping("/render/{source}")
  public void render(@PathVariable("source") String source, HttpServletResponse response)
      throws IOException {
    log.info("第三方登录进入render：" + source);
    AuthRequest authRequest = factory.get(source);
    String authorizeUrl = authRequest.authorize(AuthStateUtils.createState());
    // 替换gitlab私服验证验证
    authorizeUrl = authorizeUrl.replace("https://gitlab.com", "http://10.26.201.103:9000");
    log.info("第三方登录认证地址：" + authorizeUrl);
    response.sendRedirect(authorizeUrl);
  }

  @RequestMapping("/{source}/callback")
  public void loginThird(
      @PathVariable("source") String source,
      AuthCallback callback,
      HttpServletResponse httpServletResponse)
      throws IOException {
    log.info("第三方登录进入callback：" + source + " params：" + JSONObject.toJSONString(callback));
    AuthResponse<?> response = this.login(callback);
    log.info(JSONObject.toJSONString(response));
    if (response.getCode() == 2000) {
      JSONObject data = JSONObject.parseObject(JSONObject.toJSONString(response.getData()));
      String avatar = data.getString("avatar");
      String uuid = data.getString("uuid");
      User user;
      // 判断有没有这个人
      List<User> userList =
          userService.list(new LambdaQueryWrapper<User>().eq(User::getAccount, uuid));
      if (userList == null || userList.size() == 0) {
        // 否则直接创建新账号
        user = saveThirdUser(data);
        userService.save(user);
        // FIXME 测试时新建用户默认为管理员
        Role role = roleMapper.selectRoleByInputCode("admin");
        userMapper.insertUserRole(user.getIdUser(), role.getIdRole());
      } else {
        // 已存在更新用户数据
        user = userList.get(0);
        user.setAvatarUrl(avatar);
        userService.updateById(user);
      }
      // 生成token
      TokenUser token = saveToken(user);
      httpServletResponse.sendRedirect(
          StrUtil.format(
              "{}token={}&idUser={}&weights={}&nickname={}&avatarURL={}",
              loginCallBackUrl,
              token.getToken(),
              token.getIdUser(),
              token.getWeights(),
              URLUtil.encode(token.getNickname()),
              token.getAvatarUrl()));
    } else {
      httpServletResponse.sendRedirect(loginCallBackUrl + "loginFailed");
    }
  }

  /**
   * 创建新用户
   *
   * @param json 第三方登录信息
   */
  private User saveThirdUser(JSONObject json) {
    User user = new User();
    user.setNickname(json.getString("nickname"));
    user.setRealName(json.getString("username"));
    user.setAccount(json.getString("uuid"));
    user.setEmail(json.getString("email"));
    user.setPassword(Utils.entryptPassword("123456"));
    user.setCreatedTime(new Date());
    user.setUpdatedTime(user.getCreatedTime());
    user.setAvatarUrl(json.getString("avatar"));
    return user;
  }

  private TokenUser saveToken(User user) {
    userMapper.updateLastLoginTime(user.getIdUser());
    TokenUser tokenUser = new TokenUser();
    BeanCopierUtil.copy(user, tokenUser);
    tokenUser.setToken(tokenManager.createToken(user.getAccount()));
    tokenUser.setWeights(userMapper.selectRoleWeightsByUser(user.getIdUser()));
    return tokenUser;
  }

  public AuthResponse<?> login(AuthCallback authCallback) {
    AuthToken authToken = this.getAccessToken(authCallback);
    AuthUser user = this.getUserInfo(authToken);
    return AuthResponse.builder().code(AuthResponseStatus.SUCCESS.getCode()).data(user).build();
  }

  protected AuthUser getUserInfo(AuthToken authToken) {
    HttpResponse response = this.doGetUserInfo(authToken);
    JSONObject object = JSONObject.parseObject(response.body());
    return AuthUser.builder()
        .uuid(object.getString("id"))
        .username(object.getString("username"))
        .nickname(object.getString("name"))
        .avatar(object.getString("avatar_url"))
        .blog(object.getString("web_url"))
        .company(object.getString("organization"))
        .location(object.getString("location"))
        .email(object.getString("email"))
        .remark(object.getString("bio"))
        .gender(AuthUserGender.UNKNOWN)
        .token(authToken)
        .source(GitLabSource.userInfo)
        .build();
  }

  protected HttpResponse doGetUserInfo(AuthToken authToken) {
    return HttpRequest.get(this.userInfoUrl(authToken)).execute();
  }

  protected String userInfoUrl(AuthToken authToken) {
    return UrlBuilder.fromBaseUrl(GitLabSource.userInfo)
        .queryParam("access_token", authToken.getAccessToken())
        .build();
  }

  protected AuthToken getAccessToken(AuthCallback authCallback) {
    HttpResponse response = this.doPostAuthorizationCode(authCallback.getCode());
    JSONObject object = JSONObject.parseObject(response.body());
    return AuthToken.builder()
        .accessToken(object.getString("access_token"))
        .refreshToken(object.getString("refresh_token"))
        .idToken(object.getString("id_token"))
        .tokenType(object.getString("token_type"))
        .scope(object.getString("scope"))
        .build();
  }

  protected HttpResponse doPostAuthorizationCode(String code) {
    return HttpRequest.post(this.accessTokenUrl(code)).execute();
  }

  protected String accessTokenUrl(String code) {
    return UrlBuilder.fromBaseUrl(GitLabSource.accessToken)
        .queryParam("code", code)
        .queryParam("client_id", GitLabSource.clientId)
        .queryParam("client_secret", GitLabSource.secret)
        .queryParam("grant_type", "authorization_code")
        .queryParam("redirect_uri", GitLabSource.redirectUrl)
        .build();
  }
}
