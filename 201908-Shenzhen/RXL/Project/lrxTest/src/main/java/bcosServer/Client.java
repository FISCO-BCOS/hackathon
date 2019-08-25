package bcosServer;


import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Client {
    private static final Logger logger = Logger.getLogger(Client.class.getName());

    private final ManagedChannel channel;
    private final CreditCertGrpc.CreditCertBlockingStub blockingStub;

    public Client(String host,int port) {
        this(ManagedChannelBuilder.forAddress(host,port).usePlaintext().build());
    }

    public Client(ManagedChannel channel) {
        this.channel=channel;
        blockingStub=CreditCertGrpc.newBlockingStub(channel);
    }

    public void shutdown() throws InterruptedException {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }


    public void greet(String name) {
        logger.info("Will try to greet " + name + " ...");
        StuInfoInitInput request = StuInfoInitInput.newBuilder().setStuID(1).setStuName("quincy").setMajor("网信")
                .setTime(20190809).setExtInfo("hello").setUsName("quincy").build();
        StuInfoInitOutput response;
        CertInfoInitOutput response2;
        CertInfoInitInput request2 = CertInfoInitInput.newBuilder().setCertID(1).setStuID(1).setStuName("A").setUsName("xdu").setMajor("cs")
                .setStudyTime("2018").setExtInfo("").setCertStatus("").setTime(20190813).setCertSignature("").build();
        try {
//            response = blockingStub.stuInfoInit(request);
            response2 = blockingStub.certInfoInit(request2);
        } catch (StatusRuntimeException e) {
            logger.log(Level.WARNING, "RPC failed: {0}", e.getStatus());
            return;
        }
        logger.info("Greeting: " + response2.getSuccess());
    }

    public void greet() {
        logger.info("Will try to greet");
        StuInfoQueryInput request = StuInfoQueryInput.newBuilder().setStuID(1).setStuID(1).build();
        StuInfoQueryOutput response;
        try {
            response = blockingStub.stuInfoQuery(request);
        } catch (StatusRuntimeException e) {
            logger.log(Level.WARNING, "RPC failed: {0}", e.getStatus());
            return;
        }
        logger.info("Greeting: " + response.getStuID()+response.getStuName());
    }


    public static void main(String[] args) throws Exception {
        Client client = new Client("localhost", 50051);
        try {
            /* Access a service running on the local machine on port 50051 */
            String user = "CreditCert";
            if (args.length > 0) {
                user = args[0]; /* Use the arg as the name to greet if provided */
            }
            client.greet("");
        } finally {
            client.shutdown();
        }
    }
}
