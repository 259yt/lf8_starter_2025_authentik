package de.szut.lf8_starter.project.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProjectEmployeeDto {

    @JsonProperty("ma_id")
    private Long employeeId;

    @JsonProperty("role_id")
    private Long roleId;
}
