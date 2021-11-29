package com.media.controller;

import com.media.DemoApplication;
import com.media.bcos.client.PointControllerClient;
import com.media.dao.AutoReturnPoints;
import com.media.dao.ExtractMark;
import com.media.pojo.CommonResult;
import com.media.pojo.ImageKey;
import com.media.pojo.MarkResult;
import com.media.pojo.TradeHistory;
import com.media.service.ImageKeyService;
import com.media.service.TradeHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

@RestController
@RequestMapping("/fisco-bcos")
public class FiscoController {

    private PointControllerClient pointControllerClient = DemoApplication.pointControllerClient;
    private Timer timer;

    @Autowired
    TradeHistoryService iTradeHistoryService;
    @Autowired
    ImageKeyService iImageKeyService;


    /**
     * 确权信息上链
     *
     * @param phoneNumber
     * @param imgName
     * @param key
     * @return
     */
    @PostMapping("/confirm")
    @ResponseBody
    public CommonResult confirmInfo(@RequestParam(value = "phoneNumber") long phoneNumber,
                                    @RequestParam(value = "imgName") String imgName,
                                    @RequestParam(value = "key") String key) {
        if (pointControllerClient.recordEvidence(phoneNumber, 1, imgName, key)) {
            System.out.println("确权信息上链成功");
            return new CommonResult(true, "确权信息上链成功");
        } else {
            System.out.println("确权信息上链失败");
            return new CommonResult(false, "确权信息上链失败", null);
        }
    }

    /**
     * 查询---积分
     *
     * @param uid : 用户id
     * @return : points
     */
    @PostMapping("/getMyPoints")
    @ResponseBody
    public CommonResult getMyPoints(@RequestParam(value = "uid") Integer uid) {
        int points = pointControllerClient.queryPoint(uid);
        if (points != -1) {
            return new CommonResult(true, "查询成功", points);
        } else {
            return new CommonResult(false, "查询失败", null);
        }
    }

    /**
     * 查询---txt的哈希
     *
     * @param key : txtName(xxx.txt)
     * @return ：hash
     */
    @PostMapping("/getTxtHash")
    @ResponseBody
    public CommonResult getTxtHash(@RequestParam(value = "txtName") String key) {
        String hash = pointControllerClient.queryHash(key);
        if (!hash.equals("")) {
            return new CommonResult(true, "查询hash成功", hash);
        } else {
            return new CommonResult(false, "查询hash失败", hash);
        }
    }

    /**
     * 上链---注册信息上链
     *
     * @param uid : uid
     * @return
     */
    @PostMapping("/register")
    @ResponseBody
    public CommonResult register(@RequestParam(value = "uid") Integer uid) {
        if (pointControllerClient.registerId(uid)) {
            System.out.println("注册信息上链成功");
            return new CommonResult(true, "注册信息上链成功");
        } else {
            System.out.println("注册信息上链失败");
            return new CommonResult(false, "注册信息上链失败");
        }
    }

    /**
     * 上链---冻结积分
     *
     * @param uid   ：买家uid
     * @param price : 图片价格
     * @return
     */
    @PostMapping("/freezePoints")
    @ResponseBody
    public CommonResult freezePoints(@RequestParam(value = "purchaserUid") Integer uid,
                                     @RequestParam(value = "imgPrice") Integer price) {
        String result = pointControllerClient.freezeAccount(uid, price);
        if (result.equals("冻结成功")) {
            timer = new Timer();
            timer.schedule(new AutoReturnPoints(uid), 30000);
            return new CommonResult(true, "冻结成功");
        } else if (result.equals("已经冻结")) {
            return new CommonResult(false, "已经是冻结状态");
        } else {
            return new CommonResult(false, "冻结失败");
        }
    }

    /**
     * 上链---解冻积分
     *
     * @param uid : 买方uid
     * @return
     */
    @PostMapping("/returnPoints")
    @ResponseBody
    public CommonResult returnPoints(@RequestParam(value = "purchaserUid") Integer uid) {
        if (pointControllerClient.returnPoint(uid)) {
            if (timer != null) {
                timer.cancel();
            }
            return new CommonResult(true, "解冻成功");
        } else {
            return new CommonResult(false, "解冻失败");
        }
    }

    /**
     * 上链---交易成功
     *
     * @param tradeHistory
     * @return
     */
    @RequestMapping("/succeedTrade")
    @ResponseBody
    public CommonResult succeedTrade(@RequestBody TradeHistory tradeHistory) {
        if (pointControllerClient.succeedTrade(tradeHistory.getPurchaseUid(), tradeHistory.getSellerUid())) {
            if (timer != null) {
                timer.cancel();
            }
            // 上链成功, 入库
            try {
                iTradeHistoryService.save(tradeHistory);
                return new CommonResult(true, "交易成功");
            } catch (Exception e) {
                return new CommonResult(false, "交易失败---入库异常");
            }
        } else {
            return new CommonResult(false, "交易失败");
        }
    }

    /**
     * 上链---上架信息
     *
     * @param key   : Hash对应的key
     * @param value : Hash对应的value
     */
    @PostMapping("/sell")
    @ResponseBody
    public CommonResult sell(@RequestParam(value = "txtName") String key, @RequestParam(value = "txtHash") String value) {
        if (pointControllerClient.putImgOnTheShelf(key, value)) {
            return new CommonResult(true, "上链成功---上架");
        } else {
            return new CommonResult(false, "上链失败---上架");
        }
    }

    /**
     * 普通交易
     *
     * @param purchaserUid : 买家uid
     * @param sellerUid    : 卖家uid
     * @param imgPrice     : 价格
     * @return
     */
    @PostMapping("/normalTrade")
    @ResponseBody
    public CommonResult normalPurchase(@RequestParam(value = "purchaserUid") Integer purchaserUid,
                                       @RequestParam(value = "sellerUid") Integer sellerUid,
                                       @RequestParam(value = "imgPrice") Integer imgPrice) {
        if (pointControllerClient.normalTrade(purchaserUid, sellerUid, imgPrice)) {
            return new CommonResult(true, "普通交易成功");
        } else {
            return new CommonResult(false, "普通交易失败");
        }
    }


    /**
     * 上链---侵权检测结果
     *
     * @param file
     * @return
     */
    @PostMapping("/infringement")
    @ResponseBody
    public CommonResult infringement(@RequestParam(value = "uid") long uid,
                                     @RequestParam(value = "phoneNumber") long phoneNumber,
                                     @RequestParam(value = "file") MultipartFile file) {
        // 先保存图片
        String filePath = file.getOriginalFilename() + "";
        System.out.println("图片路径为：" + filePath);
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(filePath);
            fos.write(file.getBytes());
            // 保存成功, 进行水印提取
            ExtractMark extractMark = new ExtractMark(filePath);
            MarkResult markResult = extractMark.extract();
            String robustSeq = markResult.getRobustSeq();
            String ownerPhone = robustSeq.substring(0, 11);
            System.out.println(robustSeq);
            if (!ownerPhone.equals(phoneNumber + "")) {
                return new CommonResult(false, "未检测到侵权", null);
                /*if (pointControllerClient.recordResult(uid, "未检测到侵权", null)) {
                    return new CommonResult(false, "未检测到侵权", null);
                } else {
                    return new CommonResult(false, "区块链通信失败-1", null);
                }*/
            }
            List<String> zeroSeq = markResult.getZeroSeq();
            List<ImageKey> imageKeyList = iImageKeyService.findImageKeyByPhoneNumber(Long.valueOf(ownerPhone));
            List<ImageKey> text = new ArrayList<>();
            // 切割数据库key 每个对比16次
            for (int i = 0; i < imageKeyList.size(); i++) {
                String dateKey = imageKeyList.get(i).getImgKey().trim();
                dateKey = dateKey.replaceAll("\n", "");
                dateKey = dateKey.replaceAll("\r", "");
                int index = dateKey.length() / 136;
                int tag = 0;
                for (int k = 0; k < zeroSeq.size(); k++) {
                    String zeroI = zeroSeq.get(k);
                    int start = 0;
                    int end = 136;
                    for (int j = 0; j < index; j++) {
                        if (zeroI.equals(dateKey.substring(start, end))) {
                            tag++;
                            if (tag == 4) {
                                text.add(imageKeyList.get(i));
                            }
                            break;
                            //text.add(imageKeyList.get(i));
                            //return new CommonResult(true, "该图片侵权", imageKeyList.get(i));
                        } else {
                            start += 136;
                            end += 136;
                        }
                    }

                }
                System.out.println(tag);
            }
            if (text.size() > 0) {
                return new CommonResult(true, "检测到侵权", text);
                /*if (pointControllerClient.recordResult(uid, "检测到侵权", text.get(0).getImgName())) {
                    return new CommonResult(true, "检测到侵权", text);
                } else {
                    return new CommonResult(false, "区块链通信失败-2", null);
                }*/
            } else {
                return new CommonResult(false, "未检测到侵权", null);
                /*if (pointControllerClient.recordResult(uid, "未检测到侵权", null)) {
                    return new CommonResult(false, "未检测到侵权", null);
                } else {
                    return new CommonResult(false, "区块链通信失败-3", null);
                }*/
            }
        } catch (IOException e) {
            return new CommonResult(false, "图片保存失败");
            //e.printStackTrace();
        }
    }
}
