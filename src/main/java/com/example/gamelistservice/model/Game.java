package com.example.gamelistservice.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.*;

@Entity
@Getter @Setter
@NoArgsConstructor
@ToString @EqualsAndHashCode
public class Game {
    @Id @GeneratedValue
    private Long id;
    private @NonNull String title;
    private @NonNull String platform;
    private @NonNull String score;
    private @NonNull String genre;
    private @NonNull String editors_choice;
}