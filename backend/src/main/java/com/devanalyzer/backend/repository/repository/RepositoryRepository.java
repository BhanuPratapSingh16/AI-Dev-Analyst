package com.devanalyzer.backend.repository.repository;

import com.devanalyzer.backend.repository.entity.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

@org.springframework.stereotype.Repository
public interface RepositoryRepository extends JpaRepository<Repository, Long> {

}
