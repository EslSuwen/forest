package com.rymcu.forest.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rymcu.forest.entity.Notification;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/** @author ronger */
public interface NotificationMapper extends BaseMapper<Notification> {

  /**
   * 获取未读通知数据
   *
   * @param page
   * @param idUser
   * @return
   */
  IPage<Notification> selectUnreadNotifications(Page<?> page, @Param("idUser") Integer idUser);

  /**
   * 获取消息数据
   *
   * @param idUser
   * @return
   */
  List<Notification> selectNotifications(@Param("idUser") Integer idUser);

  /**
   * 获取消息数据
   *
   * @param idUser
   * @param dataId
   * @param dataType
   * @return
   */
  Notification selectNotification(
      @Param("idUser") Integer idUser,
      @Param("dataId") Integer dataId,
      @Param("dataType") String dataType);

  /**
   * 创建消息通知
   *
   * @param idUser
   * @param dataId
   * @param dataType
   * @param dataSummary
   * @return
   */
  Integer insertNotification(
      @Param("idUser") Integer idUser,
      @Param("dataId") Integer dataId,
      @Param("dataType") String dataType,
      @Param("dataSummary") String dataSummary);

  /**
   * 标记消息已读
   *
   * @param id
   * @return
   */
  Integer readNotification(@Param("id") Integer id);
}
