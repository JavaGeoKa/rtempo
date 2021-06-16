package com.geo.controller;


import com.geo.dto.TempDto;
import com.geo.service.TemperatureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/temp")
public class EndpointController {

    @Autowired
    TemperatureService temperatureService;

    @PostMapping("/add/")
    public TempDto addDriver(@RequestBody TempDto tempDto) {
        return temperatureService.addNewEvent(tempDto);
    }






}
