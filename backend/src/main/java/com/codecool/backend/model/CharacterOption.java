package com.codecool.backend.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "characters")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CharacterOption {
    @Id
    private String id;

    private String key;
    private String displayName;
    private String color;
}