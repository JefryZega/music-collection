package com.example.tubesPBW.model;

import lombok.Data;

@Data
public class Album {
    private Long albumID;
    private String albumTitle;
    private String albumArt;
    private Long artistID;
    private String artistName; 
    
    public Album() {}
    
    public Album(Long albumID, String albumTitle, String albumArt, Long artistID) {
        this.albumID = albumID;
        this.albumTitle = albumTitle;
        this.albumArt = albumArt;
        this.artistID = artistID;
    }
}