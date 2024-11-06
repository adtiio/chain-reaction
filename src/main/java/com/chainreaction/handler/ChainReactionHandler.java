package com.chainreaction.handler;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.context.annotation.ComponentScan;
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
public class ChainReactionHandler extends TextWebSocketHandler {

  //private final ChainReactionService chainReactionService;
  private final ObjectMapper objectMapper;
  private final Map<String, ChainReactionService> sessions = new ConcurrentHashMap<>();
  private final Map<String, List<WebSocketSession>> sessionMap = new ConcurrentHashMap<>(); 
  

  @Override
  public void afterConnectionEstablished(WebSocketSession session) throws Exception {
    String sessionId = session.getUri().getQuery().split("=")[1];
  
    sessions.computeIfAbsent(sessionId, id -> new ChainReactionService(this,new ObjectMapper()));
    sessionMap.computeIfAbsent(sessionId, id -> new ArrayList<>()).add(session);
    sendBoardState(session);
  }

  @Override
  protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
    String sessionId =session.getUri().getQuery().split("=")[1];
    ChainReactionService chainReactionService=sessions.get(sessionId);

    Map<String, Object> data = objectMapper.readValue(message.getPayload(), Map.class);
    
    

    int row = (Integer) data.get("row");
    int col = (Integer) data.get("col");
    chainReactionService.move(row, col, sessionId);


  }

  @Override
  public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
    String sessionId = session.getUri().getQuery().split("=")[1];
    // sessions.remove(sessionId);
    // sessionMap.remove(sessionId);

  }

  public void sendBoardState(WebSocketSession session) throws Exception {
    String sessionId = session.getUri().getQuery().split("=")[1];
    ChainReactionService chainReactionService = sessions.get(sessionId);

    if(chainReactionService!=null){
      Block grid[][] = sessions.get(sessionId).getBoardState();
      session.sendMessage(new TextMessage(objectMapper.writeValueAsString(grid)));
    }
  }

  public void broadcastBoardState(String sessionId) throws Exception {
    ChainReactionService chainReactionService = sessions.get(sessionId);

    if(chainReactionService!=null){
      Block[][] grid =sessions.get(sessionId).getBoardState();
      String gridJson = objectMapper.writeValueAsString(grid);

      List<WebSocketSession> sessions = sessionMap.get(sessionId); 
    
      for (WebSocketSession s : sessions) {
        s.sendMessage(new TextMessage(gridJson));
      }
            
    }
 
    
  }

}
