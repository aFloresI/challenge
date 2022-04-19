package com.user.registration.challenge.business;

import com.user.registration.challenge.error.InvalidPasswordException;
import com.user.registration.challenge.error.InvalidRequestException;
import com.user.registration.challenge.model.RegisterDTO;
import com.user.registration.challenge.model.RegisterRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j
public class RegistrationService {
    /* Password need to be greater than 8 characters, containing at least 1 number,
     1 Captialized letter, 1 special character in this set "_ # $ % ."
     It was made for accepting all chars, but it can be changed to only accept letters, numbers and the specified symbols [A-Za-z\d_#$%.]
     */
    private static final Pattern PWD_PATTERN = Pattern.compile("(?=.*\\d)(?=.*[A-Z])(?=.*[_#$%.])[\\w\\W]{8,}");
    private static final String WRONG_PARAMS_MSG = "All parameters must not be blank ";
    private static final String INVALID_PWD_MSG= "Password must be at least 8 characters and contain an uppercase letter, a symbol _#$%. and a number";


    public RegisterDTO register(RegisterRequest registerRequest) {
        validateRegistrationRequest(registerRequest);
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
}
