package com.gcp.labs.http.trigger.cloud.function.properties;

import com.google.cloud.pubsub.v1.Publisher;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Scopes;
import com.google.inject.Singleton;
import com.google.pubsub.v1.TopicName;

import java.io.IOException;

public class PropertiesModule extends AbstractModule {

    @Override
    public void configure() {
        this.bind(PropertiesService.class).in(Scopes.SINGLETON);
    }

    @Provides
    @Singleton
    public Publisher publisher(PropertiesService propertiesService) throws IOException {
        return Publisher.newBuilder(TopicName.of(propertiesService.provideProjectId(), propertiesService.getPubsubTopic())).build();
    }

}
