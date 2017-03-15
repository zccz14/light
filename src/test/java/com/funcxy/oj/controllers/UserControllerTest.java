package com.funcxy.oj.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.funcxy.oj.Application;
import com.funcxy.oj.contents.Passport;
import com.funcxy.oj.models.User;
import com.funcxy.oj.repositories.UserRepository;
import org.bson.types.ObjectId;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

/**
 * @author  aak1247 on 2017/3/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
public class UserControllerTest {

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


    private User userExisted = new User();
    {
        userExisted.setId(ObjectId.get().toString());
        userExisted.setUsername("abc_123");
        userExisted.setPassword("abc_123");
        userExisted.setEmail("abc@1234.com");
    }

    private Passport userNew = new Passport();
    private User userForUserNew;
    {
        userNew.username = "bdc456234";
        userNew.email = "bdc@a344dc.com";
        userNew.password = "bcbcbc1212";
    }

    @Before
    public void setUp(){//把一个用户加入数据库,并保持登录态
        this.mockMvc = webAppContextSetup(webApplicationContext).build();//加载上下文
        userRepository.insert(userExisted);
        //维持登录态
        sessionAttr = new HashMap<String, Object>();
        sessionAttr.put("userId", userExisted.getId());
    }

    @Test
    public void signUpTestNormal() throws Exception{
        MvcResult result = mockMvc.perform(post("/users/sign-up")
        .content(this.json(userNew))
        .contentType(contentType))
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn();
        String userString = result.getResponse().getContentAsString();
        ObjectMapper objectMapper = new ObjectMapper();
        userForUserNew = objectMapper.readValue(userString,User.class);
    }

    @After
    public void clear(){//去除加入的数据
        userRepository.delete(userExisted);
        userRepository.delete(userForUserNew);
    }

    protected String json(Object o) throws IOException {
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
        this.mappingJackson2HttpMessageConverter.write(
                o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
        return mockHttpOutputMessage.getBodyAsString();
    }

}
