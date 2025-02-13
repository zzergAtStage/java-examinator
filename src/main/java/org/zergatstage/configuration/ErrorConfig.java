package org.zergatstage.configuration;

import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.ErrorPageRegistrar;
import org.springframework.boot.web.server.ErrorPageRegistry;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

@Configuration
public class ErrorConfig implements ErrorPageRegistrar {

    @Override
    public void registerErrorPages(ErrorPageRegistry registry) {
        // Register 404 error page explicitly
        registry.addErrorPages(new ErrorPage(HttpStatus.NOT_FOUND, "/error-404"));

        // Register other error pages
        registry.addErrorPages(
                new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/error"),
                new ErrorPage(Throwable.class, "/error")
        );
    }
}