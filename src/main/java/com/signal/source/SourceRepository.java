package com.signal.source;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface SourceRepository extends JpaRepository<Source, Long> {

    @Query("SELECT s FROM Source s WHERE s.enabled = true AND s.nextFetchAt <= :now")
    List<Source> findDueSources(@Param("now") Instant now);

    List<Source> findByHealth(SourceHealth health);
}

