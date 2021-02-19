package com.rymcu.forest.web.api.v1.notification;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rymcu.forest.dto.NotificationDTO;
import com.rymcu.forest.dto.result.Result;
import com.rymcu.forest.entity.User;
import com.rymcu.forest.service.NotificationService;
import com.rymcu.forest.util.UserUtils;
import com.rymcu.forest.web.api.v1.exception.BaseApiException;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 消息通知
 *
 * @author ronger
 */
@RestController
@RequestMapping("/api/v1/notification")
public class NotificationController {

  @Resource private NotificationService notificationService;

  @GetMapping("/all")
  public Result<IPage<NotificationDTO>> notifications(
      @RequestParam(defaultValue = "0") Integer page,
      @RequestParam(defaultValue = "10") Integer rows)
      throws BaseApiException {
    User user = UserUtils.getCurrentUserByToken();
    return Result.OK(
        notificationService.findNotifications(new Page<>(page, rows), user.getIdUser()));
  }

  @GetMapping("/unread")
  public Result<?> unreadNotification(
      @RequestParam(defaultValue = "0") Integer page,
      @RequestParam(defaultValue = "10") Integer rows)
      throws BaseApiException {
    User user = UserUtils.getCurrentUserByToken();
    return Result.OK(
        notificationService.findUnreadNotifications(new Page<>(page, rows), user.getIdUser()));
  }

  @PutMapping("/read/{id}")
  public Result<?> read(@PathVariable Integer id) {
    Integer result = notificationService.readNotification(id);
    return result != 0 ? Result.OK("标记已读成功") : Result.error("标记已读失败");
  }
}
