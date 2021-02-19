package com.rymcu.forest.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rymcu.forest.dto.Author;
import com.rymcu.forest.dto.PortfolioArticleDTO;
import com.rymcu.forest.dto.PortfolioDTO;
import com.rymcu.forest.dto.UserDTO;
import com.rymcu.forest.dto.result.Result;
import com.rymcu.forest.entity.Portfolio;
import com.rymcu.forest.entity.User;
import com.rymcu.forest.mapper.PortfolioMapper;
import com.rymcu.forest.service.ArticleService;
import com.rymcu.forest.service.PortfolioService;
import com.rymcu.forest.service.UserService;
import com.rymcu.forest.util.UserUtils;
import com.rymcu.forest.web.api.v1.common.UploadController;
import com.rymcu.forest.web.api.v1.exception.BaseApiException;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/** @author ronger */
@Service
public class PortfolioServiceImpl extends ServiceImpl<PortfolioMapper, Portfolio>
    implements PortfolioService {

  @Resource private PortfolioMapper portfolioMapper;
  @Resource private UserService userService;
  @Resource private ArticleService articleService;

  @Override
  public List<PortfolioDTO> findUserPortfoliosByUser(Page<?> page, UserDTO userDTO) {
    List<PortfolioDTO> list = portfolioMapper.selectUserPortfoliosByIdUser(userDTO.getIdUser());
    Author author = new Author();
    author.setIdUser(userDTO.getIdUser());
    author.setUserAvatarURL(userDTO.getAvatarUrl());
    author.setUserNickname(userDTO.getNickname());
    list.forEach(
        portfolioDTO -> {
          genPortfolioAuthor(portfolioDTO, author);
        });
    return list;
  }

  @Override
  public PortfolioDTO findPortfolioDTOById(Integer idPortfolio, Integer type) {
    PortfolioDTO portfolio = portfolioMapper.selectPortfolioDTOById(idPortfolio, type);
    if (portfolio == null) {
      return new PortfolioDTO();
    }
    Author author = userService.selectAuthor(portfolio.getPortfolioAuthorId());
    genPortfolioAuthor(portfolio, author);
    Integer articleNumber = portfolioMapper.selectCountArticleNumber(portfolio.getIdPortfolio());
    portfolio.setArticleNumber(articleNumber);
    return portfolio;
  }

  @Override
  public Portfolio postPortfolio(Portfolio portfolio) throws BaseApiException {
    User user = UserUtils.getCurrentUserByToken();
    if (StringUtils.isNotBlank(portfolio.getHeadImgType())) {
      String headImgUrl = UploadController.uploadBase64File(portfolio.getHeadImgUrl(), 0);
      portfolio.setHeadImgUrl(headImgUrl);
    }
    if (portfolio.getIdPortfolio() == null || portfolio.getIdPortfolio() == 0) {
      portfolio.setPortfolioAuthorId(user.getIdUser());
      portfolio.setCreatedTime(new Date());
      portfolio.setUpdatedTime(portfolio.getCreatedTime());
      save(portfolio);
    } else {
      portfolio.setUpdatedTime(new Date());
      updateById(portfolio);
    }
    return portfolio;
  }

  @Override
  public Result<?> findUnbindArticles(Page<?> page, String searchText, Integer idPortfolio)
      throws BaseApiException {
    User user = UserUtils.getCurrentUserByToken();
    Portfolio portfolio = getById(idPortfolio);
    if (portfolio == null) {
      return Result.error("该作品集不存在或已被删除!");
    } else {
      if (!user.getIdUser().equals(portfolio.getPortfolioAuthorId())) {
        return Result.error("非法操作!");
      } else {
        return Result.OK(
            articleService.selectUnbindArticles(page, idPortfolio, searchText, user.getIdUser()));
      }
    }
  }

  @Override
  public Result<?> bindArticle(PortfolioArticleDTO portfolioArticle) {
    Integer count =
        portfolioMapper.selectCountPortfolioArticle(
            portfolioArticle.getIdArticle(), portfolioArticle.getIdPortfolio());
    if (count.equals(0)) {
      Integer maxSortNo = portfolioMapper.selectMaxSortNo(portfolioArticle.getIdPortfolio());
      portfolioMapper.insertPortfolioArticle(
          portfolioArticle.getIdArticle(), portfolioArticle.getIdPortfolio(), maxSortNo);
      return Result.OK();
    } else {
      return Result.error("该文章已经在作品集下!!");
    }
  }

  @Override
  public Result<?> updateArticleSortNo(PortfolioArticleDTO portfolioArticle) {
    if (portfolioArticle.getIdPortfolio() == null || portfolioArticle.getIdPortfolio().equals(0)) {
      return Result.error("作品集数据异常!");
    }
    if (portfolioArticle.getIdArticle() == null || portfolioArticle.getIdArticle().equals(0)) {
      return Result.error("文章数据异常!");
    }
    if (portfolioArticle.getSortNo() == null) {
      return Result.error("排序号不能为空!");
    }
    Integer result =
        portfolioMapper.updateArticleSortNo(
            portfolioArticle.getIdPortfolio(),
            portfolioArticle.getIdArticle(),
            portfolioArticle.getSortNo());
    return result > 0 ? Result.OK() : Result.error("更新失败!");
  }

  @Override
  public Result<?> unbindArticle(Integer idPortfolio, Integer idArticle) {
    if (idPortfolio == null || idPortfolio.equals(0)) {
      return Result.error("作品集数据异常!");
    }
    if (idArticle == null || idArticle.equals(0)) {
      return Result.error("文章数据异常!");
    }
    Integer result = portfolioMapper.unbindArticle(idPortfolio, idArticle);
    return result > 0 ? Result.OK() : Result.error("操作失败!");
  }

  @Override
  public Result<?> deletePortfolio(Integer idPortfolio) {
    if (idPortfolio == null || idPortfolio.equals(0)) {
      return Result.error("作品集数据异常!");
    }
    Integer articleNumber = portfolioMapper.selectCountArticleNumber(idPortfolio);
    if (articleNumber > 0) {
      return Result.error("该作品集已绑定文章不允许删除!");
    } else {
      return removeById(idPortfolio) ? Result.OK() : Result.error("操作失败!");
    }
  }

  private PortfolioDTO genPortfolioAuthor(PortfolioDTO portfolioDTO, Author author) {
    portfolioDTO.setPortfolioAuthorAvatarUrl(author.getUserAvatarURL());
    portfolioDTO.setPortfolioAuthorName(author.getUserNickname());
    portfolioDTO.setPortfolioAuthorId(author.getIdUser());
    portfolioDTO.setPortfolioAuthor(author);
    return portfolioDTO;
  }
}
