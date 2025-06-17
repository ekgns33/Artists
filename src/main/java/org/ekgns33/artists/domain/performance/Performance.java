package org.ekgns33.artists.domain.performance;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "performances")
@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class Performance {

    /* surrogate PK */
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /* KOPIS natural ID (PFxxxxx) */
    @Column(name = "performance_id", length = 8,
        nullable = false, unique = true, updatable = false)
    private String performanceId;

    /* 기본 */
    @Column(length = 200, nullable = false) private String name;
    @Enumerated(EnumType.STRING) @Column(length = 30) private Genre genre;
    @Enumerated(EnumType.STRING) @Column(length = 20) private Status status;
    private LocalDate startDate;
    private LocalDate endDate;

    /* 미디어 */
    @Column(columnDefinition = "text") private String posterUrl;

    /* 플래그 (Y/N → boolean) */
    private boolean openRun;       // openrun
    private boolean childShow;     // 아동
    private String dtGuidance;     // 운영시간

    /* 수정일자 (KOPIS updatedate) */
    private LocalDateTime updatedAt;

    /* 관계 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "venue_id")               // FK → venues.id
    private Venue venue;

    /* 상세 1 : 1 */
    @OneToOne(mappedBy = "performance",
        cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private PerformanceDetail detail;

    /* 도메인 편의 메서드 */
    public void attachDetail(PerformanceDetail d) {
        this.detail = d;
        d.setPerformance(this);
    }

    public void updateFromVenue(Venue v) {
        this.venue = v;
    }

    public void updateFromDetail(String dateTimeGuidance, String childShow) {
        this.dtGuidance = dateTimeGuidance;
        this.childShow = "Y".equalsIgnoreCase(childShow);
    }
}
