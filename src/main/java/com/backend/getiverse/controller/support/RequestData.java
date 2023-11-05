package com.backend.getiverse.controller.support;

import com.backend.getiverse.model.Page;
import com.backend.getiverse.model.PostType;
import lombok.Data;

@Data
public class RequestData {
    private String name;
    private Page page;
    private SearchType searchType;
}
