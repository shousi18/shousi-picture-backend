package com.shousi.web.api.imagesearch;

import com.shousi.web.api.imagesearch.model.ImageSearchResult;
import com.shousi.web.api.imagesearch.sub.baidu.GetImageFirstUrlApi;
import com.shousi.web.api.imagesearch.sub.baidu.GetImageListApi;
import com.shousi.web.api.imagesearch.sub.baidu.GetImagePageUrlApi;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * 百度以图搜图搜索接口
 */
@Slf4j
public class ImageSearchApiFacade {

    /**
     * 搜索图片
     *
     * @param imageUrl
     * @return
     */
    public static List<ImageSearchResult> searchImage(String imageUrl) {
        String imagePageUrl = GetImagePageUrlApi.getImagePageUrl(imageUrl);
        String imageFirstUrl = GetImageFirstUrlApi.getImageFirstUrl(imagePageUrl);
        return GetImageListApi.getImageList(imageFirstUrl);
    }

    public static void main(String[] args) {
        // 测试以图搜图功能
        String imageUrl = "https://www.codefather.cn/logo.png";
        List<ImageSearchResult> resultList = searchImage(imageUrl);
        System.out.println("结果列表" + resultList);
    }
}
