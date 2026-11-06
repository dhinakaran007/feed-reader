package com.signal.cluster;

import com.signal.source.PoliticalLean;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ClusterService {

    public record ClusterMetrics(int articleCount, List<String> leanSpread, boolean singleLean) {}

    public ClusterMetrics calculateMetrics(List<PoliticalLean> leans) {
        if (leans == null || leans.isEmpty()) {
            return new ClusterMetrics(0, List.of(), false);
        }

        Set<String> distinctApplicableLeans = leans.stream()
                .filter(l -> l != PoliticalLean.NOT_APPLICABLE)
                .map(Enum::name)
                .collect(Collectors.toSet());

        boolean singleLean = distinctApplicableLeans.size() == 1 && leans.size() > 1;

        return new ClusterMetrics(
                leans.size(),
                distinctApplicableLeans.stream().sorted().toList(),
                singleLean
        );
    }
}

