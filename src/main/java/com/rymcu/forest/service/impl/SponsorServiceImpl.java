package com.rymcu.forest.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rymcu.forest.dto.ArticleDTO;
import com.rymcu.forest.dto.result.Result;
import com.rymcu.forest.entity.Sponsor;
import com.rymcu.forest.entity.TransactionRecord;
import com.rymcu.forest.entity.User;
import com.rymcu.forest.enumerate.SponsorEnum;
import com.rymcu.forest.mapper.SponsorMapper;
import com.rymcu.forest.service.ArticleService;
import com.rymcu.forest.service.SponsorService;
import com.rymcu.forest.service.TransactionRecordService;
import com.rymcu.forest.util.UserUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;

/** @author ronger */
@Service
public class SponsorServiceImpl extends ServiceImpl<SponsorMapper, Sponsor>
    implements SponsorService {

  @Resource private SponsorMapper sponsorMapper;
  @Resource private ArticleService articleService;
  @Resource private TransactionRecordService transactionRecordService;

  @Override
  @Transactional(rollbackFor = Exception.class)
  public Result<?> sponsorship(Sponsor sponsor) throws Exception {
    if (Objects.isNull(sponsor)
        || Objects.isNull(sponsor.getDataId())
        || Objects.isNull(sponsor.getDataType())) {
      return Result.error("数据异常");
    } else {
      SponsorEnum result =
          Arrays.stream(SponsorEnum.values())
              .filter(sponsorEnum -> sponsorEnum.getDataType().equals(sponsor.getDataType()))
              .findFirst()
              .orElse(SponsorEnum.Article);
      BigDecimal money = BigDecimal.valueOf(result.getMoney());
      sponsor.setSponsorshipMoney(money);
      User user = UserUtils.getCurrentUserByToken();
      sponsor.setSponsor(user.getIdUser());
      sponsor.setSponsorshipTime(new Date());
      save(sponsor);
      // 赞赏金额划转
      if (result.isArticle()) {
        ArticleDTO articleDTO = articleService.findArticleDTOById(sponsor.getDataId(), 1);
        TransactionRecord transactionRecord =
            transactionRecordService.transferByUserId(
                articleDTO.getArticleAuthorId(), user.getIdUser(), money);
        if (Objects.isNull(transactionRecord.getIdTransactionRecord())) {
          return Result.error("余额不足");
        }
        // 更新文章赞赏数
        sponsorMapper.updateArticleSponsorCount(articleDTO.getIdArticle());
      }
      return Result.OK("赞赏成功");
    }
  }
}
