package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.Exception.Error;
import com.thoughtworks.rslist.Exception.RsEventIndexInvalidException;
import com.thoughtworks.rslist.Exception.RsEventRequestParamException;
import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.dto.RsEventDto;
import com.thoughtworks.rslist.dto.UserDto;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Transient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@Validated
public class RsController {
  @Autowired
  RsEventRepository rsEventRepository;
  @Autowired
  UserRepository userRepository;

  public RsController() throws SQLException {
  }

  @GetMapping("/rs/{index}")
  public ResponseEntity getRsEventByIndex(@PathVariable int index) {
    List<RsEventDto> rsEventDtoList = rsEventRepository.findAll();
    if (index < 1 || index > rsEventDtoList.size()) {
      throw new RsEventIndexInvalidException("invalid index");
    }
    return ResponseEntity.ok(rsEventDtoList.get(index - 1));
  }

  @GetMapping("/rs/list")
  public ResponseEntity getRsEventBetween(@RequestParam(required = false) Integer start, @RequestParam(required = false
  ) Integer end) {
    List<RsEventDto> rsEventDtoList = rsEventRepository.findAll();
    if (start != null || end != null) {
      if (start < 1 || end > rsEventDtoList.size()) {
        throw new RsEventRequestParamException("invalid request param");
      } else {
        return ResponseEntity.ok().body(rsEventDtoList.subList(start - 1, end));
      }
    } else {
      return ResponseEntity.ok().body(rsEventDtoList);
    }
  }

  @PostMapping("/rs/event")
  public ResponseEntity addRsEvent(@RequestBody @Valid RsEvent rsEvent) {
    List<RsEventDto> rsEventDtoList = rsEventRepository.findAll();
    Optional<UserDto> userDto = userRepository.findById(rsEvent.getUserId());
    if (!userRepository.findById(rsEvent.getUserId()).isPresent()) {
      return ResponseEntity.badRequest().build();
    }
    RsEventDto rsEventDto = RsEventDto.builder()
            .eventName(rsEvent.getEventName()).keyWord(rsEvent.getKeyWord())
            .userDto(userDto.get()).build();
    rsEventRepository.save(rsEventDto);

    return ResponseEntity.created(null)
            .body(String.valueOf(rsEventDtoList.size() - 1));
  }

  @PatchMapping("/rs/update/{rsEventId}")
  public ResponseEntity patchRsEvent(@PathVariable int rsEventId, @RequestBody RsEvent rsEvent) {
    Optional<UserDto> userDto = userRepository.findById(rsEvent.getUserId());
    Optional<RsEventDto> rsEventDto = rsEventRepository.findById(rsEventId);

    if (rsEventDto.isPresent() && userDto.isPresent()) {
      if (rsEventDto.get().getUserDto().getId() == userDto.get().getId()) {
        if (rsEvent.getEventName() != null) {
          rsEventDto.get().setEventName(rsEvent.getEventName());
        }
        if (rsEvent.getKeyWord() != null) {
          rsEventDto.get().setKeyWord(rsEvent.getKeyWord());
        }
        rsEventRepository.save(rsEventDto.get());
      } else {
        return ResponseEntity.badRequest().build();
      }
    } else {
      return ResponseEntity.badRequest().build();
    }
    return ResponseEntity.ok().build();
  }

  @DeleteMapping("/rs/{index}")
  public void deleteRsEventByIndex(@PathVariable int index) {
    List<RsEventDto> rsEventDtoList = rsEventRepository.findAll();
    RsEventDto rsEventDto = rsEventDtoList.remove(index - 1);
    rsEventRepository.delete(rsEventDto);
  }

  @PostMapping("/rs/add")
  public ResponseEntity addEventWithoutUser(@RequestBody @Valid RsEvent rsEvent) {
    List<RsEventDto> rsEventDtoList = rsEventRepository.findAll();
    UserDto userDto = userRepository.findById(rsEvent.getUserId()).get();
    rsEventDtoList.add(RsEventDto.builder()
            .eventName(rsEvent.getEventName())
            .keyWord(rsEvent.getKeyWord())
            .userDto(userDto)
            .voteNum(rsEvent.getVoteNum())
            .build());
    return ResponseEntity.created(null).body(String.valueOf(rsEventDtoList.size() - 1));
  }

}
