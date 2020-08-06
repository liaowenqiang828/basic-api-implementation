package com.thoughtworks.rslist;

import com.fasterxml.jackson.core.JsonProcessingException;
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

import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RsEventRepository rsEventRepository;
    ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        objectMapper = new ObjectMapper();
        UserDto userDto1 = UserDto.builder().userName("zhao").age(20).gender("male").email("zhao@zhao.com").phone("18888888881").voteNum(1).build();
        UserDto userDto2 = UserDto.builder().userName("qian").age(20).gender("male").email("qian@qian.com").phone("18888888882").voteNum(2).build();
        UserDto userDto3 = UserDto.builder().userName("sun").age(20).gender("male").email("sun@sun.com").phone("18888888883").voteNum(3).build();
        userRepository.save(userDto1);
        userRepository.save(userDto2);
        userRepository.save(userDto3);
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
        rsEventRepository.deleteAll();
    }

    @Test
    void should_register_user() throws Exception {
        User user = new User("xiaoming", 25, "male", "a@b.com", "18888888888", 5);
        String jsonString = objectMapper.writeValueAsString(user);

        mockMvc.perform(post("/user").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        List<UserDto> userDtoList = userRepository.findAll();
        assertEquals(4, userDtoList.size());
        assertEquals("xiaoming", userDtoList.get(3).getUserName());
        assertEquals("a@b.com", userDtoList.get(3).getEmail());
    }

    @Test
    void should_return_user_by_id () throws Exception {
        mockMvc.perform(get("/user/3").contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.userName", is("sun")))
                .andExpect(jsonPath("$.gender", is("male")))
                .andExpect(jsonPath("$.email", is("sun@sun.com")))
                .andExpect(jsonPath("$.phone", is("18888888883")))
                .andExpect(jsonPath("$.age", is(20)))
                .andExpect(jsonPath("$.voteNum", is(3)))
                .andExpect(status().isOk());
    }

    @Test
    void should_delete_user_by_id () throws Exception {
        mockMvc.perform(delete("/user/2").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        List<UserDto> userDotList = userRepository.findAll();
        assertEquals(2, userDotList.size());
        assertEquals("zhao", userDotList.get(0).getUserName());

    }

    @Test
    public void should_delete_user() throws Exception {
        UserDto save = userRepository.save(UserDto.builder().userName("li").age(20)
                .gender("male").email("li@li.com").phone("18888888884").voteNum(4).build());

        RsEventDto rsEventDto = RsEventDto.builder().eventName("猪肉涨价了").keyWord("经济").userId(save.getId()).build();
        rsEventRepository.save(rsEventDto);

        mockMvc.perform(delete("/user/{id}", save.getId())).andExpect(status().isOk());
        assertEquals(3, userRepository.findAll().size());
        assertEquals(0, rsEventRepository.findAll().size());
    }
}
