package szu.zhl.test1.utils;

import java.util.List;
import java.util.stream.Collectors;

public class DistancemoreThanThreshold {

    public static boolean isDistanceLessThanThreshold(List<String> emb_new_str, List<String> emb_old_str) {
        int dim = 128;
        double threshold = 0.75;  // 欧氏距离阈值

        // 将字符串列表转换为 Double 列表
        List<Double> emb_new = emb_new_str.stream()
                                          .map(Double::parseDouble)
                                          .collect(Collectors.toList());
        List<Double> emb_old = emb_old_str.stream()
                                          .map(Double::parseDouble)
                                          .collect(Collectors.toList());

        // 计算欧氏距离
        double squaredSum = 0.0;

        for (int i = 0; i < dim; i++) {
            double newVal = emb_new.get(i);
            double oldVal = emb_old.get(i);
            squaredSum += Math.pow(newVal - oldVal, 2);
        }

        double euclideanDistance = Math.sqrt(squaredSum);

        System.out.println("欧氏距离: " + euclideanDistance);
        double similarity = 1 / (euclideanDistance + 1);
        System.out.println("相似度: " + similarity);

        return similarity > threshold;  //
    }

    public static void main(String[] args) {
        // 测试用例
        List<String> emb_new_str = List.of("-0.14594994485378265", "0.05070602893829346", "-0.04750529304146767" /* ... */);
        List<String> emb_old_str = List.of("-0.06875241547822952", "0.10010932385921478", "0.03299073502421379" /* ... */);

        boolean result = isDistanceLessThanThreshold(emb_new_str, emb_old_str);
        System.out.println("距离是否小于阈值: " + result);
    }
}