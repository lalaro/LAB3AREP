package edu.escuelaing.app.AppSvr.controllers;

import edu.escuelaing.app.AppSvr.RestController;
import edu.escuelaing.app.AppSvr.GetMapping;
import edu.escuelaing.app.AppSvr.RequestParam;

import java.util.concurrent.atomic.AtomicLong;

@RestController
public class MathController {
    @GetMapping("/square")
    public static String square(@RequestParam("n") int n) {
        return String.valueOf(n * n);
    }
}