package com.tomorrow.queueSystem.repository;

import com.tomorrow.queueSystem.persistence.JobRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobRequestRepository extends JpaRepository<JobRequest,Long> {
}
