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

}
