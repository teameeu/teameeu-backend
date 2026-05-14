package com.teameau.waymore.chat.controller;

import com.teameau.waymore.chat.dto.ChatSendRequest;
import com.teameau.waymore.chat.service.ChatCommandService;
import com.teameau.waymore.chat.service.ChatQueryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class ChatStompController {
    private final ChatCommandService chatCommandService;
    private final ChatQueryService chatQueryService;

    @MessageMapping("/api/chat/message")
    public void sendMessage(@Valid ChatSendRequest request, Principal principal) {
        Long userId = Long.parseLong(principal.getName());
        chatCommandService.sendMessage(userId, request);
    }
}
