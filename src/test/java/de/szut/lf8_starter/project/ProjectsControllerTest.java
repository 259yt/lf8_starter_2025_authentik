package de.szut.lf8_starter.project;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.szut.lf8_starter.project.dto.ProjectCreateDto;
import de.szut.lf8_starter.testcontainers.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;

import de.szut.lf8_starter.project.ProjectEntity;

import java.time.LocalDate;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ProjectsControllerTest extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ProjectRepository projectRepository;


    // GET ALL Test
    @Test
    void testGetAllProjects() throws Exception {
        mockMvc.perform(get("/api/projects")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray());
    }


    // POST Test
    @Test
    void storeAndFind() throws Exception {
        final String content = """
                {
                    "title": "project_1",
                    "customerId": 42,
                    "startDate": "2021-01-01",
                    "endDate": "2025-01-01"
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


//        this.mockMvc.perform(post("/api/projects").content(content).contentType(MediaType.APPLICATION_JSON)
//                        .with(csrf()))
//                .andExpect(status().is2xxSuccessful())
//                .andExpect(status().isCreated());
//        assertThat(projectRepository.findAllByEmployeeId(42L));
    }

    // _______________________________________


    // Get by ID Test
    @Test
    void testGetProjectById() throws Exception {
        // Arrange - Add project first
        ProjectCreateDto dto = new ProjectCreateDto();
        dto.setCustomerId(42L);
        dto.setTitle("project_for_get");
        dto.setStartDate(LocalDate.of(2021, 1, 1));
        dto.setEndDate(LocalDate.of(2025, 1, 1));

        var project = projectRepository.save(
                new ProjectEntity(
                        dto.getTitle(),
                        dto.getCustomerId(),
                        dto.getStartDate(),
                        dto.getEndDate()
                )
        );

        // Act & Assert
        mockMvc.perform(get("/api/projects/" + project.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("project_for_get"))
                .andExpect(jsonPath("$.customerId").value(42));
    }


    // _______________________________________
    @Test
    void testUpdateProject() throws Exception {
        var project = projectRepository.save(
                new ProjectEntity("old_title", 42L,
                        LocalDate.of(2020, 1, 1),
                        LocalDate.of(2025, 1, 1))
        );

        String updatedContent = """
                {
                    "title": "new_title",
                    "customerId": 42,
                    "startDate": "2020-01-01",
                    "endDate": "2025-01-01"
                }
                """;

        mockMvc.perform(put("/api/projects/" + project.getId())
                        .content(updatedContent)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isNoContent()); // 404

        var updated = projectRepository.findById(project.getId()).orElseThrow();
        assertThat(updated.getTitle()).isEqualTo("new_title");
    }

    // _______________________________________

    @Test
    void testDeleteProject() throws Exception {
        var project = projectRepository.save(
                new ProjectEntity("to_delete", 42L, LocalDate.of(2020, 1, 1), LocalDate.of(2025, 1, 1))
        );

        mockMvc.perform(delete("/api/projects/" + project.getId()).with(csrf()))
                .andExpect(status().isNoContent());

        assertThat(projectRepository.existsById(project.getId())).isFalse();
    }


}