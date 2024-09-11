package org.zergatstage.services;

/**
 * @author father
 */

import org.springframework.context.annotation.Primary;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.file.FileHeaders;
import org.springframework.messaging.handler.annotation.Header;

@MessagingGateway(name = "myFileGateway", defaultRequestChannel = "textInputChannel")
@Primary
public interface IntegrationFileGateway {
    void writeToFile(@Header(FileHeaders.FILENAME) String filename, String data);
}
