package com.rymcu.forest.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rymcu.forest.core.service.redis.RedisService;
import com.rymcu.forest.dto.*;
import com.rymcu.forest.dto.result.Result;
import com.rymcu.forest.entity.Role;
import com.rymcu.forest.entity.User;
import com.rymcu.forest.entity.UserExtend;
import com.rymcu.forest.jwt.service.TokenManager;
import com.rymcu.forest.mapper.RoleMapper;
import com.rymcu.forest.mapper.UserExtendMapper;
import com.rymcu.forest.mapper.UserMapper;
import com.rymcu.forest.service.UserService;
import com.rymcu.forest.util.BeanCopierUtil;
import com.rymcu.forest.util.Utils;
import com.rymcu.forest.web.api.v1.common.UploadController;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.exceptions.TooManyResultsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author CodeGenerator
 * @date 2018/05/29
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
  @Resource private UserMapper userMapper;
  @Resource private RoleMapper roleMapper;
  @Resource private RedisService redisService;
  @Resource private TokenManager tokenManager;
  @Resource private UserExtendMapper userExtendMapper;

  private static final String AVATAR_SVG_TYPE = "1";
  private static final String DEFAULT_AVATAR = "https://static.rymcu.com/article/1578475481946.png";

  @Override
  public User findByAccount(String account) throws TooManyResultsException {
    return userMapper.findByAccount(account);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public Result<?> register(String email, String password, String code) {
    String vCode = redisService.get(email);
    if (StringUtils.isNotBlank(vCode)) {
      if (vCode.equals(code)) {
        User user = userMapper.findByAccount(email);
        if (user != null) {
          return Result.error("该邮箱已被注册！");
        } else {
          user = new User();
          String nickname = email.split("@")[0];
          nickname = checkNickname(nickname);
          user.setNickname(nickname);
          user.setAccount(nickname);
          user.setEmail(email);
          user.setPassword(Utils.entryptPassword(password));
          user.setCreatedTime(new Date());
          user.setUpdatedTime(user.getCreatedTime());
          user.setAvatarUrl(DEFAULT_AVATAR);
          save(user);
          user = userMapper.findByAccount(email);
          Role role = roleMapper.selectRoleByInputCode("user");
          userMapper.insertUserRole(user.getIdUser(), role.getIdRole());
          redisService.delete(email);
          return Result.OK(1);
        }
      }
    }
    return Result.error("验证码无效！");
  }

  private String checkNickname(String nickname) {
    nickname = formatNickname(nickname);
    Integer result = userMapper.selectCountByNickName(nickname);
    if (result > 0) {
      StringBuilder stringBuilder = new StringBuilder(nickname);
      return checkNickname(stringBuilder.append("_").append(System.currentTimeMillis()).toString());
    }
    return nickname;
  }

  @Override
  public Result<?> login(String account, String password) {
    User user = userMapper.findByAccount(account);
    if (user != null) {
      if (Utils.comparePwd(password, user.getPassword())) {
        userMapper.updateLastLoginTime(user.getIdUser());
        TokenUser tokenUser = new TokenUser();
        BeanCopierUtil.copy(user, tokenUser);
        tokenUser.setToken(tokenManager.createToken(account));
        tokenUser.setWeights(userMapper.selectRoleWeightsByUser(user.getIdUser()));
        return Result.OK(tokenUser);
      } else {
        return Result.error("密码错误！");
      }
    } else {
      return Result.error("该账号不存在！");
    }
  }

  @Override
  public UserDTO findUserDTOByNickname(String nickname) {
    return userMapper.selectUserDTOByNickname(nickname);
  }

  @Override
  public Result<?> forgetPassword(String code, String password) {
    String account = redisService.get(code);
    System.out.println("account:\n" + account);
    if (StringUtils.isBlank(account)) {
      return Result.error("链接已失效");
    } else {
      userMapper.updatePasswordByAccount(account, Utils.entryptPassword(password));
      return Result.OK("修改成功，正在跳转登录登陆界面！", 1);
    }
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public Result<?> updateUserRole(Integer idUser, Integer idRole) {
    Integer result = userMapper.updateUserRole(idUser, idRole);
    return result != 0 ? Result.OK() : Result.error("更新失败!");
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public Result<?> updateStatus(Integer idUser, String status) {
    Integer result = userMapper.updateStatus(idUser, status);
    return result != 0 ? Result.OK() : Result.error("更新失败!");
  }

  @Override
  public Result<?> findUserInfo(Integer idUser) {
    UserInfoDTO user = userMapper.selectUserInfo(idUser);
    if (user == null) {
      return Result.error("用户不存在!");
    } else {
      UserExtend userExtend = userExtendMapper.selectById(user.getIdUser());
      if (Objects.isNull(userExtend)) {
        userExtend = new UserExtend();
        userExtend.setIdUser(user.getIdUser());
        userExtendMapper.insert(userExtend);
      }
      Map<String, Object> map = new HashMap<>(2);
      map.put("user", user);
      map.put("userExtend", userExtend);
      return Result.OK(map);
    }
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public Result<?> updateUserInfo(UserInfoDTO user) {
    user.setNickname(formatNickname(user.getNickname()));
    Integer number = userMapper.checkNicknameByIdUser(user.getIdUser(), user.getNickname());
    if (number > 0) {
      return Result.error("该昵称已使用!");
    }
    if (StringUtils.isNotBlank(user.getAvatarType())
        && AVATAR_SVG_TYPE.equals(user.getAvatarType())) {
      String avatarUrl = UploadController.uploadBase64File(user.getAvatarUrl(), 0);
      user.setAvatarUrl(avatarUrl);
      user.setAvatarType("0");
    }
    Integer result =
        userMapper.updateUserInfo(
            user.getIdUser(),
            user.getNickname(),
            user.getAvatarType(),
            user.getAvatarUrl(),
            user.getEmail(),
            user.getPhone(),
            user.getSignature(),
            user.getSex());
    if (result == 0) {
      return Result.error("操作失败!");
    }
    return Result.OK(user);
  }

  private String formatNickname(String nickname) {
    return nickname.replaceAll("\\.", "");
  }

  @Override
  public Result<?> checkNickname(Integer idUser, String nickname) {
    Integer number = userMapper.checkNicknameByIdUser(idUser, nickname);
    if (number > 0) {
      Result.error("该昵称已使用!");
    }
    return Result.OK();
  }

  @Override
  public Integer findRoleWeightsByUser(Integer idUser) {
    return userMapper.selectRoleWeightsByUser(idUser);
  }

  @Override
  public Author selectAuthor(Integer idUser) {
    return userMapper.selectAuthor(idUser);
  }

  @Override
  public Result<?> updateUserExtend(UserExtend userExtend) {
    int result = userExtendMapper.updateById(userExtend);
    if (result == 0) {
      return Result.error("操作失败!");
    }
    return Result.OK(userExtend);
  }

  @Override
  public UserExtend selectUserExtendByNickname(String nickname) {
    return userExtendMapper.selectUserExtendByNickname(nickname);
  }

  @Override
  public Result<?> updateEmail(ChangeEmailDTO changeEmailDTO) {
    Integer idUser = changeEmailDTO.getIdUser();
    String email = changeEmailDTO.getEmail();
    String code = changeEmailDTO.getCode();
    String vCode = redisService.get(email);
    if (StringUtils.isNotBlank(vCode)) {
      if (vCode.equals(code)) {
        userMapper.updateEmail(idUser, email);
        return Result.OK(email);
      }
    }
    return Result.error("验证码无效！");
  }

  @Override
  public Result<?> updatePassword(UpdatePasswordDTO updatePasswordDTO) {
    String password = Utils.entryptPassword(updatePasswordDTO.getPassword());
    userMapper.updatePasswordById(updatePasswordDTO.getIdUser(), password);
    return Result.OK("修改成功");
  }
}
