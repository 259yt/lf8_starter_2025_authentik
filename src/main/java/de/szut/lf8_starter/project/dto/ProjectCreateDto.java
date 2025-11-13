package de.szut.lf8_starter.project.dto;

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

    @NotNull
    private Long responsibleEmployeeId;

    @NotBlank
    private String customerContactName;

    @NotBlank
    private String comment;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate plannedEndDate;

    private LocalDate actualEndDate;
}
