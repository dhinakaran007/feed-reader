package com.signal.feed;

import com.signal.source.PoliticalLean;
import com.signal.source.SourceTier;

import java.time.Instant;

public record FeedItemResponse(
        Long id,
        String title,
        String snippet,
        String url,
        String itemType,
        Instant publishedAt,
        SourceInfo source
) {
    public record SourceInfo(
            String name,
            SourceTier tier,
            PoliticalLean lean,
            String country,
            String ownershipNote
    ) {}
}

