package com.codecool.backend.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.*;

@Document(collection = "enemies")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Enemy {
    @Id
    private String id;

    private int damage;
    private int speed;
    private int hp;
    private String color;
    private boolean canJump = false;
}

