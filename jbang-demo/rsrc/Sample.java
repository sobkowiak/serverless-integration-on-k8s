// camel-k: language=java

import static org.apache.camel.builder.endpoint.StaticEndpointBuilders.telegram;
import static org.apache.camel.builder.endpoint.StaticEndpointBuilders.http;
import static org.apache.camel.builder.endpoint.StaticEndpointBuilders.direct;

import java.util.UUID;



import org.apache.camel.Predicate;
import org.apache.camel.builder.RouteBuilder;


public class Sample extends RouteBuilder {

    Predicate isStart = jsonpath("$.text").isEqualTo("/start");
    Predicate isChuck = jsonpath("$.text").isEqualTo("/chuck");

    @Override
    public void configure() throws Exception {

        from(telegram("bots"))
            .choice()
                .when(isStart)
                    .transform(simple("{{msg.bot.start}}"))
                .when(isChuck)
                    .to(direct("chuck"))
                .otherwise()
                    .log("${body}")
            .end()
            .to(telegram("bots"));

        from(direct("chuck"))
            .to(http("api.chucknorris.io/jokes/random?category=dev")
                //.advanced().followRedirects(true))
                .followRedirects(true))
            .unmarshal().json()
            .setBody().simple("${body[value]} 😄😄😄") //.jsonpath("$.value")  
            .log("Joke from api.chucknorris.io: ${body}");            
    }
}
