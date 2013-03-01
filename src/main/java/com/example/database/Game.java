package com.example.database;

import org.springframework.stereotype.Component;

@Component
public class Game {

    private Integer id;

    private String name;
    
    private Integer[][] board;

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer encodeBoard() {
    	Integer encoding = new Integer(0);
    	int k = 0;
    	for (int i = 0; i < 3; i++){
    		for (int j = 0; j < 3; j++) {
    			encoding |= (this.board[i][j] << 2*(k++));
    		}
    	}
    	return encoding;
    }
    
    public Integer[][] getBoard() {
    	return this.board;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public void decodeBoard(Integer boardEncoding){
    	this.board = new Integer[3][3];
    	int k = 0;
    	for (int i = 0; i < 3; i++){
    		for (int j = 0; j < 3; j++) {
    			this.board[i][j] = (boardEncoding >> 2*(k++)) & 3;
    		}
    	}
    }
    
    public void setBoard(Integer[][] board) {
    	this.board = board;
    }
    
    @Override
    public String toString() {
        return "id:" + id + ",name:" + name;
    }

}
