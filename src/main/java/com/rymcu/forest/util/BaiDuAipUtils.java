package com.rymcu.forest.util;

import com.alibaba.fastjson.JSON;
import com.baidu.aip.nlp.AipNlp;
import com.rymcu.forest.dto.baidu.TagNlpDTO;
import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

/**
 * @author ronger
 */
public class BaiDuAipUtils {

    public static final String APP_ID = "18094949";
    public static final String API_KEY = "3h3BOgejXI1En5aq1iGHeWrF";
    public static final String SECRET_KEY = "8guDNvxWF1wu8ogpVxLHlRY5FeOBE8z7";

    public static final Integer MAX_CONTENT_LENGTH = 3000;

    public static List<TagNlpDTO> getKeywords(String title, String content) {
        if (StringUtils.isBlank(content)) {
            return null;
        }
        // api 限制内容不能超过 3000 字
        if (content.length() > MAX_CONTENT_LENGTH) {
            content = content.substring(0, MAX_CONTENT_LENGTH);
        }
        // 初始化一个AipNlp
        AipNlp client = new AipNlp(APP_ID, API_KEY, SECRET_KEY);

        // 可选：设置网络连接参数
        client.setConnectionTimeoutInMillis(2000);
        client.setSocketTimeoutInMillis(60000);

        // 传入可选参数调用接口
        HashMap<String, Object> options = new HashMap<String, Object>(1);

        // 新闻摘要接口
        JSONObject res = client.keyword(title, Html2TextUtil.getContent(content), options);
        List<TagNlpDTO> list = JSON.parseArray(res.get("items").toString(), TagNlpDTO.class);
        return list;
    }

    public static String getTopic(String title, String content) {
        if (StringUtils.isBlank(content)) {
            return "";
        }
        // api 限制内容不能超过 3000 字
        if (content.length() > MAX_CONTENT_LENGTH) {
            content = content.substring(0, MAX_CONTENT_LENGTH);
        }
        // 初始化一个AipNlp
        AipNlp client = new AipNlp(APP_ID, API_KEY, SECRET_KEY);

        // 可选：设置网络连接参数
        client.setConnectionTimeoutInMillis(2000);
        client.setSocketTimeoutInMillis(60000);

        // 传入可选参数调用接口
        HashMap<String, Object> options = new HashMap<String, Object>(1);

        // 新闻摘要接口
        JSONObject res = client.topic(title, Html2TextUtil.getContent(content), options);
        return res.toString(2);
    }

    public static String getNewsSummary(String title, String content, int maxSummaryLen) {
        if (StringUtils.isBlank(content)) {
            return "";
        }
        // api 限制内容不能超过 3000 字
        if (content.length() > MAX_CONTENT_LENGTH) {
            content = content.substring(0, MAX_CONTENT_LENGTH);
        }
        // 初始化一个AipNlp
        AipNlp client = new AipNlp(APP_ID, API_KEY, SECRET_KEY);

        // 可选：设置网络连接参数
        client.setConnectionTimeoutInMillis(2000);
        client.setSocketTimeoutInMillis(60000);

        // 传入可选参数调用接口
        HashMap<String, Object> options = new HashMap<String, Object>(1);
        options.put("title", title);

        // 新闻摘要接口
        JSONObject res = client.newsSummary(Html2TextUtil.getContent(content), maxSummaryLen, options);
        return res.getString("summary");
    }

}
