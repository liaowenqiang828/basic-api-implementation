package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.domian.RsEvent;
import org.springframework.web.bind.annotation.*;

import java.util.*;


@RestController
public class RsController {
  private final List<RsEvent> rsEventList = initRsEventList();

  private List<RsEvent> initRsEventList() {
    List<RsEvent> rsEventList = new ArrayList<>(3);
    rsEventList.add(new RsEvent("第一事件", "无标签"));
    rsEventList.add(new RsEvent("第二事件", "无标签"));
    rsEventList.add(new RsEvent("第三事件", "无标签"));
    return rsEventList;
  }

//  private Map jsonToMap(String string) {
//    JSONObject jsonObject = JSON.parseObject(string);
//
//    Map<String, String> map = new HashMap<>();
//    Iterator<Map.Entry<String, String>> iterator = jsonObject.entrySet().iterator();
//    while (iterator.hasNext()) {
//      Map.Entry<String, String> entry = iterator.next();
//      map.put(entry.getKey(), entry.getValue());
//    }
//    return map;
//  }

  private Map rsEventToMap(RsEvent rsEvent) {
    Map<String, String> map = new HashMap<>();

    map.put("eventName", rsEvent.getEventName());
    map.put("keyWord", rsEvent.getKeyWord());
    return map;
  }

  @GetMapping("/rs/{index}")
  public RsEvent getRsEventByIndex(@PathVariable int index) {
    return rsEventList.get(index - 1);
  }

  @GetMapping("/rs/list")
  public List<RsEvent> getRsEventBetween(@RequestParam(required = false) Integer start, @RequestParam(required = false
  ) Integer end) {
    if (start != null && end != null) {
      return rsEventList.subList(start - 1, end - 1);
    }
    return rsEventList;
  }

  @PostMapping("/rs/event")
  public void addRsEvent(@RequestBody RsEvent rsEvent) {
    rsEventList.add(rsEvent);
  }

  @PatchMapping("/rs/{index}")
  public void patchRsEvent(@PathVariable int index, @RequestBody String jsonString) throws JsonProcessingException {
    ObjectMapper objectMapper = new ObjectMapper();
    RsEvent originRsEvent = rsEventList.get(index - 1);

    Map<String, String> stringMap = objectMapper.readValue(jsonString, Map.class);
//    Map<String, String> rsEventMap =
//    Map stringMap = jsonToMap(jsonString);
    Map rsEventMap = rsEventToMap(originRsEvent);

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
}
