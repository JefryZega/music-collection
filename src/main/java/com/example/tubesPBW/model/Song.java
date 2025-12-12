package com.example.tubesPBW.model;

import lombok.Data;

@Data
public class Song {
    private Long songID;
    private String title;
    private Long albumID;
    private String albumTitle;
    private Long artistID;
    private String artistName;    
    private boolean favorite;

    // tambahan
    private String albumArt; 
    private Long weeklyLikes; 
}