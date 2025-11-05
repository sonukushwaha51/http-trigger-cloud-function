package com.gcp.labs.http.trigger.cloud.function;

import com.gcp.labs.http.trigger.cloud.function.properties.PropertiesModule;
import com.google.api.core.ApiFuture;
import com.google.cloud.functions.HttpFunction;
import com.google.cloud.functions.HttpRequest;
import com.google.cloud.functions.HttpResponse;
import com.google.cloud.pubsub.v1.Publisher;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.protobuf.ByteString;
import com.google.pubsub.v1.PubsubMessage;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
public class HttpTriggerFunction implements HttpFunction {

    private final Publisher publisher;

    public HttpTriggerFunction() {
        Injector injector = Guice.createInjector(new PropertiesModule());
        this.publisher = injector.getInstance(Publisher.class);
    }

    @Override
    public void service(HttpRequest httpRequest, HttpResponse response) throws Exception {

        if (publisher == null) {
            response.setStatusCode(500);
            response.getWriter().write("Pub/Sub publisher not initialized.");
            return;
        }

        // Read the request body as a string
        BufferedReader reader = new BufferedReader(new InputStreamReader(httpRequest.getInputStream()));
        String requestBody = reader.lines().collect(Collectors.joining(System.lineSeparator()));
        reader.close();

        if (requestBody.isEmpty()) {
            response.setStatusCode(400);
            response.getWriter().write("Request body cannot be empty.");
            return;
        }

        try {
            // Convert the message to a ByteString
            ByteString data = ByteString.copyFromUtf8(requestBody);

            // Create a Pub/Sub message
            PubsubMessage pubsubMessage = PubsubMessage.newBuilder()
                    .setData(data)
                    .putAttributes("id", UUID.randomUUID().toString())
                    .build();
            // Publish the message
            // The future result can be ignored for simple fire-and-forget
            ApiFuture<String> message = publisher.publish(pubsubMessage);

            // Log the action and send an HTTP response
            log.info("Published message: {}  to topic: {}, with request: {}", message.get(), publisher.getTopicNameString(), requestBody);
            response.setStatusCode(200);
            response.getWriter().write("Message published to Pub/Sub topic: " + publisher.getTopicNameString());

        } catch (Exception e) {
            log.error("Error publishing message to Pub/Sub: " + e.getMessage());
            e.printStackTrace();
            response.setStatusCode(500);
            response.getWriter().write("Error publishing message.");
        }
    }
}
