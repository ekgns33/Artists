// 공연시설 목록 조회 응답 dto
package org.ekgns33.artists.external.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.*;
import lombok.*;

import java.util.List;

@Getter @NoArgsConstructor @AllArgsConstructor
@Builder @JsonIgnoreProperties(ignoreUnknown = true)
@JacksonXmlRootElement(localName = "dbs")
public class KopisVenueListResponse {

    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "db")
    private List<Venue> venues;

    @Getter @NoArgsConstructor @AllArgsConstructor @Builder
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Venue {

        @JacksonXmlProperty(localName = "mt10id")   private String venueId;
        @JacksonXmlProperty(localName = "fcltynm")  private String name;
        @JacksonXmlProperty(localName = "mt13cnt")  private Integer hallCount;
        @JacksonXmlProperty(localName = "fcltychartr") private String facilityType;
        @JacksonXmlProperty(localName = "sidonm")   private String sido;
        @JacksonXmlProperty(localName = "gugunnm")  private String gugun;
        @JacksonXmlProperty(localName = "opende")   private Integer openYear;
    }
}
