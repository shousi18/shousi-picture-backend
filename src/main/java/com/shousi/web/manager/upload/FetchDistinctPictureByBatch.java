package com.shousi.web.manager.upload;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class FetchDistinctPictureByBatch extends FetchPictureByBatchTemplate {

    @Override
    protected Elements selectElement(Element div) {
        return div.select("a.iusc");
    }

    @Override
    protected String getPictureUrl(Element imgElement) {
        String jsonData = imgElement.attr("m");
        if (StrUtil.isBlank(jsonData)) {
            return null;
        }
        JSONObject jsonObject = JSONUtil.toBean(jsonData, JSONObject.class);
        return (String) jsonObject.get("murl");
    }
}
