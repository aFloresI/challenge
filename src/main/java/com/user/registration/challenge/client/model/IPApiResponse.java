package com.user.registration.challenge.client.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class IPApiResponse {
    private String status;
    private String country;
    private String countryCode;
    private String city;
    private String query;
}



