package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.dto.UserDto;
import com.thoughtworks.rslist.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull.*;
import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
public class UserController {

    private static List<User> userList = new ArrayList<>();
    @Autowired
    UserRepository userRepository;

//    private final List<RsEvent> rsEventList = initRsEventList();
//    private final List<User> userList = initUserList();
//
//    private List<RsEvent> initRsEventList() {
//        return getRsEventList();
//    }
//
//    private List<User> initUserList() {
//        return getUserList();
//    }
//
//    static List<User> getUserList() {
//        List<User> userList = new ArrayList<>();
//
//        userList.add(new User("XiMin", 19, "male", "xiao.ming@thoughtworks.com", "18888888888", 1));
//        userList.add(new User("XiLi", 20, "male", "xiao.li@thoughtworks.com", "18888888887", 2));
//        userList.add(new User("XiHong", 21, "female", "xiao.hong@thoughtworks.com", "18888888886", 3));
//
//        return userList;
//    }
//
//    static List<RsEvent> getRsEventList() {
//        List<RsEvent> rsEventList = new ArrayList<>();
//
//        User user1 = new User("XiMin", 19, "male", "xiao.ming@thoughtworks.com", "18888888888");
//        User user2 = new User("XiLi", 20, "male", "xiao.li@thoughtworks.com", "18888888887");
//        User user3 = new User("XiHong", 21, "female", "xiao.hong@thoughtworks.com", "18888888886");
//
//        rsEventList.add(new RsEvent("第一事件", "无标签", user1));
//        rsEventList.add(new RsEvent("第二事件", "无标签", user2));
//        rsEventList.add(new RsEvent("第三事件", "无标签", user3));
//
//        return rsEventList;
//    }

    @PostMapping("/user")
    public void userRegister(@RequestBody @Valid User user) {
        UserDto userDto = new UserDto();

        userDto.setAge(user.getAge());
        userDto.setEmail(user.getEmail());
        userDto.setGender(user.getGender());
        userDto.setPhone(user.getPhone());
        userDto.setUserName(user.getUserName());
        userDto.setVoteNum(user.getVoteNum());

        userRepository.save(userDto);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity getUserById(@PathVariable Integer id) {
        UserDto userDto = userRepository.findUserDtosById(id);

        return ResponseEntity.ok(userDto);
    }

    @DeleteMapping("/user/{id}")
    public void deleteUserById(@PathVariable Integer id) {
        userRepository.deleteById(id);
    }

}
