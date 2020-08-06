package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.Exception.Error;
import com.thoughtworks.rslist.Exception.RsEventIndexInvalidException;
import com.thoughtworks.rslist.Exception.RsEventRequestParamException;
import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.domain.User;
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
  private final List<RsEvent> rsEventList = initRsEventList();
  private final List<User> userList = initUserList();

  public RsController() throws SQLException {
  }

  private static List<RsEvent> createInitialise() throws SQLException {
    Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/rsSystem",
            "root", "liaowenqiang");
    DatabaseMetaData databaseMetaData = connection.getMetaData();
    ResultSet resultSet = databaseMetaData.getTables(null, null, "rsEvent", null);

    if (!resultSet.next()) {
      String createTableSql = "create table rs_event(eventName varchar(200) not null, keyWord varchar(100) not null)";
      Statement statement = connection.createStatement();
      statement.execute(createTableSql);
    }
    return new ArrayList<>();
  }


  private List<RsEvent> initRsEventList() {
    return getRsEventList();
  }

  private List<User> initUserList() {
    return getUserList();
  }

  static List<User> getUserList() {
    List<User> userList = new ArrayList<>();

    userList.add(new User("XiMin", 19, "male", "xiao.ming@thoughtworks.com", "18888888888", 1));
    userList.add(new User("XiLi", 20, "male", "xiao.li@thoughtworks.com", "18888888887", 2));
    userList.add(new User("XiHong", 21, "female", "xiao.hong@thoughtworks.com", "18888888886", 3));

    return userList;
  }

  static List<RsEvent> getRsEventList() {
    List<RsEvent> rsEventList = new ArrayList<>();

    User user1 = new User("XiMin", 19, "male", "xiao.ming@thoughtworks.com", "18888888888", 1);
    User user2 = new User("XiLi", 20, "male", "xiao.li@thoughtworks.com", "18888888887", 2);
    User user3 = new User("XiHong", 21, "female", "xiao.hong@thoughtworks.com", "18888888886", 3);

    rsEventList.add(new RsEvent("第一事件", "无标签", user1));
    rsEventList.add(new RsEvent("第二事件", "无标签", user2));
    rsEventList.add(new RsEvent("第三事件", "无标签", user3));

    return rsEventList;
  }

  @GetMapping("/rs/{index}")
  public ResponseEntity getRsEventByIndex(@PathVariable int index) {
    if (index < 1 || index > rsEventList.size()) {
      throw new RsEventIndexInvalidException("invalid index");
    }
    return ResponseEntity.ok(rsEventList.get(index - 1));
  }

  @GetMapping("/rs/list")
  public ResponseEntity getRsEventBetween(@RequestParam(required = false) Integer start, @RequestParam(required = false
  ) Integer end) {
    if (start != null || end != null) {
      if (start < 1 || end > rsEventList.size()) {
        throw new RsEventRequestParamException("invalid request param");
      } else {
        return ResponseEntity.ok().body(rsEventList.subList(start - 1, end));
      }
    } else {
      return ResponseEntity.ok().body(rsEventList);
    }
  }

  @PostMapping("/rs/event")
  public ResponseEntity addRsEvent(@RequestBody @Valid RsEvent rsEvent) {
//    ObjectMapper objectMapper = new ObjectMapper();
//    RsEvent event = objectMapper.readValue(rsEvent, RsEvent.class);
//
//    String insertedUserName = rsEvent.getUser().getUserName();
//    List<RsEvent> rsEventListFiltered = rsEventList.stream().filter(rsEvent1 -> rsEvent1.getUser().getUserName() == insertedUserName).collect(Collectors.toList());
//    if (rsEventListFiltered.isEmpty()) {
//      userList.add(rsEvent.getUser());
//    }

    rsEventList.add(rsEvent);
    return ResponseEntity.created(null)
            .body(String.valueOf(rsEventList.size() - 1));
  }

  @GetMapping("/user")
  public List<User> getUsersList() {
    return userList;
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
  public ResponseEntity addEventWithoutUser(@RequestBody @Valid RsEvent rsEvent) {
    rsEventList.add(rsEvent);
    return ResponseEntity.created(null).body(String.valueOf(rsEventList.size() - 1));
  }

}
