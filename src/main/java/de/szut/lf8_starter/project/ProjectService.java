package de.szut.lf8_starter.project;

import de.szut.lf8_starter.employee.EmployeeClient;
import de.szut.lf8_starter.exception.ResourceNotFoundException;
import de.szut.lf8_starter.project.dto.EmployeeProjectsResponseDto;
import de.szut.lf8_starter.project.dto.ProjectCreateDto;
import de.szut.lf8_starter.project.dto.ProjectEmployeeDto;
import de.szut.lf8_starter.project.entity.ProjectAssignmentEntity;
import de.szut.lf8_starter.project.entity.ProjectEntity;
import de.szut.lf8_starter.project.repository.ProjectAssignmentRepository;
import de.szut.lf8_starter.project.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectAssignmentRepository assignmentRepository;
    private final EmployeeClient employeeClient;


    private boolean customerExistsDummy(Long customerId) {
        return customerId != null && (customerId == 42L || customerId == 101L);
    }

    public void createProject(ProjectCreateDto dto) {
        if (dto.getTitle() == null || dto.getTitle().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Title darf nicht leer sein.");
        }

        if (!customerExistsDummy(dto.getCustomerId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found.");
        }

        if (dto.getStartDate().isAfter(dto.getPlannedEndDate())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Startdatum darf nicht nach geplantem Enddatum liegen.");
        }

        ProjectEntity entity = ProjectEntity.builder()
                .title(dto.getTitle())
                .customerId(dto.getCustomerId())
                .responsibleEmployeeId(dto.getResponsibleEmployeeId())
                .customerContactName(dto.getCustomerContactName())
                .comment(dto.getComment())
                .startDate(dto.getStartDate())
                .plannedEndDate(dto.getPlannedEndDate())
                .actualEndDate(dto.getActualEndDate())
                .build();

        projectRepository.save(entity);
    }

    public List<ProjectEntity> getAllProjects() {
        return projectRepository.findAll();
    }

    public ProjectEntity getProjectById(long id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + id));
    }

    public boolean deleteProjectById(long id) {
        if (!projectRepository.existsById(id)) {
            return false;
        }
        projectRepository.deleteById(id);
        return true;
    }

    public boolean updateProject(long id, ProjectCreateDto dto) {
        ProjectEntity p = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + id));

        if (dto.getTitle() == null || dto.getTitle().isBlank()) {
            throw new IllegalArgumentException("Title darf nicht leer sein.");
        }

        if (!customerExistsDummy(dto.getCustomerId())) {
            throw new ResourceNotFoundException("Customer not found with id: " + dto.getCustomerId());
        }

        if (dto.getStartDate().isAfter(dto.getPlannedEndDate())) {
            throw new IllegalArgumentException("Startdatum darf nicht nach geplantem Enddatum liegen.");
        }

        p.setTitle(dto.getTitle());
        p.setCustomerId(dto.getCustomerId());
        p.setResponsibleEmployeeId(dto.getResponsibleEmployeeId());
        p.setCustomerContactName(dto.getCustomerContactName());
        p.setComment(dto.getComment());
        p.setStartDate(dto.getStartDate());
        p.setPlannedEndDate(dto.getPlannedEndDate());
        p.setActualEndDate(dto.getActualEndDate());

        projectRepository.save(p);
        return true;
    }


    public List<EmployeeProjectsResponseDto> getProjectsForEmployee(Long employeeId, String bearerToken) {
        if (!employeeClient.employeeExists(employeeId, bearerToken)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found.");
        }

        List<ProjectAssignmentEntity> assignments =
                assignmentRepository.findAllByEmployeeIdWithProject(employeeId);

        return assignments.stream()
                .map(a -> {
                    ProjectEntity p = a.getProject();
                    EmployeeProjectsResponseDto dto = new EmployeeProjectsResponseDto();
                    dto.setId(p.getId());
                    dto.setTitle(p.getTitle());
                    dto.setCustomerId(p.getCustomerId());
                    dto.setStartDate(p.getStartDate());
                    dto.setPlannedEndDate(p.getPlannedEndDate());
                    dto.setActualEndDate(p.getActualEndDate());
                    dto.setRoleId(a.getRoleId());
                    return dto;
                })
                .toList();
    }


    @Transactional
    public void assignEmployee(Long projectId, Long employeeId, Long roleId, String bearerToken) {
        ProjectEntity project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found"));

        if (!employeeClient.employeeExists(employeeId, bearerToken)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found");
        }

        if (!employeeClient.hasQualification(employeeId, roleId, bearerToken)) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Employee lacks required qualification");
        }

        if (assignmentRepository.existsByProject_IdAndEmployeeId(projectId, employeeId)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Already assigned to this project");
        }

        boolean overlap = assignmentRepository.existsOverlappingAssignment(
                employeeId, project.getStartDate(), project.getPlannedEndDate()
        );
        if (overlap) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Employee has overlapping project");
        }

        ProjectAssignmentEntity a = ProjectAssignmentEntity.builder()
                .project(project)
                .employeeId(employeeId)
                .roleId(roleId)
                .build();

        assignmentRepository.save(a);
    }


    @Transactional
    public void removeEmployeeFromProject(Long projectId, Long employeeId, String bearerToken) {
        projectRepository.findById(projectId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found"));

        if (!employeeClient.employeeExists(employeeId, bearerToken)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found");
        }

        ProjectAssignmentEntity assignment = assignmentRepository
                .findByProject_IdAndEmployeeId(projectId, employeeId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Assignment not found"));

        assignmentRepository.delete(assignment);
    }

    // 🔹 GET /api/projects/{id}/employees
    public List<ProjectEmployeeDto> getEmployeesForProject(Long projectId) {
        ProjectEntity project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found"));

        return assignmentRepository.findAllByProject_Id(project.getId())
                .stream()
                .map(a -> new ProjectEmployeeDto(a.getEmployeeId(), a.getRoleId()))
                .toList();
    }
}
