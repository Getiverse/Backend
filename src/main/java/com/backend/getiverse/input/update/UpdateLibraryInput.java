package com.backend.getiverse.input.update;

import lombok.Data;

@Data
public class UpdateLibraryInput {
    private Boolean isPrivate;
    private String title;
    private String description;
    private String id;

    public Boolean getIsPrivate() {
        return isPrivate;
    }

    public Boolean setIsPrivate(Boolean isPrivate) {
        return this.isPrivate = isPrivate;
    }
}
