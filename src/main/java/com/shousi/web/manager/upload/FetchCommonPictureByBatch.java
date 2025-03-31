package com.shousi.web.manager.upload;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class FetchCommonPictureByBatch extends FetchPictureByBatchTemplate {

    @Override
    protected Elements selectElement(Element div) {
        return div.select("img.mimg");
    }
    @Override
    protected String getPictureUrl(Element imgElement) {
        String fileUrl = imgElement.attr("src");
        if (StringUtils.isBlank(fileUrl)) {
            return null;
        }
        return fileUrl;
    }
}
