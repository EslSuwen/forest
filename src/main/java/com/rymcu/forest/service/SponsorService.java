package com.rymcu.forest.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rymcu.forest.entity.Sponsor;

import java.util.Map;

/** @author ronger */
public interface SponsorService extends IService<Sponsor> {
  /**
   * 赞赏
   *
   * @param sponsor
   * @return
   * @throws Exception
   */
  Map sponsorship(Sponsor sponsor) throws Exception;
}
