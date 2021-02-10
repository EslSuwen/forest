package com.rymcu.forest.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

/** @author ronger */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@TableName("forest_currency_rule")
public class CurrencyRule implements Serializable, Cloneable {
  /** 主键 */
  @TableId(value = "id", type = IdType.AUTO)
  private Integer idCurrencyRule;
  /** 规则名称 */
  private String ruleName;
  /** 规则标志(与枚举变量对应) */
  private String ruleSign;
  /** 规则描述 */
  private String ruleDescription;
  /** 金额 */
  private BigDecimal money;
  /** 奖励(0)/消耗(1)状态 */
  private String awardStatus;
  /** 上限金额 */
  private BigDecimal maximumMoney;
  /** 重复(0: 不重复,单位:天) */
  private Integer repeatDays;
  /** 状态 */
  private String status;
}
