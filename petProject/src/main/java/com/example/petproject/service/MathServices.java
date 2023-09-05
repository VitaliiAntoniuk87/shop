package com.example.petproject.service;

import org.springframework.stereotype.Service;

@Service
public class MathServices {

    public static double roundToHundredths(double value) {
        return Math.round(value * 100.0) / 100.0;
    }

}
