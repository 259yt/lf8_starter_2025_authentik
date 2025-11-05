package de.szut.lf8_starter.project.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ProjectCreateDto {

    @NotBlank
    private String title;

    @NotNull
    private Long customerId;

    private String description;

    @NotNull
    private LocalDate startDate;

    @Future
    private LocalDate endDate;
}

