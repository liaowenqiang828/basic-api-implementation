package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.Vote;
import com.thoughtworks.rslist.dto.RsEventDto;
import com.thoughtworks.rslist.dto.UserDto;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.repository.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class VoteController {
    @Autowired
    VoteRepository voteRepository;
    @Autowired
    RsEventRepository rsEventRepository;
    @Autowired
    UserRepository userRepository;

    UserDto userDto;
    RsEventDto rsEventDto;

    @GetMapping("/voteRecord")
    public ResponseEntity<List<Vote>> getVoteRecord(@RequestParam int userId, @RequestParam int rsEventId) {
        return ResponseEntity.ok(
                voteRepository.findAllByUserIdAndRsEventId(userId, rsEventId)
                        .stream().map(item -> Vote.builder().userId(item.getUser().getId())
                        .rsEventId(item.getRsEvent().getId()).localDateTime(item.getLocalDateTime())
                        .voteNum(item.getVoteNum()).build()).collect(Collectors.toList()));
    }

    @PostMapping("/rs/vote/{rsEventId}")
    public ResponseEntity vote(@PathVariable int rsEventId, @RequestBody Vote vote) {
        rsEventDto = rsEventRepository.findById(rsEventId).get();
        userDto = userRepository.findById(vote.getUserId()).get();
        if (userDto.getVoteNum() >= vote.getVoteNum()) {
            userDto.setVoteNum(userDto.getVoteNum() - vote.getVoteNum());
            userRepository.save(userDto);

            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }
}
