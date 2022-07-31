package com.sutej.broadcast.services;

import com.sutej.broadcast.dto.SubcastDto;
import com.sutej.broadcast.exception.BroadcastExceptiion;
import com.sutej.broadcast.mapper.SubcastMapper;
import com.sutej.broadcast.modals.SubCast;
import com.sutej.broadcast.repository.SubcastRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class SubcastService {

    private final SubcastRepository subcastRepository;
    private SubcastMapper subcastMapper;

    @Transactional
    public SubcastDto save(SubcastDto subcastDto){
        SubCast save = subcastRepository.save(subcastMapper.mapDtoToSubcast(subcastDto));
        subcastDto.setId(save.getSubcastId());
        return subcastDto;
    }

    @Transactional()
    public List<SubcastDto> getAll() {
        return subcastRepository.findAll()
                .stream()
                .map(subcastMapper::mapSubcastToDto)
                .collect(Collectors.toList());
    }

    public SubcastDto getSubcast(Long id) throws BroadcastExceptiion {
        SubCast subCast = subcastRepository.findById(id)
                .orElseThrow(()->new BroadcastExceptiion("No subcast found with that Id"));
        return subcastMapper.mapSubcastToDto(subCast);
    }
}
