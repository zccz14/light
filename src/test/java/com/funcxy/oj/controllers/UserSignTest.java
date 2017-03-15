package com.funcxy.oj.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.funcxy.oj.Application;
import com.funcxy.oj.contents.Passport;
import com.funcxy.oj.contents.SignInPassport;
import com.funcxy.oj.models.User;
import com.funcxy.oj.repositories.UserRepository;
import org.bson.types.ObjectId;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.HashMap;

import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

/**
 * @author  DDHEE on 2017/3/14.
 */

/**
 * 说明：
 * 通过MockMvc发出请求
 * 通过sessisonAtrs设定session
 * 通过content设定请求体
 * 通过andExpect进行响应的断言测试
 *
 */

@RunWith(SpringJUnit4ClassRunner.class)  //使用junit4进行测试
@SpringBootTest(classes = Application.class)
@SpringBootConfiguration
public class UserSignTest {
    private String usernameValid = "zccz13";
    private String usernameValid1 = "zzcz13";
    private String usernameDuplicated = "  z ccz 1  3 ";
    private String usernameEmpty = "      ";
    private String emailValid = "hell@yeahfuncxy.net";
    private String emailValid1 = "hello@yeahfuncxy.com";
    private String emailDuplicated = "  h ell@yeah    func   xy.net";
    private String emailEmpty = "     ";
    private String emailInvalid = "asfdjklas@.com";
    private String passwordValid = "abc6789067890";
    private String passwordWrong = "ab6789067890";
    private String passwordEmpty = "";
    private String passwordInvalid = "243";

    @Autowired
    private UserRepository userRepository;

    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    private MockMvc mockMvc;

    private HttpMessageConverter mappingJackson2HttpMessageConverter;

    private HashMap<String, Object> sessionAttr;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    void setConverters(HttpMessageConverter<?>[] converters) {

        this.mappingJackson2HttpMessageConverter = Arrays.asList(converters).stream()
                .filter(hmc -> hmc instanceof MappingJackson2HttpMessageConverter)
                .findAny()
                .orElse(null);

        assertNotNull("the JSON message converter must not be null",
                this.mappingJackson2HttpMessageConverter);
    }

    private Passport testUserPassport = new Passport();
    private SignInPassport testUserSignInPassport = new SignInPassport();

    @Before
    public void validUser () throws Exception {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();//加载上下文
        testUserPassport.username = usernameValid;
        testUserPassport.email = emailValid;
        testUserPassport.password = passwordValid;

        //session
        //维持登录态
        sessionAttr = new HashMap<String, Object>();
    }

    @Test
    public void signUpTestNormal() throws Exception {
        mockMvc.perform(post("/users/sign-up")
                .content(this.json(testUserPassport))
                .contentType(contentType))
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn();
    }

    @Test
    public void signUpTestEmptyUsername() throws Exception {
        testUserPassport.username = usernameEmpty;
        mockMvc.perform(post("/users/sign-up")
                .content(this.json(testUserPassport))
                .contentType(contentType))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    public void signUpTestDuplicatedUsername() throws Exception {
        mockMvc.perform(post("/users/sign-up")
                .content(this.json(testUserPassport))
                .contentType(contentType))
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn();
        testUserPassport.username = usernameDuplicated;
        testUserPassport.email = emailValid1;
        mockMvc.perform(post("/users/sign-up")
                .content(this.json(testUserPassport))
                .contentType(contentType))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    public void signUpTestEmptyEmail() throws Exception {
        testUserPassport.email = emailEmpty;
        mockMvc.perform(post("/users/sign-up")
                .content(this.json(testUserPassport))
                .contentType(contentType))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    public void signUpTestInvalidEmail() throws Exception {
        testUserPassport.email = emailInvalid;
        mockMvc.perform(post("/users/sign-up")
                .content(this.json(testUserPassport))
                .contentType(contentType))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    public void signUpTestDuplicatedEmail() throws Exception {
        mockMvc.perform(post("/users/sign-up")
                .content(this.json(testUserPassport))
                .contentType(contentType))
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn();
        testUserPassport.username = usernameValid1;
        testUserPassport.email = emailDuplicated;
        mockMvc.perform(post("/users/sign-up")
                .content(this.json(testUserPassport))
                .contentType(contentType))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    public void signUpTestEmptyPassword() throws Exception {
        testUserPassport.password = passwordEmpty;
        mockMvc.perform(post("/users/sign-up")
                .content(this.json(testUserPassport))
                .contentType(contentType))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    public void signUpTestInvalidPassword() throws Exception {
        testUserPassport.password = passwordInvalid;
        mockMvc.perform(post("/users/sign-up")
                .content(this.json(testUserPassport))
                .contentType(contentType))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    public void signInTestNormalByUsername() throws Exception {
        mockMvc.perform(post("/users/sign-up")
                .content(this.json(testUserPassport))
                .contentType(contentType))
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn();
        testUserSignInPassport.username = usernameValid;
        testUserSignInPassport.password = passwordValid;
        mockMvc.perform(post("/users/sign-in")
                .content(this.json(testUserSignInPassport))
                .contentType(contentType))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void signInTestWrongPassword() throws Exception {
        mockMvc.perform(post("/users/sign-up")
                .content(this.json(testUserPassport))
                .contentType(contentType))
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn();
        testUserSignInPassport.username = usernameValid;
        testUserSignInPassport.password = passwordWrong;
        mockMvc.perform(post("/users/sign-in")
                .content(this.json(testUserSignInPassport))
                .contentType(contentType))
                .andDo(print())
                .andExpect(status().isForbidden())
                .andReturn();
    }

    @Test
    public void signInTestNormalByEmail() throws Exception {
        mockMvc.perform(post("/users/sign-up")
                .content(this.json(testUserPassport))
                .contentType(contentType))
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn();
        testUserSignInPassport.username = emailValid;
        testUserSignInPassport.password = passwordValid;
        mockMvc.perform(post("/users/sign-in")
                .content(this.json(testUserSignInPassport))
                .contentType(contentType))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void signInTestUserNotFound() throws Exception {
        mockMvc.perform(post("/users/sign-up")
                .content(this.json(testUserPassport))
                .contentType(contentType))
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn();
        testUserSignInPassport.username = usernameValid1;
        testUserSignInPassport.password = passwordValid;
        mockMvc.perform(post("/users/sign-in")
                .content(this.json(testUserSignInPassport))
                .contentType(contentType))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    public void signInTestHaveSignedInWithSameRequest() throws Exception {
        mockMvc.perform(post("/users/sign-up")
                .content(this.json(testUserPassport))
                .contentType(contentType))
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn();
        testUserSignInPassport.username = usernameValid;
        testUserSignInPassport.password = passwordValid;
        mockMvc.perform(post("/users/sign-in")
                .content(this.json(testUserSignInPassport))
                .contentType(contentType))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        mockMvc.perform(post("/users/sign-in")
                .content(this.json(testUserSignInPassport))
                .contentType(contentType))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void signInTestHaveSignedInWithDifferentRequest() throws Exception {
        mockMvc.perform(post("/users/sign-up")
                .content(this.json(testUserPassport))
                .contentType(contentType))
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn();
        testUserSignInPassport.username = usernameValid;
        testUserSignInPassport.password = passwordValid;
        mockMvc.perform(post("/users/sign-in")
                .content(this.json(testUserSignInPassport))
                .contentType(contentType))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        User user = userRepository.findOneByUsername(usernameValid);
        sessionAttr.put("userId",user.getId());
        testUserSignInPassport.username = usernameValid1;
        mockMvc.perform(post("/users/sign-in")
                .sessionAttrs(sessionAttr)
                .content(this.json(testUserSignInPassport))
                .contentType(contentType))
                .andDo(print())
                .andExpect(status().isForbidden())
                .andReturn();
    }

    @Test
    public void signOutTestNormal() throws Exception {
        mockMvc.perform(post("/users/sign-up")
                .content(this.json(testUserPassport))
                .contentType(contentType))
                .andDo(print())
                .andExpect(status().isCreated());
        testUserSignInPassport.username = usernameValid;
        testUserSignInPassport.password = passwordValid;
        mockMvc.perform(post("/users/sign-in")
                .content(this.json(testUserSignInPassport))
                .contentType(contentType))
                .andDo(print())
                .andExpect(status().isOk());
        User user = userRepository.findOneByUsername(usernameValid);
        sessionAttr.put("userId",user.getId());
        mockMvc.perform(get("/users/sign-out")
                .sessionAttrs(sessionAttr)
                .contentType(contentType))
                .andDo(print())
                .andExpect(status().isNoContent());
        sessionAttr.clear();
    }

    @Test
    public void signOutTestNeverSignedIn() throws Exception {
        mockMvc.perform(post("/users/sign-up")
                .content(this.json(testUserPassport))
                .contentType(contentType))
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn();
        mockMvc.perform(get("/users/sign-out")
                .contentType(contentType))
                .andDo(print())
                .andExpect(status().isForbidden())
                .andReturn();
    }

    @After
    public void clear(){//去除加入的数据
        userRepository.deleteAll();
    }

    protected String json(Object o) throws IOException {
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
        this.mappingJackson2HttpMessageConverter.write(
                o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
        return mockHttpOutputMessage.getBodyAsString();
    }

}
