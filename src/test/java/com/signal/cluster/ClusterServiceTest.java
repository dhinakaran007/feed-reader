package com.signal.cluster;

import com.signal.source.PoliticalLean;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ClusterServiceTest {

    private final ClusterService clusterService = new ClusterService();

    @Test
    void flagsSingleLeanWhenAllCoverageHasSameLean() {
        var leans = List.of(PoliticalLean.LEFT, PoliticalLean.LEFT, PoliticalLean.LEFT);
        var metrics = clusterService.calculateMetrics(leans);

        assertThat(metrics.articleCount()).isEqualTo(3);
        assertThat(metrics.singleLean()).isTrue();
        assertThat(metrics.leanSpread()).containsExactly("LEFT");
    }

    @Test
    void doesNotFlagSingleLeanWhenCoverageIsDiverse() {
        var leans = List.of(PoliticalLean.LEFT, PoliticalLean.RIGHT, PoliticalLean.CENTER);
        var metrics = clusterService.calculateMetrics(leans);

        assertThat(metrics.singleLean()).isFalse();
        assertThat(metrics.leanSpread()).containsExactly("CENTER", "LEFT", "RIGHT");
    }
}

