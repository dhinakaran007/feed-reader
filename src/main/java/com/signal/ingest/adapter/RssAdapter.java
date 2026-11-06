package com.signal.ingest.adapter;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import com.signal.ingest.FetchContext;
import com.signal.ingest.FetchException;
import com.signal.ingest.RawItem;
import com.signal.ingest.SourceAdapter;
import com.signal.source.Source;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Component
public class RssAdapter implements SourceAdapter {

    private static final Logger log = LoggerFactory.getLogger(RssAdapter.class);
    private final HttpClient httpClient;

    public RssAdapter() {
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .followRedirects(HttpClient.Redirect.NORMAL)
                .build();
    }

    @Override
    public String type() {
        return "RSS";
    }

    @Override
    public List<RawItem> fetch(Source source, FetchContext ctx) throws FetchException {
        if (source.getFeedUrl() == null || source.getFeedUrl().isBlank()) {
            throw new FetchException("Feed URL is empty for source: " + source.getName());
        }

        try {
            HttpRequest.Builder reqBuilder = HttpRequest.newBuilder()
                    .uri(URI.create(source.getFeedUrl()))
                    .header("User-Agent", ctx.userAgent())
                    .timeout(Duration.ofSeconds(ctx.timeoutSeconds()))
                    .GET();

            if (source.getEtag() != null) {
                reqBuilder.header("If-None-Match", source.getEtag());
            }
            if (source.getLastModified() != null) {
                reqBuilder.header("If-Modified-Since", source.getLastModified());
            }

            HttpResponse<InputStream> response = httpClient.send(reqBuilder.build(), HttpResponse.BodyHandlers.ofInputStream());

            if (response.statusCode() == 304) {
                log.info("Source {} returned 304 Not Modified", source.getName());
                return List.of();
            }

            if (response.statusCode() >= 400) {
                throw new FetchException("HTTP error " + response.statusCode() + " fetching " + source.getFeedUrl());
            }

            try (InputStream in = response.body()) {
                SyndFeed feed = new SyndFeedInput().build(new XmlReader(in));
                List<RawItem> items = new ArrayList<>();

                for (SyndEntry entry : feed.getEntries()) {
                    Instant published = entry.getPublishedDate() != null
                            ? entry.getPublishedDate().toInstant()
                            : (entry.getUpdatedDate() != null ? entry.getUpdatedDate().toInstant() : ctx.executionTime());

                    String description = entry.getDescription() != null ? entry.getDescription().getValue() : "";

                    items.add(new RawItem(
                            entry.getUri(),
                            entry.getLink(),
                            entry.getTitle(),
                            description,
                            "NEWS",
                            published,
                            feed.getLanguage(),
                            description
                    ));
                }
                return items;
            }
        } catch (Exception e) {
            throw new FetchException("Failed to fetch RSS feed: " + source.getFeedUrl(), e);
        }
    }
}

