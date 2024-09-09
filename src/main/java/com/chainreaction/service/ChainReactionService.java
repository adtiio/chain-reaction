package com.chainreaction.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chainreaction.handler.ChainReactionHandler;
import com.chainreaction.model.Block;

@Service
public class ChainReactionService {


    int trav[][]={{0,1},{1,0},{0,-1},{-1,0}};
    Block [][] grid=new Block[10][10];
    int n=10,m=10;
    int player=1;
   


     public ChainReactionService() {
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

    public void increment(int row,int col,int currPlayer) throws Exception{
        int newVal=grid[row][col].getCount()+1;
        grid[row][col].setPlayer(currPlayer);
        grid[row][col].setCount(newVal);
        
        if(explode(row,col)){
            grid[row][col].setCount(0);
            for(int a[] : trav){
                int x=row+a[0], y=col+a[1];
                if(x<n && y<m && x>=0 && y>=0){
                    increment(x,y,currPlayer);
                }
            }
        }
    }

    public void move(int row,int col) throws Exception{
        Block block=grid[row][col];
        if(block.getPlayer() == player || block.getCount() == 0){
            increment(row,col,player);
            player = (player % 2) + 1;
        } 

    }
}
