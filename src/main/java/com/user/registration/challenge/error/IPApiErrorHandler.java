package com.user.registration.challenge.error;


import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.DefaultResponseErrorHandler;

import java.io.IOException;

@Component
public class IPApiErrorHandler extends DefaultResponseErrorHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(IPApiErrorHandler.class);

    @Override
    public void handleError(ClientHttpResponse httpResponse) throws IOException {
        LOGGER.error("Error occurred while requesting IP-API rest api: {} {}", httpResponse.getRawStatusCode(), httpResponse.getStatusText());
        throw new IpAPIException("Something went wrong trying to make a request to the API Server");
    }

}
