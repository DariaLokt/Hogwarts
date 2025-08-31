package ru.hogwarts.school.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("info")
@Profile("!production")
public class InfoControllerPort1 implements InfoController{
    @Value("${server.port}")
    private int port;

    public InfoControllerPort1() {
    }

    @GetMapping("/port")
    public int getPort() {
        return port;
    }
}
