
import org.apache.camel.builder.RouteBuilder;

public class S3ToKnative extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("aws2-s3:{{aws-s3.bucket-name}}?delay=1500")
            .log("Receiving message  ${body}")
            .to("knative:channel/feedback");
    }
    
}
