package practice.shop.common;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Getter
public class ApiResponse<T> {
    private final ZonedDateTime time = ZonedDateTime.of(LocalDateTime.now(), ZoneId.of("Asia/Seoul"));
    private T data;
    private responseStatus status;

    @Builder
    public ApiResponse(T data, responseStatus status) {
        this.data = data;
        this.status = status;
    }
}
