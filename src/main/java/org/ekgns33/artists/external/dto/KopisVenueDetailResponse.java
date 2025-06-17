// 공연시설 상세 조회 응답 dto
package org.ekgns33.artists.external.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.*;
import lombok.*;

@Getter @NoArgsConstructor @AllArgsConstructor
@Builder @JsonIgnoreProperties(ignoreUnknown = true)
@JacksonXmlRootElement(localName = "dbs")
public class KopisVenueDetailResponse {

    @JacksonXmlProperty(localName = "db")
    private Detail detail;

    @Getter @NoArgsConstructor @AllArgsConstructor @Builder
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Detail {

        @JacksonXmlProperty(localName = "mt10id")   private String venueId;
        @JacksonXmlProperty(localName = "fcltynm")  private String name;
        @JacksonXmlProperty(localName = "seatscale")private Integer seatScale;
        @JacksonXmlProperty(localName = "opende")   private Integer openYear;
        @JacksonXmlProperty(localName = "fcltychartr") private String facilityType;

        @JacksonXmlProperty(localName = "adres")    private String address;
        @JacksonXmlProperty(localName = "la")       private Double latitude;
        @JacksonXmlProperty(localName = "lo")       private Double longitude;

        @JacksonXmlProperty(localName = "telno")    private String tel;
        @JacksonXmlProperty(localName = "relateurl")private String homepage;

        // 부대시설 Y/N ― JSON 으로 묶어 저장하면 편리
        @JacksonXmlProperty(localName = "restaurant") private String restaurant;
        @JacksonXmlProperty(localName = "cafe")       private String cafe;
        @JacksonXmlProperty(localName = "store")      private String store;
        @JacksonXmlProperty(localName = "parkinglot") private String parkingLot;
    }
}
