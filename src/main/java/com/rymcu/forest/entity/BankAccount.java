package com.rymcu.forest.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 银行账户
 *
 * @author ronger
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@TableName("forest_bank")
public class BankAccount {
  /** 主键 */
  @TableId(value = "id", type = IdType.AUTO)
  private Integer idBankAccount;
  /** 所属银行 */
  private Integer idBank;
  /** 银行账户 */
  private String bankAccount;
  /** 账户余额 */
  private BigDecimal accountBalance;
  /** 账户所有者 */
  private Integer accountOwner;
  /** 创建时间 */
  @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private Date createdTime;
  /** 账户类型 */
  private String accountType;
}
