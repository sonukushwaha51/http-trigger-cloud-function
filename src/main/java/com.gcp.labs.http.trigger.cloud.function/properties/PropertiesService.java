package com.gcp.labs.http.trigger.cloud.function.properties;

import com.google.cloud.pubsub.v1.Publisher;
import com.google.inject.Inject;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.google.pubsub.v1.TopicName;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Slf4j
public class PropertiesService {

    private final Properties properties;

    @Inject
    public PropertiesService(Properties properties) {
        this.properties = properties;
    }

    @Provides
    @Named("projectId")
    public String provideProjectId() {
        return properties.getProperty("projectId");
    }

    @Provides
    @Named("region")
    public String provideRegion() {
        return properties.getProperty("region");
    }

    @Provides
    @Named("pubSubTopic")
    public String getPubsubTopic() {
        return properties.getProperty("pubSubTopic");
    }

    @Provides
    @Singleton
    public Properties fetchProperties() {
        Properties properties = new Properties();
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("application.properties")) {
            properties.load(inputStream);
        } catch (IOException exception) {
            log.error("Error while reading from properties: {}", exception.getLocalizedMessage(), exception);
        }
        return properties;
    }

    @Provides
    @Singleton
    public Publisher publisher() throws IOException {
        return Publisher.newBuilder(TopicName.of(provideProjectId(), getPubsubTopic())).build();
    }
}
