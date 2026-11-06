package com.signal.classify;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
public class KeywordRuleEngine implements Classifier {

    public record KeywordRule(String topicSlug, List<String> keywords, float confidence) {}

    private final List<KeywordRule> rules = new ArrayList<>();

    public KeywordRuleEngine() {
        // Default initial rules for Phase 1 skeleton
        rules.add(new KeywordRule("lawsuits.india", List.of("supreme court", "high court", "verdict", "bench", "bail"), 0.9f));
        rules.add(new KeywordRule("innovations.tech", List.of("artificial intelligence", "semiconductor", "quantum", "llm"), 0.85f));
        rules.add(new KeywordRule("innovations.medical", List.of("clinical trial", "fda", "vaccine", "crispr"), 0.85f));
        rules.add(new KeywordRule("innovations.math-science", List.of("conjecture", "proof", "physics", "astronomy"), 0.85f));
        rules.add(new KeywordRule("politics.geopolitics", List.of("sanctions", "treaty", "diplomacy", "nato", "un security council"), 0.85f));
    }

    @Override
    public List<ClassificationResult> classify(String title, String snippet, Integer defaultTopicId) {
        String combined = ((title != null ? title : "") + " " + (snippet != null ? snippet : "")).toLowerCase(Locale.ROOT);
        List<ClassificationResult> results = new ArrayList<>();

        for (KeywordRule rule : rules) {
            for (String kw : rule.keywords()) {
                if (combined.contains(kw)) {
                    results.add(new ClassificationResult(rule.topicSlug(), rule.confidence(), "KEYWORD"));
                    break;
                }
            }
        }
        return results;
    }
}

