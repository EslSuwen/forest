package com.rymcu.forest.web.api.v1.user;

import com.rymcu.forest.core.result.GlobalResult;
import com.rymcu.forest.core.result.GlobalResultGenerator;
import com.rymcu.forest.dto.ChangeEmailDTO;
import com.rymcu.forest.dto.UpdatePasswordDTO;
import com.rymcu.forest.dto.UserInfoDTO;
import com.rymcu.forest.dto.result.Result;
import com.rymcu.forest.entity.UserExtend;
import com.rymcu.forest.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

/** @author ronger */
@RestController
@RequestMapping("/api/v1/user-info")
public class UserInfoController {

  @Resource private UserService userService;

  @GetMapping("/detail/{idUser}")
  public Result<?> detail(@PathVariable Integer idUser) {
    return userService.findUserInfo(idUser);
  }

  @GetMapping("/check-nickname")
  public Result<?> checkNickname(@RequestParam Integer idUser, @RequestParam String nickname) {
    return userService.checkNickname(idUser, nickname);
  }

  @PatchMapping("/update")
  public Result<?> updateUserInfo(@RequestBody UserInfoDTO user) {
    return userService.updateUserInfo(user);
  }

  @PatchMapping("/update-extend")
  public Result<?> updateUserExtend(@RequestBody UserExtend userExtend) {
    return userService.updateUserExtend(userExtend);
  }

  @PatchMapping("/update-email")
  public GlobalResult updateEmail(@RequestBody ChangeEmailDTO changeEmailDTO) {
    Map map = userService.updateEmail(changeEmailDTO);
    return GlobalResultGenerator.genSuccessResult(map);
  }

  @PatchMapping("/update-password")
  public GlobalResult updatePassword(@RequestBody UpdatePasswordDTO updatePasswordDTO) {
    Map map = userService.updatePassword(updatePasswordDTO);
    return GlobalResultGenerator.genSuccessResult(map);
  }
}
