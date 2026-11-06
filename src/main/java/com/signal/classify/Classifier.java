package com.signal.classify;

import java.util.List;

public interface Classifier {
    record ClassificationResult(String topicSlug, float confidence, String method) {}

    List<ClassificationResult> classify(String title, String snippet, Integer defaultTopicId);
}

