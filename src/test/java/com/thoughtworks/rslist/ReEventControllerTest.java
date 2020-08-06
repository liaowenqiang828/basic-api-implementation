package com.thoughtworks.rslist;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.dto.RsEventDto;
import com.thoughtworks.rslist.dto.UserDto;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ReEventControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RsEventRepository rsEventRepository;

    @BeforeEach
        void setup() {
//        UserDto userDto1 = UserDto.builder().userName("zhao").age(20).gender("male").email("zhao@zhao.com").phone("18888888881").voteNum(1).build();
//        UserDto userDto2 = UserDto.builder().userName("qian").age(20).gender("male").email("qian@qian.com").phone("18888888882").voteNum(2).build();
//        UserDto userDto3 = UserDto.builder().userName("sun").age(20).gender("male").email("sun@sun.com").phone("18888888883").voteNum(3).build();
//        userRepository.save(userDto1);
//        userRepository.save(userDto2);
//        userRepository.save(userDto3);

//        rsEventRepository.save(RsEventDto.builder().eventName("第一事件").keyWord("无标签").userId(1).build());
//        rsEventRepository.save(RsEventDto.builder().eventName("第二事件").keyWord("无标签").userId(2).build());
//        rsEventRepository.save(RsEventDto.builder().eventName("第三事件").keyWord("无标签").userId(3).build());

    }
    @AfterEach
        void tearDown() {
        userRepository.deleteAll();
        rsEventRepository.deleteAll();
    }

    @Test
    void should_add_rs_event_when_user_exist() throws Exception {
        UserDto save = userRepository.save(UserDto.builder().userName("li").age(20).gender("male").email("li@li.com").phone("18888888884").voteNum(4).build());

        String jsonString = "{\"eventName\":\"猪肉涨价了\",\"keyWord\":\"经济\",\"userId\":" + save.getId() + "}";

        mockMvc.perform(post("/rs/event").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        List<RsEventDto> rsEventDtoList = rsEventRepository.findAll();
        assertNotNull(rsEventDtoList);
        assertEquals(1, rsEventDtoList.size());
        assertEquals("猪肉涨价了", rsEventDtoList.get(0).getEventName());
        assertEquals("经济", rsEventDtoList.get(0).getKeyWord());
//        assertEquals(save.getId(), rsEventDtoList.get(0).getUserId());
    }

    @Test
    void should_add_rs_event_when_user_not_exist() throws Exception {
        String jsonString = "{\"eventName\":\"猪肉涨价了\",\"keyWord\":\"经济\",\"userId\":100}";

        mockMvc.perform(post("/rs/event").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
