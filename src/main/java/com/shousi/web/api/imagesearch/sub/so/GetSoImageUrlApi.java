package com.shousi.web.api.imagesearch.sub.so;

import com.shousi.web.exception.BusinessException;
import com.shousi.web.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 * 获取360搜图的图片的接口
 *
 */
@Slf4j
@Deprecated
public class GetSoImageUrlApi {
	public static String getSoImageUrl(String imageUrl) {
		String url = "https://st.so.com/r?src=st&srcsp=home&img_url=" + imageUrl + "&submittype=imgurl";
		try {
			Document document = Jsoup.connect(url).timeout(5000).get();
			Element imgElement = document.selectFirst(".img_img");
			if (imgElement != null) {
				String soImageUrl = "";
				// 获取当前元素的属性
				String style = imgElement.attr("style");
				if (style.contains("background-image:url(")) {
					// 提取URL部分
					int start = style.indexOf("url(") + 4;  // 从"Url("之后开始
					int end = style.indexOf(")", start);    // 找到右括号的位置
					if (start > 4 && end > start) {
						soImageUrl = style.substring(start, end);
					}
				}
				return soImageUrl;
			}
			throw new BusinessException(ErrorCode.OPERATION_ERROR, "搜图失败");
		} catch (Exception e) {
			log.error("搜图失败", e);
			throw new BusinessException(ErrorCode.OPERATION_ERROR, "搜图失败");
		}
	}

	public static void main(String[] args) {
		String imageUrl = "https://shousi-1324630026.cos.ap-beijing.myqcloud.com//public/1903709990332473345/2025-04-03_C8qGyyzEAnwIQpzn.jfif";
		String result = getSoImageUrl(imageUrl);
		System.out.println("搜索成功，结果 URL：" + result);
	}
}
