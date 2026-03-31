package org.zzu.schoolimsystem.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;

@TableName("wll_event")
public class WllEvent {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String eventNo;
    private String title;
    private String content;
    private String unitCode;
    private Integer threatLevel;
    private Integer businessLevel;
    private Integer eventLevel;
    private Integer baseScore;
    private String creatorId;
    private String auditorId;
    private LocalDateTime creatorTime;
    private LocalDateTime updateTime;
    private Integer status;
    private String verifyName;
    private String verifyTel;
    private String cotent1;
    private String cotent2;
    private String content3;
    private String auditorNote;
    private String creatorIp;
    private String auditorIp;
    private LocalDateTime auditorTime;
    private String requireSkill;
    private Integer isValid;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEventNo() {
        return eventNo;
    }

    public void setEventNo(String eventNo) {
        this.eventNo = eventNo;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUnitCode() {
        return unitCode;
    }

    public void setUnitCode(String unitCode) {
        this.unitCode = unitCode;
    }

    public Integer getThreatLevel() {
        return threatLevel;
    }

    public void setThreatLevel(Integer threatLevel) {
        this.threatLevel = threatLevel;
    }

    public Integer getBusinessLevel() {
        return businessLevel;
    }

    public void setBusinessLevel(Integer businessLevel) {
        this.businessLevel = businessLevel;
    }

    public Integer getEventLevel() {
        return eventLevel;
    }

    public void setEventLevel(Integer eventLevel) {
        this.eventLevel = eventLevel;
    }

    public Integer getBaseScore() {
        return baseScore;
    }

    public void setBaseScore(Integer baseScore) {
        this.baseScore = baseScore;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public String getAuditorId() {
        return auditorId;
    }

    public void setAuditorId(String auditorId) {
        this.auditorId = auditorId;
    }

    public LocalDateTime getCreatorTime() {
        return creatorTime;
    }

    public void setCreatorTime(LocalDateTime creatorTime) {
        this.creatorTime = creatorTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getVerifyName() {
        return verifyName;
    }

    public void setVerifyName(String verifyName) {
        this.verifyName = verifyName;
    }

    public String getVerifyTel() {
        return verifyTel;
    }

    public void setVerifyTel(String verifyTel) {
        this.verifyTel = verifyTel;
    }

    public String getCotent1() {
        return cotent1;
    }

    public void setCotent1(String cotent1) {
        this.cotent1 = cotent1;
    }

    public String getCotent2() {
        return cotent2;
    }

    public void setCotent2(String cotent2) {
        this.cotent2 = cotent2;
    }

    public String getContent3() {
        return content3;
    }

    public void setContent3(String content3) {
        this.content3 = content3;
    }

    public String getAuditorNote() {
        return auditorNote;
    }

    public void setAuditorNote(String auditorNote) {
        this.auditorNote = auditorNote;
    }

    public String getCreatorIp() {
        return creatorIp;
    }

    public void setCreatorIp(String creatorIp) {
        this.creatorIp = creatorIp;
    }

    public String getAuditorIp() {
        return auditorIp;
    }

    public void setAuditorIp(String auditorIp) {
        this.auditorIp = auditorIp;
    }

    public LocalDateTime getAuditorTime() {
        return auditorTime;
    }

    public void setAuditorTime(LocalDateTime auditorTime) {
        this.auditorTime = auditorTime;
    }

    public String getRequireSkill() {
        return requireSkill;
    }

    public void setRequireSkill(String requireSkill) {
        this.requireSkill = requireSkill;
    }

    public Integer getIsValid() {
        return isValid;
    }

    public void setIsValid(Integer isValid) {
        this.isValid = isValid;
    }
}
