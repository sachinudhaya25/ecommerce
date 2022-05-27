package com.ecommerce.training;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import com.ecommerce.training.models.User;
import com.ecommerce.training.payload.request.LoginRequest;
import com.ecommerce.training.repository.UserRepository;
import com.ecommerce.training.service.UserDetailsServiceImpl;
import com.ecommerce.training.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTests {

	@MockBean
	private UserRepository userRepository;

	@Autowired
	private WebApplicationContext context;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private UserDetailsServiceImpl userDetailsServiceImpl;

	@MockBean
	private AuthenticationManager authenticationManager;

	@MockBean
	UserService userService;

	private static User user;
//    @Test
//    @WithMockUser
//	public void testAuthenticateUser() throws Exception {
//		LoginRequest loginRequest = new LoginRequest("test1", "123456");
//		GrantedAuthority grantedAuthority = new SimpleGrantedAuthority("ROLE_USER");
//		Collection<GrantedAuthority> listOfgrantedAuthorities = new ArrayList<>();
//        listOfgrantedAuthorities.add(grantedAuthority);
//		UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername("test1");
//		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails,null, listOfgrantedAuthorities);
//		Mockito.when(authenticationManager.authenticate(Mockito.any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
//		String json = objectMapper.writeValueAsString(loginRequest);
//		mockMvc.perform(post("/api/auth/signin").contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8").content(json).accept(MediaType.APPLICATION_JSON))
//				.andExpect(status().isOk())
//				.andDo(print());
//	}

//    @Test
//	public void testRegisterUser() throws Exception {
//    	Long mobile=new Long(867262465);
//    	SignupRequest signUpRequest = new SignupRequest("Test3", "test2@gmail.com", mobile, "123456");
//		Mockito.when(userService.registerNewUser(any(SignupRequest.class))).thenReturn(user);
//		String json = objectMapper.writeValueAsString(signUpRequest);
//		mockMvc.perform(post("/api/auth/signup").contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8").content(json).accept(MediaType.APPLICATION_JSON))
//				.andExpect(status().isOk()).andExpect(jsonPath("$.message").value("User registered successfully!"));
//	}

//  @Test
//	public void testVerfiyRegisterEmail() throws Exception {
//    	Long mobile=new Long(867262465);
//    	SignupRequest signUpRequest = new SignupRequest("Test1", "test1@gmail.com", mobile, "123456");
//    	String json = objectMapper.writeValueAsString(signUpRequest);
//		mockMvc.perform(post("/api/auth/signup").contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8").content(json).accept(MediaType.APPLICATION_JSON))
//				.andExpect(status().isBadRequest()).andExpect(jsonPath("$.message").value("Error: Email is already in use!"));
//    }

	@Test
	@DisplayName("")
	void shouldNotNull() throws Exception {
		LoginRequest request = new LoginRequest();
		request.setUsername("Anand");
		String json = objectMapper.writeValueAsString(request);
		mockMvc.perform(post("/api/auth/test").contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8")
				.content(json).accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
	}

	@Test
	@DisplayName("")
	void shouldNotWrongcredentials() throws Exception {
		LoginRequest actualRequest = new LoginRequest("Anand", "1234567");
		String json = objectMapper.writeValueAsString(actualRequest);
		mockMvc.perform(post("/api/auth/test").contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8")
				.content(json).accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value("username or password is incorrect"));
	}

	@Test
	@DisplayName("")
	public void shouldAuthenticateUser() throws Exception {
		LoginRequest loginRequest = new LoginRequest("Anand", "123456");
		String json = objectMapper.writeValueAsString(loginRequest);
		mockMvc.perform(post("/api/auth/test").contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8")
				.content(json).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
		        .andExpect(jsonPath("$.message").value("User login successfully"));
	}
	@Test
	@DisplayName("")
	public void shouldNotWrong() throws Exception {
		LoginRequest loginRequest = new LoginRequest();
		String json = objectMapper.writeValueAsString(loginRequest);
		mockMvc.perform(post("/api/auth/test").contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8")
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
	}
}
