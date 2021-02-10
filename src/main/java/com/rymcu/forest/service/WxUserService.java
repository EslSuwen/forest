package com.rymcu.forest.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rymcu.forest.entity.WxUser;
import me.chanjar.weixin.mp.bean.result.WxMpUser;

/** @author ronger */
public interface WxUserService extends IService<WxUser> {

  WxUser saveUser(WxMpUser wxMpUser, String appId);
}
