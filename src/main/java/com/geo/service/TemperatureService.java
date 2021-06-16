package com.geo.service;

import com.geo.bot.Bot;
import com.geo.dto.TempDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TemperatureService {

    @Autowired
    Bot bot;

    public TempDto addNewEvent(TempDto tempDto) {

        bot.sendTemperatureFromBot(tempDto);
        System.out.println(tempDto);
//        Driver driver = new Driver(newDriver.getFirstName(), newDriver.getLastName(),
//                newDriver.getAge(), newDriver.getAddress(), newDriver.getDriversLicense());
//        driver = driversRepository.save(driver);
//        return convertToDriverDto(driver);
        return tempDto;
    }



}
