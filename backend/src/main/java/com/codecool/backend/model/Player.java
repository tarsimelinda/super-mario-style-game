package com.codecool.backend.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.*;

@Document(collection = "players")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Player {
    @Id
    private String id;

    private String name;
    private int hp;
    private int coins;
    private Boolean shield = false;
    private String status;
}

