package com.taxah.hspd.service.auth;

import com.taxah.hspd.entity.auth.RefreshToken;
import com.taxah.hspd.exception.NotFoundException;
import com.taxah.hspd.repository.auth.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static com.taxah.hspd.util.constant.Exceptions.REFRESH_TOKEN_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshToken getRefreshToken(String token) {
        return refreshTokenRepository.findByRefreshToken(token)
                .orElseThrow(() -> new NotFoundException(REFRESH_TOKEN_NOT_FOUND));
    }

    public void saveRefreshToken(RefreshToken refreshToken) {
        refreshTokenRepository.save(refreshToken);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void deleteByRefreshToken(String token) {
        refreshTokenRepository.removeByRefreshToken(token);
    }
}
