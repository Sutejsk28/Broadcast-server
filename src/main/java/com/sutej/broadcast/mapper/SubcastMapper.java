package com.sutej.broadcast.mapper;

import com.sutej.broadcast.dto.SubcastDto;
import com.sutej.broadcast.modals.SubCast;
import org.springframework.stereotype.Component;

@Component
public class SubcastMapper{

    public SubcastDto mapSubcastToDto(SubCast subCast) {
        return SubcastDto.builder().name(subCast.getSubcastName())
                .description(subCast.getSubcastDescription())
                .Id(subCast.getSubcastId())
                .numberOfPosts(subCast.getPosts().size())
                .build();
    }

    public SubCast mapDtoToSubcast(SubcastDto subcastDto) {
        return SubCast.builder().subcastName(subcastDto.getName())
                .subcastDescription(subcastDto.getDescription())
                .build();
    }

}