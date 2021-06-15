package com.blessing.lentaldream.modules.zone;

import lombok.Data;

@Data
public class ZoneForm {
    private String zoneName;
    private String city;
    private String province;
    private String localCityName;

    public String getCity() {
        return zoneName.substring(0,getZoneName().indexOf("("));
    }

    public String getProvince() {
        return zoneName.substring(zoneName.indexOf("/") + 1);
    }

    public String getLocalCityName() {
        return zoneName.substring(zoneName.indexOf("(") + 1, zoneName.indexOf(")"));
    }

    public Zone getZone() {
        return Zone.builder().city(this.getCity())
                .localCityName(this.getLocalCityName())
                .province(this.getProvince()).build();
    }
}
