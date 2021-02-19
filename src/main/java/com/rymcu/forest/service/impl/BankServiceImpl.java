package com.rymcu.forest.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rymcu.forest.dto.BankDTO;
import com.rymcu.forest.entity.Bank;
import com.rymcu.forest.mapper.BankMapper;
import com.rymcu.forest.service.BankService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/** @author ronger */
@Service
public class BankServiceImpl extends ServiceImpl<BankMapper, Bank> implements BankService {

  @Resource private BankMapper bankMapper;

  @Override
  public IPage<BankDTO> findBanks(Page<?> page) {
      return bankMapper.selectBanks(page);
  }
}
