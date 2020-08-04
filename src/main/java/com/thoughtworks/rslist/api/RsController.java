package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domian.RsEvent;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
  public void patchRsEvent(@PathVariable int index, @RequestBody RsEvent rsEvent) {
    rsEventList.set(index - 1, rsEvent);
  }

  @DeleteMapping("/rs/{index}")
  public void deleteRsEventByIndex(@PathVariable int index) {
    rsEventList.remove(index - 1);
  }
}
