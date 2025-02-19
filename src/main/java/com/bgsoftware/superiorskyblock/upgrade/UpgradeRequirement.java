package com.bgsoftware.superiorskyblock.upgrade;

public final class UpgradeRequirement {

    private final String placeholder;
    private final String errorMessage;

    public UpgradeRequirement(String placeholder, String errorMessage) {
        this.placeholder = placeholder;
        this.errorMessage = errorMessage;
    }

    public String getPlaceholder() {
        return placeholder;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
