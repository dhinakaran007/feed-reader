package com.signal.normalize;

import org.jsoup.parser.Parser;
import org.springframework.stereotype.Component;

@Component
public class TitleNormalizer {

    public String normalize(String title) {
        if (title == null || title.isBlank()) {
            return "";
        }

        // Unescape HTML entities
        String unescaped = Parser.unescapeEntities(title, false);

        // Strip surrounding whitespace and common trailing site names like "... - The Hindu" or "... | LiveLaw"
        String cleaned = unescaped.replaceAll("\\s*[-|–—]\\s*[^-|–—]+$", "").trim();

        // Normalized for similarity search (lowercase, alphanumeric + spaces only)
        return cleaned.toLowerCase().replaceAll("[^a-z0-9\\s]", " ").replaceAll("\\s+", " ").trim();
    }
}

