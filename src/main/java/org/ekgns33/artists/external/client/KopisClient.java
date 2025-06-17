package org.ekgns33.artists.external.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;
import java.net.URI;

@Slf4j
@Component
@RequiredArgsConstructor
public class KopisClient {

    @Value("${openapi.kopis.api.key}")
    private String apiKey;
    @Value("${openapi.kopis.api.base-url}")
    private String baseUrl; // ex) http://kopis.or.kr/openApi/restful

    private final KopisRateLimiter limiter;
    private final RestClient rest;


    /**
     * 공연 상세 XML
     */
    public String performanceDetailXml(String pfId) {
        limiter.acquire();

        String testUrl = "http://kopis.or.kr/openApi/restful/pblprfr/" + pfId + "?service="
            + apiKey;
        return rest.get()
            .uri(testUrl)
            .accept(MediaType.APPLICATION_XML)
            .header(HttpHeaders.USER_AGENT,
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36")
            .retrieve()
            .body(String.class);
    }

    /**
     * 공연장 상세 XML
     */
    public String venueDetailXml(String venueId) {
        limiter.acquire();                          // <— 속도 제한
        URI uri = UriComponentsBuilder.fromUriString(baseUrl)
            .pathSegment("prfplc", venueId)
            .queryParam("service", apiKey)
            .build(true)          // ← 인코딩 ON
            .toUri();
        log.debug("REQ -> {}", uri);

        return rest.get()
            .uri(uri)
            .accept(MediaType.APPLICATION_XML)     // ★ 핵심 ★
            .header(HttpHeaders.USER_AGENT, "Mozilla/5.0")   // 안전용
            .retrieve()
            .body(String.class);
    }

    /**
     * 목록 XML (쿼리 스트링만 전달)
     */
    public String performanceListXml(MultiValueMap<String, String> queryParams) {
        limiter.acquire();                          // <— 속도 제한
        String uri = UriComponentsBuilder.fromUriString(baseUrl)
            .pathSegment("pblprfr")
            .queryParams(queryParams)
            .queryParam("service", apiKey)
            .build()
            .toUriString();
        return rest.get()
            .uri(uri)
            .accept(MediaType.APPLICATION_XML)     // ★ 핵심 ★
            .header(HttpHeaders.USER_AGENT, "Mozilla/5.0")   // 안전용

            .retrieve()
            .body(String.class);
    }
}
