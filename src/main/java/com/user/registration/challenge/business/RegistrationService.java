package com.user.registration.challenge.business;

import com.user.registration.challenge.client.IPRestClient;
import com.user.registration.challenge.client.model.IPApiResponse;
import com.user.registration.challenge.error.InvalidIPAddressException;
import com.user.registration.challenge.error.InvalidPasswordException;
import com.user.registration.challenge.error.InvalidRequestException;
import com.user.registration.challenge.model.RegisterDTO;
import com.user.registration.challenge.model.RegisterRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
@Slf4j
public class RegistrationService {

    private static final Pattern PWD_PATTERN = Pattern.compile("(?=.*\\d)(?=.*[A-Z])(?=.*[_#$%.])[A-Za-z\\d_#$%.]{8,}");

    private static final String WRONG_PARAMS_MSG = "All parameters must not be blank ";
    private static final String INVALID_PWD_MSG= "Password must be at least 8 characters and contain an uppercase letter, a symbol _#$%. and a number";
    private static final String INVALID_ORIGIN_IP_MSG= "The given IP address is not from Canada";

    private static final String COUNTRY_CANADA_STR= "Canada";

    @Autowired
    IPRestClient ipRestClient;



    public RegisterDTO register(RegisterRequest registerRequest) {

        validateRegistrationRequest(registerRequest);
        IPApiResponse ipApiResponse=ipRestClient.getIPInfo(registerRequest.getIp());
        validateCanadianIP(ipApiResponse);

        return null;
    }

    private void validateRegistrationRequest(RegisterRequest registerRequest){
        if(StringUtils.isAnyBlank(
                registerRequest.getUsername(),registerRequest.getPassword(),registerRequest.getIp())){
            throw new InvalidRequestException(WRONG_PARAMS_MSG);
        }
        if(!PWD_PATTERN.matcher(registerRequest.getPassword()).matches()){
            throw new InvalidPasswordException(INVALID_PWD_MSG);
        }
    }

    private void validateCanadianIP(IPApiResponse ipApiResponse){
        if(COUNTRY_CANADA_STR.equals(ipApiResponse.getCountry())){
            throw new InvalidIPAddressException(INVALID_ORIGIN_IP_MSG);
        }
    }
}
