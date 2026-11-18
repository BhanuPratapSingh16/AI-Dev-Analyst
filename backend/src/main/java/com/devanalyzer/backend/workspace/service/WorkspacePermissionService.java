package com.devanalyzer.backend.workspace.service;

import com.devanalyzer.backend.exception.PermissionDeniedException;
import com.devanalyzer.backend.workspace.entity.Workspace;
import com.devanalyzer.backend.workspace.entity.WorkspaceMember;
import com.devanalyzer.backend.workspace.entity.WorkspacePermission;
import com.devanalyzer.backend.workspace.repository.WorkspaceMemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class WorkspacePermissionService {

    private final WorkspaceMemberRepository workspaceMemberRepository;

    @Autowired
    public WorkspacePermissionService(WorkspaceMemberRepository workspaceMemberRepository){
        this.workspaceMemberRepository = workspaceMemberRepository;
    }

    private boolean hasPermission(UUID workspaceId, UUID userId, WorkspacePermission permission){
        Optional<WorkspaceMember> workspaceMember = workspaceMemberRepository.findByWorkspaceIdAndUserId(workspaceId, userId);

        if (workspaceMember.isEmpty()){
            return false;
        }

        WorkspaceMember member = workspaceMember.get();
        return WorkspaceRolePermission.getPermissions(member.getRole()).contains(permission);
    }

    public void requirePermission(UUID workspaceId, UUID userId, WorkspacePermission permission){
        if (!hasPermission(workspaceId, userId, permission)){
            throw new PermissionDeniedException("You do not have following permission "+permission.toString());
        }
    }
}
