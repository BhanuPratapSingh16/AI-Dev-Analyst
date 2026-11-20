package com.devanalyzer.backend.repository.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class WorkspaceGithubRepositoryId implements Serializable {
    @Column(name = "workspace_id")
    private UUID workspaceId;

    @Column(name = "github_repo_id")
    private Long githubRepoId;
}
