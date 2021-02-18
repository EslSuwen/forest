package com.rymcu.forest.web.api.v1.notification;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rymcu.forest.core.result.GlobalResult;
import com.rymcu.forest.core.result.GlobalResultGenerator;
import com.rymcu.forest.dto.result.Result;
import com.rymcu.forest.entity.Notification;
import com.rymcu.forest.entity.User;
import com.rymcu.forest.service.NotificationService;
import com.rymcu.forest.util.UserUtils;
import com.rymcu.forest.web.api.v1.exception.BaseApiException;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

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
  public Result<?> unreadNotification(
      @RequestParam(defaultValue = "0") Integer page,
      @RequestParam(defaultValue = "10") Integer rows)
      throws BaseApiException {
        User user = UserUtils.getCurrentUserByToken();
    return Result.OK(notificationService.findUnreadNotifications(new Page<>(page,rows), user.getIdUser()));
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
