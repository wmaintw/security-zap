package com.thoughtworks.tools.security.zap.domain

class Alert {
    int id
    String alert
    Risk risk
    Reliability reliability
    String url
    String other
    String param
    String attack
    String evidence
    String description
    String reference
    String solution
    int cweId
    int wascId

    int getId() {
        return id
    }

    void setId(int id) {
        this.id = id
    }

    String getAlert() {
        return alert
    }

    void setAlert(String alert) {
        this.alert = alert
    }

    Risk getRisk() {
        return risk
    }

    void setRisk(Risk risk) {
        this.risk = risk
    }

    Reliability getReliability() {
        return reliability
    }

    void setReliability(Reliability reliability) {
        this.reliability = reliability
    }

    String getUrl() {
        return url
    }

    void setUrl(String url) {
        this.url = url
    }

    String getOther() {
        return other
    }

    void setOther(String other) {
        this.other = other
    }

    String getParam() {
        return param
    }

    void setParam(String param) {
        this.param = param
    }

    String getAttack() {
        return attack
    }

    void setAttack(String attack) {
        this.attack = attack
    }

    String getEvidence() {
        return evidence
    }

    void setEvidence(String evidence) {
        this.evidence = evidence
    }

    String getDescription() {
        return description
    }

    void setDescription(String description) {
        this.description = description
    }

    String getReference() {
        return reference
    }

    void setReference(String reference) {
        this.reference = reference
    }

    String getSolution() {
        return solution
    }

    void setSolution(String solution) {
        this.solution = solution
    }

    int getCweId() {
        return cweId
    }

    void setCweId(int cweId) {
        this.cweId = cweId
    }

    int getWascId() {
        return wascId
    }

    void setWascId(int wascId) {
        this.wascId = wascId
    }
}
