package szu.blockchain.check.utils;






// 自定义异常类 BlogException
public class BlogException extends Exception {
    private final int errorCode; // 错误代码

    // 构造函数，接受错误代码和错误消息
    public BlogException(int errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    // 获取错误代码的方法
    public int getErrorCode() {
        return errorCode;
    }

    // 重写 toString 方法，提供更详细的异常信息
    @Override
    public String toString() {
        return "BlogException{" +
                "errorCode=" + errorCode +
                ", message=" + getMessage() +
                '}';
    }
}