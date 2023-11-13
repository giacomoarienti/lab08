package it.unibo;

import java.util.Date;
import java.util.Objects;
import java.util.Map;
import java.util.HashMap;

import it.unibo.deathnote.api.DeathNote;

public class DeathNoteImpl implements DeathNote {

    private String currentName;
    private final Map<String, DeathInfo> book = new HashMap<>();

    /**
     * {@inheritDoc}
     */
    @Override
    public String getRule(int ruleNumber) {
        if(ruleNumber < 1 || ruleNumber > RULES.size()) {
            throw new IllegalArgumentException("Invalid ruleNumber");
        }
        // return rule
        return RULES.get(ruleNumber - 1);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void writeName(String name) {
        if(Objects.isNull(name)) {
            throw new NullPointerException("Parameter name cannot be null");
        }
        // save name and time
        this.currentName = name;
        // save name in book
        this.book.put(name, new DeathInfo());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean writeDeathCause(String cause) {
        if(Objects.isNull(this.currentName)) {
            throw new IllegalStateException("Name is null");
        }
        if(Objects.isNull(cause)) {
            throw new IllegalStateException("Parameter cause cannot be null");
        }
        // save cause
        final DeathInfo info = this.book.get(this.currentName);
        info.setCause(cause);
        // check if less then 40 ms have passed
        return !info.isCauseTimedOut();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean writeDetails(String details) {
        if(Objects.isNull(currentName)) {
            throw new IllegalStateException("Name is null");
        }
        if(Objects.isNull(details)) {
            throw new IllegalStateException("Parameter details cannot be null");
        }
        final DeathInfo info = this.book.get(this.currentName);
        // save details
        info.setDetails(details);
        return !info.isDetailTimedOut();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String getDeathCause(String name) {
        if(Objects.isNull(name) || !this.book.containsKey(name)) {
            throw new IllegalArgumentException("DeathNote does not cotains name: " + name);
        }
        return this.book.get(name).getCause();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDeathDetails(String name) {
        if(Objects.isNull(name) || !this.book.containsKey(name)) {
            throw new IllegalArgumentException("DeathNote does not cotains name: " + name);
        }
        return this.book.get(name).getDetails();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isNameWritten(String name) {
        return this.book.containsKey(name);
    }

    private class DeathInfo {
        private static final int NAME_TIMEOUT = 40;
        private static final int CAUSE_TIMEOUT = 6 * 1000 + 40;

        private String cause = "heart attack";
        private String details;
        private Date nameWrittenWhen;

        public DeathInfo() {
            this.nameWrittenWhen = new Date();
        }

        public String getCause() {
            return this.cause;
        }

        public String getDetails() {
            return this.details;
        }

        public boolean isCauseTimedOut() {
            return this.nameWrittenWhen.getTime() + NAME_TIMEOUT < new Date().getTime();
        }

        public boolean isDetailTimedOut() {
            return this.nameWrittenWhen.getTime() + CAUSE_TIMEOUT < new Date().getTime();
        }

        public void setCause(final String cause) {
            if(!this.isCauseTimedOut()) {
                this.cause = cause;
            }
        }

        public void setDetails(final String details) {
            if(!this.isDetailTimedOut()) {
                this.details = details;
            }
        }
    }

}
