package com.thoughtworks.rslist.api;

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
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class RsEventControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RsEventRepository rsEventRepository;
    UserDto userDto;
    RsEventDto rsEventDto;

    @BeforeEach
    void setUp() {
       userDto = UserDto.builder().userName("li").age(20).gender("male").email("li@li.com").phone("18888888884").voteNum(4).build();
       rsEventDto = RsEventDto.builder().eventName("猪肉涨价了").keyWord("经济").userDto(userDto).build();
    }

    @AfterEach
        void tearDown() {
        userRepository.deleteAll();
        rsEventRepository.deleteAll();
    }

    @Test
    void should_add_rs_event_when_user_exist() throws Exception {
        userDto = userRepository.save(userDto);
        String jsonString = "{\"eventName\":\"猪肉涨价了\",\"keyWord\":\"经济\",\"userId\":" + userDto.getId() + "}";

        mockMvc.perform(post("/rs/event").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        List<RsEventDto> rsEventDtoList = rsEventRepository.findAll();
        assertNotNull(rsEventDtoList);
        assertEquals(1, rsEventDtoList.size());
        assertEquals("猪肉涨价了", rsEventDtoList.get(0).getEventName());
        assertEquals("经济", rsEventDtoList.get(0).getKeyWord());
        assertEquals(userDto.getId(), rsEventDtoList.get(0).getUserDto().getId());
    }

    @Test
    void should_add_rs_event_when_user_not_exist() throws Exception {
        String jsonString = "{\"eventName\":\"猪肉涨价了\",\"keyWord\":\"经济\",\"userId\":100}";

        mockMvc.perform(post("/rs/event").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_update_rs_event_when_userId_match_with_eventId() throws Exception {
        userDto = userRepository.save(userDto);
        rsEventDto = rsEventRepository.save(rsEventDto);
        String jsonString = "{\"eventName\":\"猪肉涨价了\",\"keyWord\":\"经济\",\"userId\":" + userDto.getId() + "}";

        mockMvc.perform(patch("/rs/update/" + rsEventDto.getUserDto().getId())
                .content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Optional<RsEventDto> rsEventDtoUpdated = rsEventRepository.findById(rsEventDto.getId());
        assertEquals("猪肉涨价了", rsEventDtoUpdated.get().getEventName());
        assertEquals("经济", rsEventDtoUpdated.get().getKeyWord());
    }

    @Test
    void should_return_bad_request_when_userId_not_match_with_eventId() throws Exception {
        userDto = userRepository.save(userDto);
        rsEventDto = rsEventRepository.save(rsEventDto);
        String jsonString = "{\"eventName\":\"猪肉涨价了\",\"keyWord\":\"经济\",\"userId\": 1}";

        mockMvc.perform(patch("/rs/update/" + rsEventDto.getUserDto().getId())
                .content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_only_update_event_name() throws Exception {
        userDto = userRepository.save(userDto);
        rsEventDto = rsEventRepository.save(rsEventDto);
        String jsonString = "{\"eventName\":\"猪肉降价了\",\"userId\":" + userDto.getId() + "}";

        mockMvc.perform(patch("/rs/update/" + rsEventDto.getUserDto().getId())
                .content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        rsEventDto = rsEventRepository.findById(userDto.getId()).get();
        assertEquals(rsEventDto.getEventName(), "猪肉降价了");
        assertEquals(rsEventDto.getKeyWord(), "经济");
    }

    @Test
    void should_only_update_key_word() throws Exception {
        userDto = userRepository.save(userDto);
        rsEventDto = rsEventRepository.save(rsEventDto);
        String jsonString = "{\"keyWord\":\"生活\",\"userId\":" + userDto.getId() + "}";

        mockMvc.perform(patch("/rs/update/" + rsEventDto.getUserDto().getId())
                .content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        rsEventDto = rsEventRepository.findById(userDto.getId()).get();
        assertEquals(rsEventDto.getEventName(), "猪肉涨价了");
        assertEquals(rsEventDto.getKeyWord(), "生活");
    }
}
