package de.szut.lf8_starter.project;

import de.szut.lf8_starter.employee.EmployeeClient;
import de.szut.lf8_starter.project.entity.ProjectEntity;
import de.szut.lf8_starter.project.repository.ProjectRepository;
import de.szut.lf8_starter.testcontainers.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ProjectsControllerTest extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProjectRepository projectRepository;

    @MockBean
    private EmployeeClient employeeClient;

    // ------------------ GET /api/projects ------------------
    @Test
    void testGetAllProjects() throws Exception {
        mockMvc.perform(get("/api/projects")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray());
    }

    // ------------------ POST /api/projects ------------------
    @Test
    void storeAndFind() throws Exception {
        final String content = """
                {
                  "title": "project_1",
                  "customerId": 42,
                  "responsibleEmployeeId": 1337,
                  "customerContactName": "Max Mustermann",
                  "comment": "Testprojekt",
                  "startDate": "2021-01-01",
                  "plannedEndDate": "2025-01-01",
                  "actualEndDate": null
                }
                """;

        this.mockMvc.perform(post("/api/projects")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isCreated());

        var projects = projectRepository.findAll();
        assertThat(projects).isNotEmpty();
        assertThat(projects.get(0).getTitle()).isEqualTo("project_1");
        assertThat(projects.get(0).getCustomerId()).isEqualTo(42L);
        assertThat(projects.get(0).getResponsibleEmployeeId()).isEqualTo(1337L);
    }

    // ------------------ GET /api/projects/{id} ------------------
    @Test
    void testGetProjectById() throws Exception {

        ProjectEntity project = new ProjectEntity();
        project.setTitle("project_for_get");
        project.setCustomerId(42L);
        project.setResponsibleEmployeeId(1337L);
        project.setCustomerContactName("Max Mustermann");
        project.setComment("Some comment");
        project.setStartDate(LocalDate.of(2021, 1, 1));
        project.setPlannedEndDate(LocalDate.of(2025, 1, 1));
        project.setActualEndDate(null);

        project = projectRepository.save(project);

        // Act & Assert
        mockMvc.perform(get("/api/projects/" + project.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("project_for_get"))
                .andExpect(jsonPath("$.customerId").value(42))
                .andExpect(jsonPath("$.responsibleEmployeeId").value(1337));
    }

    // ------------------ PUT /api/projects/{id} ------------------
    @Test
    void testUpdateProject() throws Exception {

        ProjectEntity project = new ProjectEntity();
        project.setTitle("old_title");
        project.setCustomerId(42L);
        project.setResponsibleEmployeeId(1337L);
        project.setCustomerContactName("Old Contact");
        project.setComment("Old comment");
        project.setStartDate(LocalDate.of(2020, 1, 1));
        project.setPlannedEndDate(LocalDate.of(2025, 1, 1));
        project.setActualEndDate(null);
        project = projectRepository.save(project);

        String updatedContent = """
                {
                  "title": "new_title",
                  "customerId": 42,
                  "responsibleEmployeeId": 1337,
                  "customerContactName": "New Contact",
                  "comment": "Updated comment",
                  "startDate": "2020-01-01",
                  "plannedEndDate": "2025-01-01",
                  "actualEndDate": null
                }
                """;

        mockMvc.perform(put("/api/projects/" + project.getId())
                        .content(updatedContent)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isNoContent());

        var updated = projectRepository.findById(project.getId()).orElseThrow();
        assertThat(updated.getTitle()).isEqualTo("new_title");
        assertThat(updated.getCustomerContactName()).isEqualTo("New Contact");
        assertThat(updated.getComment()).isEqualTo("Updated comment");
    }

    // ------------------ DELETE /api/projects/{id} ------------------
    @Test
    void testDeleteProject() throws Exception {
        ProjectEntity project = new ProjectEntity();
        project.setTitle("to_delete");
        project.setCustomerId(42L);
        project.setResponsibleEmployeeId(1337L);
        project.setCustomerContactName("Contact");
        project.setComment("To be deleted");
        project.setStartDate(LocalDate.of(2020, 1, 1));
        project.setPlannedEndDate(LocalDate.of(2025, 1, 1));
        project.setActualEndDate(null);
        project = projectRepository.save(project);

        mockMvc.perform(delete("/api/projects/" + project.getId())
                        .with(csrf()))
                .andExpect(status().isNoContent());

        assertThat(projectRepository.existsById(project.getId())).isFalse();
    }

    // ------------------ GET /api/projects/{id}/employees ------------------
    @Test
    void testGetProjectEmployees() throws Exception {

        ProjectEntity project = new ProjectEntity();
        project.setTitle("project_with_employees");
        project.setCustomerId(42L);
        project.setResponsibleEmployeeId(1337L);
        project.setCustomerContactName("Contact");
        project.setComment("Has employees (maybe)");
        project.setStartDate(LocalDate.of(2021, 1, 1));
        project.setPlannedEndDate(LocalDate.of(2025, 1, 1));
        project.setActualEndDate(null);
        project = projectRepository.save(project);

        mockMvc.perform(get("/api/projects/" + project.getId() + "/employees")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray());
    }

    // ------------------ POST /api/projects/{projectId}/employees ------------------

    @Test
    void testAddEmployeeToProject() throws Exception {

        ProjectEntity project = new ProjectEntity();
        project.setTitle("project_assign");
        project.setCustomerId(42L);
        project.setResponsibleEmployeeId(1337L);
        project.setCustomerContactName("Contact");
        project.setComment("Assignment test");
        project.setStartDate(LocalDate.of(2021, 1, 1));
        project.setPlannedEndDate(LocalDate.of(2025, 1, 1));
        project.setActualEndDate(null);
        project = projectRepository.save(project);



        String body = """
                {
                  "maId": 1001,
                  "roleId": 7
                }
                """;

        mockMvc.perform(post("/api/projects/" + project.getId() + "/employees")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isNotFound());
    }
    // ------------------ DELETE /api/projects/{projectId}/employees/{employeeId} ------------------
    @Test
    void testRemoveEmployeeFromProject_notExistingAssignment_returns404() throws Exception {

        ProjectEntity project = new ProjectEntity();
        project.setTitle("project_for_remove_employee");
        project.setCustomerId(42L);
        project.setResponsibleEmployeeId(1337L);
        project.setCustomerContactName("Contact");
        project.setComment("Remove employee test");
        project.setStartDate(LocalDate.of(2021, 1, 1));
        project.setPlannedEndDate(LocalDate.of(2025, 1, 1));
        project.setActualEndDate(null);
        project = projectRepository.save(project);

        Long projectId = project.getId();
        Long employeeId = 1001L;


        mockMvc.perform(delete("/api/projects/" + projectId + "/employees/" + employeeId)
                        .with(csrf()))
                .andExpect(status().isNotFound());
    }
}
