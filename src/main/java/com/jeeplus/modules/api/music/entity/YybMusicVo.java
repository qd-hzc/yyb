package com.jeeplus.modules.api.music.entity;

import java.io.Serializable;
import java.util.List;

public class YybMusicVo implements Serializable {

    private String multiCondition;

    private String tagName;

    private String musicTimeBegin;

    private String musicTimeEnd;

    private String musicianName;

    private String publishTimeBegin;

    private String publishTimeEnd;

    private Integer startPage;

    private Integer pageSize;

    private List<String> tagNames;

    public String getMultiCondition() {
        return multiCondition;
    }

    public void setMultiCondition(String multiCondition) {
        this.multiCondition = multiCondition;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public String getMusicTimeBegin() {
        return musicTimeBegin;
    }

    public void setMusicTimeBegin(String musicTimeBegin) {
        this.musicTimeBegin = musicTimeBegin;
    }

    public String getMusicTimeEnd() {
        return musicTimeEnd;
    }

    public void setMusicTimeEnd(String musicTimeEnd) {
        this.musicTimeEnd = musicTimeEnd;
    }

    public String getMusicianName() {
        return musicianName;
    }

    public void setMusicianName(String musicianName) {
        this.musicianName = musicianName;
    }

    public String getPublishTimeBegin() {
        return publishTimeBegin;
    }

    public void setPublishTimeBegin(String publishTimeBegin) {
        this.publishTimeBegin = publishTimeBegin;
    }

    public String getPublishTimeEnd() {
        return publishTimeEnd;
    }

    public void setPublishTimeEnd(String publishTimeEnd) {
        this.publishTimeEnd = publishTimeEnd;
    }

    public Integer getStartPage() {
        return startPage;
    }

    public void setStartPage(Integer startPage) {
        this.startPage = startPage;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public List<String> getTagNames() {
        return tagNames;
    }

    public void setTagNames(List<String> tagNames) {
        this.tagNames = tagNames;
    }
}