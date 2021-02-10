package com.rymcu.forest.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rymcu.forest.entity.CurrencyRule;
import com.rymcu.forest.mapper.CurrencyRuleMapper;
import com.rymcu.forest.service.CurrencyRuleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/** @author ronger */
@Service
public class CurrencyRuleServiceImpl extends ServiceImpl<CurrencyRuleMapper, CurrencyRule>
    implements CurrencyRuleService {

  @Resource private CurrencyRuleMapper currencyRuleMapper;
}
