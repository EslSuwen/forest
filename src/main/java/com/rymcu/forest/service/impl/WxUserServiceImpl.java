package com.rymcu.forest.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rymcu.forest.entity.WxUser;
import com.rymcu.forest.mapper.WxUserMapper;
import com.rymcu.forest.service.WxUserService;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/** @author ronger */
@Service
public class WxUserServiceImpl extends ServiceImpl<WxUserMapper, WxUser> implements WxUserService {

  @Resource private WxUserMapper wxUserMapper;

  @Override
  public WxUser saveUser(WxMpUser wxMpUser, String appId) {
    WxUser searchWxUser = new WxUser();
    /*    if (StringUtils.isBlank(wxMpUser.getUnionId())) {
      searchWxUser.setUnionId(wxMpUser.getUnionId());
    } else {
      searchWxUser.setAppId(appId);
      searchWxUser.setOpenId(searchWxUser.getOpenId());
    }
    List<WxUser> wxUsers = wxUserMapper.select(searchWxUser);
    WxUser wxUser;
    if (wxUsers.isEmpty()) {
      wxUser = new WxUser();
      wxUser.setAppId(appId);
      wxUser = copyWxUser(wxMpUser, wxUser);
      wxUserMapper.insertSelective(wxUser);
    } else {
      wxUser = wxUsers.get(0);
      wxUser = copyWxUser(wxMpUser, wxUser);
      wxUserMapper.updateByPrimaryKeySelective(wxUser);
    }
    return wxUser;*/
    return null;
  }

  private WxUser copyWxUser(WxMpUser wxMpUser, WxUser wxUser) {
    wxUser.setNickname(wxMpUser.getNickname());
    wxUser.setHeadImgUrl(wxMpUser.getHeadImgUrl());
    wxUser.setCountry(wxMpUser.getCountry());
    wxUser.setProvince(wxMpUser.getProvince());
    wxUser.setCity(wxMpUser.getCity());
    wxUser.setSex(wxMpUser.getSex());
    wxUser.setSubscribe(wxMpUser.getSubscribe());
    wxUser.setSubscribeTime(wxMpUser.getSubscribeTime());
    wxUser.setUnionId(wxMpUser.getUnionId());
    wxUser.setOpenId(wxMpUser.getOpenId());
    wxUser.setLanguage(wxMpUser.getLanguage());
    wxUser.setSexDesc(wxMpUser.getSexDesc());
    return wxUser;
  }
}
