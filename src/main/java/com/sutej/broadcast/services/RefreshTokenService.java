package com.sutej.broadcast.services;

import com.sutej.broadcast.exception.BroadcastExceptiion;
import com.sutej.broadcast.modals.RefreshToken;
import com.sutej.broadcast.repository.RefreshTokenRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.UUID;

@Service
@AllArgsConstructor
@Transactional
public class RefreshTokenService {

    @Autowired
    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshToken generateRefreshToken(){
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setCreatedDate(Instant.now());

        return refreshTokenRepository.save(refreshToken) ;
    }

    public void validateRefreshToken(String token) throws BroadcastExceptiion {
        refreshTokenRepository.findByToken(token)
                .orElseThrow(()-> new BroadcastExceptiion("Invalid refresh token"));
    }

    public void deleteRefreshToken(String token){
        refreshTokenRepository.deleteByToken(token);
    }

}
