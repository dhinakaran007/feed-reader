package com.signal.ingest;

import com.signal.source.Source;
import com.signal.source.SourceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class IngestionScheduler {

    private static final Logger log = LoggerFactory.getLogger(IngestionScheduler.class);

    private final SourceRepository sourceRepository;
    private final Map<String, SourceAdapter> adapters = new ConcurrentHashMap<>();

    @Value("${signal.ingestion.user-agent:SignalNewsAggregator/1.0}")
    private String userAgent;

    @Value("${signal.ingestion.read-timeout-seconds:15}")
    private int timeoutSeconds;

    public IngestionScheduler(SourceRepository sourceRepository, List<SourceAdapter> adapterList) {
        this.sourceRepository = sourceRepository;
        for (SourceAdapter adapter : adapterList) {
            this.adapters.put(adapter.type().toUpperCase(), adapter);
        }
    }

    @Scheduled(fixedDelayString = "${signal.ingestion.poll-interval-ms:60000}")
    public void scheduleFetchDueSources() {
        Instant now = Instant.now();
        List<Source> dueSources = sourceRepository.findDueSources(now);

        if (dueSources.isEmpty()) {
            return;
        }

        log.info("Found {} sources due for ingestion", dueSources.size());

        FetchContext ctx = new FetchContext(now, userAgent, timeoutSeconds);

        // Run per-source fetch in virtual threads
        for (Source source : dueSources) {
            Thread.ofVirtual().name("fetch-" + source.getId()).start(() -> {
                SourceAdapter adapter = adapters.get(source.getAdapter().toUpperCase());
                if (adapter == null) {
                    log.warn("No adapter configured for type: {}", source.getAdapter());
                    return;
                }

                try {
                    List<RawItem> items = adapter.fetch(source, ctx);
                    log.info("Ingested {} items from {}", items.size(), source.getName());
                    source.setLastSuccessAt(now);
                    source.setConsecutiveFailures(0);
                    source.setNextFetchAt(now.plusSeconds((long) source.getFetchIntervalMin() * 60));
                    sourceRepository.save(source);
                } catch (FetchException e) {
                    log.error("Failed fetching source {}: {}", source.getName(), e.getMessage());
                    source.setConsecutiveFailures(source.getConsecutiveFailures() + 1);
                    source.setNextFetchAt(now.plusSeconds(300)); // 5 min retry backoff
                    sourceRepository.save(source);
                }
            });
        }
    }
}

