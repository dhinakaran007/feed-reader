package com.signal.dedupe;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DeduplicatorTest {

    private final Deduplicator deduplicator = new Deduplicator();

    @Test
    void producesDeterministicSha256Hash() {
        String url = "https://example.com/story";
        String hash1 = deduplicator.computeUrlHash(url);
        String hash2 = deduplicator.computeUrlHash(url);

        assertThat(hash1).isNotEmpty().hasSize(64).isEqualTo(hash2);
    }

    @Test
    void producesSimHashForText() {
        String text = "Supreme Court delivers landmark verdict on technology regulation";
        long simHash = deduplicator.computeSimHash(text);

        assertThat(simHash).isNotZero();
    }
}

