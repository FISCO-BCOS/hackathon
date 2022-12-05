package com.brecycle.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.brecycle.config.FiscoBcos;
import com.brecycle.contract.EntAccess;
import com.brecycle.controller.hanlder.BusinessException;
import com.brecycle.entity.*;
import com.brecycle.entity.dto.*;
import com.brecycle.enums.AccessStatus;
import com.brecycle.enums.RoleEnums;
import com.brecycle.mapper.EntInfoMapper;
import com.brecycle.mapper.UserFileMapper;
import com.brecycle.mapper.UserMapper;
import com.brecycle.mapper.UserRoleMapper;
import com.brecycle.service.EntService;
import com.brecycle.service.MongoFileRepository;
import com.google.common.collect.Lists;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.bson.types.Binary;
import org.fisco.bcos.sdk.BcosSDK;
import org.fisco.bcos.sdk.abi.datatypes.generated.tuples.generated.Tuple5;
import org.fisco.bcos.sdk.client.Client;
import org.fisco.bcos.sdk.crypto.CryptoSuite;
import org.fisco.bcos.sdk.crypto.keypair.CryptoKeyPair;
import org.fisco.bcos.sdk.model.CryptoType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author cmgun
 */
@Slf4j
@Service
public class EntServiceImpl implements EntService {

    @Autowired
    MongoFileRepository mongoFileRepository;
    @Autowired
    GridFsTemplate gridFsTemplate;
    @Autowired
    MongoTemplate mongoTemplate;
    @Autowired
    GridFSBucket gridFSBucket;
    @Autowired
    UserMapper userMapper;
    @Autowired
    UserFileMapper userFileMapper;
    @Autowired
    EntInfoMapper entInfoMapper;
    @Autowired
    UserRoleMapper userRoleMapper;
    @Autowired
    FiscoBcos fiscoBcos;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void apply(List<MultipartFile> files, String userName) throws Exception {
        // 查询当前用户
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getUserName, userName));
        if (user == null) {
            throw new BusinessException("用户信息不存在");
        }
        // 文件上传
        List<String> fileIds = Lists.newArrayList();
        for (MultipartFile file : files) {
            MongoFile mongoFile = uploadFile(file, userName);
            fileIds.add(mongoFile.getId());
        }
        // 保存文件关联
        List<UserFile> userFiles = fileIds.stream().map(e -> {
            UserFile userFile = new UserFile();
            userFile.setUserId(user.getId());
            userFile.setFileId(e);
            return userFile;
        }).collect(Collectors.toList());
        userFiles.forEach(e -> userFileMapper.insert(e));
        // 更新准入状态
        EntInfo entInfo = new EntInfo();
        entInfo.setAccessStatus(AccessStatus.AUDIT.getValue());
        entInfoMapper.update(entInfo, new LambdaUpdateWrapper<EntInfo>().eq(EntInfo::getUserId, user.getId()));
    }

    @Override
    public PageResult<EntListDTO> getEntList(EntListParam param) {
        IPage page = new Page<>(param.getPageNo(), param.getPageSize());
        IPage<EntListDTO> data = userMapper.selectEntListByPage(page, param);
        for (EntListDTO item : data.getRecords()) {
            List<UserFile> files = userFileMapper.selectList(new LambdaQueryWrapper<UserFile>().eq(UserFile::getUserId, item.getId()));
            if (CollectionUtils.isNotEmpty(files)) {
                item.setFileId(files.stream().map(UserFile::getFileId).collect(Collectors.toList()));
            }
        }
        PageResult<EntListDTO> result = new PageResult<>();
        result.setTotal(data.getTotal());
        result.setPageNo(data.getCurrent());
        result.setPageCount(data.getPages());
        result.setPageSize(data.getSize());
        result.setData(data.getRecords());
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void accessPass(EntAccessAuditParam param, String currentUserName) throws Exception {
        // SDK配置
        BcosSDK bcosSDK = fiscoBcos.getBcosSDK();
        Client client = bcosSDK.getClient(1);
        // 获取当前登录用户的账户信息
        User user = userMapper.selectByUserName(currentUserName);
        CryptoSuite cryptoSuite = new CryptoSuite(CryptoType.ECDSA_TYPE);
        CryptoKeyPair currentKeyPair = cryptoSuite.getKeyPairFactory().createKeyPair(user.getPrivateKey());
        // 查询准入企业信息
        User accessEnt = userMapper.selectOne(new LambdaUpdateWrapper<User>().eq(User::getUserName, param.getUserName()));
        // 创建准入合约
        EntAccess entAccess = EntAccess.deploy(client, currentKeyPair, accessEnt.getAddr()
                , accessEnt.getName(), accessEnt.getIdno(), accessEnt.getMobile()
                , accessEnt.getAddress(), param.getRemark());
        log.info("准入合约部署成功，合约地址:{}", entAccess.getContractAddress());
        // 保存合约地址
        EntInfo entInfo = new EntInfo();
        entInfo.setAccessContractAddr(entAccess.getContractAddress());
        entInfo.setAccessStatus(AccessStatus.PASS.getValue());
        entInfo.setRemark(param.getRemark());
        entInfoMapper.update(entInfo, new LambdaUpdateWrapper<EntInfo>().eq(EntInfo::getUserId, accessEnt.getId()));
        // 更新角色信息
        UserRole userRole = new UserRole();
        userRole.setRoleId(Long.valueOf(RoleEnums.RECYCLE.getKey()));
        userRoleMapper.update(userRole, new LambdaUpdateWrapper<UserRole>()
                .eq(UserRole::getUserId, accessEnt.getId())
                .eq(UserRole::getRoleId, RoleEnums.AUDIT.getKey()));
    }

    @Override
    public void accessReject(EntAccessAuditParam param, String currentUserName) {
        // 获取对应企业的用户信息
        User accessEnt = userMapper.selectOne(new LambdaUpdateWrapper<User>().eq(User::getUserName, param.getUserName()));
        EntInfo entInfo = new EntInfo();
        entInfo.setAccessStatus(AccessStatus.REJECT.getValue());
        entInfo.setRemark(param.getRemark());
        // 更新准入状态和备注
        entInfoMapper.update(entInfo, new LambdaUpdateWrapper<EntInfo>().eq(EntInfo::getUserId, accessEnt.getId()));
    }

    @Override
    public AccessInfoDTO getAccessInfo(String userName) throws Exception {
        User user = userMapper.selectByUserName(userName);
        EntInfo entInfo = entInfoMapper.selectOne(new LambdaUpdateWrapper<EntInfo>().eq(EntInfo::getUserId, user.getId()));
        AccessInfoDTO result = new AccessInfoDTO();
        result.setAccessStatus(entInfo.getAccessStatus());
        result.setEntName(user.getName());
        if (result.getAccessStatus().equals(AccessStatus.PASS.getValue())) {
            result.setAccessContractAddr(entInfo.getAccessContractAddr());
            BcosSDK bcosSDK = fiscoBcos.getBcosSDK();
            Client client = bcosSDK.getClient(1);
            CryptoSuite cryptoSuite = new CryptoSuite(CryptoType.ECDSA_TYPE);
            CryptoKeyPair currentKeyPair = cryptoSuite.getKeyPairFactory().createKeyPair(user.getPrivateKey());
            // 查询合约信息
            EntAccess entAccess = EntAccess.load(result.getAccessContractAddr(), client, currentKeyPair);
            Tuple5<String, String, String, String, BigInteger> info = entAccess.getAccessInfo();
            // _enterpriseAddr, _name, _remark, _approvalAddr, _accessTime
            // 审批机构地址
            result.setApprovalEntAddr(info.getValue4());
            result.setRemark(info.getValue3());
            // 填充审批机构信息
            User approval = userMapper.selectOne(new LambdaUpdateWrapper<User>().eq(User::getAddr, result.getApprovalEntAddr()));
            result.setApprovalEntName(approval.getName());
        } else if (result.getAccessStatus().equals(AccessStatus.REJECT.getValue())) {
            // 审批拒绝的备注从表里取，因为没有创建合约
            result.setRemark(entInfo.getRemark());
        }
        return result;
    }

    @Override
    public MongoFile downloadFile(String userName) {
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getUserName, userName));
        List<UserFile> files = userFileMapper.selectList(new LambdaQueryWrapper<UserFile>().eq(UserFile::getUserId, user.getId()));
        String fileId = files.get(0).getFileId();
        // 获取Binary文件
        Optional<MongoFile> file = mongoFileRepository.findById(fileId);
        if (file.isPresent()) {
            MongoFile mongoFile = file.get();
            if(Objects.isNull(mongoFile.getContent())){
                file = this.getGridFsFileById(fileId);
            }
        }
        return file.get();
    }

    private Optional<MongoFile> getGridFsFileById(String id){
        MongoFile mongoFile = mongoTemplate.findById(id , MongoFile.class );
        if(Objects.nonNull(mongoFile)){
            Query gridQuery = new Query().addCriteria(Criteria.where("filename").is(mongoFile.getGridFsId()));
            try {
                // 根据id查询文件
                GridFSFile fsFile = gridFsTemplate.findOne(gridQuery);
                // 打开流下载对象
                GridFSDownloadStream in = gridFSBucket.openDownloadStream(fsFile.getObjectId());
                if(in.getGridFSFile().getLength() > 0){
                    // 获取流对象
                    GridFsResource resource = new GridFsResource(fsFile, in);
                    // 获取数据
                    mongoFile.setContent(new Binary(resource.getInputStream().readAllBytes()));
                    return Optional.of(mongoFile);
                }else {
                    return Optional.empty();
                }
            }catch (IOException e){
                log.error("获取MongoDB大文件失败", e);
            }
        }

        return Optional.empty();
    }

    private MongoFile uploadFile(MultipartFile file, String userId) throws Exception {
        if (file.getSize() > 16777216) {
            return this.saveGridFsFile(file, userId);
        } else {
            return this.saveBinaryFile(file, userId);
        }
    }

    /**
     * 保存GridFs文件（大文件）
     * @param file
     * @return
     * @throws Exception
     */
    private MongoFile saveGridFsFile(MultipartFile file, String userName) throws Exception {
        String suffix = getFileSuffix(file);
        String fileName = userName + "_" + file.getOriginalFilename();
        gridFsTemplate.store(file.getInputStream(), fileName, file.getContentType());
        String md5 = DigestUtils.md5DigestAsHex(file.getInputStream());

        MongoFile mongoFile = mongoTemplate.save(
                MongoFile.builder()
                        .fileName(file.getOriginalFilename())
                        .fileSize(file.getSize())
                        .contentType(file.getContentType())
                        .uploadDate(new Date())
                        .suffix(suffix)
                        .md5(md5)
                        .gridFsId(fileName)
                        .build()
        );

        return mongoFile;
    }

    /**
     * 保存Binary文件（小文件）
     * @param file
     * @return
     * @throws Exception
     */
    private MongoFile saveBinaryFile(MultipartFile file, String userName) throws Exception {
        String suffix = getFileSuffix(file);
        String fileName = userName + "_" + file.getOriginalFilename();
        String md5 = DigestUtils.md5DigestAsHex(file.getInputStream());

        MongoFile mongoFile = mongoFileRepository.save(
                MongoFile.builder()
                        .fileName(fileName)
                        .fileSize(file.getSize())
                        .content(new Binary(file.getBytes()))
                        .contentType(file.getContentType())
                        .uploadDate(new Date())
                        .suffix(suffix)
                        .md5(md5)
                        .build()
        );

        return mongoFile;
    }

    /**
     * 获取文件后缀
     * @param file
     * @return
     */
    private String getFileSuffix(MultipartFile file) {
        String suffix = "";
        if (Objects.requireNonNull(file.getOriginalFilename()).contains(".")) {
            suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        }
        return suffix;
    }
}
