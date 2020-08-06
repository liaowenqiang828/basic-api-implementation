package com.thoughtworks.rslist;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.dto.UserDto;
import com.thoughtworks.rslist.repository.UserRepository;
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
        User user = new User("xiaoming", 25, "male", "a@b.com", "18888888888", 5);
        String jsonString = objectMapper.writeValueAsString(user);

        mockMvc.perform(post("/user").content(jsonString).contentType(MediaType.APPLICATION_JSON));

        mockMvc.perform(get("/user/4").contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.userName", is("xiaoming")))
                .andExpect(jsonPath("$.gender", is("male")))
                .andExpect(jsonPath("$.email", is("a@b.com")))
                .andExpect(jsonPath("$.phone", is("18888888888")))
                .andExpect(jsonPath("$.age", is(25)))
                .andExpect(jsonPath("$.voteNum", is(5)))
                .andExpect(status().isOk());
    }

    @Test
    void should_delete_user_by_id () throws Exception {
        mockMvc.perform(delete("/user/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        List<UserDto> userDotList = userRepository.findAll();
        assertEquals(2, userDotList.size());
        assertEquals("qian", userDotList.get(0).getUserName());

    }
}
