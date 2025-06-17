package org.ekgns33.artists.external.dto;

import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.ekgns33.artists.PointUtils;
import org.ekgns33.artists.domain.performance.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import org.locationtech.jts.geom.Point;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class KopisMapper {

    private static final DateTimeFormatter DOT_DATE = DateTimeFormatter.ofPattern("yyyy.MM.dd");


    public static Venue toVenue(KopisVenueDetailResponse.Detail d) {

        Point point = PointUtils.fromLatLon(d.getLatitude(), d.getLongitude());

        return Venue.builder()
            .venueId(d.getVenueId())          // mt10id
            .name(d.getName())                // fcltynm
            .seatScale(d.getSeatScale())      // seatscale
            .openYear(d.getOpenYear())        // opende
            .facilityType(d.getFacilityType())// fcltychartr
            .address(d.getAddress())          // adres
            .location(point)                  // la / lo
            .tel(d.getTel())                  // telno
            .homepage(d.getHomepage())        // relateurl
            .build();
    }


    public static Performance toPerformance(KopisResponse.Performance dto) {
        return Performance.builder()
            .performanceId(dto.getPerformanceId())
            .name(dto.getPerformanceName())
            .genre(Genre.fromRaw(dto.getGenreName()))
            .status(Status.of(dto.getPerformanceState()))
            .startDate(parseDate(dto.getPerformanceStartDate()))
            .endDate(parseDate(dto.getPerformanceEndDate()))
            .posterUrl(dto.getPosterUrl())
            .openRun("Y".equalsIgnoreCase(dto.getOpenRun()))
            .childShow(false)                   // LIST 응답엔 kidState 없음
            .build();
    }


    /**
     * 상세 응답 ➜ Performance + PerformanceDetail 동시 갱신
     */
    public static void mergeDetail(KopisPerformanceDetailResponse.Detail dto,
        Performance perf) {

        perf.updateFromDetail(
            dto.getDateTimeGuidance(),
            dto.getChildShow()
        );

        PerformanceDetail d = toDetailEntity(
            dto,
            toJsonSafe(dto.getCast()),
            toJsonSafe(dto.getCrew()),
            toJsonSafe(dto.getIntroImages()));

        perf.attachDetail(d);
    }

    public static void mergeVenue(KopisVenueDetailResponse.Detail dto,
        Performance perf, Venue venue) {
        perf.updateFromVenue(venue);
    }


    public static PerformanceDetail toDetailEntity(KopisPerformanceDetailResponse.Detail d,
        String castJson,
        String crewJson, String imgJson) {
        return PerformanceDetail.builder()
            .runningTimeMin(TimeParser.toMinutes(d.getRunningTime()))
            .ageLimit(d.getAgeLimit())
            .seatPrice(d.getSeatPrice())
            .dtGuidance(d.getDateTimeGuidance())
            .synopsis(d.getSynopsis())
            .introImagesJson(imgJson)
            .castJson(castJson)
            .crewJson(crewJson)
            .producer(d.getProducer())
            .agency(d.getAgency())
            .host(d.getHost())
            .sponsor(d.getSponsor())
            .build();
    }

    private static LocalDate parseDate(String raw) {
        if (raw == null || raw.isBlank()) {
            return null;
        }
        return LocalDate.parse(raw.contains(".") ? raw : raw.replace("-", "."), DOT_DATE);
    }

    private static List<String> csvToList(String csv) {
        return csv == null || csv.isBlank()
            ? java.util.Collections.emptyList()
            : Arrays.stream(csv.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .toList();
    }

    public static String toJsonSafe(Object obj) {
        try {
            return JsonHolder.MAPPER.writeValueAsString(obj);
        } catch (Exception e) {
            return "[]";
        }
    }

    private static final class JsonHolder {

        private static final com.fasterxml.jackson.databind.ObjectMapper MAPPER =
            new com.fasterxml.jackson.databind.ObjectMapper();
    }

    /* 런타임 “1시간 30분” → 90 */
    private static final class TimeParser {

        private static int toMinutes(String raw) {
            if (raw == null) {
                return 0;
            }
            int h = 0, m = 0;
            var cleaned = raw.replace(" ", "");
            if (cleaned.contains("시간")) {
                String[] split = cleaned.split("시간");
                h = Integer.parseInt(split[0]);
                if (split.length > 1 && split[1].contains("분")) {
                    m = Integer.parseInt(split[1].replace("분", ""));
                }
            } else if (cleaned.contains("분")) {
                m = Integer.parseInt(cleaned.replace("분", ""));
            }
            return h * 60 + m;
        }
    }

    private static boolean isY(String v) {
        return "Y".equalsIgnoreCase(v);
    }

    private static LocalDateTime parseDateTime(String raw) {
        return raw == null ? null : LocalDateTime.parse(
            raw.replace(" ", "T")); // “2019-07-25 10:03:14” → ISO
    }
}
