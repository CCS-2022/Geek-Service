package com.geek.geekservice.controller;

import com.geek.geekservice.exception.RepairHubException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ping")
public class Ping {
    @GetMapping
    public ResponseEntity<String> ping(@RequestParam int errorCode){
        if(errorCode == 404) {
            throw new RepairHubException(
                    HttpStatus.NOT_FOUND,
                    "404 - NOT FOUND error sample response",
                    (Integer) errorCode
            );
        }else if( errorCode == 500){
            throw new RepairHubException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "500 - INTERNAL SERVER ERROR sample response",
                    (Integer) errorCode
            );
        }
        return new ResponseEntity<>( "status: OK", HttpStatus.OK);
    }


}
