package org.ekgns33.artists.external.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Setter;


/**
 * KOPIS 공연 정보 조회 요청 DTO
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KopisPerformanceRequest {

    /**
     * 공연시작일 (필수, 8자리, YYYYMMDD 형식)
     */
    @NotBlank(message = "공연시작일은 필수입니다.")
    @Pattern(regexp = "\\d{8}", message = "공연시작일은 YYYYMMDD 형식이어야 합니다.")
    private String stdate;

    /**
     * 공연종료일 (필수, 8자리, YYYYMMDD 형식, 최대 31일)
     */
    @NotBlank(message = "공연종료일은 필수입니다.")
    @Pattern(regexp = "\\d{8}", message = "공연종료일은 YYYYMMDD 형식이어야 합니다.")
    private String eddate;

    /**
     * 현재페이지 (필수, 기본값: 1)
     */
    @NotNull(message = "현재페이지는 필수입니다.")
    @Min(value = 1, message = "페이지는 1 이상이어야 합니다.")
    @Builder.Default
    private Integer cpage = 1;

    /**
     * 페이지당 목록 수 (필수, 최대 100건, 기본값: 10)
     */
    @NotNull(message = "페이지당 목록 수는 필수입니다.")
    @Min(value = 1, message = "페이지당 목록 수는 1 이상이어야 합니다.")
    @Max(value = 100, message = "페이지당 목록 수는 100 이하여야 합니다.")
    @Builder.Default
    private Integer rows = 10;

    /**
     * 장르코드 (선택, 4자리)
     * AAAA: 연극, BBBC: 뮤지컬, CCCA: 클래식, CCCC: 오페라, EEEB: 콘서트 등
     */
    @Size(max = 4, message = "장르코드는 최대 4자리입니다.")
    private String shcate;

    /**
     * 공연명 (선택, 최대 100자)
     */
    @Size(max = 100, message = "공연명은 최대 100자입니다.")
    private String shprfnm;

    /**
     * 공연시설명 (선택, 최대 100자)
     */
    @Size(max = 100, message = "공연시설명은 최대 100자입니다.")
    private String shprfnmfct;

    /**
     * 공연장코드 (선택, 4자리)
     */
    @Size(max = 4, message = "공연장코드는 최대 4자리입니다.")
    private String prfplccd;

    /**
     * 지역(시도)코드 (선택, 2자리)
     */
    @Size(max = 2, message = "지역(시도)코드는 최대 2자리입니다.")
    private String signgucode;

    /**
     * 지역(구군)코드 (선택, 4자리)
     */
    @Size(max = 4, message = "지역(구군)코드는 최대 4자리입니다.")
    private String signgucodesub;

    /**
     * 아동공연여부 (선택, Y/N, 기본값: N)
     */
    @Pattern(regexp = "^[YN]$", message = "아동공연여부는 Y 또는 N이어야 합니다.")
    private String kidstate;

    /**
     * 공연상태코드 (선택, 4자리)
     * 01: 공연예정, 02: 공연중, 03: 공연완료
     */
    @Size(max = 4, message = "공연상태코드는 최대 4자리입니다.")
    private String prfstate;

    /**
     * 오픈런여부 (선택, Y/N)
     */
    @Pattern(regexp = "^[YN]$", message = "오픈런여부는 Y 또는 N이어야 합니다.")
    private String openrun;

    /**
     * 해당일자 이후 등록/수정된 항목만 출력 (선택, 8자리, YYYYMMDD 형식)
     */
    @Pattern(regexp = "\\d{8}", message = "해당일자는 YYYYMMDD 형식이어야 합니다.")
    private String afterdate;


    /**
     * 기본 요청 생성 (시작일, 종료일만 필수)
     */
    public static KopisPerformanceRequest of(String startDate, String endDate) {
        return KopisPerformanceRequest.builder()
            .stdate(startDate)
            .eddate(endDate)
            .cpage(1)
            .rows(10)
            .build();
    }

    /**
     * 페이징 포함 요청 생성
     */
    public static KopisPerformanceRequest of(String startDate, String endDate, int page, int size) {
        return KopisPerformanceRequest.builder()
            .stdate(startDate)
            .eddate(endDate)
            .cpage(page)
            .rows(size)
            .build();
    }
}