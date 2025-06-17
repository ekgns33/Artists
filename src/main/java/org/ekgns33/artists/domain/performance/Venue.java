// src/main/java/org/ekgns33/artists/domain/performance/Venue.java
package org.ekgns33.artists.domain.performance;

import jakarta.persistence.*;
import lombok.*;
import org.locationtech.jts.geom.Point;

@Entity
@Table(name = "venues")
@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class Venue {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "venue_id", length = 20, nullable = false,
        unique = true, updatable = false)
    private String venueId;

    @Column(length = 200, nullable = false) private String name;
    private Integer seatScale;
    private Integer openYear;

    @Column(length = 50) private String facilityType;
    @Column(columnDefinition = "text") private String address;

    /** PostGIS Point */
    @Column(columnDefinition = "geometry(Point,4326)")
    private Point location;

    private String tel;
    private String homepage;

    public void update(Venue v) {
        this.name = v.getName();
        this.seatScale = v.getSeatScale();
        this.openYear = v.getOpenYear();
        this.facilityType = v.getFacilityType();
        this.address = v.getAddress();
        this.location = v.getLocation();
        this.tel = v.getTel();
        this.homepage = v.getHomepage();

    }
}
