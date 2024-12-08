package szu.zhl.test1.utils; // 定义包名

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.commons.codec.digest.DigestUtils;
import szu.zhl.test1.entity.LoginData;
import szu.zhl.test1.entity.Pedersen; // 导入Pedersen类

import java.io.FileWriter;
import java.io.IOException; // 导入IOException类，用于处理IO异常
import java.nio.charset.StandardCharsets;
import java.nio.file.Files; // 导入Files类，用于文件操作
import java.nio.file.Paths; // 导入Paths类，用于路径操作
import java.math.BigInteger; // 导入BigInteger类，用于大整数运算
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import org.json.JSONObject; // 导入JSONObject类，用于JSON操作
import szu.zhl.test1.entity.ReceiveLoginData;

//
public class CreateProofFile { // 定义CreateProofFile类
    private String filepath = "C:/example/proof.json";

    private int DIM = 128; // 定义常量DIM，表示维度大小
    private final BigInteger G = new BigInteger("56544564"); // 定义常量G，作为大整数
    private final BigInteger H = new BigInteger("237684576"); // 定义常量H，作为大整数

    private  Random RANDOM = new Random(); // 创建Random对象，用于生成随机数

    // 将大整数转换为指定长度的十六进制字符串
    public  String intToHex(BigInteger value, int length) {
        String hex = value.toString(16); // 将大整数转换为十六进制字符串
        int padding = length - hex.length(); // 计算需要填充的零的数量
        if (padding > 0) {
            hex = "0x" + "0".repeat(padding) + hex; // 如果需要，填充零
        } else {
            hex = "0x" + hex; // 否则直接加上前缀
        }
        return hex; // 返回十六进制字符串
    }


    // 生成包含新嵌入、随机数和承诺的JSON字符串
    public Map<String, List<BigInteger>> getRandomNewFeatures(ReceiveLoginData loginData) {
        System.out.println("G：" + G);
        System.out.println("H：" + H);
        Pedersen code = new Pedersen(G, H); // 创建Pedersen对象
        List<String> embNewStr = loginData.getNewfaceinfo(); // 使用LoginData中的newfaceinfo
        List<BigInteger> embNew = embNewStr.stream()
                                           .map(Double::parseDouble) // 将字符串转换为Double
                                           .map(d -> BigInteger.valueOf(Math.round(d))) // 转换为BigInteger
                                           .collect(Collectors.toList());

        List<BigInteger> randNew = new ArrayList<>(DIM); // 创建randNew列表
        List<BigInteger> cmtNew = new ArrayList<>(DIM); // 创建cmtNew列表
        //测试
//        randNew.add(BigInteger.valueOf(9108));
//        randNew.add(BigInteger.valueOf(4508));

        for (int i = 0; i < DIM; i++) { // 遍历DIM次
            randNew.add(BigInteger.valueOf(RANDOM.nextInt(10000))); // 生成随机数并添加到 randNew randNew.add(BigInteger.valueOf(RANDOM.nextInt(10000)));
            cmtNew.add(code.commitment(embNew.get(i), randNew.get(i))); // 计算承诺并添加到cmtNew
        }

        // 将数据转换为HashMap
        Map<String, List<BigInteger>> resultMap = new HashMap<>();
        resultMap.put("embNew", embNew);
        resultMap.put("randNew", randNew);
        resultMap.put("cmtNew", cmtNew);

        // 打印数组
        System.out.println("embNew: " + embNew);
        System.out.println("randNew: " + randNew);
        System.out.println("cmtNew: " + cmtNew);

        return resultMap;
    }

    // 生成证明文件





            public Map<String, Object> GenerateProofString(Map<String, Object> olddata, ReceiveLoginData logindata) {
                // 初始化数组
                BigInteger[] emb_eq_old = new BigInteger[DIM];
                BigInteger[] emb_eq_new = new BigInteger[DIM];
                BigInteger[] rand_eq_old = new BigInteger[DIM];
                BigInteger[] rand_eq_new = new BigInteger[DIM];
                BigInteger[] cmt_eq_old = new BigInteger[DIM];
                BigInteger[] cmt_eq_new = new BigInteger[DIM];
                BigInteger[] emb_mult = new BigInteger[DIM];
                BigInteger[] rand_mult = new BigInteger[DIM];
                BigInteger[] cmt_mult = new BigInteger[DIM];

                // 获取旧数据

               List<?> oldfaceinfoRaw = (List<?>) olddata.get("oldfaceinfo");
BigInteger[] emb_old = oldfaceinfoRaw.stream()
        .map(obj -> new BigInteger(obj.toString())) // 直接转换为 BigInteger
        .toArray(BigInteger[]::new);

List<?> randomnumbersRaw = (List<?>) olddata.get("randomnumbers");
BigInteger[] rand_old = randomnumbersRaw.stream()
        .map(obj -> new BigInteger(obj.toString())) // 直接转换为 BigInteger
        .toArray(BigInteger[]::new);

List<?> commitmentsRaw = (List<?>) olddata.get("commitment");
BigInteger[] old_cmt = commitmentsRaw.stream()
        .map(obj -> new BigInteger(obj.toString())) // 直接转换为 BigInteger
        .toArray(BigInteger[]::new);

                String id = (String) olddata.get("id");

                // 假设已经有了新生成的emb_new和rand_new数组
                Map<String, List<BigInteger>> proof = getRandomNewFeatures(logindata);
                BigInteger[] emb_new = proof.get("embNew").toArray(new BigInteger[0]);
                BigInteger[] rand_new = proof.get("randNew").toArray(new BigInteger[0]);
                BigInteger[] cmt_new = proof.get("cmtNew").toArray(new BigInteger[0]);
                // Print arrays
                System.out.println("emb_old: " + Arrays.toString(emb_old));
                System.out.println("old_cmt: " + Arrays.toString(old_cmt));
                System.out.println("rand_old: " + Arrays.toString(rand_old));



                Random random = new Random();
                Pedersen code = new Pedersen(G, H); // g和h需要预先定义
                //测试
//                BigInteger[] randValue1 = new BigInteger[DIM];
//                BigInteger[] randValue2 = new BigInteger[DIM];
//                BigInteger[] randValue3 = new BigInteger[DIM];
//                randValue1[0] = BigInteger.valueOf(948676);
//                randValue2[0] = BigInteger.valueOf(251100);
//                randValue3[0] = BigInteger.valueOf(144);
//                randValue1[1] = BigInteger.valueOf(858640);
//                randValue2[1] = BigInteger.valueOf(877514);
//                randValue3[1] = BigInteger.valueOf(814);



                // 生成证明
                for(int i = 0; i < DIM; i++) {
                    // 计算平方和乘积
                    emb_eq_old[i] = emb_old[i].multiply(emb_old[i]);
                    emb_eq_new[i] = emb_new[i].multiply(emb_new[i]);
                    emb_mult[i] = emb_old[i].multiply(emb_new[i]);

                    //生成随机数并计算随机因子
                    //1.修改过
                    BigInteger randValue1 = BigInteger.valueOf((long) Math.floor(Math.random() * 1000));
                    BigInteger randValue2 = BigInteger.valueOf((long) Math.floor(Math.random() * 1000));
                    BigInteger randValue3 = BigInteger.valueOf((long) Math.floor(Math.random() * 100));


                    rand_eq_old[i] = emb_old[i].multiply(rand_old[i]).add(randValue1);
                    rand_eq_new[i] = emb_new[i].multiply(rand_new[i]).add(randValue2);
                    rand_mult[i] = emb_new[i].multiply(rand_old[i]).add(randValue3);



                    //测试
//                    rand_eq_old[i] = emb_old[i].multiply(rand_old[i]).add(randValue1[i]);
//                    rand_eq_new[i] = emb_new[i].multiply(rand_new[i]).add(randValue2[i]);
//                    rand_mult[i] = emb_new[i].multiply(rand_old[i]).add(randValue3[i]);


                    // 计算承诺
                    // PedersenCommitment是一个实现承诺算法的类

                    cmt_eq_old[i] = code.commitment(emb_eq_old[i], rand_eq_old[i]);
                    cmt_eq_new[i] = code.commitment(emb_eq_new[i], rand_eq_new[i]);
                    cmt_mult[i] = code.commitment(emb_mult[i], rand_mult[i]);
                }


                // 打印所有数组的内容
                System.out.println("emb_eq_old: " + Arrays.toString(emb_eq_old));
                System.out.println("emb_eq_new: " + Arrays.toString(emb_eq_new));
                System.out.println("emb_mult: " + Arrays.toString(emb_mult));
                System.out.println("rand_eq_old: " + Arrays.toString(rand_eq_old));
                System.out.println("rand_eq_new: " + Arrays.toString(rand_eq_new));
                System.out.println("rand_mult: " + Arrays.toString(rand_mult));
                System.out.println("cmt_eq_old: " + Arrays.toString(cmt_eq_old));
                System.out.println("cmt_eq_new: " + Arrays.toString(cmt_eq_new));
                System.out.println("cmt_mult: " + Arrays.toString(cmt_mult));


//                    // 构造乘法同态的验证因子
                    BigInteger b1 = BigInteger.valueOf((long) Math.floor(Math.random() * 10000));
                    BigInteger b2 = BigInteger.valueOf((long) Math.floor(Math.random() * 10000));
                    BigInteger b3 = BigInteger.valueOf((long) Math.floor(Math.random() * 10000));
                    BigInteger b4 = BigInteger.valueOf((long) Math.floor(Math.random() * 10000));
                    BigInteger b5 = BigInteger.valueOf((long) Math.floor(Math.random() * 10000));
                    BigInteger b6 = BigInteger.valueOf((long) Math.floor(Math.random() * 10000));
                    BigInteger b7 = BigInteger.valueOf((long) Math.floor(Math.random() * 10000));
                    BigInteger a1 = code.commitment(b1, b2);
                    BigInteger a2 = code.commitment(b3, b4);


                //测试
//                BigInteger b1 = BigInteger.valueOf(3055);
//                BigInteger b2 = BigInteger.valueOf(5676);
//                BigInteger b3 = BigInteger.valueOf(614);
//                BigInteger b4 = BigInteger.valueOf(2421);
//                BigInteger b5 = BigInteger.valueOf(8101);
//                BigInteger b6 = BigInteger.valueOf(682);
//                BigInteger b7 = BigInteger.valueOf(4713);
//                BigInteger a1 = code.commitment(b1, b2);
//                BigInteger a2 = code.commitment(b3, b4);

                // 打印验证因子
                System.out.println("b1: " + b1);
                System.out.println("b2: " + b2);
                System.out.println("b3: " + b3);
                System.out.println("b4: " + b4);
                System.out.println("b5: " + b5);
                System.out.println("b6: " + b6);
                System.out.println("b7: " + b7);
                System.out.println("a1: " + a1);
                System.out.println("a2: " + a2);


                    // 初始化数组用于存储承诺值
                    BigInteger[] p1 = new BigInteger[DIM];
                    BigInteger[] p2 = new BigInteger[DIM];
                    BigInteger[] p3 = new BigInteger[DIM];


                // 生成承诺验证值
                for(int i = 0; i < DIM; i++) {
                    // 使用旧承诺值创建Pedersen实例
                    Pedersen temp_eq_old = new Pedersen(old_cmt[i], H);
                    p1[i] = temp_eq_old.commitment(b1, b5);

                    // 使用新承诺值创建Pedersen实例
                    Pedersen temp_eq_new = new Pedersen(cmt_new[i], H);
                    p2[i] = temp_eq_new.commitment(b3, b6);

                    // 复用旧承诺实例生成p3
                    p3[i] = temp_eq_old.commitment(b3, b7);

                    // 调试输出
                    // logger.debug("p1: {}, p2: {}, p3: {}", p1[i], p2[i], p3[i]);
                }


                // 打印数组
                System.out.println("p1: " + Arrays.toString(p1));
                System.out.println("p2: " + Arrays.toString(p2));
                System.out.println("p3: " + Arrays.toString(p3));

                // 初始化十六进制字符串数组
                String[] cmt_old_json = new String[DIM];
                String[] cmt_new_json = new String[DIM];
                String[] cmt_eq_old_json = new String[DIM];
                String[] cmt_eq_new_json = new String[DIM];
                String[] cmt_mult_json = new String[DIM];

// 转换所有承诺值为十六进制字符串
                for(int i = 0; i < DIM; i++) {
                    // 使用intToHex方法转换每个BigInteger值为十六进制字符串
                    cmt_old_json[i] = intToHex(old_cmt[i], 16);
                    cmt_new_json[i] = intToHex(cmt_new[i], 16);
                    cmt_eq_old_json[i] = intToHex(cmt_eq_old[i], 16);
                    cmt_eq_new_json[i] = intToHex(cmt_eq_new[i], 16);
                    cmt_mult_json[i] = intToHex(cmt_mult[i], 16);
                }

                // 使用 Arrays.toString() 打印数组
                System.out.println("cmt_old_json: " + Arrays.toString(cmt_old_json));
                System.out.println("cmt_new_json: " + Arrays.toString(cmt_new_json));
                System.out.println("cmt_eq_old_json: " + Arrays.toString(cmt_eq_old_json));
                System.out.println("cmt_eq_new_json: " + Arrays.toString(cmt_eq_new_json));
                System.out.println("cmt_mult_json: " + Arrays.toString(cmt_mult_json));


                // 创建 SimpleDateFormat 对象，指定所需的日期格式
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                 // 测试：使用 Calendar 设置特定的日期和时间
//        Calendar calendar = Calendar.getInstance();
//        calendar.set(Calendar.YEAR, 2024);
//        calendar.set(Calendar.MONTH, Calendar.NOVEMBER); // 月份从0开始，11表示12月
//        calendar.set(Calendar.DAY_OF_MONTH, 17);
//        calendar.set(Calendar.HOUR_OF_DAY, 13);
//        calendar.set(Calendar.MINUTE, 26);
//        calendar.set(Calendar.SECOND, 54);
//
//        // 获取指定日期的 Date 对象
//        Date specificDate = calendar.getTime();

        // 格式化指定日期
        String t = sdf.format(new Date());

                //String t = sdf.format(new Date());


/// 构建challenge对象
            // 使用 LinkedHashMap 保持顺序
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("cmt_old_json", Arrays.asList(cmt_old_json));
        map.put("cmt_new_json", Arrays.asList(cmt_new_json));
        map.put("cmt_eq_old_json", Arrays.asList(cmt_eq_old_json));
        map.put("cmt_eq_new_json", Arrays.asList(cmt_eq_new_json));
        map.put("cmt_mult_json", Arrays.asList(cmt_mult_json));
        map.put("id", id);
        map.put("t", t);

        // 打印 LinkedHashMap 内容以验证顺序
        System.out.println("LinkedHashMap content: " + map);

        // 使用 Jackson 序列化为 JSON 字符串
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, false);
                String test = null;
                try {
                    test = objectMapper.writeValueAsString(map);
                    System.out.println("JSON String: " + test);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }

                // 打印 JSON 字符串
        System.out.println("challenge: " + test);

        // 计算 SHA-256 哈希
        String hash = DigestUtils.sha256Hex(test);
        System.out.println("hash: " + hash);



//                // 转换为JSON字符串
//                String test = challenge.toString(2);


// 计算SHA256哈希
//                String hash = DigestUtils.sha256Hex(test);
//                System.out.println("SHA-256 Hash: " + hash);

// 计算最终的e值
                BigInteger e = new BigInteger(hash, 16).mod(BigInteger.valueOf(5000));
                System.out.println("e: " + e);


                // 初始化辅助证明因子数组
                BigInteger[] z1 = new BigInteger[DIM];
                BigInteger[] z2 = new BigInteger[DIM];
                BigInteger[] z3 = new BigInteger[DIM];
                BigInteger[] z4 = new BigInteger[DIM];
                BigInteger[] z5 = new BigInteger[DIM];
                BigInteger[] z6 = new BigInteger[DIM];
                BigInteger[] z7 = new BigInteger[DIM];

// 生成辅助证明因子
                for(int i = 0; i < DIM; i++) {
                    z1[i] = b1.add(e.multiply(emb_old[i]));
                    z2[i] = b2.add(e.multiply(rand_old[i]));
                    z3[i] = b3.add(e.multiply(emb_new[i]));
                    z4[i] = b4.add(e.multiply(rand_new[i]));

                    // 计算z5: b5 + e * (rand_eq_old[i] - emb_old[i] * rand_old[i])
                    BigInteger temp5 = emb_old[i].multiply(rand_old[i]);
                    z5[i] = b5.add(e.multiply(rand_eq_old[i].subtract(temp5)));

                    // 计算z6: b6 + e * (rand_eq_new[i] - emb_new[i] * rand_new[i])
                    BigInteger temp6 = emb_new[i].multiply(rand_new[i]);
                    z6[i] = b6.add(e.multiply(rand_eq_new[i].subtract(temp6)));

                    // 计算z7: b7 + e * (rand_mult[i] - emb_new[i] * rand_old[i])
                    BigInteger temp7 = emb_new[i].multiply(rand_old[i]);
                    z7[i] = b7.add(e.multiply(rand_mult[i].subtract(temp7)));
                }

                // 使用 Arrays.toString() 打印数组
                System.out.println("z1: " + Arrays.toString(z1));
                System.out.println("z2: " + Arrays.toString(z2));
                System.out.println("z3: " + Arrays.toString(z3));
                System.out.println("z4: " + Arrays.toString(z4));
                System.out.println("z5: " + Arrays.toString(z5));
                System.out.println("z6: " + Arrays.toString(z6));
                System.out.println("z7: " + Arrays.toString(z7));



                // 计算生物特征编码间的欧式距离
                BigInteger emb_dist = BigInteger.ZERO;
                BigInteger rand_dist = BigInteger.ZERO;
                for (int i = 0; i < DIM; i++) {
                    // 计算特征向量的欧氏距离
                    BigInteger diff = emb_new[i].subtract(emb_old[i]);
                    emb_dist = emb_dist.add(diff.multiply(diff));

                    // 计算随机数的距离
                    rand_dist = rand_dist.add(
                            rand_eq_new[i].add(rand_eq_old[i]).subtract(
                                    rand_mult[i].multiply(BigInteger.valueOf(2))
                            )
                    );
                }

                // 打印 BigInteger 变量
                System.out.println("emb_dist: " + emb_dist);
                System.out.println("rand_dist: " + rand_dist);
// 生成距离的承诺
                BigInteger cmt_dist = code.commitment(emb_dist, rand_dist);
                System.out.println("cmt_dist: " + cmt_dist);



                // 创建数组存储十六进制字符串
                String[] a_json = new String[2];
                a_json[0] = intToHex(a1, 16);
                a_json[1] = intToHex(a2, 16);
                System.out.println("a_json: " + Arrays.toString(a_json));

                String[] z_json = new String[DIM * 7];
                String[] p_json = new String[DIM * 3];


// 填充数组
                for (int i = 0; i < DIM; i++) {
                    // z 数组的七个部分
                    z_json[i] = intToHex(z1[i], 16);
                    z_json[DIM + i] = intToHex(z2[i], 16);
                    z_json[2 * DIM + i] = intToHex(z3[i], 16);
                    z_json[3 * DIM + i] = intToHex(z4[i], 16);
                    z_json[4 * DIM + i] = intToHex(z5[i], 16);
                    z_json[5 * DIM + i] = intToHex(z6[i], 16);
                    z_json[6 * DIM + i] = intToHex(z7[i], 16);

                    // p 数组的三个部分
                    p_json[i] = intToHex(p1[i], 16);
                    p_json[DIM + i] = intToHex(p2[i], 16);
                    p_json[2 * DIM + i] = intToHex(p3[i], 16);
                }
                System.out.println("z_json: " + Arrays.toString(z_json));
                System.out.println("p_json: " + Arrays.toString(p_json));


                Map<String, Object> new_proofs = new HashMap<>();
                Map<String, Object> proofInner = new HashMap<>();
                Map<String, String[]> cmt = new HashMap<>();

// 填充cmt对象
                cmt.put("cmt_eq_old_json", cmt_eq_old_json);
                cmt.put("cmt_eq_new_json", cmt_eq_new_json);
                cmt.put("cmt_mult_json", cmt_mult_json);

// 填充内部proof对象
                proofInner.put("emb", cmt_new_json);
                proofInner.put("cmt", cmt);
                proofInner.put("a", a_json);
                proofInner.put("z", z_json);
                proofInner.put("p", p_json);

// 填充主proof对象
                new_proofs.put("id", id);
                new_proofs.put("time", t);
                new_proofs.put("proof", proofInner);
                new_proofs.put("emb_dist", emb_dist);
                new_proofs.put("rand_dist", rand_dist);
                new_proofs.put("cmt_dist", cmt_dist);


                System.out.println("new_proofs: " + new_proofs);
                System.out.println("cmt: " + cmt);
                System.out.println("proofInner: " + proofInner);


                return new_proofs;

            }


            // 保存证明到文件
            // 版本2：更灵活的版本，可以指定文件名和格式化选项
            public void saveToJsonFile(Map<String, Object> data, boolean prettyPrint) {
                try {
                    ObjectMapper mapper = new ObjectMapper();
                    if (prettyPrint) {
                        mapper.enable(SerializationFeature.INDENT_OUTPUT);
                    }
                    String jsonString = mapper.writeValueAsString(data);

                    try (FileWriter file = new FileWriter(filepath)) {
                        file.write(jsonString);
                        System.out.println("JSON file has been saved: " + filepath);
                    }
                } catch (IOException e) {
                    System.out.println("An error occurred while writing JSON Object to File.");
                    e.printStackTrace();
                }
            }

    public static void main(String[] args) {
        CreateProofFile proofFile = new CreateProofFile();
        ReadFile readFile = new ReadFile();
        HashMap<String, Object> oldmap = readFile.readJsonFileToMap();
        for (Map.Entry<String, Object> entry : oldmap.entrySet()) {
            // 打印每个键值对
            System.out.println("Key: " + entry.getKey() + ", Value: " + entry.getValue());
        }

        ReceiveLoginData loginData = new ReceiveLoginData();
        loginData.setNewfaceinfo(Arrays.asList("741", "268"));
        Map<String, Object> proofjson = proofFile.GenerateProofString(oldmap, loginData);
        proofFile.saveToJsonFile(proofjson,  true);


    }
}