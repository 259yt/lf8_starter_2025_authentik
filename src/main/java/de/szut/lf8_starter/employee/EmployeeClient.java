package de.szut.lf8_starter.employee;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

@Component
public class EmployeeClient {

    private final RestClient restClient;

    public EmployeeClient(RestClient.Builder builder) {
        this.restClient = builder
                .baseUrl("https://employee-api.szut.dev")
                .build();
    }

    public boolean employeeExists(Long employeeId, String bearerToken) {
        try {
            restClient.get()
                    .uri("/employees/{id}", employeeId)
                    .header(HttpHeaders.AUTHORIZATION, bearerToken == null ? "" : bearerToken)
                    .retrieve()
                    .toBodilessEntity();
            return true;
        } catch (HttpClientErrorException.NotFound e) {
            return false;
        }
    }

    public boolean hasQualification(Long employeeId, Long roleId, String bearerToken) {
        try {
            restClient.get()
                    .uri("/employees/{id}/qualifications", employeeId)
                    .header(HttpHeaders.AUTHORIZATION, bearerToken == null ? "" : bearerToken)
                    .retrieve()
                    .toEntity(String.class);

            return true;
        } catch (HttpClientErrorException.NotFound e) {
            return false;
        }
    }
}
