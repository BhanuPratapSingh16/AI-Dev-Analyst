package com.devanalyzer.backend.repository.controller;


import com.devanalyzer.backend.repository.dto.RepositoryConnectRequest;
import com.devanalyzer.backend.repository.dto.RepositoryConnectResponse;
import com.devanalyzer.backend.repository.service.RepositoryService;
import com.devanalyzer.backend.security.CustomUserDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/repos")
public class RepositoryController {

    private final RepositoryService repositoryService;

    public RepositoryController(RepositoryService repositoryService) {
        this.repositoryService = repositoryService;
    }

    @PostMapping("/{workspaceId}/connect")
    public ResponseEntity<RepositoryConnectResponse> connectRepository(@RequestBody RepositoryConnectRequest request, @PathVariable UUID workspaceId, @AuthenticationPrincipal CustomUserDetails userDetails){
        UUID userId = userDetails.getId();
        return ResponseEntity.status(HttpStatus.CREATED).body(repositoryService.connectRepository(request, workspaceId, userId));
    }
}
