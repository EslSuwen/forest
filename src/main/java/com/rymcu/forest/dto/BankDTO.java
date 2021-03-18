package com.rymcu.forest.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

/** @author ronger */
@Data
public class BankDTO {

  private Integer idBank;
  /** 银行名称 */
  private String bankName;
  /** 银行负责人 */
  private Integer bankOwner;
  /** 银行负责人 */
  private String bankOwnerName;
  /** 银行账户 */
  private String bankAccount;
  /** 账户余额 */
  private BigDecimal accountBalance;
  /** 银行描述 */
  private String bankDescription;
  /** 创建人 */
  private Integer createdBy;
  /** 创建时间 */
  @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private Date createdTime;
}
