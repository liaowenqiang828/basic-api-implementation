package com.thoughtworks.rslist.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Builder
@Table(name = "rs_event")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RsEventDto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String eventName;
    private String keyWord;
    private int voteNum;
    @ManyToOne
    private UserDto userDto;

}
