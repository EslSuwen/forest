package com.rymcu.forest.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rymcu.forest.dto.BankDTO;
import com.rymcu.forest.entity.Bank;

import java.util.List;

/** @author ronger */
public interface BankMapper extends BaseMapper<Bank> {
  /**
   * 查询银行列表数据
   *
   * @return
   */
  List<BankDTO> selectBanks(Page<?> page);
}
