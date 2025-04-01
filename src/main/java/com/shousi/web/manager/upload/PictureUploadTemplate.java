package com.shousi.web.manager.upload;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.RandomUtil;
import com.qcloud.cos.model.PutObjectResult;
import com.qcloud.cos.model.ciModel.persistence.CIObject;
import com.qcloud.cos.model.ciModel.persistence.ImageInfo;
import com.qcloud.cos.model.ciModel.persistence.ProcessResults;
import com.shousi.web.config.CosClientConfig;
import com.shousi.web.exception.BusinessException;
import com.shousi.web.exception.ErrorCode;
import com.shousi.web.manager.CosManager;
import com.shousi.web.model.dto.file.UploadPictureResult;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@Slf4j
public abstract class PictureUploadTemplate {

    @Resource
    private CosManager cosManager;

    @Resource
    private CosClientConfig cosClientConfig;

    /**
     * 校验输入源（本地文件 / URL）
     *
     * @param inputSource
     */
    protected abstract void validPicture(Object inputSource);

    /**
     * 获取输入源的原始文件名
     *
     * @param inputSource
     * @return
     */
    protected abstract String getOriginalFilename(Object inputSource);

    /**
     * 处理输入源并生成本地临时文件
     *
     * @param inputSource
     * @param file
     */
    protected abstract void processFile(Object inputSource, File file) throws IOException;

    /**
     * 图片上传方法模板
     *
     * @param inputSource
     * @param uploadPathPrefix
     * @return
     */
    public UploadPictureResult uploadPicture(Object inputSource, String uploadPathPrefix) {
        // 1.校验图片格式
        validPicture(inputSource);
        // 2.拼接图片上传地址
        String uuid = RandomUtil.randomString(16);
        String originalFilename = getOriginalFilename(inputSource);
        String uploadFilename = String.format("%s_%s.%s",
                DateUtil.formatDate(new Date()), uuid, FileUtil.getSuffix(originalFilename));
        // 真正要上传到腾讯云的路径 目录 + 文件名称
        String uploadPath = String.format("%s/%s", uploadPathPrefix, uploadFilename);
        File file = null;
        try {
            // 3.创建临时文件
            file = File.createTempFile(uploadPath, null);
            // 处理文件来源（file / url）
            processFile(inputSource, file);
            // 4.上传到图片到对象存储
            PutObjectResult putObjectResult = cosManager.putPictureObject(uploadPath, file);
            ImageInfo imageInfo = putObjectResult.getCiUploadResult().getOriginalInfo().getImageInfo();
            // 获取webp压缩图
            ProcessResults processResults = putObjectResult.getCiUploadResult().getProcessResults();
            List<CIObject> objectList = processResults.getObjectList();
            if (CollUtil.isNotEmpty(objectList)) {
                CIObject compressCiObject = objectList.get(0);
                CIObject thumbnailCiObject = compressCiObject;
                // 有生成缩略图，才使用缩略图
                if (objectList.size() > 1) {
                    thumbnailCiObject = objectList.get(1);
                }
                return buildResult(uploadPath, originalFilename, compressCiObject, thumbnailCiObject);
            }
            // 5.封装返回结果
            return buildResult(imageInfo, uploadPath, originalFilename, file);
        } catch (Exception e) {
            log.error("图片上传到对象存储失败", e);
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "上传失败");
        } finally {
            // 6.清理临时文件
            this.deleteTempFile(file);
        }
    }

    /**
     * 封装返回结果
     *
     * @param originalFilename
     * @param compressCiObject
     * @param thumbnailCiObject
     * @return
     */
    private UploadPictureResult buildResult(String uploadPath, String originalFilename, CIObject compressCiObject, CIObject thumbnailCiObject) {
        UploadPictureResult uploadPictureResult = new UploadPictureResult();
        String format = compressCiObject.getFormat();
        int pictureWidth = compressCiObject.getWidth();
        int pictureHeight = compressCiObject.getHeight();
        double scale = NumberUtil.round(pictureWidth * 1.0 / pictureHeight, 2).doubleValue();
        uploadPictureResult.setUrl(cosClientConfig.getHost() + "/" + compressCiObject.getKey());
        uploadPictureResult.setOriginUrl(cosClientConfig.getHost() + "/" +uploadPath);
        uploadPictureResult.setThumbnailUrl(cosClientConfig.getHost() + "/" + thumbnailCiObject.getKey());
        uploadPictureResult.setPicName(FileUtil.mainName(originalFilename));
        uploadPictureResult.setPicSize(compressCiObject.getSize().longValue());
        uploadPictureResult.setPicWidth(pictureWidth);
        uploadPictureResult.setPicHeight(pictureHeight);
        uploadPictureResult.setPicScale(scale);
        uploadPictureResult.setPicFormat(format);
        return uploadPictureResult;
    }

    /**
     * 封装返回结果
     *
     * @param imageInfo
     * @param uploadPath
     * @param originalFilename
     * @param file
     * @return
     */
    private UploadPictureResult buildResult(ImageInfo imageInfo, String uploadPath, String originalFilename, File file) {
        UploadPictureResult uploadPictureResult = new UploadPictureResult();
        String format = imageInfo.getFormat();
        int pictureWidth = imageInfo.getWidth();
        int pictureHeight = imageInfo.getHeight();
        double scale = NumberUtil.round(pictureWidth * 1.0 / pictureHeight, 2).doubleValue();
        uploadPictureResult.setUrl(cosClientConfig.getHost() + "/" + uploadPath);
        uploadPictureResult.setOriginUrl(cosClientConfig.getHost() + "/" +uploadPath);
        uploadPictureResult.setThumbnailUrl(cosClientConfig.getHost() + "/" + uploadPath);
        uploadPictureResult.setPicName(FileUtil.mainName(originalFilename));
        uploadPictureResult.setPicSize(FileUtil.size(file));
        uploadPictureResult.setPicWidth(pictureWidth);
        uploadPictureResult.setPicHeight(pictureHeight);
        uploadPictureResult.setPicScale(scale);
        uploadPictureResult.setPicFormat(format);
        return uploadPictureResult;
    }

    /**
     * 删除临时文件
     *
     * @param file 临时文件
     */
    private void deleteTempFile(File file) {
        if (file == null) {
            return;
        }
        boolean result = file.delete();
        if (!result) {
            // 删除失败
            log.error("file delete error, filepath = {}", file.getAbsolutePath());
        }
    }
}
