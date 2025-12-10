package com.example.tubesPBW.model;

import lombok.Data;

@Data
public class Song {
    private Long songID;
    private String title;
    private String artistName;    
    private String albumTitle;    
    private boolean favorite;
}