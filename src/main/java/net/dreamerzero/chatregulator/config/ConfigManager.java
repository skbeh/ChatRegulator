package net.dreamerzero.chatregulator.config;

import java.util.ArrayList;
import java.util.List;

import de.leonhard.storage.Yaml;
import net.dreamerzero.chatregulator.InfractionPlayer;
import net.dreamerzero.chatregulator.modules.checks.FloodCheck;
import net.dreamerzero.chatregulator.modules.checks.InfractionCheck;
import net.dreamerzero.chatregulator.utils.PlaceholderUtils;
import net.dreamerzero.chatregulator.utils.TypeUtils;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.Template;
import net.kyori.adventure.title.Title;

/**
 * Utilities for using the configuration paths in an orderly manner
 */
public class ConfigManager {
    private Yaml messages;
	private Yaml config;
    private MiniMessage mm;
    /**
     * Constructor of the ConfigManager
     * @param config plugin config
     */
    public ConfigManager(Yaml messages, Yaml config){
        this.messages = messages;
		this.config = config;
        this.mm = MiniMessage.miniMessage();
    }
    /**
     * Get the warning format according to the configuration
     * @param infraction the type of infraction
     * @return the format of the warning
     */
    public TypeUtils.WarningType getWarningType(TypeUtils.InfractionType infraction){
        switch(infraction){
            case REGULAR: return TypeUtils.WarningType.valueOf(config.getString("infractions.warning-type").toUpperCase());
            case FLOOD: return TypeUtils.WarningType.valueOf(config.getString("flood.warning-type").toUpperCase());
            case SPAM: return TypeUtils.WarningType.valueOf(config.getString("spam.warning-type").toUpperCase());
            case BCOMMAND: return TypeUtils.WarningType.valueOf(config.getString("blocked-commands.warning-type").toUpperCase());
            case UNICODE: return TypeUtils.WarningType.valueOf(config.getString("unicode-blocker.warning-type").toUpperCase());
            case NONE: return TypeUtils.WarningType.MESSAGE;
            default: return TypeUtils.WarningType.MESSAGE;
        }
    }

    /**
     * Send a message of some kind to the offender.
     * @param infractor offender
     * @param type the type of infraction
     */
    public void sendWarningMessage(InfractionPlayer infractor, TypeUtils.InfractionType type){
        String message = messages.getString("spam.warning");
        Audience player = infractor.getPlayer().get();

        switch(getWarningType(type)){
            case TITLE:
                if(!message.contains(";")){
                    player.showTitle(
                    Title.title(
                        Component.empty(),
                        mm.parse(
                            message,
                            PlaceholderUtils.getTemplates(infractor))));
                } else {
                    String titleParts[] = message.split(";");
                    player.showTitle(
                        Title.title(
                            mm.parse(
                                titleParts[0],
                                PlaceholderUtils.getTemplates(infractor)),
                            mm.parse(
                                titleParts[1],
                                PlaceholderUtils.getTemplates(infractor))));
                }
                break;
            case MESSAGE: player.sendMessage(mm.parse(message, PlaceholderUtils.getTemplates(infractor))); break;
            case ACTIONBAR: player.sendActionBar(mm.parse(message, PlaceholderUtils.getTemplates(infractor))); break;
        }
    }

    /**
     * Send a message of some kind to the offender.
     * @param infractor offender
     * @param type the type of infraction
     * @param fUtils the flood check
     */
    public void sendWarningMessage(InfractionPlayer infractor, TypeUtils.InfractionType type, FloodCheck fUtils){
        String message = messages.getString("flood.warning");
        List<Template> template = new ArrayList<>();
        template.add(Template.of("infraction", fUtils.getInfractionWord()));
        template.addAll(PlaceholderUtils.getTemplates(infractor));

        Audience player = infractor.getPlayer().get();

        switch(getWarningType(type)){
            case TITLE:
                if(!message.contains(";")){
                    player.showTitle(
                    Title.title(
                        Component.empty(),
                        mm.parse(
                            message,
                            template)));
                } else {
                    String titleParts[] = message.split(";");
                    player.showTitle(
                        Title.title(
                            mm.parse(
                                titleParts[0],
                                template),
                            mm.parse(
                                titleParts[1],
                                template)));
                }
                break;
            case MESSAGE: player.sendMessage(mm.parse(message, template)); break;
            case ACTIONBAR: player.sendActionBar(mm.parse(message, template)); break;
        }
    }

    /**
     * Send a message of some kind to the offender.
     * @param infractor offender
     * @param type the type of infraction
     * @param iUtils the infractions check
     */
    public void sendWarningMessage(InfractionPlayer infractor, TypeUtils.InfractionType type, InfractionCheck iUtils){
        String message = messages.getString("infractions.warning");
        List<Template> template = new ArrayList<>();
        template.add(Template.of("infraction", iUtils.getInfractionWord()));
        template.addAll(PlaceholderUtils.getTemplates(infractor));

        Audience player = infractor.getPlayer().get();

        switch(getWarningType(type)){
            case TITLE:
                if(!message.contains(";")){
                    player.showTitle(
                    Title.title(
                        Component.empty(),
                        mm.parse(
                            message,
                            template)));
                } else {
                    String titleParts[] = message.split(";");
                    player.showTitle(
                        Title.title(
                            mm.parse(
                                titleParts[0],
                                template),
                            mm.parse(
                                titleParts[1],
                                template)));
                }
                break;
            case MESSAGE: player.sendMessage(mm.parse(message, template)); break;
            case ACTIONBAR: player.sendActionBar(mm.parse(message, template)); break;
        }
    }

    /**
     * Sends an alert message to users who are in the audience with the required permissions
     * @param staff audience that has the required permission to receive the alert
     * @param infractor the player who committed the infraction
     * @param type the type of infraction
     */
    public void sendAlertMessage(Audience staff, InfractionPlayer infractor, TypeUtils.InfractionType type){
        String message;
        switch(type){
            case FLOOD: message = messages.getString("flood.alert"); break;
            case REGULAR: message = messages.getString("infractions.alert"); break;
            case SPAM: message = messages.getString("spam.alert"); break;
            case BCOMMAND:  message = messages.getString("blocked-commands.alert"); break;
            case UNICODE: message = messages.getString("unicode-blocker.alert"); break;
            default: message = null;
        }

        staff.sendMessage(
            mm.parse(
                message,
                PlaceholderUtils.getTemplates(infractor)));
    }

    /**
     * Sends the message of a successful
     * warning reset to the command executor
     * @param sender command executor
     * @param type type of infraction
     * @param player the infraction player
     *               whose warnings have been reset
     */
    public void sendResetMessage(Audience sender, TypeUtils.InfractionType type, InfractionPlayer player){
        switch(type){
            case REGULAR: sender.sendMessage(mm.parse(messages.getString("infractions.reset"), PlaceholderUtils.getTemplates(player))); break;
            case FLOOD: sender.sendMessage(mm.parse(messages.getString("flood.reset"), PlaceholderUtils.getTemplates(player))); break;
            case SPAM: sender.sendMessage(mm.parse(messages.getString("spam.reset"), PlaceholderUtils.getTemplates(player))); break;
            case NONE: sender.sendMessage(mm.parse(messages.getString("general.all-reset"), PlaceholderUtils.getTemplates(player))); break;
            case BCOMMAND: sender.sendMessage(mm.parse(messages.getString("commands-blocked.reset"), PlaceholderUtils.getTemplates(player))); break;
            case UNICODE: sender.sendMessage(mm.parse(messages.getString("unicode-blocker.reset"), PlaceholderUtils.getTemplates(player))); break;
        }
    }
}
