package com.brecycle.entity;

import lombok.Builder;
import lombok.Data;
import org.bson.types.Binary;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * @author cmgun
 */
@Document
@Builder
@Data
public class MongoFile {

    /**
     * 主键
     */
    @Id
    public String id;

    /**
     * 文件名称
     */
    public String fileName;

    /**
     * 文件大小
     */
    public long fileSize;

    /**
     * 上传时间
     */
    public Date uploadDate;

    /**
     * MD5值
     */
    public String md5;

    /**
     * 文件内容
     */
    private Binary content;

    /**
     * 文件类型
     */
    public String contentType;

    /**
     * 文件后缀名
     */
    public String suffix;

    /**
     * 文件描述
     */
    public String description;

    /**
     * 大文件管理GridFS的ID
     */
    private String gridFsId;

}