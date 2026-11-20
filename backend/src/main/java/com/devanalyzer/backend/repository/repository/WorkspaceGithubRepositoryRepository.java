package com.devanalyzer.backend.repository.repository;

import com.devanalyzer.backend.repository.entity.WorkspaceGithubRepository;
import com.devanalyzer.backend.repository.entity.WorkspaceGithubRepositoryId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkspaceGithubRepositoryRepository extends JpaRepository<WorkspaceGithubRepository, WorkspaceGithubRepositoryId> {
}
