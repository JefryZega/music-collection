package com.example.tubesPBW.model;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Favorite {
    private Long favoritesID;
    private Long userID;
    private Long songID;
    private LocalDateTime addedAt;
    
    public Favorite(Long userID, Long songID) {
        this.userID = userID;
        this.songID = songID;
        this.addedAt = LocalDateTime.now();
    }
}