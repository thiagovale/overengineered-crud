package com.example.backend.repository;

import com.example.backend.entity.Log;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface LogRepository extends JpaRepository<Log, Long> {

    List<Log> findByTraceIdOrderByTimestampAsc(String traceId);

    List<Log> findByTypeOrderByTimestampDesc(String type);

    List<Log> findByTimestampBetweenOrderByTimestampDesc(Instant startDate, Instant endDate);

    List<Log> findByPathContainingOrderByTimestampDesc(String path);
}