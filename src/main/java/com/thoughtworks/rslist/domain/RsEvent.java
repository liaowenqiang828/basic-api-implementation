package com.thoughtworks.rslist.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jdk.nashorn.internal.ir.annotations.Ignore;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class RsEvent {
    String eventName;
    String keyWord;
    @Valid
    int userId;
    int voteNum;

    public RsEvent(String eventName, String keyWord, int userId, int voteNum) {
        this.eventName = eventName;
        this.keyWord = keyWord;
        this.userId = userId;
        this.voteNum = voteNum;
    }

    public RsEvent() {}

    public RsEvent(String eventName, String keyWord) {
        this.eventName = eventName;
        this.keyWord = keyWord;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getVoteNum() {
        return voteNum;
    }

    public void setVoteNum(int voteNum) {
        this.voteNum = voteNum;
    }

    @Ignore
    public int getUserId() {
        return this.userId;
    }

    @JsonIgnore
    public void setUser(int userId) {
        this.userId = userId;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }
}
