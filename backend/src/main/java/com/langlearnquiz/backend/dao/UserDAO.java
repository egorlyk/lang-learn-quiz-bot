package com.langlearnquiz.backend.dao;

import com.langlearnquiz.backend.dto.QuestionDTO;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
@RedisHash("user")
public class UserDAO {
    @Id
    private long id;
    private String topic;
    private int userState;
    private QuestionDTO questionDTO;
}
