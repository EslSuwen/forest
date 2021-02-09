package com.rymcu.forest.web.api.v1.portfolio;

import com.rymcu.forest.core.result.GlobalResult;
import com.rymcu.forest.core.result.GlobalResultGenerator;
import com.rymcu.forest.dto.PortfolioArticleDTO;
import com.rymcu.forest.dto.PortfolioDTO;
import com.rymcu.forest.entity.Portfolio;
import com.rymcu.forest.service.PortfolioService;
import com.rymcu.forest.web.api.v1.exception.BaseApiException;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ronger
 */
@RestController
@RequestMapping("/api/v1/portfolio")
public class PortfolioController {

    @Resource
    private PortfolioService portfolioService;

    @GetMapping("/detail/{id}")
    public GlobalResult detail(@PathVariable Integer id,@RequestParam(defaultValue = "0") Integer type) {
        PortfolioDTO portfolio = portfolioService.findPortfolioDTOById(id, type);
        Map map = new HashMap<>(1);
        map.put("portfolio", portfolio);
        return GlobalResultGenerator.genSuccessResult(map);
    }

    @PostMapping("/post")
    public GlobalResult add(@RequestBody Portfolio portfolio) throws BaseApiException {
        portfolio = portfolioService.postPortfolio(portfolio);
        return GlobalResultGenerator.genSuccessResult(portfolio);
    }

    @PutMapping("/post")
    public GlobalResult update(@RequestBody Portfolio portfolio) throws BaseApiException {
        portfolio = portfolioService.postPortfolio(portfolio);
        return GlobalResultGenerator.genSuccessResult(portfolio);
    }

    @GetMapping("/{id}/unbind-articles")
    public GlobalResult unbindArticles(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "10") Integer rows, @RequestParam(defaultValue = "") String searchText,@PathVariable Integer id) throws BaseApiException {
        Map map = portfolioService.findUnbindArticles(page, rows, searchText, id);
        return GlobalResultGenerator.genSuccessResult(map);
    }

    @PostMapping("/bind-article")
    public GlobalResult bindArticle(@RequestBody PortfolioArticleDTO portfolioArticle) {
        Map map = portfolioService.bindArticle(portfolioArticle);
        return GlobalResultGenerator.genSuccessResult(map);
    }

    @PutMapping("/update-article-sort-no")
    public GlobalResult updateArticleSortNo(@RequestBody PortfolioArticleDTO portfolioArticle) {
        Map map = portfolioService.updateArticleSortNo(portfolioArticle);
        return GlobalResultGenerator.genSuccessResult(map);
    }

    @DeleteMapping("/unbind-article")
    public GlobalResult unbindArticle(Integer idArticle,Integer idPortfolio) {
        Map map = portfolioService.unbindArticle(idPortfolio,idArticle);
        return GlobalResultGenerator.genSuccessResult(map);
    }

    @DeleteMapping("/delete")
    public GlobalResult delete(Integer idPortfolio){
        Map map = portfolioService.deletePortfolio(idPortfolio);
        return GlobalResultGenerator.genSuccessResult(map);
    }

}
