package com.signal.normalize;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UrlCanonicalizerTest {

    private final UrlCanonicalizer canonicalizer = new UrlCanonicalizer();

    @Test
    void stripsTrackingParameters() {
        String input = "https://example.com/article?utm_source=twitter&utm_medium=social&id=123";
        String output = canonicalizer.canonicalize(input);
        assertThat(output).isEqualTo("https://example.com/article?id=123");
    }

    @Test
    void normalizesTrailingSlash() {
        String input = "https://example.com/news/";
        String output = canonicalizer.canonicalize(input);
        assertThat(output).isEqualTo("https://example.com/news");
    }
}

