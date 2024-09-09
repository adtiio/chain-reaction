package com.chainreaction.handler;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.chainreaction.model.Block;
import com.chainreaction.service.ChainReactionService;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Component
public class ChainReactionHandler extends TextWebSocketHandler{
    
    private final ChainReactionService chainReactionService;
    private final ObjectMapper objectMapper;    
    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.put(session.getId(), session);
        sendBoardState(session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session,TextMessage message) throws Exception{
        Map<String, Object> data = objectMapper.readValue(message.getPayload(), Map.class);

        int row = (Integer) data.get("row");
        int col = (Integer) data.get("col");
        chainReactionService.move(row, col);
        //chainReactionService.increment(row,col);
        broadcastBoardState();
            

    }

    @Override
    public void afterConnectionClosed(WebSocketSession session,CloseStatus status){
        sessions.remove(session.getId());
    }

    public void sendBoardState(WebSocketSession session) throws Exception{
        Block grid[][]=chainReactionService.getBoardState();
        session.sendMessage(new TextMessage(objectMapper.writeValueAsString(grid)));
    }
    public void broadcastBoardState() throws Exception {
        Block [][]grid=chainReactionService.getBoardState();
        String gridJson = objectMapper.writeValueAsString(grid);
        for (WebSocketSession session : sessions.values()) {
            session.sendMessage(new TextMessage(gridJson));
        }
    }
    
}
