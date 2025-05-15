package com.shousi.web.model.dto.thumb;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @Author 小小星仔
 * @Create 2025-05-13 21:11
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ThumbEvent implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Long userId;
    private Long pictureId;
    private EventType type;
    private LocalDateTime eventTime;
    
    public enum EventType {
        /**
         * 增加点赞
         */
        INCR,
        /**
         * 减少点赞
         */
        DECR
    }
}
