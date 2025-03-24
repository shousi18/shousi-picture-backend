package com.shousi.web.manager;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.RandomUtil;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.model.PutObjectResult;
import com.qcloud.cos.model.ciModel.persistence.ImageInfo;
import com.shousi.web.config.CosClientConfig;
import com.shousi.web.exception.BusinessException;
import com.shousi.web.exception.ErrorCode;
import com.shousi.web.exception.ThrowUtils;
import com.shousi.web.model.dto.file.UploadPictureResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class FileManager {

    @Resource
    private CosManager cosManager;

    @Resource
    private CosClientConfig cosClientConfig;

    public UploadPictureResult uploadPicture(MultipartFile multipartFile, String uploadPathPrefix) {
        // 1.校验图片格式
        validPicture(multipartFile);
        // 拼接图片上传地址
        String uuid = RandomUtil.randomString(16);
        String originalFilename = multipartFile.getOriginalFilename();
        String uploadFilename = String.format("%s_%s.%s",
                DateUtil.formatDate(new Date()), uuid, FileUtil.getSuffix(originalFilename));
        // 真正要上传到腾讯云的路径 目录 + 文件名称
        String uploadPath = String.format("%s/%s", uploadPathPrefix, uploadFilename);
        File file = null;
        try {
            // 创建临时文件
            file = File.createTempFile(uploadPath, null);
            multipartFile.transferTo(file);
            PutObjectResult putObjectResult = cosManager.putPictureObject(uploadPath, file);
            // 封装返回结果
            ImageInfo imageInfo = putObjectResult.getCiUploadResult().getOriginalInfo().getImageInfo();
            String format = imageInfo.getFormat();
            int pictureWidth = imageInfo.getWidth();
            int pictureHeight = imageInfo.getHeight();
            double scale = NumberUtil.round(pictureWidth * 1.0 / pictureHeight, 2).doubleValue();
            UploadPictureResult uploadPictureResult = new UploadPictureResult();
            uploadPictureResult.setUrl(cosClientConfig.getHost() + "/" + uploadPath);
            uploadPictureResult.setPicName(FileUtil.mainName(originalFilename));
            uploadPictureResult.setPicSize(FileUtil.size(file));
            uploadPictureResult.setPicWidth(pictureWidth);
            uploadPictureResult.setPicHeight(pictureHeight);
            uploadPictureResult.setPicScale(scale);
            uploadPictureResult.setPicFormat(format);
            return uploadPictureResult;
        } catch (Exception e) {
            log.error("图片上传到对象存储失败", e);
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "上传失败");
        } finally {
            this.deleteTempFile(file);
        }
    }

    /**
     * 校验文件
     *
     * @param file
     */
    public void validPicture(MultipartFile file) {
        ThrowUtils.throwIf(file == null, ErrorCode.PARAMS_ERROR, "文件不能为空");
        // 1.校验文件大小
        long size = file.getSize();
        final long ONE_M = 1024 * 1024L;
        ThrowUtils.throwIf(size > 2 * ONE_M, ErrorCode.PARAMS_ERROR, "文件大小不能超过 2M");
        // 2.校验文件后缀
        String suffix = FileUtil.getSuffix(file.getOriginalFilename());
        // 允许上传的文件后缀
        final List<String> ALLOW_FORMAT_LIST = Arrays.asList("png", "jpg", "jpeg", "webp", "jfif");
        ThrowUtils.throwIf(!ALLOW_FORMAT_LIST.contains(suffix), ErrorCode.PARAMS_ERROR, "不支持的文件格式");
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
