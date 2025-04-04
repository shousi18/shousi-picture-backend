package com.shousi.web.api.imagesearch.sub.baidu;

import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpStatus;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.shousi.web.api.imagesearch.model.ImageSearchResult;
import com.shousi.web.exception.BusinessException;
import com.shousi.web.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * 获取图片列表
 */
@Slf4j
public class GetImageListApi {

    /**
     * 获取图片列表
     * @param url
     * @return
     */
    public static List<ImageSearchResult> getImageList(String url) {
        try {
            HttpResponse response = HttpUtil.createGet(url).execute();
            // 获取响应体内容
            int status = response.getStatus();
            String body = response.body();
            // 处理响应
            if (status == HttpStatus.HTTP_OK) {
                // 解析并处理json
                return processResponse(body);
            }else {
                throw new BusinessException(ErrorCode.OPERATION_ERROR, "接口调用失败");
            }
        } catch (Exception e) {
            log.error("接口调用失败", e);
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "接口调用失败");
        }
    }

    /**
     * 解析响应体内容
     * @param body
     * @return
     */
    private static List<ImageSearchResult> processResponse(String body) {
        JSONObject jsonObject = new JSONObject(body);
        if (!jsonObject.containsKey("data")) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "未获取到图片列表");
        }
        JSONObject data = jsonObject.getJSONObject("data");
        if (!data.containsKey("list")) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "未获取到图片列表");
        }
        JSONArray list = data.getJSONArray("list");
        return JSONUtil.toList(list, ImageSearchResult.class);
    }

    public static void main(String[] args) {
        String url = "https://graph.baidu.com/ajax/pcsimi?carousel=503&entrance=GENERAL&extUiData%5BisLogoShow%5D=1&inspire=general_pc&limit=30&next=2&render_type=card&session_id=9578222756948985346&sign=121b39aa48d944d3c6cd501743736775&tk=c28e9&tpl_from=pc";
        List<ImageSearchResult> imageList = getImageList(url);
        System.out.println("搜索成功，结果列表：" + imageList);
    }
}
