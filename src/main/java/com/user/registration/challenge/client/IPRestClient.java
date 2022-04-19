package com.user.registration.challenge.client;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.user.registration.challenge.client.model.IPApiResponse;
import com.user.registration.challenge.error.InvalidIPAddressException;
import com.user.registration.challenge.error.IpAPIException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.MessageFormat;

@Service
@Slf4j
public class IPRestClient {
    @Autowired
    private RestTemplate ipApiRestTemplate;

    private static final String GET_IP = "/json/{ipAddress}?fields=status,query,countryCode,city";
    private static final String RESPONSE_FAILED = "fail";
    private static final String RESPONSE_SUCCESS = "success";
    private static final String INVALID_IP_MSG = "Couldn't find the given ip: {0} ";
    private static final String UNKOWN_STATUS_MSG = "Api returned an unknown status response message while trying to get {0}";


    public IPApiResponse getIPInfo(String ipAddress) {
        ResponseEntity<IPApiResponse> responseEntity = ipApiRestTemplate.getForEntity(GET_IP, IPApiResponse.class, ipAddress);
        IPApiResponse ipApiResponse = responseEntity.getBody();
        if(responseEntity.getStatusCode().equals(HttpStatus.OK)){
            if(RESPONSE_FAILED.equals(ipApiResponse.getStatus())){
                throw new InvalidIPAddressException(MessageFormat.format(INVALID_IP_MSG, ipAddress));
            }
            if(!RESPONSE_SUCCESS.equals(ipApiResponse.getStatus())){
                throw new IpAPIException(MessageFormat.format(UNKOWN_STATUS_MSG, ipAddress));
            }
        }
        return ipApiResponse;
    }

}


