package com.shousi.web.api.imagesearch.sub.baidu;

import cn.hutool.core.util.URLUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpStatus;
import cn.hutool.json.JSONUtil;
import com.shousi.web.exception.BusinessException;
import com.shousi.web.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * 获取图片页面地址
 *
 * @return
 */
@Slf4j
public class GetImagePageUrlApi {

    /**
     * 获取图片页面地址
     *
     * @param imageUrl
     * @return
     */
    public static String getImagePageUrl(String imageUrl) {
        // 1. 准备请求参数
        Map<String, Object> formData = new HashMap<>();
        formData.put("image", imageUrl);
        formData.put("tn", "pc");
        formData.put("from", "pc");
        formData.put("image_source", "PC_UPLOAD_URL");
        // 获取当前时间戳
        long uptime = System.currentTimeMillis();
        // 请求地址
        String url = "https://graph.baidu.com/upload?uptime=" + uptime;
        String ascToken = "1743654470212_1743733616636_Y8loABWH5Zab382e5gSqpwlnVHIkDneNym+Zm8rPRZVzV6aLaS3OhDK5gEQFSaW2bdRdm9Ov7I3FCvDIqfjZ+Hma39q+Y7HuYKdhk/c7mzwNbBM0D9Z2jPePMCU5TxMn1CQ+2Agv7oPtgQtDMI6WYjUCF2jnrzdVUD/FstIOvD3SwKEWKAGfsM66aWaGfwtDvY08q44Ib/OAsP2xJE7XEXwdnuFolUg3QanGPqU72Jbdafw2wu0aPUASX7tbl6rcJHZTub36hY42Qp4wutFOFh+sF/y+j5R5QctWlTnqd9sx4qEnTvsXwtLoWhDw99GIWrIZd4tJE+ektXTv1m2QUWipltkh0m987V3IvZNM1sEkqazcbfUo4sWAURKtz6PJmKyjQV3tx2znjWMRUqj5qL+lnxkwmRfUHY5KCr3XstoM8vE1WHZEP2Itbx4+JzT6";

        try {
            // 2. 发送 POST 请求到百度接口
            HttpResponse response = HttpRequest.post(url)
                    .form(formData)
                    .timeout(5000)
                    .header("Acs-Token", ascToken)
                    .execute();
            // 判断响应状态
            if (HttpStatus.HTTP_OK != response.getStatus()) {
                throw new BusinessException(ErrorCode.OPERATION_ERROR, "接口调用失败");
            }
            // 解析响应
            String responseBody = response.body();
            Map<String, Object> result = JSONUtil.toBean(responseBody, Map.class);

            // 3. 处理响应结果
            if (result == null || !Integer.valueOf(0).equals(result.get("status"))) {
                throw new BusinessException(ErrorCode.OPERATION_ERROR, "接口调用失败");
            }
            Map<String, Object> data = (Map<String, Object>) result.get("data");
            String rawUrl = (String) data.get("url");
            // 对 URL 进行解码
            String searchResultUrl = URLUtil.decode(rawUrl, StandardCharsets.UTF_8);
            // 如果 URL 为空
            if (searchResultUrl == null) {
                throw new BusinessException(ErrorCode.OPERATION_ERROR, "未返回有效结果");
            }
            return searchResultUrl;
        } catch (Exception e) {
            log.error("搜索失败", e);
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "搜索失败");
        }
    }

    public static void main(String[] args) {
        // 测试以图搜图功能
        String imageUrl = "https://shousi-1324630026.cos.ap-beijing.myqcloud.com/space/1/2025-04-03_vMeeV5334nXfZgfb.png";
        String result = getImagePageUrl(imageUrl);
        System.out.println("搜索成功，结果 URL：" + result);
    }
}
