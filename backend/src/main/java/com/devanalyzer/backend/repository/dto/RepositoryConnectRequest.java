package com.devanalyzer.backend.repository.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RepositoryConnectRequest {

    @NotBlank(message = "GitHub URL is required")
    @URL(protocol = "https", host = "github.com", message = "URL must be a valid HTTPS GitHub URL")
    @Pattern(regexp = "^https://github\\.com/[A-Za-z0-9-]+/[A-Za-z0-9._-]+(?:\\.git)?/?$", message = "Expected format: https://github.com/{owner}/{repository}")
    private String url;
}