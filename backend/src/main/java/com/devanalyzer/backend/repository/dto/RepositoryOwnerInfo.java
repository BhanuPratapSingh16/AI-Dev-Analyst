package com.devanalyzer.backend.repository.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RepositoryOwnerInfo {
    private String owner;
    private String repositoryName;
}
