package com.user.registration.challenge.model;

import com.user.registration.challenge.client.model.IPApiResponse;
import org.mapstruct.*;

import java.text.MessageFormat;
import java.util.UUID;

@Mapper(componentModel = "spring")
public interface RegisterMapper {

    static final String WELCOME_MSG="Welcome {0} from {1}";
    @Mappings({
            @Mapping(source = "request.username",target = "name"),
            @Mapping(expression = "java(setWelcomeMessage(request.getUsername(),ipApiResponse.getCity()))",
                    target = "message")

    })
    RegisterDTO requestIpToRegisterDTO(RegisterRequest request, IPApiResponse ipApiResponse);

    @AfterMapping
    default void createID(@MappingTarget RegisterDTO registerDTO) {
        registerDTO.setId(UUID.randomUUID().toString());
    }

    default String setWelcomeMessage(String userName, String city){
        return MessageFormat.format(WELCOME_MSG,userName,city);
    }
}
