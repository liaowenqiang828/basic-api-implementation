package com.thoughtworks.rslist;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class RsListApplicationTests {

    @Autowired
    MockMvc mockMvc;

    @BeforeEach
    void setup() {
        List<RsEvent> rsEventList = new ArrayList<>();

        User user1 = new User("XiMin", 19, "male", "xiao.ming@thoughtworks.com", "18888888888", 1);
        User user2 = new User("XiLi", 20, "male", "xiao.li@thoughtworks.com", "18888888887", 2);
        User user3 = new User("XiHong", 21, "female", "xiao.hong@thoughtworks.com", "18888888886", 3);

        rsEventList.add(new RsEvent("第一事件", "无标签", 1));
        rsEventList.add(new RsEvent("第二事件", "无标签", 2));
        rsEventList.add(new RsEvent("第三事件", "无标签", 3));
    }

    @Test
    void get_one_rs_event_by_index() throws Exception {
        mockMvc.perform(get("/rs/1"))
                .andExpect(jsonPath("$.eventName", is("第一事件")))
                .andExpect(jsonPath("$.keyWord", is("无标签")))
                .andExpect(status().isOk());
    }

    @Test
    void get_rs_event_list_between_start_and_end_index() throws Exception {
        mockMvc.perform(get("/rs/list?start=1&end=2"))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].eventName", is("第一事件")))
                .andExpect(jsonPath("$[0].keyWord", is("无标签")))
                .andExpect(jsonPath("$[1].eventName", is("第二事件")))
                .andExpect(jsonPath("$[1].keyWord", is("无标签")))
                .andExpect(status().isOk());
    }

    @Test
    void should_return_add_rs_event() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        User user = new User("XiMin", 19, "male", "xiao.ming@thoughtworks.com", "18888888888", 4);
        RsEvent rsEvent = new RsEvent("猪肉涨价了", "经济", 4);
        String jsonString = objectMapper.writeValueAsString(rsEvent);
        mockMvc.perform(post("/rs/event").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().string("3"));
        mockMvc.perform(get("/rs/list"))
                .andExpect(jsonPath("$", hasSize(4)))
                .andExpect(jsonPath("$[0].eventName", is("第一事件")))
                .andExpect(jsonPath("$[0].keyWord", is("无标签")))
                .andExpect(jsonPath("$[1].eventName", is("第二事件")))
                .andExpect(jsonPath("$[1].keyWord", is("无标签")))
                .andExpect(jsonPath("$[2].eventName", is("第三事件")))
                .andExpect(jsonPath("$[2].keyWord", is("无标签")))
                .andExpect(jsonPath("$[3].eventName", is("猪肉涨价了")))
                .andExpect(jsonPath("$[3].keyWord", is("经济")))
                .andExpect(status().isOk());
    }

    @Test
    void should_patch_rs_event_by_index() throws Exception {
        String jsonString = "{\"keyWord\":\"生活\"}";
        mockMvc.perform(patch("/rs/1").content(jsonString).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/list"))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].eventName", is("第一事件")))
                .andExpect(jsonPath("$[0].keyWord", is("生活")))
                .andExpect(jsonPath("$[1].eventName", is("第二事件")))
                .andExpect(jsonPath("$[1].keyWord", is("无标签")))
                .andExpect(jsonPath("$[2].eventName", is("第三事件")))
                .andExpect(jsonPath("$[2].keyWord", is("无标签")))
                .andExpect(status().isOk());
    }

    @Test
    void should_delete_rs_event_by_index() throws Exception {
        mockMvc.perform(delete("/rs/2").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mockMvc.perform(get("/rs/list"))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].eventName", is("第一事件")))
                .andExpect(jsonPath("$[0].keyWord", is("无标签")))
                .andExpect(jsonPath("$[1].eventName", is("第三事件")))
                .andExpect(jsonPath("$[1].keyWord", is("无标签")));
    }

    @Test
    void should_add_user() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        User user = new User("XiMin", 19, "male", "xiao.ming@thoughtworks.com", "18888888888", 4);
        String jsonString = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void should_add_user_to_user_list() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        User user = new User("XiQin", 19, "male", "xiao.ming@thoughtworks.com", "18888888888", 4);
        RsEvent rsEvent = new RsEvent("添加一条热搜", "娱乐", 5);

        String jsonString = objectMapper.writeValueAsString(rsEvent);
//        String jsonString = "{\"eventName\":\"猪肉涨价了\",\"keyWord\":\"经济\",\"user\": {\"userName\":\"xyxia\",\"age\": 19,\"gender\": \"male\",\"email\": \"a@b.com\",\"phone\": \"18888888888\"}}";

        mockMvc.perform(post("/rs/event").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/user"))
                .andExpect(jsonPath("$", hasSize(4)))
                .andExpect(jsonPath("$[3].user.userName", is("xyxia")))
                .andExpect(status().isOk());
    }

    @Test
    void user_name_should_less_than_8() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        User user = new User("XiaoMingMing", 18, "male", "xiaoming@thoughtworks.com", "18888888888", 4);
        String jsonString = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
    @Test
    void user_age_should_between_18_and_100() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        User user = new User("XiaoMi", 17, "male", "xiaoming@thoughtworks.com", "18888888888", 4);
        String jsonString = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_have_right_format_email() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        User user = new User("XiaoMi", 19, "male", "xiaomi.thoughtworks.com", "18888888888", 4);
        String jsonString = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void phone_number_should_start_with_1() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        User user = new User("XiaoMi", 19, "male", "xiaomi@thoughtworks.com", "88888888888", 4);
        String jsonString = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void phone_number_length_should_be_11() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        User user = new User("XiaoMi", 19, "male", "xiaomi@thoughtworks.com", "188888888", 4);
        String jsonString = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_return_201_and_index() throws Exception{
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        User user = new User("XiMin", 19, "male", "xiao.ming@thoughtworks.com", "18888888888", 4);
        RsEvent rsEvent = new RsEvent("添加一条热搜", "娱乐", 5);
        String jsonString = objectMapper.writeValueAsString(rsEvent);
        mockMvc.perform(post("/rs/add").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().string("3"));
    }

    @Test
    void should_return_rs_event_without_user() throws Exception {
        mockMvc.perform(get("/rs/list").contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].eventName", is("第一事件")))
                .andExpect(jsonPath("$[0].keyWord", is("无标签")))
                .andExpect(jsonPath("$[0]", not(hasKey("user"))))
                .andExpect(jsonPath("$[1].eventName", is("第二事件")))
                .andExpect(jsonPath("$[1].keyWord", is("无标签")))
                .andExpect(jsonPath("$[1]", not(hasKey("user"))))
                .andExpect(jsonPath("$[2].eventName", is("第三事件")))
                .andExpect(jsonPath("$[2].keyWord", is("无标签")))
                .andExpect(jsonPath("$[0]", not(hasKey("user"))))
                .andExpect(status().isOk());
    }

    @Test
    void should_return_rs_event_by_index_without_user() throws Exception {
        mockMvc.perform(get("/rs/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.eventName", is("第一事件")))
                .andExpect(jsonPath("$.keyWord", is("无标签")))
                .andExpect(jsonPath("$", not(hasKey("user"))))
                .andExpect(status().isOk());
    }

    @Test
    void should_throw_index_exception() throws Exception {
        mockMvc.perform(get("/rs/0"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("invalid index")));
    }

    @Test
    void should_throw_index_param_exception() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        User user = new User("XiaoMiiiiiii", 19, "male", "xiaomi@thoughtworks.com", "18888888888", 4);
        RsEvent rsEvent = new RsEvent("添加一条热搜", "娱乐", 5);
        String jsonString = objectMapper.writeValueAsString(rsEvent);

        mockMvc.perform(post("/rs/event").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("invalid param")));
    }

    @Test
    void should_throw_invalid_request_param_exception() throws Exception {
        mockMvc.perform(get("/rs/list?start=1&end=10"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("invalid request param")));
    }

    @Test
    void should_throw_invalid_user_exception() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        User user = new User("XiaoMi", 19, "male", "xiaomi@thoughtworks.com", "188888888", 4);
        String jsonString = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("invalid user")));
    }

    @Test
    void should_return_all_users() throws Exception {

    }

}













