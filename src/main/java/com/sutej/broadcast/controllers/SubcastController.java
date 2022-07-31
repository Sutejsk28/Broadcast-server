package com.sutej.broadcast.controllers;

import com.sutej.broadcast.dto.SubcastDto;
import com.sutej.broadcast.exception.BroadcastExceptiion;
import com.sutej.broadcast.services.SubcastService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subcast")
@AllArgsConstructor
@Slf4j
public class SubcastController {

    private SubcastService subcastService;

    @PostMapping
    public ResponseEntity<SubcastDto> createSubcast(@RequestBody SubcastDto subcastDto){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(subcastService.save(subcastDto));
    }

    @GetMapping
    public ResponseEntity<List<SubcastDto>> getAllSubcasts(){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(subcastService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubcastDto> getSubcast(@PathVariable Long id) throws BroadcastExceptiion {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(subcastService.getSubcast(id));
    }

}
