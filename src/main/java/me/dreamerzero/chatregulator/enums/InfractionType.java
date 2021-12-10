package me.dreamerzero.chatregulator.enums;

import me.dreamerzero.chatregulator.config.Configuration;
import me.dreamerzero.chatregulator.config.MainConfig;
import me.dreamerzero.chatregulator.config.Messages;

/**
 * Infraction Types
 */
public enum InfractionType {
    /**
     * Represents a regular violation, i.e.
     * detection based on the blacklist.yml file.
     */
    REGULAR("chatregulator.bypass.infractions", Configuration.getConfig().getInfractionsConfig(), Configuration.getMessages().getInfractionsMessages()),
    /**
     * Represents an infraction for repeating
     * the same character several times in a row.
     */
    FLOOD("chatregulator.bypass.flood", Configuration.getConfig().getFloodConfig(), Configuration.getMessages().getFloodMessages()),
    /**
     * Represents an infraction for repeating
     * the same word or command several times.
     */
    SPAM("chatregulator.bypass.spam", Configuration.getConfig().getSpamConfig(), Configuration.getMessages().getSpamMessages()),
    /**
     * Represents a blocked command
     */
    BCOMMAND("chatregulator.bypass.command", Configuration.getConfig().getCommandBlacklistConfig(), Configuration.getMessages().getBlacklistMessages()),
    /**
     * Represents a Unicode check
     */
    UNICODE("chatregulator.bypass.unicode", Configuration.getConfig().getUnicodeConfig(), Configuration.getMessages().getUnicodeMessages()),
    /**
     * Represents a Caps limit check
     */
    CAPS("chatregulator.bypass.caps", Configuration.getConfig().getCapsConfig(), Configuration.getMessages().getCapsMessages()),
    /**
     * Used internally to represent a
     * multiple warning and in other cases more
     */
    NONE;

    private String bypassPermission;
    private MainConfig.Toggleable config;
    private Messages.Warning messages;

    InfractionType(String permission, MainConfig.Toggleable config, Messages.Warning messages){
        this.bypassPermission = permission;
        this.config = config;
        this.messages = messages;
    }

    InfractionType(){
        this.bypassPermission = "";
    }

    public String bypassPermission(){
        return this.bypassPermission;
    }

    public MainConfig.Toggleable getConfig(){
        return this.config;
    }

    public Messages.Warning getMessages(){
        return this.messages;
    }
}