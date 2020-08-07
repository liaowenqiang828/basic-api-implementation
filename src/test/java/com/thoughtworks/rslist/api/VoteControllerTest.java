package com.thoughtworks.rslist.api;

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
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

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
    @BeforeEach
    void setUp() {
        userDto = UserDto.builder()
                .userName("zhou").age(20).gender("male").email("zhou@zhou.com")
                .phone("18888888885").voteNum(5).build();
        userDto = userRepository.save(userDto);
        rsEventDto = RsEventDto.builder().keyWord("key word").
                eventName("event name").userDto(userDto).build();
        rsEventDto = rsEventRepository.save(rsEventDto);
        VoteDto voteDto = VoteDto.builder().localDateTime(LocalDateTime.now())
                .user(userDto).rsEvent(rsEventDto).num(5).build();
        voteRepository.save(voteDto);
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

}