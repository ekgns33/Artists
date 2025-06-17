package org.ekgns33.artists.external.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.*;
import lombok.*;

import java.util.List;

/**
 * KOPIS 공연 상세 조회 응답 DTO (pblprfrDetailRequest)
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@JacksonXmlRootElement(localName = "dbs")
public class KopisPerformanceDetailResponse {

    /** <db> … </db> 단일 노드 */
    @JacksonXmlProperty(localName = "db")
    private Detail detail;

    /* ───────────────────────── 내부 클래스 ───────────────────────── */

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Detail {

        /* 기본 식별자 */
        @JacksonXmlProperty(localName = "mt20id") private String performanceId;   // PF132236
        @JacksonXmlProperty(localName = "mt10id") private String venueId;         // FC001431

        /* 핵심 정보 */
        @JacksonXmlProperty(localName = "prfnm")      private String name;
        @JacksonXmlProperty(localName = "prfpdfrom")  private String startDate;    // 2016.05.12
        @JacksonXmlProperty(localName = "prfpdto")    private String endDate;
        @JacksonXmlProperty(localName = "fcltynm")    private String venueName;
        @JacksonXmlProperty(localName = "genrenm")    private String genreName;
        @JacksonXmlProperty(localName = "prfstate")   private String stateCode;    // 02 or “공연중”

        /* 출연/제작 */
        @JacksonXmlProperty(localName = "prfcast")    private String cast;         // CSV
        @JacksonXmlProperty(localName = "prfcrew")    private String crew;         // CSV

        /* 세부 속성 */
        @JacksonXmlProperty(localName = "prfruntime") private String runningTime;  // “1시간 30분”
        @JacksonXmlProperty(localName = "prfage")     private String ageLimit;     // “만 12세 이상”
        @JacksonXmlProperty(localName = "pcseguidance") private String seatPrice; // “전석 30,000원”

        /* 회사 정보 */
        @JacksonXmlProperty(localName = "entrpsnmP")  private String producer;
        @JacksonXmlProperty(localName = "entrpsnmA")  private String agency;
        @JacksonXmlProperty(localName = "entrpsnmH")  private String host;
        @JacksonXmlProperty(localName = "entrpsnmS")  private String sponsor;

        /* 기타 플래그 */
        @JacksonXmlProperty(localName = "openrun")     private String openRun;     // Y/N
        @JacksonXmlProperty(localName = "visit")       private String visit;       // 내한 Y/N
        @JacksonXmlProperty(localName = "child")       private String childShow;   // 아동 Y/N
        @JacksonXmlProperty(localName = "daehakro")    private String daehakro;    // 대학로 Y/N
        @JacksonXmlProperty(localName = "festival")    private String festival;    // 축제 Y/N
        @JacksonXmlProperty(localName = "musicallicense") private String musicalLicense; // Y/N
        @JacksonXmlProperty(localName = "musicalcreate")  private String musicalCreate;  // Y/N

        @JacksonXmlProperty(localName = "poster")     private String posterUrl;
        @JacksonXmlProperty(localName = "sty")        private String synopsis;
        @JacksonXmlProperty(localName = "updatedate") private String updateDateTime; // 2019-07-25 10:03:14

        /* 소개 이미지 리스트 <styurls><styurl>… */
        @JacksonXmlElementWrapper(localName = "styurls")
        @JacksonXmlProperty(localName = "styurl")
        private List<String> introImages;

        /* 공연 시간 안내 (dtguidance) */
        @JacksonXmlProperty(localName = "dtguidance")
        private String dateTimeGuidance;
    }
}
