package com.signal.dedupe;

import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

@Component
public class Deduplicator {

    public String computeUrlHash(String canonicalUrl) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(canonicalUrl.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hashBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 algorithm not available", e);
        }
    }

    public long computeSimHash(String text) {
        if (text == null || text.isBlank()) {
            return 0L;
        }

        int[] v = new int[64];
        String[] tokens = text.split("\\s+");

        for (String token : tokens) {
            long hash = token.hashCode();
            for (int i = 0; i < 64; i++) {
                long bit = (hash >> i) & 1;
                v[i] += (bit == 1) ? 1 : -1;
            }
        }

        long fingerprint = 0L;
        for (int i = 0; i < 64; i++) {
            if (v[i] > 0) {
                fingerprint |= (1L << i);
            }
        }
        return fingerprint;
    }
}

