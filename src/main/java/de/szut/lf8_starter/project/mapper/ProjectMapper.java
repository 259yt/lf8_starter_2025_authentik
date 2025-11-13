package de.szut.lf8_starter.project.mapper;

import de.szut.lf8_starter.project.dto.ProjectCreateDto;
import de.szut.lf8_starter.project.dto.ProjectGetDto;
import de.szut.lf8_starter.project.entity.ProjectEntity;
import org.springframework.stereotype.Component;

@Component
public class ProjectMapper {

    public ProjectEntity mapCreateDtoToEntity(ProjectCreateDto dto) {
        if (dto == null) return null;
        return ProjectEntity.builder()
                .title(dto.getTitle())
                .customerId(dto.getCustomerId())
                .responsibleEmployeeId(dto.getResponsibleEmployeeId())
                .customerContactName(dto.getCustomerContactName())
                .comment(dto.getComment())
                .startDate(dto.getStartDate())
                .plannedEndDate(dto.getPlannedEndDate())
                .actualEndDate(dto.getActualEndDate())
                .build();
    }

    public void mapCreateDtoToExisting(ProjectCreateDto dto, ProjectEntity entity) {
        if (dto == null || entity == null) return;
        entity.setTitle(dto.getTitle());
        entity.setCustomerId(dto.getCustomerId());
        entity.setResponsibleEmployeeId(dto.getResponsibleEmployeeId());
        entity.setCustomerContactName(dto.getCustomerContactName());
        entity.setComment(dto.getComment());
        entity.setStartDate(dto.getStartDate());
        entity.setPlannedEndDate(dto.getPlannedEndDate());
        entity.setActualEndDate(dto.getActualEndDate());
    }

    public ProjectGetDto mapEntityToGetDto(ProjectEntity e) {
        if (e == null) return null;
        ProjectGetDto dto = new ProjectGetDto();
        dto.setId(e.getId());
        dto.setTitle(e.getTitle());
        dto.setCustomerId(e.getCustomerId());
        dto.setResponsibleEmployeeId(e.getResponsibleEmployeeId());
        dto.setCustomerContactName(e.getCustomerContactName());
        dto.setComment(e.getComment());
        dto.setStartDate(e.getStartDate());
        dto.setPlannedEndDate(e.getPlannedEndDate());
        dto.setActualEndDate(e.getActualEndDate());
        return dto;
    }
}