package com.rymcu.forest.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rymcu.forest.dto.BankAccountDTO;
import com.rymcu.forest.entity.BankAccount;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/** @author ronger */
public interface BankAccountMapper extends BaseMapper<BankAccount> {
  /**
   * 查询银行账户
   *
   *
   * @param page
   * @param bankName
   * @param accountOwnerName
   * @param bankAccount
   * @return
   */
  List<BankAccountDTO> selectBankAccounts(
      Page<?> page,
      @Param("bankName") String bankName,
      @Param("accountOwnerName") String accountOwnerName,
      @Param("bankAccount") String bankAccount);

  /**
   * 获取银行账户信息
   *
   * @param idBank
   * @return
   */
  BankAccountDTO selectBankAccount(@Param("idBank") Integer idBank);

  /**
   * 获取当前最大卡号
   *
   * @return
   */
  String selectMaxBankAccount();
}
