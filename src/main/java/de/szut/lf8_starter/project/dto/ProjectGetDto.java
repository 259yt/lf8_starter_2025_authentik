package de.szut.lf8_starter.project.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ProjectGetDto {
    private Long id;
    private String title;
    private Long customerId;
    private Long responsibleEmployeeId;
    private String customerContactName;
    private String comment;
    private LocalDate startDate;
    private LocalDate plannedEndDate;
    private LocalDate actualEndDate;
}