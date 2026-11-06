package com.signal.ingest;

import java.time.Instant;

public record RawItem(
    String externalId,
    String url,
    String title,
    String snippet,
    String itemType,
    Instant publishedAt,
    String language,
    String rawContent
) {}

