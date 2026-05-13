package com.teameau.waymore.chat.config;

import com.teameau.waymore.user.service.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class StompAuthCahnnelInterceptor implements ChannelInterceptor {

    private final JwtTokenProvider jwtTokenProvider;

    /**
     * jwt 검증 , 사용자 principal 세션 주입
     * @param message STOMP 메세지
     * @param channel 메시지 채널
     * @return 인증 정보가 반영된 메세지
     */
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if (accessor == null || accessor.getCommand() != StompCommand.CONNECT) {
            return message;
        }

        String authorization = accessor.getFirstNativeHeader("Authorization");
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            throw new IllegalArgumentException("WebSocket 인증 헤더 필요");
        }

        String token = authorization.substring(7);
        if (!jwtTokenProvider.validateToken(token)) {
            throw new IllegalArgumentException("유효하지 않은 access token");
        }

        Long userId = jwtTokenProvider.getUserId(token);
        accessor.setUser(new UsernamePasswordAuthenticationToken(userId, null, List.of()));
        return message;

    }
}
