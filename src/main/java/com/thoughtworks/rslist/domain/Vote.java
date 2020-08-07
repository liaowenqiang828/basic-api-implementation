package com.thoughtworks.rslist.domain;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class Vote {
    @Setter
    @Getter
    private int voteNum;
    @Setter
    @Getter
    private int userId;
    @Setter
    @Getter
    private int rsEventId;
    @Setter
    @Getter
    private LocalDateTime localDateTime;
}
