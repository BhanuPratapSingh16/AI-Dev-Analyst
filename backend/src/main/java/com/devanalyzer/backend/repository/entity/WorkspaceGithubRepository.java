package com.devanalyzer.backend.repository.entity;

import com.devanalyzer.backend.workspace.entity.Workspace;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "workspace_repositories")
public class WorkspaceGithubRepository {
    @EmbeddedId
    private WorkspaceGithubRepositoryId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("workspaceId")
    @JoinColumn(name = "workspace_id", nullable = false)
    private Workspace workspace;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("githubRepoId")
    @JoinColumn(name = "github_repo_id", nullable = false)
    private Repository repository;

    @Column(name = "connected_at", nullable = false)
    private LocalDateTime connectedAt;
}
