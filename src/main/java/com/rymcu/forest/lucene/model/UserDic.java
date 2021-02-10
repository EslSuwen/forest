package com.rymcu.forest.lucene.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * UserDic 用户个性化字典
 *
 * @author suwen
 * @date 2021/2/4 09:09
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@TableName("lucene_user_dic")
public class UserDic {
  /** 主键 */
  @TableId(type = IdType.AUTO)
  private Integer id;

  /** 字典 */
  private String dic;
}
