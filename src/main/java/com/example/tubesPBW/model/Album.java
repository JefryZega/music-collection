package com.example.tubesPBW.model;

import lombok.Data;

@Data
public class Album {
    private Long albumID;
    private String albumTitle;
    private String albumArt;
    private Long artistID;
    private String artistName; 
    private String artistProfile;
    
    public Album() {}
    
    public Album(Long albumID, String albumTitle, String albumArt, Long artistID, String artistName, String artistProfile) {
        this.albumID = albumID;
        this.albumTitle = albumTitle;
        this.albumArt = albumArt;
        this.artistID = artistID;
        this.artistName = artistName;
        this.artistProfile = artistProfile;
    }

    public String getAlbumArtUrl() {
        if (albumArt == null || albumArt.trim().isEmpty()) {
            return "/assets/img/album-art/default.jpg";
        }
        return albumArt.startsWith("/") ? albumArt : "/" + albumArt;
    }

    public String getArtistProfileUrl() {
        if (artistProfile == null || artistProfile.trim().isEmpty()) {
            return "/assets/img/artist-profile/default.jpg";
        }
        return artistProfile.startsWith("/") ? artistProfile : "/" + artistProfile;
    }
}