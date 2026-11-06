package com.signal.admin;

import com.signal.source.Source;
import com.signal.source.SourceRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "Sources & Admin", description = "Source registry and health monitoring endpoints")
public class AdminSourceController {

    private final SourceRepository sourceRepository;

    public AdminSourceController(SourceRepository sourceRepository) {
        this.sourceRepository = sourceRepository;
    }

    @GetMapping("/sources")
    @Operation(summary = "List all registered news sources")
    public ResponseEntity<List<Source>> getSources() {
        return ResponseEntity.ok(sourceRepository.findAll());
    }

    @GetMapping("/admin/health/sources")
    @Operation(summary = "Source ingestion health statistics")
    public ResponseEntity<Map<String, Object>> getSourcesHealth() {
        long total = sourceRepository.count();
        return ResponseEntity.ok(Map.of(
                "totalSources", total,
                "status", "HEALTHY"
        ));
    }
}

