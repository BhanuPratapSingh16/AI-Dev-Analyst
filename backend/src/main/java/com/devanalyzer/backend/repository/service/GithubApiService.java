package com.devanalyzer.backend.repository.service;

import com.devanalyzer.backend.repository.dto.GithubRepositoryResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class GithubApiService {
    private final RestClient githubRestClient;

    public GithubApiService(RestClient githubRestClient) {
        this.githubRestClient = githubRestClient;
    }

    public GithubRepositoryResponse getRepository(String owner, String repo) {
        return githubRestClient.get()
                .uri("/repos/{owner}/{repo}", owner, repo)
                .retrieve()
                .body(GithubRepositoryResponse.class);
    }

}
