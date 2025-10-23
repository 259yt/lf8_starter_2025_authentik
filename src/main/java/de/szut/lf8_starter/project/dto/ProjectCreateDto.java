package de.szut.lf8_starter.project.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ProjectCreateDto {
    @NotBlank
    private String title;
    private long customerId;
    private LocalDate startDate;
    private LocalDate endDate;

}
