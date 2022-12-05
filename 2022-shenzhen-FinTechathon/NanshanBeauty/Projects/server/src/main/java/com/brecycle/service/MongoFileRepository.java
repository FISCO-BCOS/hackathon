package com.brecycle.service;

import com.brecycle.entity.MongoFile;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * MongoDB文件仓储
 */
public interface MongoFileRepository extends MongoRepository<MongoFile, String> {
}
