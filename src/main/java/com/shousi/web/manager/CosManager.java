package com.shousi.web.manager;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.exception.CosClientException;
import com.qcloud.cos.exception.CosServiceException;
import com.qcloud.cos.model.COSObject;
import com.qcloud.cos.model.GetObjectRequest;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.PutObjectResult;
import com.qcloud.cos.model.ciModel.persistence.PicOperations;
import com.shousi.web.config.CosClientConfig;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;

@Component
public class CosManager {

    @Resource
    private CosClientConfig cosClientConfig;

    @Resource
    private COSClient cosClient;

    /**
     * 上传文件
     *
     * @param key  key
     * @param file 文件
     * @return PutObjectResult
     * @throws CosClientException
     * @throws CosServiceException
     */
    public PutObjectResult putObject(String key, File file)
            throws CosClientException, CosServiceException {
        PutObjectRequest putObjectRequest = new PutObjectRequest(cosClientConfig.getBucket(), key, file);
        return cosClient.putObject(putObjectRequest);
    }

    /**
     * 下载文件
     *
     * @param key key
     * @return COSObject
     * @throws CosClientException
     * @throws CosServiceException
     */
    public COSObject getObject(String key)
            throws CosClientException, CosServiceException {
        GetObjectRequest objectRequest = new GetObjectRequest(cosClientConfig.getBucket(), key);
        return cosClient.getObject(objectRequest);
    }


    /**
     * 上传图片 (并附带图片信息)
     *
     * @param key  key
     * @param file 文件
     * @return PutObjectResult
     * @throws CosClientException
     * @throws CosServiceException
     */
    public PutObjectResult putPictureObject(String key, File file)
            throws CosClientException, CosServiceException {
        PutObjectRequest putObjectRequest = new PutObjectRequest(cosClientConfig.getBucket(), key, file);
        // 对图片进行处理（获取基本信息也是对图片的处理）
        PicOperations picOperations = new PicOperations();
        // 返回原图信息
        picOperations.setIsPicInfo(1);
        // 构造处理参数
        putObjectRequest.setPicOperations(picOperations);
        return cosClient.putObject(putObjectRequest);
    }
}
