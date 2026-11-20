package com.devanalyzer.backend.repository.service;

import com.devanalyzer.backend.repository.dto.GithubRepositoryResponse;
import com.devanalyzer.backend.repository.dto.RepositoryConnectRequest;
import com.devanalyzer.backend.repository.dto.RepositoryConnectResponse;
import com.devanalyzer.backend.repository.dto.RepositoryOwnerInfo;
import com.devanalyzer.backend.repository.entity.Repository;
import com.devanalyzer.backend.repository.entity.WorkspaceGithubRepository;
import com.devanalyzer.backend.repository.entity.WorkspaceGithubRepositoryId;
import com.devanalyzer.backend.repository.repository.RepositoryRepository;
import com.devanalyzer.backend.repository.repository.WorkspaceGithubRepositoryRepository;
import com.devanalyzer.backend.workspace.entity.Workspace;
import com.devanalyzer.backend.workspace.entity.WorkspacePermission;
import com.devanalyzer.backend.workspace.repository.WorkspaceRepository;
import com.devanalyzer.backend.workspace.service.WorkspacePermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class RepositoryService {

    private final GithubApiService githubApiService;
    private final RepositoryRepository repositoryRepository;
    private final WorkspaceGithubRepositoryRepository workspaceGithubRepositoryRepository;
    private final WorkspaceRepository workspaceRepository;
    private final WorkspacePermissionService workspacePermissionService;

    @Autowired
    public RepositoryService(GithubApiService githubApiService, RepositoryRepository repositoryRepository, WorkspaceGithubRepositoryRepository workspaceGithubRepositoryRepository, WorkspaceRepository workspaceRepository, WorkspacePermissionService workspacePermissionService) {
        this.githubApiService = githubApiService;
        this.repositoryRepository = repositoryRepository;
        this.workspaceGithubRepositoryRepository = workspaceGithubRepositoryRepository;
        this.workspaceRepository = workspaceRepository;
        this.workspacePermissionService = workspacePermissionService;
    }

    @Transactional
    public RepositoryConnectResponse connectRepository(RepositoryConnectRequest request, UUID workspaceId, UUID userId){
        workspacePermissionService.requirePermission(workspaceId, userId, WorkspacePermission.CONNECT_REPOSITORY);

        // Validate url
        String url = request.getUrl();

        RepositoryOwnerInfo info = extractUserAndName(url);

        String owner = info.getOwner();
        String repositoryName = info.getRepositoryName();

        // Fetch data from github api
        GithubRepositoryResponse response = githubApiService.getRepository(owner, repositoryName);

        // Save the github data
        Repository repository = Repository.builder()
                .githubRepoId(response.getId())
                .fullName(response.getFullName())
                .name(response.getName())
                .cloneUrl(response.getCloneUrl())
                .defaultBranch(response.getDefaultBranch())
                .description(response.getDescription())
                .build();

        Repository savedRepository = repositoryRepository.save(repository);

        // Create connection between workspace and repository
        WorkspaceGithubRepositoryId id = new WorkspaceGithubRepositoryId(workspaceId, savedRepository.getGithubRepoId());
        if (workspaceGithubRepositoryRepository.existsById(id)){
            throw new RuntimeException("Connection already exists!");
        }
        Workspace workspace = workspaceRepository.findById(workspaceId).orElseThrow(() -> new RuntimeException("Workspace doesn't exist"));

        WorkspaceGithubRepository workspaceGithubRepository = WorkspaceGithubRepository.builder()
                .workspace(workspace)
                .repository(savedRepository)
                .connectedAt(LocalDateTime.now())
                .build();
        workspaceGithubRepositoryRepository.save(workspaceGithubRepository);

        return RepositoryConnectResponse.builder()
                .githubRepoId(savedRepository.getGithubRepoId())
                .fullName(savedRepository.getFullName())
                .name(savedRepository.getName())
                .cloneUrl(savedRepository.getCloneUrl())
                .defaultBranch(savedRepository.getDefaultBranch())
                .description(savedRepository.getDescription())
                .build();
    }

    private RepositoryOwnerInfo extractUserAndName(String url){
        URI uri = URI.create(url);

        String[] parts = uri.getRawPath()
                .replaceAll("^/|/$", "")
                .split("/");

        if (parts.length != 2){
            throw new RuntimeException("Invalid URL!");
        }

        String owner = parts[0];
        String repo = parts[1].replaceFirst("\\.git$", "");

        if (repo.isEmpty() || repo.equals(".") || repo.equals("..")) {
            throw new RuntimeException("Invalid repository name");
        }

        return new RepositoryOwnerInfo(owner, repo);
    }
}
