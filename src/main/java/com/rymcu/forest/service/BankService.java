package com.rymcu.forest.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rymcu.forest.dto.BankDTO;
import com.rymcu.forest.entity.Bank;

import java.util.List;

/** @author ronger */
public interface BankService extends IService<Bank> {

  List<BankDTO> findBanks(Page<?> page);
}
