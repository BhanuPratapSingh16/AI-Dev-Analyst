package com.devanalyzer.backend.workspace.controller;

import com.devanalyzer.backend.security.CustomUserDetails;
import com.devanalyzer.backend.workspace.dto.*;
import com.devanalyzer.backend.workspace.entity.WorkspaceMember;
import com.devanalyzer.backend.workspace.entity.WorkspaceRole;
import com.devanalyzer.backend.workspace.service.WorkspaceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/workspaces")
public class WorkspaceController {


    private final WorkspaceService workspaceService;

    public WorkspaceController(WorkspaceService workspaceService) {
        this.workspaceService = workspaceService;
    }

    @PostMapping("/create")
    public ResponseEntity<WorkspaceResponse> createWorkspace(@RequestBody WorkspaceCreateRequest request, @AuthenticationPrincipal CustomUserDetails userDetails){
        UUID userId = userDetails.getId();
        return ResponseEntity.status(HttpStatus.CREATED).body(workspaceService.createWorkspace(request, userId));
    }

    @GetMapping("/")
    public ResponseEntity<List<WorkspaceResponse>> getAllOfUser(@AuthenticationPrincipal CustomUserDetails userDetails){
        UUID userId = userDetails.getId();
        return ResponseEntity.ok(workspaceService.getAllOfUser(userId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<WorkspaceResponse> getWorkspaceOfUser(@PathVariable UUID id, @AuthenticationPrincipal CustomUserDetails userDetails){
        UUID userId = userDetails.getId();
        return ResponseEntity.ok(workspaceService.getWorkspaceOfUser(id, userId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteWorkspaceOfUser(@PathVariable UUID id, @AuthenticationPrincipal CustomUserDetails userDetails){
        UUID userId = userDetails.getId();
        workspaceService.deleteWorkspaceOfUser(id, userId);
        return ResponseEntity.ok(Map.of("message", "Deleted workspace successfully"));
    }

    @GetMapping("/{id}/members")
    public ResponseEntity<List<WorkspaceMemberResponse>> getAllMembersOfWorkspace(@PathVariable UUID id, @AuthenticationPrincipal CustomUserDetails userDetails){
        UUID userId = userDetails.getId();
        return ResponseEntity.ok(workspaceService.getAllMembersOfWorkspace(id, userId));
    }

    @GetMapping("/{id}/members/{userId}")
    public ResponseEntity<WorkspaceMemberResponse> getMemberOfWorkspaceById(@PathVariable UUID id, @PathVariable UUID userId, @AuthenticationPrincipal CustomUserDetails userDetails){
        UUID loggedUserId = userDetails.getId();
        return ResponseEntity.ok(workspaceService.getMemberOfWorkspaceById(id, userId, loggedUserId));
    }

    @PatchMapping("/{id}/members/changerole")
    public ResponseEntity<WorkspaceMemberResponse> changeMemberRole(@PathVariable UUID id, @RequestBody ChangeMemberRoleRequest request, @AuthenticationPrincipal CustomUserDetails userDetails){
        UUID loggedUserId = userDetails.getId();
        return ResponseEntity.ok(workspaceService.changeMemberRole(id, request, loggedUserId));
    }

    @PostMapping("/{id}/members/add/")
    public ResponseEntity<WorkspaceMemberResponse> addMemberToWorkspace(@PathVariable UUID id, @RequestBody AddMemberRequest request, @AuthenticationPrincipal CustomUserDetails userDetails){
        UUID loggedUserId = userDetails.getId();
        return ResponseEntity.ok(workspaceService.addMemberToWorkspace(id, loggedUserId, request));
    }

    @DeleteMapping("/{id}/members/{userId}")
    public ResponseEntity<?> removeMemberFromWorkspace(@PathVariable UUID id, @PathVariable UUID userId, @AuthenticationPrincipal CustomUserDetails userDetails){
        UUID loggedUserId = userDetails.getId();
        workspaceService.removeMemberFromWorkspace(id, userId, loggedUserId);
        return ResponseEntity.ok(Map.of("message", "Successfully removed the user!"));
    }
}
