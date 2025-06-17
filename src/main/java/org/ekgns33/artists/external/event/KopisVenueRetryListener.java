package org.ekgns33.artists.external.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ekgns33.artists.domain.performance.PerformanceRepository;
import org.ekgns33.artists.domain.performance.Venue;
import org.ekgns33.artists.external.KopisImporterService;
import org.ekgns33.artists.external.VenueRepository;
import org.ekgns33.artists.external.client.KopisClient;
import org.ekgns33.artists.external.dto.KopisMapper;
import org.ekgns33.artists.external.dto.KopisPerformanceDetailResponse;
import org.ekgns33.artists.external.dto.KopisVenueDetailResponse;
import org.springframework.context.event.EventListener;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.client.HttpClientErrorException;

@Slf4j
@Component
@RequiredArgsConstructor
public class KopisVenueRetryListener {

    private final KopisClient client;
    private final XmlMapper xml;
    private final VenueRepository venueRepo;
    private final TransactionTemplate txTemplate;   // ★ 주입

    private static final int MAX_ATTEMPTS = 3;
    private final PerformanceRepository performanceRepository;
    private final KopisImporterService kopisImporterService;

    @Async
    @TransactionalEventListener(
        classes = KopisVenueImportFailedEvent.class,
        phase   = TransactionPhase.AFTER_COMMIT)
    @Retryable(
        value       = HttpClientErrorException.class,
        maxAttempts = MAX_ATTEMPTS,
        backoff     = @Backoff(delay = 5_000, multiplier = 2))
    public void retryVenue(KopisVenueImportFailedEvent ev) throws JsonProcessingException {

        String venueId = ev.venueId();
        log.info("[Retry-{}] venue {} 재시도", ev.retryCount() + 1, venueId);

        // 1) 외부 API 호출
        String xmlStr = client.venueDetailXml(venueId);
        var dto       = xml.readValue(xmlStr, KopisVenueDetailResponse.class);

        // 2) REQUIRES_NEW 트랜잭션으로 DB 업데이트
        txTemplate.executeWithoutResult(status -> {
            var performance = performanceRepository.findByPerformanceId(ev.performanceId())
                .orElseThrow(() -> new IllegalStateException("해당 공연이 존재하지 않습니다: " + ev.performanceId()));
            Venue managed = venueRepo.findByVenueId(venueId)
                .orElseThrow();      // stub 존재 전제
            managed.update(KopisMapper.toVenue(dto.getDetail()));
            performance.updateFromVenue(managed);
        });
    }


    @Async
    @EventListener(
        classes = KopisImportDetailFailedEvent.class
    )
    @Retryable(
        value       = HttpClientErrorException.class,
        maxAttempts = MAX_ATTEMPTS,
        backoff     = @Backoff(delay = 5_000, multiplier = 2))
    public void retryVenue(KopisImportDetailFailedEvent ev) throws JsonProcessingException {

        String pfId = ev.pfId();
        log.info("[Retry-{}] performance {} 재시도", ev.retryCount() + 1, pfId);

        // 1) 외부 API 호출
        String xmlStr = client.performanceDetailXml(pfId);
        var detailDto    = xml.readValue(xmlStr, KopisPerformanceDetailResponse.class).getDetail();

        log.info(xmlStr);
        // 2) REQUIRES_NEW 트랜잭션으로 DB 업데이트
        txTemplate.executeWithoutResult(status -> {
            var performance = performanceRepository.findByPerformanceId(ev.pfId())
                .orElseThrow(() -> new IllegalStateException("해당 공연이 존재하지 않습니다: " + ev.pfId()));
            KopisMapper.mergeDetail(detailDto, performance);
        });
    }

    /** 모든 재시도가 실패했을 때 호출 */
    @Recover
    public void recover(HttpClientErrorException ex,
        KopisVenueImportFailedEvent ev) {
        log.error("venue {} 영구 실패 – 재시도 중단", ev.venueId(), ex);
        // Slack·Sentry 알림 등
    }
}
