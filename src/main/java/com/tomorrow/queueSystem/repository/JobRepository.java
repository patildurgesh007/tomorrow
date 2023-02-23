package com.tomorrow.queueSystem.repository;

import com.tomorrow.queueSystem.persistence.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobRepository extends JpaRepository<Job,Long> {
}
