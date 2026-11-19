package com.devanalyzer.backend.workspace.repository;

import com.devanalyzer.backend.workspace.entity.WorkspaceMember;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface WorkspaceMemberRepository extends JpaRepository<WorkspaceMember, UUID> {

    Optional<WorkspaceMember> findByWorkspaceIdAndUserId(UUID workspaceId, UUID userId);

    @EntityGraph(attributePaths = {"user"})
    List<WorkspaceMember> findAllByWorkspaceId(UUID workspaceId);

    void deleteWorkspaceMemberByWorkspaceIdAndUserId(UUID workspaceId, UUID userId);
}
