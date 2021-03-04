package com.rymcu.forest.web.api.v1.rule;

import com.rymcu.forest.dto.result.Result;
import com.rymcu.forest.service.CurrencyRuleService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/** @author ronger */
@RestController
@RequestMapping("/api/rule/currency")
public class CurrencyRuleController {

  @Resource private CurrencyRuleService currencyRuleService;

  @GetMapping("/list")
  public Result<?> list() {
    return Result.OK(currencyRuleService.list());
  }
}
