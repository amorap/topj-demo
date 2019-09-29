package com.hackaton.app.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Alberto Mora Plata (moral12)
 */
@RestController
public class HomeController {

    @GetMapping
    public String home(){
        return "Backstreet Hodlers App";
    }

}
