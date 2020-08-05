package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.domain.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class RsController {
  private final List<RsEvent> rsEventList = initRsEventList();

  private List<RsEvent> initRsEventList() {
    List<RsEvent> rsEventList = new ArrayList<>();
    User user1 = new User("XiMin", 19, "male", "xiao.ming@thoughtworks.com", "18888888888");
    User user2 = new User("XiLi", 20, "male", "xiao.li@thoughtworks.com", "18888888887");
    User user3 = new User("XiHong", 21, "female", "xiao.hong@thoughtworks.com", "18888888886");

    rsEventList.add(new RsEvent("第一事件", "无标签", user1));
    rsEventList.add(new RsEvent("第二事件", "无标签", user2));
    rsEventList.add(new RsEvent("第三事件", "无标签", user3));

    return rsEventList;
  }

  @GetMapping("/rs/{index}")
  public RsEvent getRsEventByIndex(@PathVariable int index) {
    return rsEventList.get(index - 1);
  }

  @GetMapping("/rs/list")
  public List<RsEvent> getRsEventBetween(@RequestParam(required = false) Integer start, @RequestParam(required = false
  ) Integer end) {
    if (start != null && end != null) {
      return rsEventList.subList(start - 1, end);
    }
    return rsEventList;
  }

  @PostMapping("/rs/event")
  public ResponseEntity addRsEvent(@RequestBody RsEvent rsEvent) {
    rsEventList.add(rsEvent);
    return ResponseEntity.created(null).build();
  }

  @PatchMapping("/rs/{index}")
  public void patchRsEvent(@PathVariable int index, @RequestBody String jsonString) throws JsonProcessingException {
    ObjectMapper objectMapper = new ObjectMapper();
    RsEvent originRsEvent = rsEventList.get(index - 1);

    Map<String, String> stringMap = objectMapper.readValue(jsonString, Map.class);
    Map rsEventMap = objectMapper.convertValue(originRsEvent, Map.class);

    Map<String, String> mapConcate = new HashMap<>();
    mapConcate.putAll(rsEventMap);
    mapConcate.putAll(stringMap);

    RsEvent newRsEvent = new RsEvent(mapConcate.get("eventName"), mapConcate.get("keyWord"));
    rsEventList.set(index - 1, newRsEvent);
  }

  @DeleteMapping("/rs/{index}")
  public void deleteRsEventByIndex(@PathVariable int index) {
    rsEventList.remove(index - 1);
  }

  @PostMapping("/rs/add")
  public void addEventWithoutUser(@RequestBody RsEvent rsEvent) {
    rsEventList.add(rsEvent);
//    return ResponseEntity.created(null).build();
  }
}
