package com.signal.feed;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "Feed", description = "Chronological and topic-filtered news feed endpoints")
public class FeedController {

    @GetMapping("/feed")
    @Operation(summary = "Get chronological feed", description = "Returns chronological items filtered by topic and type")
    public ResponseEntity<List<FeedItemResponse>> getFeed(
            @RequestParam(required = false) String topic,
            @RequestParam(required = false, defaultValue = "NEWS") String type,
            @RequestParam(required = false) String cursor
    ) {
        // Initial stub for Phase 1 skeleton
        return ResponseEntity.ok(List.of());
    }

    @GetMapping("/topics")
    @Operation(summary = "Get topic hierarchy", description = "Returns the taxonomy tree: politics, lawsuits, innovations")
    public ResponseEntity<List<Map<String, Object>>> getTopics() {
        return ResponseEntity.ok(List.of(
                Map.of("slug", "politics", "name", "Politics", "children", List.of("politics.india", "politics.geopolitics")),
                Map.of("slug", "lawsuits", "name", "Lawsuits", "children", List.of("lawsuits.india", "lawsuits.international", "lawsuits.corporate-tech")),
                Map.of("slug", "innovations", "name", "Innovations", "children", List.of("innovations.tech", "innovations.medical", "innovations.math-science"))
        ));
    }
}

