package com.backend.getiverse.model.support;

public class CheckFile {
    private final String[] imageFilesType = {"JPEG","JPG","PNG"};

    public boolean checkValidity(String src){
        String[] subString = src.split("\\.",2);
        String extension = subString[1];
        if(extension.equalsIgnoreCase(imageFilesType[0])
            || extension.equalsIgnoreCase(imageFilesType[1]) || extension.equalsIgnoreCase(imageFilesType[2])){
            return  true;
        }else{
            return false;
        }
    }
}
