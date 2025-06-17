package org.ekgns33.artists.domain.performance;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "performance_details")
@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class PerformanceDetail {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /* 1 : 1 FK & UNIQUE */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "performance_id", nullable = false, unique = true)
    private Performance performance;

    /* 러닝타임, 관람 연령 */
    private Integer runningTimeMin;        // “1시간 30분” → 90
    @Column(length = 50) private String ageLimit;   // “만 12세 이상”

    /* 가격·시간 안내 */
    @Column(length = 200) private String seatPrice; // pcseguidance
    @Column(length = 200) private String dtGuidance;// dtguidance

    /* 시놉시스 & 미디어 */
    @Column(columnDefinition = "text")  private String synopsis;
    @Column(columnDefinition = "jsonb") private String introImagesJson;

    /* 인물 */
    @Column(columnDefinition = "jsonb") private String castJson;   // 배열 직렬화
    @Column(columnDefinition = "jsonb") private String crewJson;

    /* 회사 */
    @Column(length = 200) private String producer; // entrpsnmP
    @Column(length = 200) private String agency;   // entrpsnmA
    @Column(length = 200) private String host;     // entrpsnmH
    @Column(length = 200) private String sponsor;  // entrpsnmS

    /* setter 한 곳만 허용 – 연관 편의 */
    public void setPerformance(Performance p) { this.performance = p; }
}
