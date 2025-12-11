// Artist.java
package com.example.tubesPBW.model;

import lombok.Data;

@Data
public class Artist {
    private Long artistID;
    private String artistName;
    private String artistProfile;
    
    public Artist() {}
    
    public Artist(Long artistID, String artistName, String artistProfile) {
        this.artistID = artistID;
        this.artistName = artistName;
        this.artistProfile = artistProfile;
    }
}