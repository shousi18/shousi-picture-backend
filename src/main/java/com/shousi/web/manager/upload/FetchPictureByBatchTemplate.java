package com.shousi.web.manager.upload;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.shousi.web.exception.BusinessException;
import com.shousi.web.exception.ErrorCode;
import com.shousi.web.exception.ThrowUtils;
import com.shousi.web.model.dto.picture.PictureUploadByBatchRequest;
import com.shousi.web.model.dto.picture.PictureUploadRequest;
import com.shousi.web.model.entity.User;
import com.shousi.web.model.vo.PictureVO;
import com.shousi.web.service.PictureService;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Objects;

@Slf4j
public abstract class FetchPictureByBatchTemplate {

    @Resource
    private PictureService pictureService;

    public Integer uploadPictureByBatch(PictureUploadByBatchRequest pictureUploadByBatchRequest, User loginUser) {
        // 校验参数
        ThrowUtils.throwIf(pictureUploadByBatchRequest == null, ErrorCode.PARAMS_ERROR);
        String searchText = pictureUploadByBatchRequest.getSearchText();
        String namePrefix = pictureUploadByBatchRequest.getNamePrefix();
        Integer first = pictureUploadByBatchRequest.getFirst();
        if (StrUtil.isBlank(namePrefix)) {
            namePrefix = searchText;
        }
        Integer count = pictureUploadByBatchRequest.getCount();
        ThrowUtils.throwIf(count > 30, ErrorCode.PARAMS_ERROR, "最多上传30张图片");
        // 抓取地址
        String fetchUrl = String.format("https://cn.bing.com/images/async?q=%s&first=%d&mmasync=1", searchText, first);
        Document document;
        try {
            // 发起get请求并获取结果
            document = Jsoup.connect(fetchUrl).get();
        } catch (IOException e) {
            log.error("获取页面失败：", e);
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "获取页面失败");
        }
        Element div = document.getElementsByClass("dgControl").first();
        if (Objects.isNull(div)) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "获取元素失败");
        }
        Elements imgElementList = selectElement(div);
        int uploadCount = 0;
        for (Element imgElement : imgElementList) {
            String fileUrl = getPictureUrl(imgElement);
            if (StringUtils.isBlank(fileUrl)) {
                log.info("当前链接为空，已跳过：{}", fileUrl);
                continue;
            }
            // 处理上述图片，防止转义
            int questionMarkIndex = fileUrl.indexOf("?");
            if (questionMarkIndex > -1) {
                fileUrl = fileUrl.substring(0, questionMarkIndex);
            }
            // 上传图片
            PictureUploadRequest pictureUploadRequest = new PictureUploadRequest();
            if (StrUtil.isNotBlank(namePrefix)) {
                pictureUploadRequest.setPicName(namePrefix + (uploadCount + 1 + first));
            }
            try {
                PictureVO pictureVO = pictureService.uploadPicture(fileUrl, pictureUploadRequest, loginUser);
                log.info("图片上传成功，id = {}", pictureVO.getId());
                uploadCount++;
            }catch (Exception e) {
                log.error("图片上传失败", e);
                continue;
            }
            if (uploadCount >= count) {
                break;
            }
        }
        return uploadCount;
    }

    protected abstract String getPictureUrl(Element imgElement);

    protected abstract Elements selectElement(Element div);
}
