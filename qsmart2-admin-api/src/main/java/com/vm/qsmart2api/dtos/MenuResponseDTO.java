/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.vm.Q2.models.SubMenu;
import java.util.List;

/**
 *
 * @author Ashok
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class MenuResponseDTO {
      private String name;
    private String link;
    private String icon;
    private List<SubMenu> submenu;
    private String id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public List<SubMenu> getSubmenu() {
        return submenu;
    }

    public void setSubmenu(List<SubMenu> submenu) {
        this.submenu = submenu;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "MenugetDto{" + "name=" + name + ", link=" + link + ", icon=" + icon + ", submenu=" + submenu + ", id=" + id + '}';
    }
}
