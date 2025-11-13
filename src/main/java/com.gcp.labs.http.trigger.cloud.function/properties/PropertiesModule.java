package com.gcp.labs.http.trigger.cloud.function.properties;

import com.google.cloud.pubsub.v1.Publisher;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Scopes;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.google.pubsub.v1.TopicName;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Slf4j
public class PropertiesModule extends AbstractModule {

    @Override
    public void configure() {
        this.bind(PropertiesService.class).in(Scopes.SINGLETON);
    }

    @Provides
    @Named("projectId")
    public String provideProjectId(Properties properties) {
        return properties.getProperty("projectId");
    }

    @Provides
    @Named("region")
    public String provideRegion(Properties properties) {
        return properties.getProperty("region");
    }

    @Provides
    @Named("pubSubTopic")
    public String getPubsubTopic(Properties properties) {
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
    public Publisher publisher(@Named("projectId") String projectId, @Named("pubSubTopic") String pubsubTopic) throws IOException {
        return Publisher.newBuilder(TopicName.of(projectId, pubsubTopic)).build();
    }

}
