package com.signal.normalize;

import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class UrlCanonicalizer {

    private static final Set<String> TRACKING_PARAMS = Set.of(
            "utm_source", "utm_medium", "utm_campaign", "utm_term", "utm_content",
            "fbclid", "gclid", "msclkid", "ref", "source"
    );

    public String canonicalize(String inputUrl) {
        if (inputUrl == null || inputUrl.isBlank()) {
            return "";
        }

        try {
            URI uri = URI.create(inputUrl.trim());
            String scheme = uri.getScheme() != null ? uri.getScheme().toLowerCase() : "https";
            String host = uri.getHost() != null ? uri.getHost().toLowerCase() : "";
            String path = uri.getPath() != null ? uri.getPath() : "";

            // Normalize trailing slash if path is longer than 1 character
            if (path.length() > 1 && path.endsWith("/")) {
                path = path.substring(0, path.length() - 1);
            }

            String query = uri.getQuery();
            String cleanQuery = "";
            if (query != null && !query.isBlank()) {
                cleanQuery = Arrays.stream(query.split("&"))
                        .map(param -> param.split("=", 2))
                        .filter(pair -> pair.length > 0 && !TRACKING_PARAMS.contains(pair[0].toLowerCase()))
                        .map(pair -> pair.length == 2 ? pair[0] + "=" + pair[1] : pair[0])
                        .collect(Collectors.joining("&"));
            }

            StringBuilder sb = new StringBuilder();
            sb.append(scheme).append("://").append(host).append(path);
            if (!cleanQuery.isEmpty()) {
                sb.append("?").append(cleanQuery);
            }
            return sb.toString();
        } catch (Exception e) {
            return inputUrl.trim();
        }
    }
}

