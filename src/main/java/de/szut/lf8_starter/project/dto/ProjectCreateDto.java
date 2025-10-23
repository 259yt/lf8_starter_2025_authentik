package de.szut.lf8_starter.project.dto;

import jakarta.validation.constraints.NotBlank;

public class ProjectCreateDto {
    @NotBlank
    private String name;
    private String description;

    private String getName(){
        return name;
    }
    private void setName(String name){
        this.name = name;
    }
    private String getDescription(){

        return description;
    }

    private void setDescription(String description){
        this.description = description;
    }
}
