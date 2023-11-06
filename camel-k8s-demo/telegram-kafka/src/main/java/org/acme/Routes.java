/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.acme;

import static org.apache.camel.builder.endpoint.StaticEndpointBuilders.telegram;
import static org.apache.camel.builder.endpoint.StaticEndpointBuilders.http;
import static org.apache.camel.builder.endpoint.StaticEndpointBuilders.kafka;
import static org.apache.camel.builder.endpoint.StaticEndpointBuilders.aws2S3;
import static org.apache.camel.builder.endpoint.StaticEndpointBuilders.direct;

import java.util.UUID;



import org.apache.camel.Predicate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.aws2.s3.AWS2S3Constants;

public class Routes extends RouteBuilder {

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
                    .to(direct("kafka"))
                    //.transform(simple("{{msg.bot.msg}}"))
                .otherwise()
                    .log("${body}")
                    .to(direct("kafka"))
            .end()
            .to(telegram("bots"));

        from(direct("chuck"))
            .to(http("api.chucknorris.io/jokes/random?category=dev")
                //.advanced().followRedirects(true))
                .followRedirects(true))
            .unmarshal().json()
            .setBody().simple("${body[value]}")
            .log("Joke from api.chucknorris.io: ${body}");

        from(direct("kafka"))
            .log("Sending message to Kafka topic telegram-message: ${body}")
            .to(kafka("telegram-message"));

        from(kafka("telegram-message"))
            .log("Incoming message from Kafka topic telegram-message: ${body}")
            .setHeader(AWS2S3Constants.KEY, () -> UUID.randomUUID())
            .log(String.format("Sending message with header :: ${header.%s}", AWS2S3Constants.KEY))
            .to(aws2S3("{{aws-s3.bucket-name}}"));

    }
}
