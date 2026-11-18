package com.devanalyzer.backend.workspace.service;

import com.devanalyzer.backend.user.entity.User;
import com.devanalyzer.backend.user.repository.UserRepository;
import com.devanalyzer.backend.workspace.dto.WorkspaceCreateRequest;
import com.devanalyzer.backend.workspace.dto.WorkspaceMemberResponse;
import com.devanalyzer.backend.workspace.dto.WorkspaceResponse;
import com.devanalyzer.backend.workspace.entity.Workspace;
import com.devanalyzer.backend.workspace.entity.WorkspaceMember;
import com.devanalyzer.backend.workspace.entity.WorkspacePermission;
import com.devanalyzer.backend.workspace.entity.WorkspaceRole;
import com.devanalyzer.backend.workspace.repository.WorkspaceMemberRepository;
import com.devanalyzer.backend.workspace.repository.WorkspaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class WorkspaceService {
    private final WorkspaceRepository workspaceRepository;
    private final UserRepository userRepository;
    private final WorkspacePermissionService workspacePermissionService;
    private final WorkspaceMemberRepository workspaceMemberRepository;

    @Autowired
    public WorkspaceService(WorkspaceRepository workspaceRepository, UserRepository userRepository, WorkspacePermissionService workspacePermissionService, WorkspaceMemberRepository workspaceMemberRepository){
        this.workspaceRepository = workspaceRepository;
        this.userRepository = userRepository;
        this.workspacePermissionService = workspacePermissionService;
        this.workspaceMemberRepository = workspaceMemberRepository;
    }

    public WorkspaceResponse createWorkspace(WorkspaceCreateRequest request, UUID userId){
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found!"));

        Workspace workspace = Workspace.builder()
                .name(request.getName())
                .owner(user)
                .createdAt(LocalDateTime.now())
                .build();


        Workspace savedWorkspace = workspaceRepository.save(workspace);
        workspaceMemberRepository.save(
                WorkspaceMember.builder()
                        .role(WorkspaceRole.OWNER)
                        .joinedAt(workspace.getCreatedAt())
                        .workspace(workspace)
                        .user(user)
                        .build()
        );

        return WorkspaceResponse.builder()
                .id(savedWorkspace.getId())
                .name(savedWorkspace.getName())
                .owner_id(savedWorkspace.getOwner().getId())
                .createdAt(savedWorkspace.getCreatedAt())
                .build();
    }

    public List<WorkspaceResponse> getAllOfUser(UUID userId) {
        List<Workspace> workspaces = workspaceRepository.findAllByMembersUserId(userId);

        return workspaces.stream()
                .map(workspace -> new WorkspaceResponse(
                        workspace.getId(),
                        workspace.getName(),
                        workspace.getOwner().getId(),
                        workspace.getCreatedAt()
                )).toList();
    }

    public WorkspaceResponse getWorkspaceOfUser(UUID workspaceId, UUID userId) {
        Workspace workspace = workspaceRepository.findByIdAndMembersUserId(workspaceId, userId).orElseThrow(() -> new RuntimeException("Unauthenticated user or workspace!"));

        return WorkspaceResponse.builder()
                .id(workspace.getId())
                .name(workspace.getName())
                .owner_id(workspace.getOwner().getId())
                .createdAt(workspace.getCreatedAt())
                .build();
    }

    public void deleteWorkspaceOfUser(UUID workspaceId, UUID userId) {
        workspacePermissionService.requirePermission(workspaceId, userId, WorkspacePermission.DELETE_WORKSPACE);
        workspaceRepository.deleteByIdAndMembersUserId(workspaceId, userId);
    }

    public List<WorkspaceMemberResponse> getAllMembersOfWorkspace(UUID workspaceId, UUID userId) {
        workspacePermissionService.requirePermission(workspaceId, userId, WorkspacePermission.VIEW_MEMBERS);

        List<WorkspaceMember> workspaceMembers = workspaceMemberRepository.findAllByWorkspaceId(workspaceId);

        return workspaceMembers.stream()
                .map(member -> new WorkspaceMemberResponse(
                        member.getUser().getId(),
                        member.getUser().getName(),
                        member.getUser().getEmail(),
                        member.getRole(),
                        member.getJoinedAt()
                ))
                .toList();
    }

    public WorkspaceMemberResponse getMemberOfWorkspaceById(UUID workspaceId, UUID userId, UUID loggedUserId) {
        workspacePermissionService.requirePermission(workspaceId, loggedUserId, WorkspacePermission.VIEW_MEMBERS);

        WorkspaceMember workspaceMember = workspaceMemberRepository.findByWorkspaceIdAndUserId(workspaceId, userId).orElseThrow(() -> new RuntimeException("Unauthenticated operation!"));
        return WorkspaceMemberResponse.builder()
                .userId(workspaceMember.getUser().getId())
                .userName(workspaceMember.getUser().getName())
                .userEmail(workspaceMember.getUser().getEmail())
                .role(workspaceMember.getRole())
                .joinedAt(workspaceMember.getJoinedAt())
                .build();
    }
}
