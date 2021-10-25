package com.blessing.rentaldream.modules.zone;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
@Setter(AccessLevel.PRIVATE)
@EqualsAndHashCode(of = "id")
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class Zone {
    @Id @GeneratedValue @Column(name = "ZONE_ID")
    private Long id;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String localCityName;

    @Column(nullable = true)
    private String province;

    @Override
    public String toString() {
        return String.format("%s(%s)/%s", city, localCityName, province);
    }
}
