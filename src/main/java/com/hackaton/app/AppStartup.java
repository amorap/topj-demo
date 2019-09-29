package com.hackaton.app;

import com.hackaton.app.connector.TopJConnector;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;

import java.io.IOException;

@Slf4j
public class AppStartup implements ApplicationListener<ApplicationReadyEvent> {

    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        log.info("Setup application...");
        try {
            TopJConnector.createInstance();
        } catch (IOException e) {
            log.error("Unable to connect to Top Network.", e);
        }
    }

}
