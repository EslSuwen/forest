package com.rymcu.forest.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rymcu.forest.dto.PortfolioArticleDTO;
import com.rymcu.forest.dto.PortfolioDTO;
import com.rymcu.forest.dto.UserDTO;
import com.rymcu.forest.dto.result.Result;
import com.rymcu.forest.entity.Portfolio;
import com.rymcu.forest.web.api.v1.exception.BaseApiException;

import java.util.List;

/** @author ronger */
public interface PortfolioService extends IService<Portfolio> {

  /**
   * 查询用户作品集
   *
   *
   * @param page
   * @param userDTO
   * @return
   */
  IPage<PortfolioDTO> findUserPortfoliosByUser(Page<?> page, UserDTO userDTO);

  /**
   * 查询作品集
   *
   * @param idPortfolio
   * @param type
   * @return
   */
  PortfolioDTO findPortfolioDTOById(Integer idPortfolio, Integer type);

  /**
   * 保持/更新作品集
   *
   * @param portfolio
   * @throws BaseApiException
   * @return
   */
  Portfolio postPortfolio(Portfolio portfolio) throws BaseApiException;

  /**
   * 查询作品集下未绑定文章
   *
   * @param page
   * @param searchText
   * @param idPortfolio
   * @throws BaseApiException
   * @return
   */
  Result<?> findUnbindArticles(Page<?> page, String searchText, Integer idPortfolio)
      throws BaseApiException;

  /**
   * 绑定文章
   *
   * @param portfolioArticle
   * @return
   */
  Result<?> bindArticle(PortfolioArticleDTO portfolioArticle);

  /**
   * 更新文章排序号
   *
   * @param portfolioArticle
   * @return
   */
  Result<?> updateArticleSortNo(PortfolioArticleDTO portfolioArticle);

  /**
   * 取消绑定文章
   *
   * @param idPortfolio
   * @param idArticle
   * @return
   */
  Result<?> unbindArticle(Integer idPortfolio, Integer idArticle);

  /**
   * 删除作品集
   *
   * @param idPortfolio
   * @return
   */
  Result<?> deletePortfolio(Integer idPortfolio);
}
