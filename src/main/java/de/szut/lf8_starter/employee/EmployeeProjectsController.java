package de.szut.lf8_starter.employee;

import de.szut.lf8_starter.project.ProjectService;
import de.szut.lf8_starter.project.dto.EmployeeProjectsResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/employees")
public class EmployeeProjectsController {

    private final ProjectService projectsService;

    @GetMapping("/{employeeId}/projects")
    public List<EmployeeProjectsResponseDto> getEmployeeProjects(
            @PathVariable("employeeId") Long employeeId,
            @RequestHeader(name = HttpHeaders.AUTHORIZATION, required = false) String authorization
    ) {
        String bearer = authorization != null ? authorization : "";
        return projectsService.getProjectsForEmployee(employeeId, bearer);
    }
}
