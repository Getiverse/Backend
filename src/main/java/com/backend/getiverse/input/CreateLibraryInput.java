package com.backend.getiverse.input;

import lombok.Data;

@Data
public class CreateLibraryInput {
    private Boolean isPrivate;
    private String title;
    private String description;
    public Boolean getIsPrivate(){
        return isPrivate;
    }
    public Boolean setIsPrivate(Boolean isPrivate){
        return this.isPrivate = isPrivate;
    }
}
