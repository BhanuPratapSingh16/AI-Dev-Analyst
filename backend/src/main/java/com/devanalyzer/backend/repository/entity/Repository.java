package com.devanalyzer.backend.repository.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "github_repositories")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Repository {

    @Id
    @Column(name = "github_repo_id", nullable = false)
    private Long githubRepoId;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "clone_url", nullable = false)
    private String cloneUrl;

    @Column(name = "default_branch")
    private String defaultBranch;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
}
