package com.hust.classroomreserve.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/calendar")
public class CalendarController {

    @GetMapping("/get")
    public String getCalendar(@RequestParam("year") int year) {
        String imageUrl = "";
        switch (year) {
            case 2023:
                imageUrl = "/images/1.jpg";
                break;
            case 2024:
                imageUrl = "/images/2.jpg";
                break;
            case 2025:
                imageUrl = "/images/3.jpg";
                break;
            default:
                imageUrl = "/images/1.jpg";
        }
        return imageUrl;
    }
}