package com.rymcu.forest.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rymcu.forest.entity.SpecialDay;
import com.rymcu.forest.mapper.SpecialDayMapper;
import com.rymcu.forest.service.SpecialDayService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/** @author ronger */
@Service
public class SpecialDayServiceImpl extends ServiceImpl<SpecialDayMapper, SpecialDay>
    implements SpecialDayService {

  @Resource private SpecialDayMapper specialDayMapper;
}
