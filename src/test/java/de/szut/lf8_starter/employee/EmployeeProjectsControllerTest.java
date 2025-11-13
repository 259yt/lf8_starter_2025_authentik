package de.szut.lf8_starter.employee;

import de.szut.lf8_starter.project.ProjectService;
import de.szut.lf8_starter.project.dto.EmployeeProjectsResponseDto;
import de.szut.lf8_starter.testcontainers.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class EmployeeProjectsControllerTest extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProjectService projectService;

    // --------------- GET /api/employees/{employeeId}/projects ---------------
    @Test
    void testGetEmployeeProjects_returnsListOfProjects() throws Exception {

        EmployeeProjectsResponseDto dto = new EmployeeProjectsResponseDto();

        given(projectService.getProjectsForEmployee(anyLong(), anyString()))
                .willReturn(List.of(dto));

        Long employeeId = 1001L;

        // Act & Assert
        mockMvc.perform(get("/api/employees/" + employeeId + "/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer test-token"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1));
    }
}