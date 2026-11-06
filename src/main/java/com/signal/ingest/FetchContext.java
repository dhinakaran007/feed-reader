package com.signal.ingest;

import java.time.Instant;

public record FetchContext(
    Instant executionTime,
    String userAgent,
    int timeoutSeconds
) {}

