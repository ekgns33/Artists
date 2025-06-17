package org.ekgns33.artists.external;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ekgns33.artists.common.DomainEventPublisher;
import org.ekgns33.artists.domain.performance.Performance;
import org.ekgns33.artists.domain.performance.PerformanceRepository;
import org.ekgns33.artists.external.client.KopisClient;
import org.ekgns33.artists.external.dto.KopisPerformanceDetailResponse;
import org.ekgns33.artists.external.dto.KopisPerformanceRequest;
import org.ekgns33.artists.external.dto.KopisResponse;
import org.ekgns33.artists.external.event.KopisImportDetailFailedEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;


@Slf4j
@RequiredArgsConstructor
@Component
public class KopisImportUsecase {

    private final KopisClient client;
    private final XmlMapper xml;
    private final KopisImporterService kopisImporterService;
    private final DomainEventPublisher eventPublisher;
    private final PerformanceRepository performanceRepository;

    @Value("${openapi.kopis.api.key}")
    private String apiKey;
    @Value("${openapi.kopis.api.base-url}")
    private String baseUrl;

    private final RestClient rest;


    public void importPerformances(KopisPerformanceRequest req) {

        var kopisResponse = fetchPerformanceList(req);
        var savedPerformanceList = kopisImporterService.savePerformances(kopisResponse);
        savedPerformanceList.forEach(this::importPerformanceDetailWithRetry);
    }


    private void importPerformanceDetailWithRetry(Performance perf) {
        try {
            var performanceDetail = fetchPerformanceDetail(perf.getPerformanceId());
            kopisImporterService.enrichDetail(performanceDetail, perf.getId());
        } catch (Exception e) {
            log.warn("공연 상세 조회 실패 - 재시도 이벤트 발행: {}", perf.getPerformanceId(), e);
            eventPublisher.publish(
                new KopisImportDetailFailedEvent(perf.getPerformanceId(), 2));
        }
    }

    private KopisResponse fetchPerformanceList(KopisPerformanceRequest req) {
        try {
            String xmlStr = client.performanceListXml(KopisUriBuilder.build(req));
            return xml.readValue(xmlStr, KopisResponse.class);
        } catch (Exception e) {
            throw new IllegalStateException("공연 목록 조회 실패", e);
        }
    }

    private KopisPerformanceDetailResponse.Detail fetchPerformanceDetail(String performanceId) {
        try {
            String xmlStr = client.performanceDetailXml(performanceId);
            return xml.readValue(xmlStr, KopisPerformanceDetailResponse.class).getDetail();
        } catch (Exception e) {
            throw new IllegalStateException("공연 상세 조회 실패: " + performanceId, e);
        }
    }

}
