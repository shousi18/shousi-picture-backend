package com.shousi.web.controller;

import com.qcloud.cos.model.COSObject;
import com.qcloud.cos.model.COSObjectInputStream;
import com.qcloud.cos.utils.IOUtils;
import com.shousi.web.annotation.AuthCheck;
import com.shousi.web.common.BaseResponse;
import com.shousi.web.constant.UserConstant;
import com.shousi.web.exception.BusinessException;
import com.shousi.web.exception.ErrorCode;
import com.shousi.web.manager.CosManager;
import com.shousi.web.utils.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("/file")
@Slf4j
public class FileController {

    @Resource
    private CosManager cosManager;

    @PostMapping("/test/upload")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<String> testUploadFile(@RequestPart("file") MultipartFile multipartFile) {
        String filename = multipartFile.getOriginalFilename();
        String filePath = String.format("/test/%s", filename);
        File file = null;
        try {
            file = File.createTempFile(filePath, null);
            multipartFile.transferTo(file);
            cosManager.putObject(filePath, file);
            return ResultUtils.success(filePath);
        } catch (Exception e) {
            log.error("file upload error, filePath = {}", filePath, e);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "上传失败");
        } finally {
            if (file != null) {
                boolean result = file.delete();
                if (!result) {
                    // 删除失败
                    log.error("file delete error, filepath = {}", filePath);
                }
            }
        }
    }

    @GetMapping("/test/download")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public void testDownloadFile(String filePath, HttpServletResponse response) {
        COSObjectInputStream cosObjectInputStream = null;
        try {
            COSObject object = cosManager.getObject(filePath);
            cosObjectInputStream = object.getObjectContent();
            byte[] bytes = IOUtils.toByteArray(cosObjectInputStream);
            // 设置响应头
            response.setContentType("application/octet-stream;charset=UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + filePath);
            // 写入响应
            response.getOutputStream().write(bytes);
            response.getOutputStream().flush();
        } catch (IOException e) {
            log.error("file download error, filePath = {}", filePath, e);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "下载失败");
        } finally {
            if (cosObjectInputStream != null) {
                try {
                    cosObjectInputStream.close();
                } catch (IOException e) {
                    log.error("cosObjectInputStream close error", e);
                    throw new BusinessException(ErrorCode.SYSTEM_ERROR, "下载失败");
                }
            }
        }
    }
}
