package com.rymcu.forest.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rymcu.forest.dto.BankAccountDTO;
import com.rymcu.forest.dto.BankAccountSearchDTO;
import com.rymcu.forest.entity.BankAccount;

import java.util.List;

/**
 * @author ronger
 */
public interface BankAccountService extends IService<BankAccount> {
    /**
     * 查询银行账户列表
     *
     * @param page
     * @param bankAccountSearchDTO
     * @return
     */
    List<BankAccountDTO> findBankAccounts(Page<?> page, BankAccountSearchDTO bankAccountSearchDTO);

    /**
     * 查询用户银行账户
     * @param idUser
     * @return
     */
    BankAccountDTO findBankAccountByIdUser(Integer idUser);

    /**
     * 根据账户查询银行账户信息
     * @param bankAccount
     * @return
     */
    BankAccount findByBankAccount(String bankAccount);
}
