package com.nextjstemplate.service.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Response DTO for competition participant eligibility checks.
 */
public class CompetitionEligibilityCheckDTO implements Serializable {

    private boolean eligible;

    private List<String> reasons = new ArrayList<>();

    public boolean isEligible() {
        return eligible;
    }

    public void setEligible(boolean eligible) {
        this.eligible = eligible;
    }

    public List<String> getReasons() {
        return reasons;
    }

    public void setReasons(List<String> reasons) {
        this.reasons = reasons;
    }

    public void addReason(String reason) {
        this.reasons.add(reason);
    }
}
