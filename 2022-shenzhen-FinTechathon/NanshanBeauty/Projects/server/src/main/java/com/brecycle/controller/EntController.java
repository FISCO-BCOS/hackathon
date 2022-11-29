package com.brecycle.controller;

import com.brecycle.common.Response;
import com.brecycle.config.shiro.JWTConfig;
import com.brecycle.config.shiro.JwtTokenUtil;
import com.brecycle.entity.MongoFile;
import com.brecycle.entity.dto.*;
import com.brecycle.service.EntService;
import com.google.common.collect.Lists;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutionException;

/**
 * 机构准入
 *
 * @author cmgun
 */
@Api(tags = "机构准入")
@RestController
@RequestMapping("ent")
public class EntController {

    @Autowired
    EntService entService;

    @ApiOperation("准入申请")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "AUTHORIZE_TOKEN", value = "AUTHORIZE_TOKEN", dataType = "String", required = true)
    })
    @PostMapping(value = "/apply", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    Response apply(@ApiParam(value = "上传的⽂件") @RequestParam("file") MultipartFile file, HttpServletRequest request) throws Exception {
        String token = request.getHeader(JWTConfig.tokenHeader);
        String userName = JwtTokenUtil.getUsername(token);
        entService.apply(Lists.newArrayList(file), userName);
        return Response.success("操作成功");
    }



    @ApiOperation("查询企业列表")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "AUTHORIZE_TOKEN", value = "AUTHORIZE_TOKEN", dataType = "String", required = true)
    })
    @PostMapping("/list")
    Response list(@RequestBody @ApiParam(value = "参数", required = true) EntListParam param) {
        PageResult<EntListDTO> result = entService.getEntList(param);
        return Response.success("查询成功", result);
    }

    @ApiOperation("审批通过")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "AUTHORIZE_TOKEN", value = "AUTHORIZE_TOKEN", dataType = "String", required = true)
    })
    @PostMapping("/access/pass")
    Response pass(@RequestBody @ApiParam(value = "参数", required = true) EntAccessAuditParam param, HttpServletRequest request) throws Exception {
        String token = request.getHeader(JWTConfig.tokenHeader);
        String userName = JwtTokenUtil.getUsername(token);
        entService.accessPass(param, userName);
        return Response.success("操作成功");
    }

    @ApiOperation("审批拒绝")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "AUTHORIZE_TOKEN", value = "AUTHORIZE_TOKEN", dataType = "String", required = true)
    })
    @PostMapping("/access/reject")
    Response reject(@RequestBody @ApiParam(value = "参数", required = true) EntAccessAuditParam param, HttpServletRequest request) throws Exception {
        String token = request.getHeader(JWTConfig.tokenHeader);
        String userName = JwtTokenUtil.getUsername(token);
        entService.accessPass(param, userName);
        return Response.success("操作成功");
    }

    @ApiOperation("查询当前企业准入审批结果")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "AUTHORIZE_TOKEN", value = "AUTHORIZE_TOKEN", dataType = "String", required = true)
    })
    @PostMapping("/access/curInfo")
    Response getCurrentAccessInfo(HttpServletRequest request) throws Exception {
        String token = request.getHeader(JWTConfig.tokenHeader);
        String userName = JwtTokenUtil.getUsername(token);
        AccessInfoDTO result = entService.getAccessInfo(userName);
        return Response.success("查询成功", result);
    }

    /**
     * 单个文件下载
     * @return
     */
    @ApiOperation("文件下载")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "AUTHORIZE_TOKEN", value = "AUTHORIZE_TOKEN", dataType = "String", required = true)
    })
    @GetMapping("/download/{userName}")
    public ResponseEntity<Object> fileDownload(@PathVariable(name = "userName") String userName) throws UnsupportedEncodingException {
        MongoFile file = entService.downloadFile(userName);

        if (file != null) {
            String fileName = file.getFileName();
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;fileName=" + URLEncoder.encode(fileName,"utf-8"))
                    .header(HttpHeaders.CONTENT_TYPE, "multipart/form-data")
                    .header(HttpHeaders.CONTENT_LENGTH, file.getFileSize() + "").header("Connection", "close")
                    .body(file.getContent().getData());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("file does not exist");
        }
    }

}
