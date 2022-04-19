package com.user.registration.challenge.web;

import com.user.registration.challenge.RegisterApi;
import com.user.registration.challenge.business.RegistrationService;
import com.user.registration.challenge.model.RegisterDTO;
import com.user.registration.challenge.model.RegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegistrationController implements RegisterApi {

    @Autowired
    RegistrationService registrationService;

    @Override
    public ResponseEntity<RegisterDTO> register(RegisterRequest registerRequest) {
        return ResponseEntity.ok(registrationService.register(registerRequest));
    }

}
