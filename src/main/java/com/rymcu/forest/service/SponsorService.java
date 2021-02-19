package com.rymcu.forest.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rymcu.forest.dto.result.Result;
import com.rymcu.forest.entity.Sponsor;

/** @author ronger */
public interface SponsorService extends IService<Sponsor> {
  /**
   * 赞赏
   *
   * @param sponsor
   * @return
   * @throws Exception
   */
  Result<?> sponsorship(Sponsor sponsor) throws Exception;
}
