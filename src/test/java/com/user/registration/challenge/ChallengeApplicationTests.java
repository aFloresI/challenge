package com.user.registration.challenge;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.user.registration.challenge.client.IPRestClient;
import com.user.registration.challenge.client.model.IPApiResponse;
import com.user.registration.challenge.model.RegisterDTO;
import com.user.registration.challenge.model.RegisterMapper;
import com.user.registration.challenge.model.RegisterRequest;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ChallengeApplicationTests {

	@Autowired
	MockMvc mvc ;

	@Autowired
	private RestTemplate restTemplate;


	@Autowired
	ObjectMapper objectMapper;
	private static final String USER_STR="Louis";
	private static final String INVALID_PWD="123Aa";
	private static final String VALID_PWD="A$3password";
	private static final String CANADA_IP="metro.ca";
	private static final String MEXICO_IP="metro.cdmx.gob.mx";
	private static final String CANADA_COUNTRY_STR="Canada";
	private static final String MONTREAL_STR="Montreal";

	@Autowired
	RegisterMapper registerMapper;

	private RegisterRequest createRequest(String user, String pwd, String ip){
		RegisterRequest registerRequest = new RegisterRequest();
		registerRequest.setUsername(user);
		registerRequest.setPassword(pwd);
		registerRequest.setIp(ip);
		return registerRequest;
	}

	@Test
	void postRegisterValid() throws Exception {
		mvc.perform(post("/register")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(createRequest(USER_STR,VALID_PWD,CANADA_IP))))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.city").value(MONTREAL_STR));
	}

	@Test
	void postRegisterInvalidIP() throws Exception {
		mvc.perform(post("/register")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(createRequest(USER_STR,VALID_PWD,MEXICO_IP))))
				.andExpect(status().isForbidden());
	}

	@Test
	void postRegisterInvalidPassword() throws Exception {
		mvc.perform(post("/register")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(createRequest(USER_STR,INVALID_PWD,CANADA_IP))))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value(Matchers.containsString("Password must be")));
	}

	@Test
	void postRegisterBlankUser() throws Exception {
		mvc.perform(post("/register")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(createRequest("",INVALID_PWD,CANADA_IP))))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value(Matchers.containsString("must not be blank")));
	}

	@Test
	public void requestValidAPI() {
		ResponseEntity<IPApiResponse> responseEntity =
				restTemplate.getForEntity("/json/metro.ca?fields=status,query,country,countryCode,city", IPApiResponse.class);
		IPApiResponse apiResponse = responseEntity.getBody();
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals("success", apiResponse.getStatus());
		assertEquals(CANADA_COUNTRY_STR, apiResponse.getCountry());
	}

	@Test
	public void requestInvalidAPI() {
		ResponseEntity<IPApiResponse> responseEntity =
				restTemplate.getForEntity("/json/123.34.5?fields=status,query,country,countryCode,city", IPApiResponse.class);
		IPApiResponse apiResponse = responseEntity.getBody();
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals("fail", apiResponse.getStatus());
	}

	@Test
	public void testMapping(){
		IPApiResponse apiResponse = new IPApiResponse("success",CANADA_COUNTRY_STR,"CA",MONTREAL_STR,"");
		RegisterRequest registerRequest = createRequest(USER_STR,VALID_PWD,CANADA_IP);
		RegisterDTO registerDTO = registerMapper.requestIpToRegisterDTO(registerRequest,apiResponse);
		assertEquals(registerDTO.getName(),USER_STR);
		assertEquals(registerDTO.getCity(),MONTREAL_STR);
		assertEquals(registerDTO.getMessage(),"Welcome "+USER_STR+" from " +MONTREAL_STR);
	}


}
