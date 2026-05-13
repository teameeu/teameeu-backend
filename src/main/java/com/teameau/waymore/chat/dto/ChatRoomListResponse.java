package com.teameau.waymore.chat.dto;

import java.util.List;

public record ChatRoomListResponse(
        List<ChatRoomSummaryResponse> chatSessions
) {
}
