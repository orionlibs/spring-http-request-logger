package com.github.orionlibs.spring_http_request_logger.config;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/")
public class MockController
{
    @GetMapping(produces = "application/json")
    public ResponseEntity<?> testURL(HttpServletRequest request, HttpServletResponse response, Model model)
    {
        return ResponseEntity.ok().body(null);
    }
}