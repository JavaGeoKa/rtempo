package com.geo.bot;


import com.geo.dao.PersonRepositoty;
import com.geo.model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Component
public class DataStarter implements CommandLineRunner {
//    final static Logger log = Logger.getLogger(DataStarter.class);


    @Autowired
    PersonRepositoty personRepositoty;

    public static Map<Long, Person> mapPersons = new ConcurrentHashMap<>();

    @Override
    public void run(String... args) throws Exception {

// prepare Persons
        personRepositoty.findAll().stream().forEach(p -> {
            mapPersons.put(p.getId(), p);
        });
        System.out.println("Persons in base -> " + mapPersons.size());


    }



}
