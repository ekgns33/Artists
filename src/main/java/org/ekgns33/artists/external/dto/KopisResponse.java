package org.ekgns33.artists.external.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * KOPIS API 응답 루트 DTO
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@JacksonXmlRootElement(localName = "dbs")
public class KopisResponse {

    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "db")
    private List<Performance> performances;

    /**
     * 공연 정보 DTO
     */
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Performance {

        /**
         * 공연 ID
         */
        @JacksonXmlProperty(localName = "mt20id")
        private String performanceId;

        /**
         * 공연명
         */
        @JacksonXmlProperty(localName = "prfnm")
        private String performanceName;

        /**
         * 공연 장르명
         */
        @JacksonXmlProperty(localName = "genrenm")
        private String genreName;

        /**
         * 공연상태
         */
        @JacksonXmlProperty(localName = "prfstate")
        private String performanceState;

        /**
         * 공연시작일
         */
        @JacksonXmlProperty(localName = "prfpdfrom")
        private String performanceStartDate;

        /**
         * 공연종료일
         */
        @JacksonXmlProperty(localName = "prfpdto")
        private String performanceEndDate;

        /**
         * 공연포스터경로
         */
        @JacksonXmlProperty(localName = "poster")
        private String posterUrl;

        /**
         * 공연시설명(공연장명)
         */
        @JacksonXmlProperty(localName = "fcltynm")
        private String facilityName;

        /**
         * 오픈런 여부
         */
        @JacksonXmlProperty(localName = "openrun")
        private String openRun;

        /**
         * 공연지역
         */
        @JacksonXmlProperty(localName = "area")
        private String area;

        /**
         * 공연상태 한글명 반환
         */
        public String getPerformanceStateKorean() {
            switch (performanceState) {
                case "01": return "공연예정";
                case "02": return "공연중";
                case "03": return "공연완료";
                default: return performanceState;
            }
        }

        /**
         * 오픈런 여부 boolean 반환
         */
        public boolean isOpenRun() {
            return "Y".equals(openRun);
        }

        /**
         * 공연 기간 문자열 반환
         */
        public String getPerformancePeriod() {
            if (performanceStartDate != null && performanceEndDate != null) {
                return performanceStartDate + " ~ " + performanceEndDate;
            }
            return "";
        }

        /**
         * 포스터 URL 유효성 확인
         */
        public boolean hasPoster() {
            return posterUrl != null && !posterUrl.trim().isEmpty() && posterUrl.startsWith("http");
        }

        /**
         * 출력용 문자열 생성
         */
        @Override
        public String toString() {
            return String.format("""
                [공연 ID: %s]
                공연명: %s
                장르: %s
                상태: %s
                기간: %s
                장소: %s (%s)
                오픈런: %s
                포스터: %s
                """,
                performanceId,
                performanceName,
                genreName,
                getPerformanceStateKorean(),
                getPerformancePeriod(),
                facilityName,
                area,
                isOpenRun() ? "예" : "아니오",
                hasPoster() ? posterUrl : "없음"
            );
        }
    }
}
