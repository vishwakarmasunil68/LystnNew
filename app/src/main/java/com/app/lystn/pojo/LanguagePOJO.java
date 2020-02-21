package com.app.lystn.pojo;

public class LanguagePOJO {
    int drawable_id;
    String name;
    boolean selected;
    String language_code;

    public LanguagePOJO(int drawable_id, String name, boolean selected,String language_code) {
        this.drawable_id = drawable_id;
        this.name = name;
        this.selected = selected;
        this.language_code=language_code;
    }

    public int getDrawable_id() {
        return drawable_id;
    }

    public void setDrawable_id(int drawable_id) {
        this.drawable_id = drawable_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getLanguage_code() {
        return language_code;
    }

    public void setLanguage_code(String language_code) {
        this.language_code = language_code;
    }
}
