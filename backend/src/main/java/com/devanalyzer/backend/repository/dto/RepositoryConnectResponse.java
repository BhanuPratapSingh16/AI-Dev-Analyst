package com.devanalyzer.backend.repository.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RepositoryConnectResponse {
    private Long githubRepoId;
    private String fullName;
    private String name;
    private String cloneUrl;
    private String defaultBranch;
    private String description;
}
