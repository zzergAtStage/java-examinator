package org.zergatstage.filemanager;

/**
 * @author father
 */

import org.springframework.integration.file.FileHeaders;
import org.springframework.messaging.handler.annotation.Header;


public interface IntegrationFileGateway {
    void writeToFile(@Header(FileHeaders.FILENAME) String filename, String data);
}
