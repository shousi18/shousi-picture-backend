package com.shousi.web.api.imagesearch;

import com.shousi.web.api.imagesearch.model.SoImageSearchResult;
import com.shousi.web.api.imagesearch.sub.so.GetSoImageListApi;
import com.shousi.web.api.imagesearch.sub.so.GetSoImageUrlApi;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * 360搜图图片搜索接口
 */
@Slf4j
public class SoImageSearchApiFacade {

	/**
	 * 搜索图片
	 *
	 * @param imageUrl 需要以图搜图的图片地址
	 * @param start    开始下表
	 * @return 图片搜索结果列表
	 */
	public static List<SoImageSearchResult> searchImage(String imageUrl, Integer start) {
		List<SoImageSearchResult> imageList = GetSoImageListApi.getImageList(imageUrl, start);
		return imageList;
	}

	public static void main(String[] args) {
		// 测试以图搜图功能
		String imageUrl = "https://shousi-1324630026.cos.ap-beijing.myqcloud.com//public/1903709990332473345/2025-04-03_C8qGyyzEAnwIQpzn.jfif";
		List<SoImageSearchResult> resultList = searchImage(imageUrl, 0);
		System.out.println("结果列表" + resultList);
	}
}
