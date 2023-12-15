package bcosServer;

import static io.grpc.stub.ClientCalls.asyncUnaryCall;
import static io.grpc.stub.ClientCalls.asyncServerStreamingCall;
import static io.grpc.stub.ClientCalls.asyncClientStreamingCall;
import static io.grpc.stub.ClientCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ClientCalls.blockingUnaryCall;
import static io.grpc.stub.ClientCalls.blockingServerStreamingCall;
import static io.grpc.stub.ClientCalls.futureUnaryCall;
import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ServerCalls.asyncUnaryCall;
import static io.grpc.stub.ServerCalls.asyncServerStreamingCall;
import static io.grpc.stub.ServerCalls.asyncClientStreamingCall;
import static io.grpc.stub.ServerCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 0.15.0)",
    comments = "Source: connect.proto")
public class CreditCertGrpc {

  private CreditCertGrpc() {}

  public static final String SERVICE_NAME = "CreditCert";

  // Static method descriptors that strictly reflect the proto.
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<bcosServer.StuInfoInitInput,
      bcosServer.StuInfoInitOutput> METHOD_STU_INFO_INIT =
      io.grpc.MethodDescriptor.create(
          io.grpc.MethodDescriptor.MethodType.UNARY,
          generateFullMethodName(
              "CreditCert", "StuInfoInit"),
          io.grpc.protobuf.ProtoUtils.marshaller(bcosServer.StuInfoInitInput.getDefaultInstance()),
          io.grpc.protobuf.ProtoUtils.marshaller(bcosServer.StuInfoInitOutput.getDefaultInstance()));
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<bcosServer.StudentInfoUpdateInput,
      bcosServer.StuInfoUpdateOutput> METHOD_STU_INFO_UPDATE =
      io.grpc.MethodDescriptor.create(
          io.grpc.MethodDescriptor.MethodType.UNARY,
          generateFullMethodName(
              "CreditCert", "StuInfoUpdate"),
          io.grpc.protobuf.ProtoUtils.marshaller(bcosServer.StudentInfoUpdateInput.getDefaultInstance()),
          io.grpc.protobuf.ProtoUtils.marshaller(bcosServer.StuInfoUpdateOutput.getDefaultInstance()));
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<bcosServer.StuGradeRecodeInput,
      bcosServer.StuGradeRecodeOutput> METHOD_STU_GRADE_RECODE =
      io.grpc.MethodDescriptor.create(
          io.grpc.MethodDescriptor.MethodType.UNARY,
          generateFullMethodName(
              "CreditCert", "StuGradeRecode"),
          io.grpc.protobuf.ProtoUtils.marshaller(bcosServer.StuGradeRecodeInput.getDefaultInstance()),
          io.grpc.protobuf.ProtoUtils.marshaller(bcosServer.StuGradeRecodeOutput.getDefaultInstance()));
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<bcosServer.StuGradeUpdateInput,
      bcosServer.StuGradeUpdateOutput> METHOD_STU_GRADE_UPDATE =
      io.grpc.MethodDescriptor.create(
          io.grpc.MethodDescriptor.MethodType.UNARY,
          generateFullMethodName(
              "CreditCert", "StuGradeUpdate"),
          io.grpc.protobuf.ProtoUtils.marshaller(bcosServer.StuGradeUpdateInput.getDefaultInstance()),
          io.grpc.protobuf.ProtoUtils.marshaller(bcosServer.StuGradeUpdateOutput.getDefaultInstance()));
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<bcosServer.ActivityRegisterInput,
      bcosServer.ActivityRegisterOutput> METHOD_ACTIVITY_REGISTER =
      io.grpc.MethodDescriptor.create(
          io.grpc.MethodDescriptor.MethodType.UNARY,
          generateFullMethodName(
              "CreditCert", "ActivityRegister"),
          io.grpc.protobuf.ProtoUtils.marshaller(bcosServer.ActivityRegisterInput.getDefaultInstance()),
          io.grpc.protobuf.ProtoUtils.marshaller(bcosServer.ActivityRegisterOutput.getDefaultInstance()));
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<bcosServer.ActivityInfoUpdateInput,
      bcosServer.ActivityInfoUpdateOutput> METHOD_ACTIVITY_INFO_UPDATE =
      io.grpc.MethodDescriptor.create(
          io.grpc.MethodDescriptor.MethodType.UNARY,
          generateFullMethodName(
              "CreditCert", "ActivityInfoUpdate"),
          io.grpc.protobuf.ProtoUtils.marshaller(bcosServer.ActivityInfoUpdateInput.getDefaultInstance()),
          io.grpc.protobuf.ProtoUtils.marshaller(bcosServer.ActivityInfoUpdateOutput.getDefaultInstance()));
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<bcosServer.ActivityGradeRecodeInput,
      bcosServer.ActivityGradeRecodeOutput> METHOD_ACTIVITY_GRADE_RECODE =
      io.grpc.MethodDescriptor.create(
          io.grpc.MethodDescriptor.MethodType.UNARY,
          generateFullMethodName(
              "CreditCert", "ActivityGradeRecode"),
          io.grpc.protobuf.ProtoUtils.marshaller(bcosServer.ActivityGradeRecodeInput.getDefaultInstance()),
          io.grpc.protobuf.ProtoUtils.marshaller(bcosServer.ActivityGradeRecodeOutput.getDefaultInstance()));
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<bcosServer.CertInfoInitInput,
      bcosServer.CertInfoInitOutput> METHOD_CERT_INFO_INIT =
      io.grpc.MethodDescriptor.create(
          io.grpc.MethodDescriptor.MethodType.UNARY,
          generateFullMethodName(
              "CreditCert", "CertInfoInit"),
          io.grpc.protobuf.ProtoUtils.marshaller(bcosServer.CertInfoInitInput.getDefaultInstance()),
          io.grpc.protobuf.ProtoUtils.marshaller(bcosServer.CertInfoInitOutput.getDefaultInstance()));
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<bcosServer.CertInfoUpdateInput,
      bcosServer.CertInfoUpdateOutput> METHOD_CERT_INFO_UPDATE =
      io.grpc.MethodDescriptor.create(
          io.grpc.MethodDescriptor.MethodType.UNARY,
          generateFullMethodName(
              "CreditCert", "CertInfoUpdate"),
          io.grpc.protobuf.ProtoUtils.marshaller(bcosServer.CertInfoUpdateInput.getDefaultInstance()),
          io.grpc.protobuf.ProtoUtils.marshaller(bcosServer.CertInfoUpdateOutput.getDefaultInstance()));
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<bcosServer.StuInfoQueryInput,
      bcosServer.StuInfoQueryOutput> METHOD_STU_INFO_QUERY =
      io.grpc.MethodDescriptor.create(
          io.grpc.MethodDescriptor.MethodType.UNARY,
          generateFullMethodName(
              "CreditCert", "StuInfoQuery"),
          io.grpc.protobuf.ProtoUtils.marshaller(bcosServer.StuInfoQueryInput.getDefaultInstance()),
          io.grpc.protobuf.ProtoUtils.marshaller(bcosServer.StuInfoQueryOutput.getDefaultInstance()));
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<bcosServer.StuGradeQueryInput,
      bcosServer.StuGradeQueryOutput> METHOD_STU_GRADE_QUERY =
      io.grpc.MethodDescriptor.create(
          io.grpc.MethodDescriptor.MethodType.UNARY,
          generateFullMethodName(
              "CreditCert", "StuGradeQuery"),
          io.grpc.protobuf.ProtoUtils.marshaller(bcosServer.StuGradeQueryInput.getDefaultInstance()),
          io.grpc.protobuf.ProtoUtils.marshaller(bcosServer.StuGradeQueryOutput.getDefaultInstance()));
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<bcosServer.StuActQueryInput,
      bcosServer.StuActQueryOutput> METHOD_STU_ACT_QUERY =
      io.grpc.MethodDescriptor.create(
          io.grpc.MethodDescriptor.MethodType.UNARY,
          generateFullMethodName(
              "CreditCert", "StuActQuery"),
          io.grpc.protobuf.ProtoUtils.marshaller(bcosServer.StuActQueryInput.getDefaultInstance()),
          io.grpc.protobuf.ProtoUtils.marshaller(bcosServer.StuActQueryOutput.getDefaultInstance()));
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<bcosServer.StuCertQueryInput,
      bcosServer.StuCertQueryOutput> METHOD_STU_CERT_QUERY =
      io.grpc.MethodDescriptor.create(
          io.grpc.MethodDescriptor.MethodType.UNARY,
          generateFullMethodName(
              "CreditCert", "StuCertQuery"),
          io.grpc.protobuf.ProtoUtils.marshaller(bcosServer.StuCertQueryInput.getDefaultInstance()),
          io.grpc.protobuf.ProtoUtils.marshaller(bcosServer.StuCertQueryOutput.getDefaultInstance()));
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<bcosServer.ActInfoQueryInput,
      bcosServer.ActInfoQueryOutput> METHOD_ACT_INFO_QUERY =
      io.grpc.MethodDescriptor.create(
          io.grpc.MethodDescriptor.MethodType.UNARY,
          generateFullMethodName(
              "CreditCert", "ActInfoQuery"),
          io.grpc.protobuf.ProtoUtils.marshaller(bcosServer.ActInfoQueryInput.getDefaultInstance()),
          io.grpc.protobuf.ProtoUtils.marshaller(bcosServer.ActInfoQueryOutput.getDefaultInstance()));
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<bcosServer.AllCreditQueryInput,
      bcosServer.AllCreditQueryOutput> METHOD_ALL_CREDIT_QUERY =
      io.grpc.MethodDescriptor.create(
          io.grpc.MethodDescriptor.MethodType.UNARY,
          generateFullMethodName(
              "CreditCert", "AllCreditQuery"),
          io.grpc.protobuf.ProtoUtils.marshaller(bcosServer.AllCreditQueryInput.getDefaultInstance()),
          io.grpc.protobuf.ProtoUtils.marshaller(bcosServer.AllCreditQueryOutput.getDefaultInstance()));
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<bcosServer.CreditEvaluationInput,
      bcosServer.CreditEvaluationOutput> METHOD_CREDIT_EVALUATION =
      io.grpc.MethodDescriptor.create(
          io.grpc.MethodDescriptor.MethodType.UNARY,
          generateFullMethodName(
              "CreditCert", "CreditEvaluation"),
          io.grpc.protobuf.ProtoUtils.marshaller(bcosServer.CreditEvaluationInput.getDefaultInstance()),
          io.grpc.protobuf.ProtoUtils.marshaller(bcosServer.CreditEvaluationOutput.getDefaultInstance()));

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static CreditCertStub newStub(io.grpc.Channel channel) {
    return new CreditCertStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static CreditCertBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new CreditCertBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary and streaming output calls on the service
   */
  public static CreditCertFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new CreditCertFutureStub(channel);
  }

  /**
   */
  @java.lang.Deprecated public static interface CreditCert {

    /**
     * <pre>
     *学生信息初始化
     * </pre>
     */
    public void stuInfoInit(bcosServer.StuInfoInitInput request,
        io.grpc.stub.StreamObserver<bcosServer.StuInfoInitOutput> responseObserver);

    /**
     * <pre>
     *学生信息更新
     * </pre>
     */
    public void stuInfoUpdate(bcosServer.StudentInfoUpdateInput request,
        io.grpc.stub.StreamObserver<bcosServer.StuInfoUpdateOutput> responseObserver);

    /**
     * <pre>
     *各学期成绩录入
     * </pre>
     */
    public void stuGradeRecode(bcosServer.StuGradeRecodeInput request,
        io.grpc.stub.StreamObserver<bcosServer.StuGradeRecodeOutput> responseObserver);

    /**
     * <pre>
     *各学期成绩更新
     * </pre>
     */
    public void stuGradeUpdate(bcosServer.StuGradeUpdateInput request,
        io.grpc.stub.StreamObserver<bcosServer.StuGradeUpdateOutput> responseObserver);

    /**
     * <pre>
     *活动发布
     * </pre>
     */
    public void activityRegister(bcosServer.ActivityRegisterInput request,
        io.grpc.stub.StreamObserver<bcosServer.ActivityRegisterOutput> responseObserver);

    /**
     * <pre>
     *活动信息更新
     * </pre>
     */
    public void activityInfoUpdate(bcosServer.ActivityInfoUpdateInput request,
        io.grpc.stub.StreamObserver<bcosServer.ActivityInfoUpdateOutput> responseObserver);

    /**
     * <pre>
     *活动成绩录入
     * </pre>
     */
    public void activityGradeRecode(bcosServer.ActivityGradeRecodeInput request,
        io.grpc.stub.StreamObserver<bcosServer.ActivityGradeRecodeOutput> responseObserver);

    /**
     * <pre>
     *证书信息初始化
     * </pre>
     */
    public void certInfoInit(bcosServer.CertInfoInitInput request,
        io.grpc.stub.StreamObserver<bcosServer.CertInfoInitOutput> responseObserver);

    /**
     * <pre>
     *证书信息更新
     * </pre>
     */
    public void certInfoUpdate(bcosServer.CertInfoUpdateInput request,
        io.grpc.stub.StreamObserver<bcosServer.CertInfoUpdateOutput> responseObserver);

    /**
     * <pre>
     *学生信息查询
     * </pre>
     */
    public void stuInfoQuery(bcosServer.StuInfoQueryInput request,
        io.grpc.stub.StreamObserver<bcosServer.StuInfoQueryOutput> responseObserver);

    /**
     * <pre>
     *学生成绩查询
     * </pre>
     */
    public void stuGradeQuery(bcosServer.StuGradeQueryInput request,
        io.grpc.stub.StreamObserver<bcosServer.StuGradeQueryOutput> responseObserver);

    /**
     * <pre>
     *学生活动查询
     * </pre>
     */
    public void stuActQuery(bcosServer.StuActQueryInput request,
        io.grpc.stub.StreamObserver<bcosServer.StuActQueryOutput> responseObserver);

    /**
     * <pre>
     *学生证书查询
     * </pre>
     */
    public void stuCertQuery(bcosServer.StuCertQueryInput request,
        io.grpc.stub.StreamObserver<bcosServer.StuCertQueryOutput> responseObserver);

    /**
     * <pre>
     *活动信息查询
     * </pre>
     */
    public void actInfoQuery(bcosServer.ActInfoQueryInput request,
        io.grpc.stub.StreamObserver<bcosServer.ActInfoQueryOutput> responseObserver);

    /**
     * <pre>
     *学分查询
     * </pre>
     */
    public void allCreditQuery(bcosServer.AllCreditQueryInput request,
        io.grpc.stub.StreamObserver<bcosServer.AllCreditQueryOutput> responseObserver);

    /**
     * <pre>
     *信用评估
     * </pre>
     */
    public void creditEvaluation(bcosServer.CreditEvaluationInput request,
        io.grpc.stub.StreamObserver<bcosServer.CreditEvaluationOutput> responseObserver);
  }

  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1469")
  public static abstract class CreditCertImplBase implements CreditCert, io.grpc.BindableService {

    @java.lang.Override
    public void stuInfoInit(bcosServer.StuInfoInitInput request,
        io.grpc.stub.StreamObserver<bcosServer.StuInfoInitOutput> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_STU_INFO_INIT, responseObserver);
    }

    @java.lang.Override
    public void stuInfoUpdate(bcosServer.StudentInfoUpdateInput request,
        io.grpc.stub.StreamObserver<bcosServer.StuInfoUpdateOutput> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_STU_INFO_UPDATE, responseObserver);
    }

    @java.lang.Override
    public void stuGradeRecode(bcosServer.StuGradeRecodeInput request,
        io.grpc.stub.StreamObserver<bcosServer.StuGradeRecodeOutput> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_STU_GRADE_RECODE, responseObserver);
    }

    @java.lang.Override
    public void stuGradeUpdate(bcosServer.StuGradeUpdateInput request,
        io.grpc.stub.StreamObserver<bcosServer.StuGradeUpdateOutput> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_STU_GRADE_UPDATE, responseObserver);
    }

    @java.lang.Override
    public void activityRegister(bcosServer.ActivityRegisterInput request,
        io.grpc.stub.StreamObserver<bcosServer.ActivityRegisterOutput> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_ACTIVITY_REGISTER, responseObserver);
    }

    @java.lang.Override
    public void activityInfoUpdate(bcosServer.ActivityInfoUpdateInput request,
        io.grpc.stub.StreamObserver<bcosServer.ActivityInfoUpdateOutput> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_ACTIVITY_INFO_UPDATE, responseObserver);
    }

    @java.lang.Override
    public void activityGradeRecode(bcosServer.ActivityGradeRecodeInput request,
        io.grpc.stub.StreamObserver<bcosServer.ActivityGradeRecodeOutput> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_ACTIVITY_GRADE_RECODE, responseObserver);
    }

    @java.lang.Override
    public void certInfoInit(bcosServer.CertInfoInitInput request,
        io.grpc.stub.StreamObserver<bcosServer.CertInfoInitOutput> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_CERT_INFO_INIT, responseObserver);
    }

    @java.lang.Override
    public void certInfoUpdate(bcosServer.CertInfoUpdateInput request,
        io.grpc.stub.StreamObserver<bcosServer.CertInfoUpdateOutput> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_CERT_INFO_UPDATE, responseObserver);
    }

    @java.lang.Override
    public void stuInfoQuery(bcosServer.StuInfoQueryInput request,
        io.grpc.stub.StreamObserver<bcosServer.StuInfoQueryOutput> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_STU_INFO_QUERY, responseObserver);
    }

    @java.lang.Override
    public void stuGradeQuery(bcosServer.StuGradeQueryInput request,
        io.grpc.stub.StreamObserver<bcosServer.StuGradeQueryOutput> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_STU_GRADE_QUERY, responseObserver);
    }

    @java.lang.Override
    public void stuActQuery(bcosServer.StuActQueryInput request,
        io.grpc.stub.StreamObserver<bcosServer.StuActQueryOutput> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_STU_ACT_QUERY, responseObserver);
    }

    @java.lang.Override
    public void stuCertQuery(bcosServer.StuCertQueryInput request,
        io.grpc.stub.StreamObserver<bcosServer.StuCertQueryOutput> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_STU_CERT_QUERY, responseObserver);
    }

    @java.lang.Override
    public void actInfoQuery(bcosServer.ActInfoQueryInput request,
        io.grpc.stub.StreamObserver<bcosServer.ActInfoQueryOutput> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_ACT_INFO_QUERY, responseObserver);
    }

    @java.lang.Override
    public void allCreditQuery(bcosServer.AllCreditQueryInput request,
        io.grpc.stub.StreamObserver<bcosServer.AllCreditQueryOutput> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_ALL_CREDIT_QUERY, responseObserver);
    }

    @java.lang.Override
    public void creditEvaluation(bcosServer.CreditEvaluationInput request,
        io.grpc.stub.StreamObserver<bcosServer.CreditEvaluationOutput> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_CREDIT_EVALUATION, responseObserver);
    }

    @java.lang.Override public io.grpc.ServerServiceDefinition bindService() {
      return CreditCertGrpc.bindService(this);
    }
  }

  /**
   */
  @java.lang.Deprecated public static interface CreditCertBlockingClient {

    /**
     * <pre>
     *学生信息初始化
     * </pre>
     */
    public bcosServer.StuInfoInitOutput stuInfoInit(bcosServer.StuInfoInitInput request);

    /**
     * <pre>
     *学生信息更新
     * </pre>
     */
    public bcosServer.StuInfoUpdateOutput stuInfoUpdate(bcosServer.StudentInfoUpdateInput request);

    /**
     * <pre>
     *各学期成绩录入
     * </pre>
     */
    public bcosServer.StuGradeRecodeOutput stuGradeRecode(bcosServer.StuGradeRecodeInput request);

    /**
     * <pre>
     *各学期成绩更新
     * </pre>
     */
    public bcosServer.StuGradeUpdateOutput stuGradeUpdate(bcosServer.StuGradeUpdateInput request);

    /**
     * <pre>
     *活动发布
     * </pre>
     */
    public bcosServer.ActivityRegisterOutput activityRegister(bcosServer.ActivityRegisterInput request);

    /**
     * <pre>
     *活动信息更新
     * </pre>
     */
    public bcosServer.ActivityInfoUpdateOutput activityInfoUpdate(bcosServer.ActivityInfoUpdateInput request);

    /**
     * <pre>
     *活动成绩录入
     * </pre>
     */
    public bcosServer.ActivityGradeRecodeOutput activityGradeRecode(bcosServer.ActivityGradeRecodeInput request);

    /**
     * <pre>
     *证书信息初始化
     * </pre>
     */
    public bcosServer.CertInfoInitOutput certInfoInit(bcosServer.CertInfoInitInput request);

    /**
     * <pre>
     *证书信息更新
     * </pre>
     */
    public bcosServer.CertInfoUpdateOutput certInfoUpdate(bcosServer.CertInfoUpdateInput request);

    /**
     * <pre>
     *学生信息查询
     * </pre>
     */
    public bcosServer.StuInfoQueryOutput stuInfoQuery(bcosServer.StuInfoQueryInput request);

    /**
     * <pre>
     *学生成绩查询
     * </pre>
     */
    public bcosServer.StuGradeQueryOutput stuGradeQuery(bcosServer.StuGradeQueryInput request);

    /**
     * <pre>
     *学生活动查询
     * </pre>
     */
    public bcosServer.StuActQueryOutput stuActQuery(bcosServer.StuActQueryInput request);

    /**
     * <pre>
     *学生证书查询
     * </pre>
     */
    public bcosServer.StuCertQueryOutput stuCertQuery(bcosServer.StuCertQueryInput request);

    /**
     * <pre>
     *活动信息查询
     * </pre>
     */
    public bcosServer.ActInfoQueryOutput actInfoQuery(bcosServer.ActInfoQueryInput request);

    /**
     * <pre>
     *学分查询
     * </pre>
     */
    public bcosServer.AllCreditQueryOutput allCreditQuery(bcosServer.AllCreditQueryInput request);

    /**
     * <pre>
     *信用评估
     * </pre>
     */
    public bcosServer.CreditEvaluationOutput creditEvaluation(bcosServer.CreditEvaluationInput request);
  }

  /**
   */
  @java.lang.Deprecated public static interface CreditCertFutureClient {

    /**
     * <pre>
     *学生信息初始化
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<bcosServer.StuInfoInitOutput> stuInfoInit(
        bcosServer.StuInfoInitInput request);

    /**
     * <pre>
     *学生信息更新
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<bcosServer.StuInfoUpdateOutput> stuInfoUpdate(
        bcosServer.StudentInfoUpdateInput request);

    /**
     * <pre>
     *各学期成绩录入
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<bcosServer.StuGradeRecodeOutput> stuGradeRecode(
        bcosServer.StuGradeRecodeInput request);

    /**
     * <pre>
     *各学期成绩更新
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<bcosServer.StuGradeUpdateOutput> stuGradeUpdate(
        bcosServer.StuGradeUpdateInput request);

    /**
     * <pre>
     *活动发布
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<bcosServer.ActivityRegisterOutput> activityRegister(
        bcosServer.ActivityRegisterInput request);

    /**
     * <pre>
     *活动信息更新
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<bcosServer.ActivityInfoUpdateOutput> activityInfoUpdate(
        bcosServer.ActivityInfoUpdateInput request);

    /**
     * <pre>
     *活动成绩录入
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<bcosServer.ActivityGradeRecodeOutput> activityGradeRecode(
        bcosServer.ActivityGradeRecodeInput request);

    /**
     * <pre>
     *证书信息初始化
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<bcosServer.CertInfoInitOutput> certInfoInit(
        bcosServer.CertInfoInitInput request);

    /**
     * <pre>
     *证书信息更新
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<bcosServer.CertInfoUpdateOutput> certInfoUpdate(
        bcosServer.CertInfoUpdateInput request);

    /**
     * <pre>
     *学生信息查询
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<bcosServer.StuInfoQueryOutput> stuInfoQuery(
        bcosServer.StuInfoQueryInput request);

    /**
     * <pre>
     *学生成绩查询
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<bcosServer.StuGradeQueryOutput> stuGradeQuery(
        bcosServer.StuGradeQueryInput request);

    /**
     * <pre>
     *学生活动查询
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<bcosServer.StuActQueryOutput> stuActQuery(
        bcosServer.StuActQueryInput request);

    /**
     * <pre>
     *学生证书查询
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<bcosServer.StuCertQueryOutput> stuCertQuery(
        bcosServer.StuCertQueryInput request);

    /**
     * <pre>
     *活动信息查询
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<bcosServer.ActInfoQueryOutput> actInfoQuery(
        bcosServer.ActInfoQueryInput request);

    /**
     * <pre>
     *学分查询
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<bcosServer.AllCreditQueryOutput> allCreditQuery(
        bcosServer.AllCreditQueryInput request);

    /**
     * <pre>
     *信用评估
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<bcosServer.CreditEvaluationOutput> creditEvaluation(
        bcosServer.CreditEvaluationInput request);
  }

  public static class CreditCertStub extends io.grpc.stub.AbstractStub<CreditCertStub>
      implements CreditCert {
    private CreditCertStub(io.grpc.Channel channel) {
      super(channel);
    }

    private CreditCertStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected CreditCertStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new CreditCertStub(channel, callOptions);
    }

    @java.lang.Override
    public void stuInfoInit(bcosServer.StuInfoInitInput request,
        io.grpc.stub.StreamObserver<bcosServer.StuInfoInitOutput> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_STU_INFO_INIT, getCallOptions()), request, responseObserver);
    }

    @java.lang.Override
    public void stuInfoUpdate(bcosServer.StudentInfoUpdateInput request,
        io.grpc.stub.StreamObserver<bcosServer.StuInfoUpdateOutput> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_STU_INFO_UPDATE, getCallOptions()), request, responseObserver);
    }

    @java.lang.Override
    public void stuGradeRecode(bcosServer.StuGradeRecodeInput request,
        io.grpc.stub.StreamObserver<bcosServer.StuGradeRecodeOutput> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_STU_GRADE_RECODE, getCallOptions()), request, responseObserver);
    }

    @java.lang.Override
    public void stuGradeUpdate(bcosServer.StuGradeUpdateInput request,
        io.grpc.stub.StreamObserver<bcosServer.StuGradeUpdateOutput> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_STU_GRADE_UPDATE, getCallOptions()), request, responseObserver);
    }

    @java.lang.Override
    public void activityRegister(bcosServer.ActivityRegisterInput request,
        io.grpc.stub.StreamObserver<bcosServer.ActivityRegisterOutput> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_ACTIVITY_REGISTER, getCallOptions()), request, responseObserver);
    }

    @java.lang.Override
    public void activityInfoUpdate(bcosServer.ActivityInfoUpdateInput request,
        io.grpc.stub.StreamObserver<bcosServer.ActivityInfoUpdateOutput> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_ACTIVITY_INFO_UPDATE, getCallOptions()), request, responseObserver);
    }

    @java.lang.Override
    public void activityGradeRecode(bcosServer.ActivityGradeRecodeInput request,
        io.grpc.stub.StreamObserver<bcosServer.ActivityGradeRecodeOutput> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_ACTIVITY_GRADE_RECODE, getCallOptions()), request, responseObserver);
    }

    @java.lang.Override
    public void certInfoInit(bcosServer.CertInfoInitInput request,
        io.grpc.stub.StreamObserver<bcosServer.CertInfoInitOutput> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_CERT_INFO_INIT, getCallOptions()), request, responseObserver);
    }

    @java.lang.Override
    public void certInfoUpdate(bcosServer.CertInfoUpdateInput request,
        io.grpc.stub.StreamObserver<bcosServer.CertInfoUpdateOutput> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_CERT_INFO_UPDATE, getCallOptions()), request, responseObserver);
    }

    @java.lang.Override
    public void stuInfoQuery(bcosServer.StuInfoQueryInput request,
        io.grpc.stub.StreamObserver<bcosServer.StuInfoQueryOutput> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_STU_INFO_QUERY, getCallOptions()), request, responseObserver);
    }

    @java.lang.Override
    public void stuGradeQuery(bcosServer.StuGradeQueryInput request,
        io.grpc.stub.StreamObserver<bcosServer.StuGradeQueryOutput> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_STU_GRADE_QUERY, getCallOptions()), request, responseObserver);
    }

    @java.lang.Override
    public void stuActQuery(bcosServer.StuActQueryInput request,
        io.grpc.stub.StreamObserver<bcosServer.StuActQueryOutput> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_STU_ACT_QUERY, getCallOptions()), request, responseObserver);
    }

    @java.lang.Override
    public void stuCertQuery(bcosServer.StuCertQueryInput request,
        io.grpc.stub.StreamObserver<bcosServer.StuCertQueryOutput> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_STU_CERT_QUERY, getCallOptions()), request, responseObserver);
    }

    @java.lang.Override
    public void actInfoQuery(bcosServer.ActInfoQueryInput request,
        io.grpc.stub.StreamObserver<bcosServer.ActInfoQueryOutput> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_ACT_INFO_QUERY, getCallOptions()), request, responseObserver);
    }

    @java.lang.Override
    public void allCreditQuery(bcosServer.AllCreditQueryInput request,
        io.grpc.stub.StreamObserver<bcosServer.AllCreditQueryOutput> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_ALL_CREDIT_QUERY, getCallOptions()), request, responseObserver);
    }

    @java.lang.Override
    public void creditEvaluation(bcosServer.CreditEvaluationInput request,
        io.grpc.stub.StreamObserver<bcosServer.CreditEvaluationOutput> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_CREDIT_EVALUATION, getCallOptions()), request, responseObserver);
    }
  }

  public static class CreditCertBlockingStub extends io.grpc.stub.AbstractStub<CreditCertBlockingStub>
      implements CreditCertBlockingClient {
    private CreditCertBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private CreditCertBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected CreditCertBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new CreditCertBlockingStub(channel, callOptions);
    }

    @java.lang.Override
    public bcosServer.StuInfoInitOutput stuInfoInit(bcosServer.StuInfoInitInput request) {
      return blockingUnaryCall(
          getChannel(), METHOD_STU_INFO_INIT, getCallOptions(), request);
    }

    @java.lang.Override
    public bcosServer.StuInfoUpdateOutput stuInfoUpdate(bcosServer.StudentInfoUpdateInput request) {
      return blockingUnaryCall(
          getChannel(), METHOD_STU_INFO_UPDATE, getCallOptions(), request);
    }

    @java.lang.Override
    public bcosServer.StuGradeRecodeOutput stuGradeRecode(bcosServer.StuGradeRecodeInput request) {
      return blockingUnaryCall(
          getChannel(), METHOD_STU_GRADE_RECODE, getCallOptions(), request);
    }

    @java.lang.Override
    public bcosServer.StuGradeUpdateOutput stuGradeUpdate(bcosServer.StuGradeUpdateInput request) {
      return blockingUnaryCall(
          getChannel(), METHOD_STU_GRADE_UPDATE, getCallOptions(), request);
    }

    @java.lang.Override
    public bcosServer.ActivityRegisterOutput activityRegister(bcosServer.ActivityRegisterInput request) {
      return blockingUnaryCall(
          getChannel(), METHOD_ACTIVITY_REGISTER, getCallOptions(), request);
    }

    @java.lang.Override
    public bcosServer.ActivityInfoUpdateOutput activityInfoUpdate(bcosServer.ActivityInfoUpdateInput request) {
      return blockingUnaryCall(
          getChannel(), METHOD_ACTIVITY_INFO_UPDATE, getCallOptions(), request);
    }

    @java.lang.Override
    public bcosServer.ActivityGradeRecodeOutput activityGradeRecode(bcosServer.ActivityGradeRecodeInput request) {
      return blockingUnaryCall(
          getChannel(), METHOD_ACTIVITY_GRADE_RECODE, getCallOptions(), request);
    }

    @java.lang.Override
    public bcosServer.CertInfoInitOutput certInfoInit(bcosServer.CertInfoInitInput request) {
      return blockingUnaryCall(
          getChannel(), METHOD_CERT_INFO_INIT, getCallOptions(), request);
    }

    @java.lang.Override
    public bcosServer.CertInfoUpdateOutput certInfoUpdate(bcosServer.CertInfoUpdateInput request) {
      return blockingUnaryCall(
          getChannel(), METHOD_CERT_INFO_UPDATE, getCallOptions(), request);
    }

    @java.lang.Override
    public bcosServer.StuInfoQueryOutput stuInfoQuery(bcosServer.StuInfoQueryInput request) {
      return blockingUnaryCall(
          getChannel(), METHOD_STU_INFO_QUERY, getCallOptions(), request);
    }

    @java.lang.Override
    public bcosServer.StuGradeQueryOutput stuGradeQuery(bcosServer.StuGradeQueryInput request) {
      return blockingUnaryCall(
          getChannel(), METHOD_STU_GRADE_QUERY, getCallOptions(), request);
    }

    @java.lang.Override
    public bcosServer.StuActQueryOutput stuActQuery(bcosServer.StuActQueryInput request) {
      return blockingUnaryCall(
          getChannel(), METHOD_STU_ACT_QUERY, getCallOptions(), request);
    }

    @java.lang.Override
    public bcosServer.StuCertQueryOutput stuCertQuery(bcosServer.StuCertQueryInput request) {
      return blockingUnaryCall(
          getChannel(), METHOD_STU_CERT_QUERY, getCallOptions(), request);
    }

    @java.lang.Override
    public bcosServer.ActInfoQueryOutput actInfoQuery(bcosServer.ActInfoQueryInput request) {
      return blockingUnaryCall(
          getChannel(), METHOD_ACT_INFO_QUERY, getCallOptions(), request);
    }

    @java.lang.Override
    public bcosServer.AllCreditQueryOutput allCreditQuery(bcosServer.AllCreditQueryInput request) {
      return blockingUnaryCall(
          getChannel(), METHOD_ALL_CREDIT_QUERY, getCallOptions(), request);
    }

    @java.lang.Override
    public bcosServer.CreditEvaluationOutput creditEvaluation(bcosServer.CreditEvaluationInput request) {
      return blockingUnaryCall(
          getChannel(), METHOD_CREDIT_EVALUATION, getCallOptions(), request);
    }
  }

  public static class CreditCertFutureStub extends io.grpc.stub.AbstractStub<CreditCertFutureStub>
      implements CreditCertFutureClient {
    private CreditCertFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private CreditCertFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected CreditCertFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new CreditCertFutureStub(channel, callOptions);
    }

    @java.lang.Override
    public com.google.common.util.concurrent.ListenableFuture<bcosServer.StuInfoInitOutput> stuInfoInit(
        bcosServer.StuInfoInitInput request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_STU_INFO_INIT, getCallOptions()), request);
    }

    @java.lang.Override
    public com.google.common.util.concurrent.ListenableFuture<bcosServer.StuInfoUpdateOutput> stuInfoUpdate(
        bcosServer.StudentInfoUpdateInput request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_STU_INFO_UPDATE, getCallOptions()), request);
    }

    @java.lang.Override
    public com.google.common.util.concurrent.ListenableFuture<bcosServer.StuGradeRecodeOutput> stuGradeRecode(
        bcosServer.StuGradeRecodeInput request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_STU_GRADE_RECODE, getCallOptions()), request);
    }

    @java.lang.Override
    public com.google.common.util.concurrent.ListenableFuture<bcosServer.StuGradeUpdateOutput> stuGradeUpdate(
        bcosServer.StuGradeUpdateInput request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_STU_GRADE_UPDATE, getCallOptions()), request);
    }

    @java.lang.Override
    public com.google.common.util.concurrent.ListenableFuture<bcosServer.ActivityRegisterOutput> activityRegister(
        bcosServer.ActivityRegisterInput request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_ACTIVITY_REGISTER, getCallOptions()), request);
    }

    @java.lang.Override
    public com.google.common.util.concurrent.ListenableFuture<bcosServer.ActivityInfoUpdateOutput> activityInfoUpdate(
        bcosServer.ActivityInfoUpdateInput request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_ACTIVITY_INFO_UPDATE, getCallOptions()), request);
    }

    @java.lang.Override
    public com.google.common.util.concurrent.ListenableFuture<bcosServer.ActivityGradeRecodeOutput> activityGradeRecode(
        bcosServer.ActivityGradeRecodeInput request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_ACTIVITY_GRADE_RECODE, getCallOptions()), request);
    }

    @java.lang.Override
    public com.google.common.util.concurrent.ListenableFuture<bcosServer.CertInfoInitOutput> certInfoInit(
        bcosServer.CertInfoInitInput request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_CERT_INFO_INIT, getCallOptions()), request);
    }

    @java.lang.Override
    public com.google.common.util.concurrent.ListenableFuture<bcosServer.CertInfoUpdateOutput> certInfoUpdate(
        bcosServer.CertInfoUpdateInput request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_CERT_INFO_UPDATE, getCallOptions()), request);
    }

    @java.lang.Override
    public com.google.common.util.concurrent.ListenableFuture<bcosServer.StuInfoQueryOutput> stuInfoQuery(
        bcosServer.StuInfoQueryInput request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_STU_INFO_QUERY, getCallOptions()), request);
    }

    @java.lang.Override
    public com.google.common.util.concurrent.ListenableFuture<bcosServer.StuGradeQueryOutput> stuGradeQuery(
        bcosServer.StuGradeQueryInput request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_STU_GRADE_QUERY, getCallOptions()), request);
    }

    @java.lang.Override
    public com.google.common.util.concurrent.ListenableFuture<bcosServer.StuActQueryOutput> stuActQuery(
        bcosServer.StuActQueryInput request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_STU_ACT_QUERY, getCallOptions()), request);
    }

    @java.lang.Override
    public com.google.common.util.concurrent.ListenableFuture<bcosServer.StuCertQueryOutput> stuCertQuery(
        bcosServer.StuCertQueryInput request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_STU_CERT_QUERY, getCallOptions()), request);
    }

    @java.lang.Override
    public com.google.common.util.concurrent.ListenableFuture<bcosServer.ActInfoQueryOutput> actInfoQuery(
        bcosServer.ActInfoQueryInput request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_ACT_INFO_QUERY, getCallOptions()), request);
    }

    @java.lang.Override
    public com.google.common.util.concurrent.ListenableFuture<bcosServer.AllCreditQueryOutput> allCreditQuery(
        bcosServer.AllCreditQueryInput request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_ALL_CREDIT_QUERY, getCallOptions()), request);
    }

    @java.lang.Override
    public com.google.common.util.concurrent.ListenableFuture<bcosServer.CreditEvaluationOutput> creditEvaluation(
        bcosServer.CreditEvaluationInput request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_CREDIT_EVALUATION, getCallOptions()), request);
    }
  }

  @java.lang.Deprecated public static abstract class AbstractCreditCert extends CreditCertImplBase {}

  private static final int METHODID_STU_INFO_INIT = 0;
  private static final int METHODID_STU_INFO_UPDATE = 1;
  private static final int METHODID_STU_GRADE_RECODE = 2;
  private static final int METHODID_STU_GRADE_UPDATE = 3;
  private static final int METHODID_ACTIVITY_REGISTER = 4;
  private static final int METHODID_ACTIVITY_INFO_UPDATE = 5;
  private static final int METHODID_ACTIVITY_GRADE_RECODE = 6;
  private static final int METHODID_CERT_INFO_INIT = 7;
  private static final int METHODID_CERT_INFO_UPDATE = 8;
  private static final int METHODID_STU_INFO_QUERY = 9;
  private static final int METHODID_STU_GRADE_QUERY = 10;
  private static final int METHODID_STU_ACT_QUERY = 11;
  private static final int METHODID_STU_CERT_QUERY = 12;
  private static final int METHODID_ACT_INFO_QUERY = 13;
  private static final int METHODID_ALL_CREDIT_QUERY = 14;
  private static final int METHODID_CREDIT_EVALUATION = 15;

  private static class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final CreditCert serviceImpl;
    private final int methodId;

    public MethodHandlers(CreditCert serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_STU_INFO_INIT:
          serviceImpl.stuInfoInit((bcosServer.StuInfoInitInput) request,
              (io.grpc.stub.StreamObserver<bcosServer.StuInfoInitOutput>) responseObserver);
          break;
        case METHODID_STU_INFO_UPDATE:
          serviceImpl.stuInfoUpdate((bcosServer.StudentInfoUpdateInput) request,
              (io.grpc.stub.StreamObserver<bcosServer.StuInfoUpdateOutput>) responseObserver);
          break;
        case METHODID_STU_GRADE_RECODE:
          serviceImpl.stuGradeRecode((bcosServer.StuGradeRecodeInput) request,
              (io.grpc.stub.StreamObserver<bcosServer.StuGradeRecodeOutput>) responseObserver);
          break;
        case METHODID_STU_GRADE_UPDATE:
          serviceImpl.stuGradeUpdate((bcosServer.StuGradeUpdateInput) request,
              (io.grpc.stub.StreamObserver<bcosServer.StuGradeUpdateOutput>) responseObserver);
          break;
        case METHODID_ACTIVITY_REGISTER:
          serviceImpl.activityRegister((bcosServer.ActivityRegisterInput) request,
              (io.grpc.stub.StreamObserver<bcosServer.ActivityRegisterOutput>) responseObserver);
          break;
        case METHODID_ACTIVITY_INFO_UPDATE:
          serviceImpl.activityInfoUpdate((bcosServer.ActivityInfoUpdateInput) request,
              (io.grpc.stub.StreamObserver<bcosServer.ActivityInfoUpdateOutput>) responseObserver);
          break;
        case METHODID_ACTIVITY_GRADE_RECODE:
          serviceImpl.activityGradeRecode((bcosServer.ActivityGradeRecodeInput) request,
              (io.grpc.stub.StreamObserver<bcosServer.ActivityGradeRecodeOutput>) responseObserver);
          break;
        case METHODID_CERT_INFO_INIT:
          serviceImpl.certInfoInit((bcosServer.CertInfoInitInput) request,
              (io.grpc.stub.StreamObserver<bcosServer.CertInfoInitOutput>) responseObserver);
          break;
        case METHODID_CERT_INFO_UPDATE:
          serviceImpl.certInfoUpdate((bcosServer.CertInfoUpdateInput) request,
              (io.grpc.stub.StreamObserver<bcosServer.CertInfoUpdateOutput>) responseObserver);
          break;
        case METHODID_STU_INFO_QUERY:
          serviceImpl.stuInfoQuery((bcosServer.StuInfoQueryInput) request,
              (io.grpc.stub.StreamObserver<bcosServer.StuInfoQueryOutput>) responseObserver);
          break;
        case METHODID_STU_GRADE_QUERY:
          serviceImpl.stuGradeQuery((bcosServer.StuGradeQueryInput) request,
              (io.grpc.stub.StreamObserver<bcosServer.StuGradeQueryOutput>) responseObserver);
          break;
        case METHODID_STU_ACT_QUERY:
          serviceImpl.stuActQuery((bcosServer.StuActQueryInput) request,
              (io.grpc.stub.StreamObserver<bcosServer.StuActQueryOutput>) responseObserver);
          break;
        case METHODID_STU_CERT_QUERY:
          serviceImpl.stuCertQuery((bcosServer.StuCertQueryInput) request,
              (io.grpc.stub.StreamObserver<bcosServer.StuCertQueryOutput>) responseObserver);
          break;
        case METHODID_ACT_INFO_QUERY:
          serviceImpl.actInfoQuery((bcosServer.ActInfoQueryInput) request,
              (io.grpc.stub.StreamObserver<bcosServer.ActInfoQueryOutput>) responseObserver);
          break;
        case METHODID_ALL_CREDIT_QUERY:
          serviceImpl.allCreditQuery((bcosServer.AllCreditQueryInput) request,
              (io.grpc.stub.StreamObserver<bcosServer.AllCreditQueryOutput>) responseObserver);
          break;
        case METHODID_CREDIT_EVALUATION:
          serviceImpl.creditEvaluation((bcosServer.CreditEvaluationInput) request,
              (io.grpc.stub.StreamObserver<bcosServer.CreditEvaluationOutput>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    return new io.grpc.ServiceDescriptor(SERVICE_NAME,
        METHOD_STU_INFO_INIT,
        METHOD_STU_INFO_UPDATE,
        METHOD_STU_GRADE_RECODE,
        METHOD_STU_GRADE_UPDATE,
        METHOD_ACTIVITY_REGISTER,
        METHOD_ACTIVITY_INFO_UPDATE,
        METHOD_ACTIVITY_GRADE_RECODE,
        METHOD_CERT_INFO_INIT,
        METHOD_CERT_INFO_UPDATE,
        METHOD_STU_INFO_QUERY,
        METHOD_STU_GRADE_QUERY,
        METHOD_STU_ACT_QUERY,
        METHOD_STU_CERT_QUERY,
        METHOD_ACT_INFO_QUERY,
        METHOD_ALL_CREDIT_QUERY,
        METHOD_CREDIT_EVALUATION);
  }

  @java.lang.Deprecated public static io.grpc.ServerServiceDefinition bindService(
      final CreditCert serviceImpl) {
    return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
        .addMethod(
          METHOD_STU_INFO_INIT,
          asyncUnaryCall(
            new MethodHandlers<
              bcosServer.StuInfoInitInput,
              bcosServer.StuInfoInitOutput>(
                serviceImpl, METHODID_STU_INFO_INIT)))
        .addMethod(
          METHOD_STU_INFO_UPDATE,
          asyncUnaryCall(
            new MethodHandlers<
              bcosServer.StudentInfoUpdateInput,
              bcosServer.StuInfoUpdateOutput>(
                serviceImpl, METHODID_STU_INFO_UPDATE)))
        .addMethod(
          METHOD_STU_GRADE_RECODE,
          asyncUnaryCall(
            new MethodHandlers<
              bcosServer.StuGradeRecodeInput,
              bcosServer.StuGradeRecodeOutput>(
                serviceImpl, METHODID_STU_GRADE_RECODE)))
        .addMethod(
          METHOD_STU_GRADE_UPDATE,
          asyncUnaryCall(
            new MethodHandlers<
              bcosServer.StuGradeUpdateInput,
              bcosServer.StuGradeUpdateOutput>(
                serviceImpl, METHODID_STU_GRADE_UPDATE)))
        .addMethod(
          METHOD_ACTIVITY_REGISTER,
          asyncUnaryCall(
            new MethodHandlers<
              bcosServer.ActivityRegisterInput,
              bcosServer.ActivityRegisterOutput>(
                serviceImpl, METHODID_ACTIVITY_REGISTER)))
        .addMethod(
          METHOD_ACTIVITY_INFO_UPDATE,
          asyncUnaryCall(
            new MethodHandlers<
              bcosServer.ActivityInfoUpdateInput,
              bcosServer.ActivityInfoUpdateOutput>(
                serviceImpl, METHODID_ACTIVITY_INFO_UPDATE)))
        .addMethod(
          METHOD_ACTIVITY_GRADE_RECODE,
          asyncUnaryCall(
            new MethodHandlers<
              bcosServer.ActivityGradeRecodeInput,
              bcosServer.ActivityGradeRecodeOutput>(
                serviceImpl, METHODID_ACTIVITY_GRADE_RECODE)))
        .addMethod(
          METHOD_CERT_INFO_INIT,
          asyncUnaryCall(
            new MethodHandlers<
              bcosServer.CertInfoInitInput,
              bcosServer.CertInfoInitOutput>(
                serviceImpl, METHODID_CERT_INFO_INIT)))
        .addMethod(
          METHOD_CERT_INFO_UPDATE,
          asyncUnaryCall(
            new MethodHandlers<
              bcosServer.CertInfoUpdateInput,
              bcosServer.CertInfoUpdateOutput>(
                serviceImpl, METHODID_CERT_INFO_UPDATE)))
        .addMethod(
          METHOD_STU_INFO_QUERY,
          asyncUnaryCall(
            new MethodHandlers<
              bcosServer.StuInfoQueryInput,
              bcosServer.StuInfoQueryOutput>(
                serviceImpl, METHODID_STU_INFO_QUERY)))
        .addMethod(
          METHOD_STU_GRADE_QUERY,
          asyncUnaryCall(
            new MethodHandlers<
              bcosServer.StuGradeQueryInput,
              bcosServer.StuGradeQueryOutput>(
                serviceImpl, METHODID_STU_GRADE_QUERY)))
        .addMethod(
          METHOD_STU_ACT_QUERY,
          asyncUnaryCall(
            new MethodHandlers<
              bcosServer.StuActQueryInput,
              bcosServer.StuActQueryOutput>(
                serviceImpl, METHODID_STU_ACT_QUERY)))
        .addMethod(
          METHOD_STU_CERT_QUERY,
          asyncUnaryCall(
            new MethodHandlers<
              bcosServer.StuCertQueryInput,
              bcosServer.StuCertQueryOutput>(
                serviceImpl, METHODID_STU_CERT_QUERY)))
        .addMethod(
          METHOD_ACT_INFO_QUERY,
          asyncUnaryCall(
            new MethodHandlers<
              bcosServer.ActInfoQueryInput,
              bcosServer.ActInfoQueryOutput>(
                serviceImpl, METHODID_ACT_INFO_QUERY)))
        .addMethod(
          METHOD_ALL_CREDIT_QUERY,
          asyncUnaryCall(
            new MethodHandlers<
              bcosServer.AllCreditQueryInput,
              bcosServer.AllCreditQueryOutput>(
                serviceImpl, METHODID_ALL_CREDIT_QUERY)))
        .addMethod(
          METHOD_CREDIT_EVALUATION,
          asyncUnaryCall(
            new MethodHandlers<
              bcosServer.CreditEvaluationInput,
              bcosServer.CreditEvaluationOutput>(
                serviceImpl, METHODID_CREDIT_EVALUATION)))
        .build();
  }
}
