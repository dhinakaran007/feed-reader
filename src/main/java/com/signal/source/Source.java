package com.signal.source;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "source")
public class Source {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(name = "homepage_url", nullable = false)
    private String homepageUrl;

    @Column(name = "feed_url")
    private String feedUrl;

    @Column(nullable = false)
    private String adapter;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SourceTier tier;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PoliticalLean lean = PoliticalLean.NOT_APPLICABLE;

    @Column(length = 2)
    private String country;

    @Column(name = "ownership_note")
    private String ownershipNote;

    @Column(name = "reliability_note")
    private String reliabilityNote;

    @Column(name = "default_topic_id")
    private Integer defaultTopicId;

    @Column(name = "fetch_interval_min", nullable = false)
    private int fetchIntervalMin = 30;

    private String etag;

    @Column(name = "last_modified")
    private String lastModified;

    @Column(name = "last_success_at")
    private Instant lastSuccessAt;

    @Column(name = "consecutive_failures", nullable = false)
    private int consecutiveFailures = 0;

    @Column(name = "next_fetch_at", nullable = false)
    private Instant nextFetchAt = Instant.now();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SourceHealth health = SourceHealth.OK;

    @Column(nullable = false)
    private boolean enabled = true;

    public Source() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getHomepageUrl() { return homepageUrl; }
    public void setHomepageUrl(String homepageUrl) { this.homepageUrl = homepageUrl; }

    public String getFeedUrl() { return feedUrl; }
    public void setFeedUrl(String feedUrl) { this.feedUrl = feedUrl; }

    public String getAdapter() { return adapter; }
    public void setAdapter(String adapter) { this.adapter = adapter; }

    public SourceTier getTier() { return tier; }
    public void setTier(SourceTier tier) { this.tier = tier; }

    public PoliticalLean getLean() { return lean; }
    public void setLean(PoliticalLean lean) { this.lean = lean; }

    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }

    public String getOwnershipNote() { return ownershipNote; }
    public void setOwnershipNote(String ownershipNote) { this.ownershipNote = ownershipNote; }

    public String getReliabilityNote() { return reliabilityNote; }
    public void setReliabilityNote(String reliabilityNote) { this.reliabilityNote = reliabilityNote; }

    public Integer getDefaultTopicId() { return defaultTopicId; }
    public void setDefaultTopicId(Integer defaultTopicId) { this.defaultTopicId = defaultTopicId; }

    public int getFetchIntervalMin() { return fetchIntervalMin; }
    public void setFetchIntervalMin(int fetchIntervalMin) { this.fetchIntervalMin = fetchIntervalMin; }

    public String getEtag() { return etag; }
    public void setEtag(String etag) { this.etag = etag; }

    public String getLastModified() { return lastModified; }
    public void setLastModified(String lastModified) { this.lastModified = lastModified; }

    public Instant getLastSuccessAt() { return lastSuccessAt; }
    public void setLastSuccessAt(Instant lastSuccessAt) { this.lastSuccessAt = lastSuccessAt; }

    public int getConsecutiveFailures() { return consecutiveFailures; }
    public void setConsecutiveFailures(int consecutiveFailures) { this.consecutiveFailures = consecutiveFailures; }

    public Instant getNextFetchAt() { return nextFetchAt; }
    public void setNextFetchAt(Instant nextFetchAt) { this.nextFetchAt = nextFetchAt; }

    public SourceHealth getHealth() { return health; }
    public void setHealth(SourceHealth health) { this.health = health; }

    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }
}

