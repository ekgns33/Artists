package org.ekgns33.artists.external;

import static org.ekgns33.artists.external.dto.KopisMapper.toDetailEntity;
import static org.ekgns33.artists.external.dto.KopisMapper.toJsonSafe;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ekgns33.artists.common.DomainEventPublisher;
import org.ekgns33.artists.domain.performance.*;
import org.ekgns33.artists.external.client.KopisClient;
import org.ekgns33.artists.external.dto.*;
import org.ekgns33.artists.external.event.KopisVenueImportFailedEvent;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class
KopisImporterService {

    private final KopisClient client;
    private final XmlMapper xml;
    private final PerformanceRepository perfRepo;
    private final VenueRepository venueRepo;
    private final DomainEventPublisher eventPublisher;




    @Transactional
    public List<Performance> savePerformances(KopisResponse listDto) {
        return listDto.getPerformances()
            .stream().map(
                dto -> perfRepo.findByPerformanceId(dto.getPerformanceId())
                    .orElseGet(() -> perfRepo.save(KopisMapper.toPerformance(dto)))
            )
            .toList();
    }

    /* 2. 단건 PFID 상세 + 공연장 보강 */
    @Transactional
    public void enrichDetail(KopisPerformanceDetailResponse.Detail detailDto, Long perfId) {
        log.info("enrichDetail {}", detailDto.getPerformanceId());
        Performance perf = perfRepo.findById(perfId).orElseThrow();
        perf.updateFromDetail(
            detailDto.getDateTimeGuidance(),
            detailDto.getChildShow()
        );

        /* === PerformanceDetail 생성/갱신 === */
        PerformanceDetail d = toDetailEntity(
            detailDto,
            toJsonSafe(detailDto.getCast()),
            toJsonSafe(detailDto.getCrew()),
            toJsonSafe(detailDto.getIntroImages()));

        /* 양방향 연결 & 영속 전파 */
        perf.attachDetail(d);
    }

    /* 공연장 XML → Venue 엔티티 */
    public Venue fetchAndSaveVenue(String originPfId, String venueId) {
        try {
            String xmlStr = client.venueDetailXml(venueId);
            var dto = xml.readValue(xmlStr, KopisVenueDetailResponse.class);
            if (venueRepo.existsByVenueId(dto.getDetail().getVenueId())) {
                log.info("venue {} 이미 존재, 중복 저장 방지", venueId);
                return venueRepo.findByVenueId(dto.getDetail().getVenueId()).orElseThrow();
            }
            return venueRepo.save(KopisMapper.toVenue(dto.getDetail()));
        } catch (Exception e) {
            log.warn("venue {} 조회 실패 – 재시도 이벤트 발행", venueId, e);

            /* ① ‘커밋 후’ 발행이 더 안전 → 트랜잭션 롤백 시 중복 방지 */
            eventPublisher.publishAfterCommit(
                new KopisVenueImportFailedEvent(originPfId, venueId, 0)
            );

            /* ② 임시 stub 저장은 그대로 수행 (애플리케이션 로직 유지) */
            return venueRepo.save(
                Venue.builder().venueId(venueId).name("미상").build());
        }
    }
}

