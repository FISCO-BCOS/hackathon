package bcosServer;

import bcosClient.CreditCertClient;
import entity.*;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import java.io.IOException;
import java.util.logging.Logger;

public class CreditCertServer {
    private static final Logger logger = Logger.getLogger(CreditCertServer.class.getName());
    private Server server;
    private void start() throws IOException {
        /* The port on which the server should run */
        int port = 50051;
        server = ServerBuilder.forPort(port)
                .addService(new CreditCertImpl()).build()
                .start();
        logger.info("Server started, listening on " + port);
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                // Use stderr here since the logger may have been reset by its JVM shutdown hook.
                System.err.println("*** shutting down gRPC server since JVM is shutting down");
                CreditCertServer.this.stop();
                System.err.println("*** server shut down");
            }
        });
    }

    private void stop() {
        if (server != null) {
            server.shutdown();
        }
    }

    private void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        final CreditCertServer creditCertServer = new CreditCertServer();
        creditCertServer.start();
        creditCertServer.blockUntilShutdown();
    }


    static class CreditCertImpl extends CreditCertGrpc.CreditCertImplBase {

        private CreditCertClient creditCertClient=new CreditCertClient();


        //学生信息初始化
        public void stuInfoInit(StuInfoInitInput req,StreamObserver<StuInfoInitOutput> responseObserver){
                int flag;
                flag=creditCertClient.stuInfoInit(req.getStuID(),req.getStuName(),req.getUsName(),req.getUsLevel(),
                        req.getMajor(),req.getExtInfo(),req.getTime(),req.getGrade());
                if(flag==0){
                    StuInfoInitOutput output = StuInfoInitOutput.newBuilder().setSuccess("success").build();
                    logger.info("init student success");
                    responseObserver.onNext(output);
                    responseObserver.onCompleted();
                }

        }


        //学生信息更新
        //stuInfoUpdate(long stuId, String stuName, String usName, String major, String extInfo, long time)
        public void stuInfoUpdate (StudentInfoUpdateInput req,StreamObserver<StuInfoUpdateOutput> responseObserver){
            int flag;
            flag=creditCertClient.stuInfoUpdate(req.getStuID(),req.getStuName(),req.getUsName(),req.getUsLevel(),req.getMajor(),
                    req.getExtInfo(),req.getTime(),req.getGrade());
            if(flag==0){
                StuInfoUpdateOutput output =StuInfoUpdateOutput.newBuilder().setSuccess("success").build();
                logger.info("update student success");
                responseObserver.onNext(output);
                responseObserver.onCompleted();
            }
        }


        //各学期成绩录入
        public void stuGradeRecode (StuGradeRecodeInput req,StreamObserver<StuGradeRecodeOutput> responseObserver) {
            int flag;
            flag=creditCertClient.stuGradeRecord(req.getStuID(),req.getStuName(),req.getGrade(),req.getAverageGrades()
            ,req.getObligatoryCredit(),req.getOptionalCredit(),req.getExtInfo(),req.getTime());
            if(flag==0){
                StuGradeRecodeOutput output =StuGradeRecodeOutput.newBuilder().setSuccess("success").build();
                logger.info("recode grade success");
                responseObserver.onNext(output);
                responseObserver.onCompleted();
            }
        }


        //各学期成绩更新
        //rpc StuGradeUpdate (StuGradeUpdateInput) returns (StuGradeUpdateOutput){}
        public void stuGradeUpdate (StuGradeUpdateInput req,StreamObserver<StuGradeUpdateOutput> responseObserver) {
            int flag;
            flag=creditCertClient.stuGradeRecord(req.getStuID(),req.getStuName(),req.getGrade(),req.getAverageGrades(),req.getObligatoryCredit(),
                    req.getOptionalCredit(),req.getExtInfo(),req.getTime());
            if(flag==0){
                StuGradeUpdateOutput output=StuGradeUpdateOutput.newBuilder().setSuccess("success").build();
                logger.info("update grade success");
                responseObserver.onNext(output);
                responseObserver.onCompleted();
            }
        }


        //活动发布
        //rpc ActivityRegister (ActivityRegisterInput) returns (ActivityRegisterOutput){}
        public void activityRegister (ActivityRegisterInput req,StreamObserver<ActivityRegisterOutput> responseObserver) {
            int flag;
            flag=creditCertClient.activityRegister(req.getActID(),req.getActName(),req.getOrganizer(),req.getStatus()
            ,req.getExtInfo(),req.getTime());
            if(flag==0){
                ActivityRegisterOutput output=ActivityRegisterOutput.newBuilder().setSuccess("success").build();
                logger.info("register activity success");
                responseObserver.onNext(output);
                responseObserver.onCompleted();
            }
        }


        //活动信息更新
        //rpc ActivityInfoUpdate (ActivityInfoUpdateInput) returns (ActivityInfoUpdateOutput){}
        public void activityInfoUpdate (ActivityInfoUpdateInput req,StreamObserver<ActivityInfoUpdateOutput> responseObserver) {
            int flag;
            flag=creditCertClient.activityInfoUpdate(req.getActID(),req.getActName(),req.getOrganizer(),req.getStatus(),
                    req.getExtInfo(),req.getTime());
            if(flag==0){
                ActivityInfoUpdateOutput output=ActivityInfoUpdateOutput.newBuilder().setSuccess("success").build();
                logger.info("update activity success");
                responseObserver.onNext(output);
                responseObserver.onCompleted();
            }
        }


        //活动成绩录入
        //rpc ActivityGradeRecode (ActivityGradeRecodeInput) returns (ActivityGradeRecodeOutput){}
        public void activityGradeRecode (ActivityGradeRecodeInput req,StreamObserver<ActivityGradeRecodeOutput> responseObserver) {
            int flag;
            flag=creditCertClient.activityGradeRecord(req.getActID(),req.getStuID(),req.getActName(),req.getStuName(),req.getExtInfo(),req.getTime(),req.getActSignature());
            if(flag==0){
                ActivityGradeRecodeOutput output=ActivityGradeRecodeOutput.newBuilder().setSuccess("success").build();
                logger.info("recode activity grade success");
                responseObserver.onNext(output);
                responseObserver.onCompleted();
            }
        }


        //证书信息初始化
        //rpc CertInfoInit (CertInfoInitInput) returns (CertInfoInitOutput){}
        public void certInfoInit (CertInfoInitInput req,StreamObserver<CertInfoInitOutput> responseObserver) {
            int flag;
            flag=creditCertClient.certInfoInit(req.getCertID(),req.getStuID(),req.getStuName(),req.getUsName(),req.getMajor(),req.getStudyTime(),req.getCertStatus(),req.getExtInfo(),req.getTime(),req.getCertSignature());
            if(flag==0){
                CertInfoInitOutput output =CertInfoInitOutput.newBuilder().setSuccess("success").build();
                logger.info("init certInfo success");
                responseObserver.onNext(output);
                responseObserver.onCompleted();
            }
        }


        //证书信息更新
        //rpc CertInfoUpdate (CertInfoUpdateInput) returns (CertInfoUpdateOutput){}
        public void certInfoUpdate (CertInfoUpdateInput req,StreamObserver<CertInfoUpdateOutput> responseObserver) {
            int flag;
            flag=creditCertClient.certInfoUpdate(req.getCertID(),req.getStuID(),req.getStuName(),req.getUsName(),req.getMajor(),req.getStudyTime(),req.getCertStatus(),req.getExtInfo(),req.getTime(),req.getCertSignature());
            if(flag==0){
                CertInfoUpdateOutput output=CertInfoUpdateOutput.newBuilder().setSuccess("successs").build();
                logger.info("update certInfo success");
                responseObserver.onNext(output);
                responseObserver.onCompleted();
            }
        }


        //学生信息查询
        //rpc StuInfoQuery (StuInfoQueryInput) returns (StuInfoQueryOutput){}
        public void stuInfoQuery (StuInfoQueryInput req,StreamObserver<StuInfoQueryOutput> responseObserver) {
            Student student = creditCertClient.stuInfoQuery(req.getStuID());
            StuInfoQueryOutput output=StuInfoQueryOutput.newBuilder().setStuID(student.getStuId()).setExtInfo(student.getExtInfo())
                    .setStuName(student.getStuName())
                    .setUsName(student.getUsName()).setMajor(student.getMajor())
                    .setTime(student.getTime()).setGrade(student.getGrade())
                    .setUsLevel(student.getUsLevel()).build();
            if(output!=null){
                logger.info("query stuInfo success");
                responseObserver.onNext(output);
                responseObserver.onCompleted();
            }
        }


        //学生成绩查询
        //rpc StuGradeQuery (StuGradeQueryInput) returns (StuGradeQueryOutput){}
        public void stuGradeQuery (StuGradeQueryInput req,StreamObserver<StuGradeQueryOutput> responseObserver) {
            System.out.println(req.getGrade());
            System.out.println(req.getStuID());
            Grades grade=creditCertClient.stuGradeQuery(req.getStuID(),req.getGrade());
            StuGradeQueryOutput output = StuGradeQueryOutput.newBuilder().setStuID(grade.getStuID()).setStuName(grade.getStuName())
                    .setGrade(grade.getGrade()).setAverageGrades(grade.getAverageGrades())
                    .setExtInfo(grade.getExtInfo()).setTime(grade.getTime())
                    .setObligatoryCredit(60).setOptionalCredit(80).build();
            if(output!=null){
                logger.info("query stuGrade success");
                responseObserver.onNext(output);
                responseObserver.onCompleted();
            }
        }


        //学生活动查询
        //rpc StuActQuery (StuActQueryInput) returns (StuActQueryOutput){}
        public void stuActQuery (StuActQueryInput req,StreamObserver<StuActQueryOutput> responseObserver) {
            StuActivity stuActivity=creditCertClient.stuActQuery(req.getStuID(),req.getActID());
            StuActQueryOutput output = StuActQueryOutput.newBuilder().setStuID(stuActivity.getStuID()).setStuName(stuActivity.getStuName())
                    .setActID(stuActivity.getActID()).setActName(stuActivity.getActName())
                    .setActSignature(stuActivity.getActSignature()).setExtInfo(stuActivity.getExtInfo())
                    .setTime(stuActivity.getTime()).build();
            if(output!=null){
                logger.info("query stuAct success");
                responseObserver.onNext(output);
                responseObserver.onCompleted();
            }
        }


        //学生证书查询
        //rpc StuCertQuery (StuCertQueryInput) returns (StuCertQueryOutput){}
        public void stuCertQuery (StuCertQueryInput req,StreamObserver<StuCertQueryOutput> responseObserver) {
            Cert cert =creditCertClient.stuCertQuery(req.getCertID());
            String sing = creditCertClient.certSignatureQueryk(req.getCertID());

            StuCertQueryOutput output=StuCertQueryOutput.newBuilder().setCertID(cert.getCertID()).setStuID(cert.getStuID())
                    .setStuName(cert.getStuName()).setUsName(cert.getUsName()).setMajor(cert.getMajor()).setStudyTime(cert.getStudyTime())
                    .setCertStatus(cert.getCertStatus()).setExtInfo(cert.getExtInfo()).setTime(cert.getTime()).setCertSignature(sing).build();
            if(output!=null){
                logger.info("query stuCert success");
                responseObserver.onNext(output);
                responseObserver.onCompleted();
            }
        }


        //活动信息查询
        //rpc ActInfoQuery (ActInfoQueryInput) returns (ActInfoQueryOutput){}
        public void actInfoQuery (ActInfoQueryInput req,StreamObserver<ActInfoQueryOutput> responseObserver) {
            Activity activity =creditCertClient.actInfoQuery(req.getActID());
            ActInfoQueryOutput output =ActInfoQueryOutput.newBuilder().setActID(activity.getActID()).setActName(activity.getActName())
                    .setOrganizer(activity.getSponsor()).setStatus(activity.getStatus())
                    .setExtInfo(activity.getExtInfo()).setTime(activity.getTime()).build();
            if(output!=null){
                logger.info("query activity success");
                responseObserver.onNext(output);
                responseObserver.onCompleted();
            }
        }

        //学生获得学分查询,新增
        //rpc ActInfoQuery (ActInfoQueryInput) returns (ActInfoQueryOutput){}
        public void allCreditQuery (AllCreditQueryInput req,StreamObserver<AllCreditQueryOutput> responseObserver) {
            AllCredit allCredit =creditCertClient.allCreditQuery(req.getStuID());
            AllCreditQueryOutput output =AllCreditQueryOutput.newBuilder().setCreditSum((int)allCredit.getTempSum()).setObligatoryCredit((int)allCredit.getTempOblgCredits())
                    .setOptionalCredit((int)allCredit.getTempOptCredits()).build();
            if(output!=null){
                logger.info("query activity success");
                responseObserver.onNext(output);
                responseObserver.onCompleted();
            }
        }

//        //学生获得学分查询,新增
//        public void certSignatureQueryk (AllCreditQueryInput req,StreamObserver<AllCreditQueryOutput> responseObserver) {
//            AllCredit allCredit =creditCertClient.allCreditQuery(req.getStuID());
//            AllCreditQueryOutput output =AllCreditQueryOutput.newBuilder().setCreditSum((int)allCredit.getTempSum()).setObligatoryCredit((int)allCredit.getTempOblgCredits())
//                    .setOptionalCredit((int)allCredit.getTempOptCredits()).build();
//            if(output!=null){
//                logger.info("query activity success");
//                responseObserver.onNext(output);
//                responseObserver.onCompleted();
//            }
//        }

        //学生获得学分查询,新增
        //rpc ActInfoQuery (ActInfoQueryInput) returns (ActInfoQueryOutput){}
        public void creditEvaluation (CreditEvaluationInput req,StreamObserver<CreditEvaluationOutput> responseObserver) {
            Evaluation evaluation =creditCertClient.creditEvaluation(req.getStuID());
            System.out.println(evaluation.getM2());
            System.out.println(evaluation.getM3());
            System.out.println(evaluation.getScore());
            CreditEvaluationOutput output = CreditEvaluationOutput.newBuilder().setStuID(req.getStuID()).setUsScore((int) evaluation.getM1())
                    .setGradeScore((int) evaluation.getM2()).setLevelScore((int) evaluation.getM3())
                    .setCreditScore((int) evaluation.getScore()).build();
            if(output!=null){
                logger.info("query activity success");
                responseObserver.onNext(output);
                responseObserver.onCompleted();
            }
        }

    }
}


