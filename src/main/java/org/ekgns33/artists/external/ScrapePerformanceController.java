package org.ekgns33.artists.external;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ekgns33.artists.external.dto.KopisPerformanceRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/scrape/performances")
@RequiredArgsConstructor
public class ScrapePerformanceController {

    private final KopisImportUsecase kopisImporterUsecase;

    /**
     * 공연 목록 조회 (파싱된 객체 반환)
     *
     * @param request 공연 조회 요청 파라미터
     * @return 파싱된 공연 목록
     */
    @GetMapping("/parsed")
    public ResponseEntity<String> getPerformancesParsed(
        @Valid @ModelAttribute KopisPerformanceRequest request) {
        log.info("공연 목록 조회 요청 (파싱): {}", request);

        kopisImporterUsecase.importPerformances(request);
        return ResponseEntity.ok("공연 목록이 성공적으로 파싱되었습니다.");
    }
}