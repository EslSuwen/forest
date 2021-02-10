package com.rymcu.forest.web.api.v1.notification;

import com.rymcu.forest.core.result.GlobalResult;
import com.rymcu.forest.core.result.GlobalResultGenerator;
import com.rymcu.forest.service.NotificationService;
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
  public GlobalResult notifications(
      @RequestParam(defaultValue = "0") Integer page,
      @RequestParam(defaultValue = "10") Integer rows)
      throws BaseApiException {
    /*    User user = UserUtils.getCurrentUserByToken();
    PageHelper.startPage(page, rows);
    List<NotificationDTO> list = notificationService.findNotifications(user.getIdUser());
    PageInfo<NotificationDTO> pageInfo = new PageInfo(list);
    Map map = Utils.getNotificationDTOsGlobalResult(pageInfo);*/
    return GlobalResultGenerator.genSuccessResult();
  }

  @GetMapping("/unread")
  public GlobalResult unreadNotification(
      @RequestParam(defaultValue = "0") Integer page,
      @RequestParam(defaultValue = "10") Integer rows)
      throws BaseApiException {
    /*    User user = UserUtils.getCurrentUserByToken();
    PageHelper.startPage(page, rows);
    List<Notification> list = notificationService.findUnreadNotifications(user.getIdUser());
    PageInfo<Notification> pageInfo = new PageInfo(list);
    Map map = Utils.getNotificationsGlobalResult(pageInfo);*/
    return GlobalResultGenerator.genSuccessResult();
  }

  @PutMapping("/read/{id}")
  public GlobalResult read(@PathVariable Integer id) {
    Integer result = notificationService.readNotification(id);
    if (result == 0) {
      return GlobalResultGenerator.genErrorResult("标记已读失败");
    }
    return GlobalResultGenerator.genSuccessResult("标记已读成功");
  }
}
