package com.hust.classroomreserve.entity;

import java.sql.Date;


import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Booking {
    @JsonProperty("bk_bd_name")
    private String bkBdName;
    @JsonProperty("bk_bd_section")
    private String bkBdSection;
    @JsonProperty("bk_time")
    private Date bkTime;
    @JsonProperty("bk_pson_name")
    private String bkPsonName;
    @JsonProperty("bk_reason")
    private String bkReason;
    @JsonProperty("bk_pson_id")
    private String bkPsonId;
    @JsonProperty("bk_bd_state")
    private String bkBdState;
    @JsonProperty("lesName")
    private String lesName;
    @JsonProperty("teaName")
    private String teaName;
    @JsonProperty("className")
    private String className;
    @JsonProperty("bk_refused_reason")
    private String bkRefusedReason;
    @JsonProperty("bk_apply_time")
    private String bkApplyTime;

    
}
