package com.rymcu.forest.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rymcu.forest.dto.BankAccountDTO;
import com.rymcu.forest.dto.BankAccountSearchDTO;
import com.rymcu.forest.dto.TransactionRecordDTO;
import com.rymcu.forest.entity.BankAccount;
import com.rymcu.forest.mapper.BankAccountMapper;
import com.rymcu.forest.service.BankAccountService;
import com.rymcu.forest.service.TransactionRecordService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/** @author ronger */
@Service
public class BankAccountServiceImpl extends ServiceImpl<BankAccountMapper, BankAccount>
    implements BankAccountService {

  private static String DEFAULT_ACCOUNT_TYPE = "0";

  @Resource BankAccountMapper bankAccountMapper;
  @Resource TransactionRecordService transactionRecordService;

  @Override
  public List<BankAccountDTO> findBankAccounts(Page<?> page, BankAccountSearchDTO bankAccountSearchDTO) {
    List<BankAccountDTO> bankAccounts =
        bankAccountMapper.selectBankAccounts(page,
                bankAccountSearchDTO.getBankName(),
                bankAccountSearchDTO.getAccountOwnerName(), bankAccountSearchDTO.getBankAccount());
    return bankAccounts;
  }

  @Override
  public BankAccountDTO findBankAccountByIdUser(Integer idUser) {
    BankAccount bankAccount = new BankAccount();
    bankAccount.setAccountOwner(idUser);
    bankAccount.setAccountType(DEFAULT_ACCOUNT_TYPE);
    List<BankAccount> bankAccounts =
        list(
            new LambdaQueryWrapper<BankAccount>()
                .eq(BankAccount::getAccountOwner, idUser)
                .eq(BankAccount::getAccountType, DEFAULT_ACCOUNT_TYPE));
    BankAccountDTO bankAccountDTO;
    if (Objects.nonNull(bankAccounts) && bankAccounts.size() > 0) {
      bankAccountDTO = bankAccountMapper.selectBankAccount(bankAccounts.get(0).getIdBankAccount());
    } else {
      bankAccount.setAccountBalance(new BigDecimal("0"));
      // 默认为社区发展与改革银行
      bankAccount.setIdBank(2);
      bankAccount.setBankAccount(nextBankAccount());
      bankAccount.setCreatedTime(new Date());
      save(bankAccount);
      bankAccountDTO = bankAccountMapper.selectBankAccount(bankAccount.getIdBankAccount());
    }
    // 查询交易记录
    List<TransactionRecordDTO> records =
        transactionRecordService.findTransactionRecords(bankAccountDTO.getBankAccount());
    bankAccountDTO.setTransactionRecords(records);
    return bankAccountDTO;
  }

  @Override
  public BankAccount findByBankAccount(String bankAccount) {
    BankAccount searchBankAccount = new BankAccount();
    searchBankAccount.setBankAccount(bankAccount);
    return getOne(
        new LambdaQueryWrapper<BankAccount>().eq(BankAccount::getBankAccount, bankAccount));
  }

  private String nextBankAccount() {
    String bankAccount = "600000001";
    String maxBankAccount = bankAccountMapper.selectMaxBankAccount();
    if (StringUtils.isNotBlank(maxBankAccount)) {
      BigDecimal bigDecimal = new BigDecimal(maxBankAccount).add(new BigDecimal("1"));
      return bigDecimal.toString();
    }
    return bankAccount;
  }
}
