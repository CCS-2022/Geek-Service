package com.geek.geekservice.pojo;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
public class StatusUpdateModel {
    @NotNull @NonNull
    private Long serviceId;
    @NotNull @NonNull
    private Status status;
    private String reason;
}
