package com.sutej.broadcast.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SubcastDto {
    private Long Id;
    private String name;
    private String description;
    private Integer numberOfPosts;
}
