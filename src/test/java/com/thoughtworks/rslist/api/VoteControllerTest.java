package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.thoughtworks.rslist.domain.Vote;
import com.thoughtworks.rslist.dto.RsEventDto;
import com.thoughtworks.rslist.dto.UserDto;
import com.thoughtworks.rslist.dto.VoteDto;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.repository.VoteRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class VoteControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RsEventRepository rsEventRepository;
    @Autowired
    VoteRepository voteRepository;
    UserDto userDto;
    RsEventDto rsEventDto;
    VoteDto voteDto;
    ObjectMapper objectMapper;
    LocalDateTime localDateTime;

    @BeforeEach
    void setUp() {
        userDto = UserDto.builder()
                .userName("zhou").age(20).gender("male").email("zhou@zhou.com")
                .phone("18888888885").voteNum(5).build();
        userDto = userRepository.save(userDto);

        rsEventDto = RsEventDto.builder().keyWord("key word").
                eventName("event name").userDto(userDto).build();
        rsEventDto = rsEventRepository.save(rsEventDto);

        voteDto = VoteDto.builder().localDateTime(LocalDateTime.now())
                .user(userDto).rsEvent(rsEventDto).voteNum(5).build();
        voteDto = voteRepository.save(voteDto);

        objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        localDateTime = LocalDateTime.now();
    }

    @AfterEach
    void tearDown() {
        voteRepository.deleteAll();
        userRepository.deleteAll();
        rsEventRepository.deleteAll();
    }

    @Test
    void should_get_vote_record() throws Exception {
        mockMvc.perform(get("/voteRecord")
                .param("userId", String.valueOf(userDto.getId()))
                .param("rsEventId", String.valueOf(rsEventDto.getId())))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].userId", is(userDto.getId())))
                .andExpect(jsonPath("$[0].rsEventId", is(rsEventDto.getId())))
                .andExpect(jsonPath("$[0].voteNum", is(5)));
    }

    @Test
    void should_vote_success() throws Exception {
        String jsonString = "{\"voteNum\":5,\"userId\":1,\"localDateTime\":\"2020-08-07T21:00:42.669\"}";
        mockMvc.perform(post("/rs/vote/1").content(jsonString).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
        userDto = userRepository.findById(userDto.getId()).get();
        assertEquals(0, userDto.getVoteNum());
    }

    @Test
    void should_return_400_when_user_voteNum_less_than_vote_voteNum() throws Exception {
        String jsonString = "{\"voteNum\":10,\"userId\":1,\"localDateTime\":\"2020-08-07T21:00:42.669\"}";
        mockMvc.perform(post("/rs/vote/1").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}