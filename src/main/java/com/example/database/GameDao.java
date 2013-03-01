package com.example.database;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class GameDao {

    Logger log = LoggerFactory.getLogger(this.getClass());

    @Resource
    JdbcTemplate jdbcTemplate;

    public void setupIfNotYet() {
        try {
            jdbcTemplate.queryForInt("select count(1) from game");
        } catch (Exception e) {
            log.info("Create table 'game' and insert some records...");
            jdbcTemplate.execute("create table game (id int primary key, name varchar(100), board int)");
            jdbcTemplate.update("insert into game values(?,?,?)", 1, "Game1", 0);
            jdbcTemplate.update("insert into game values(?,?,?)", 2, "Game2", 0);
            jdbcTemplate.update("insert into game values(?,?,?)", 3, "Game3", 0);
            jdbcTemplate.update("insert into game values(?,?,?)", 4, "Game4", 0);
            jdbcTemplate.update("insert into game values(?,?,?)", 5, "Game5", 0);
        }
    }
    
    public void updateGame(Game game) {
    	jdbcTemplate.update("update game set board = " + game.encodeBoard() +
    						" where id = " + game.getId());
    }

    public List<Game> findAll() {
        setupIfNotYet();
        return jdbcTemplate.query("select * from game", mapper);
    }

    RowMapper<Game> mapper = new RowMapper<Game>() {
        @Override
        public Game mapRow(ResultSet resultSet, int i) throws SQLException {
            return extractor.extractData(resultSet);
        }
    };

    ResultSetExtractor<Game> extractor = new ResultSetExtractor<Game>() {
        @Override
        public Game extractData(ResultSet resultSet) throws SQLException, DataAccessException {
            Game game = new Game();
            game.setId(resultSet.getInt("id"));
            game.setName(resultSet.getString("name"));
            game.decodeBoard(resultSet.getInt("board"));
            return game;
        }
    };

}
