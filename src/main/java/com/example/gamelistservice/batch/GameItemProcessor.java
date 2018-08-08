package com.example.gamelistservice.batch;

import org.springframework.batch.item.ItemProcessor;

import com.example.gamelistservice.model.Game;

public class GameItemProcessor implements ItemProcessor<Game, Game> {

 @Override
 public Game process(Game game) throws Exception {
  return game;
 }

} 
