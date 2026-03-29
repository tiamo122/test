package com.hust.classroomreserve.entity;

import java.sql.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data

@NoArgsConstructor
@AllArgsConstructor
public class Buildings {
    @JsonProperty("build_name")
    private String buildName;
    @JsonProperty("build_time")
    private Date buildTime;
    @JsonProperty("build_section")
    private String buildSection;
    @JsonProperty("build_state")
    private Integer buildState;
    @JsonProperty("build_capacity")
    private Integer buildCapacity;
    
}

