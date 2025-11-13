package de.szut.lf8_starter.project.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AssignEmployeeRequestDto {

    @NotNull
    @JsonProperty("ma_id")
    private Long maId;

    @NotNull
    @JsonProperty("role_id")
    private Long roleId;
}
