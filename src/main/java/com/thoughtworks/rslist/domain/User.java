package com.thoughtworks.rslist.domain;

import javax.validation.constraints.*;

public class User {
    @NotNull
    @Size(max = 8)
    private String userName;
    @NotNull
    @Min(18)
    @Max(100)
    private int age;
    @NotNull
    private String gender;
    @Email
    private String email;
    @Pattern(regexp = "1\\d{10}")
    private String phone;
    @NotNull
    private int voteNum;

    public int getVoteNum() {
        return voteNum;
    }

    public void setVoteNum(int voteNum) {
        this.voteNum = voteNum;
    }

    public User(String userName, int age, String gender, String email, String phone, int voteNum) {
        this.userName = userName;
        this.age = age;
        this.gender = gender;
        this.email = email;
        this.phone = phone;
        this.voteNum = voteNum;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

}
