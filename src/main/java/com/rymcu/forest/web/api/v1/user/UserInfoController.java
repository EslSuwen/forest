package com.rymcu.forest.web.api.v1.user;

import com.rymcu.forest.dto.ChangeEmailDTO;
import com.rymcu.forest.dto.UpdatePasswordDTO;
import com.rymcu.forest.dto.UserInfoDTO;
import com.rymcu.forest.dto.result.Result;
import com.rymcu.forest.entity.UserExtend;
import com.rymcu.forest.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

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
  public Result<?> updateEmail(@RequestBody ChangeEmailDTO changeEmailDTO) {
    return userService.updateEmail(changeEmailDTO);
  }

  @PatchMapping("/update-password")
  public Result<?> updatePassword(@RequestBody UpdatePasswordDTO updatePasswordDTO) {
    return userService.updatePassword(updatePasswordDTO);
  }
}
