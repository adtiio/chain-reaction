package com.chainreaction.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.*;

import com.chainreaction.handler.ChainReactionHandler;
import com.chainreaction.model.Block;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ChainReactionService {


    private ChainReactionHandler chainReactionHandler;
    private ObjectMapper objectMapper;


    int trav[][]={{0,1},{1,0},{0,-1},{-1,0}};
    Block [][] grid;
    int n=10,m=10;
    int player=1;
   


     public ChainReactionService(ChainReactionHandler chainReactionHandler,ObjectMapper objectMapper) {
        this.objectMapper=objectMapper;
        this.chainReactionHandler=chainReactionHandler;
        this.grid = new Block[n][m];
        initializeBoard();
    }

    public void initializeBoard() {
        
        for(Block row[] : grid){
            for(int i=0;i<row.length;i++){
                row[i]=new Block(0,0);
            }
        }
    }

    public Block[][] getBoardState() {
        return grid;
    }

    public boolean explode(int row,int col){
        int n=grid.length,m=grid[0].length;

        if((row==n-1 && col==m-1)|| (row==n-1 && col==0) || (row==0 && col==m-1) || (row==0 && col==0)){
            if(grid[row][col].getCount()>1) return true;
        }else if(row==n-1 || col==m-1 || row==0 || col==0){
            if(grid[row][col].getCount()>2) return true;
        }else{
            if(grid[row][col].getCount()>3) return true;
        }
        return false;
        
    }

    // public void increment(int row,int col,int currPlayer,WebSocketSession session) throws Exception{
    //     int newVal=grid[row][col].getCount()+1;
    //     grid[row][col].setPlayer(currPlayer);
    //     grid[row][col].setCount(newVal);
    //     chainReactionHandler.broadcastBoardState(session);
    //     delay(300);
        
    //     if(explode(row,col)){
    //         grid[row][col].setCount(0);
    //         for(int a[] : trav){
    //             int x=row+a[0], y=col+a[1];
    //             if(x<n && y<m && x>=0 && y>=0){
    //                 increment(x,y,currPlayer,session);
    //             }
    //         }
    //     }
    // }

    public void move(int row,int col,String sessionId) throws Exception{
        Block block=grid[row][col];
        if(block.getPlayer() == player || block.getCount() == 0){
            bfs(row,col,player,sessionId);
            player = (player % 2) + 1;
        } 

    }
    public void delay(int secs){
        try {
            Thread.sleep(secs); 
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void bfs(int row,int col,int currPlayer,String sessionId) throws Exception{
        Queue<int[]> q=new ArrayDeque<>();
        grid[row][col].setCount(grid[row][col].getCount()+1);
        grid[row][col].setPlayer(currPlayer);
        q.add(new int[]{row,col});
        
        while(!q.isEmpty()){
            int curr[]=q.poll();
            int r=curr[0], c=curr[1];
            if(explode(r, c)){
                grid[r][c].setCount(0);
                grid[r][c].setPlayer(0);
                for(int a[] : trav){
                    int x=r+a[0], y=c+a[1];
                    if(x<n && y<m && x>=0 && y>=0){
                        q.add(new int[]{x,y});
                        grid[x][y].setCount(grid[x][y].getCount()+1);
                        grid[x][y].setPlayer(currPlayer);
                    }
                    chainReactionHandler.broadcastBoardState(sessionId);
                    delay(40);
                }
                delay(150);
            }
        }
        chainReactionHandler.broadcastBoardState(sessionId);

    }
}
