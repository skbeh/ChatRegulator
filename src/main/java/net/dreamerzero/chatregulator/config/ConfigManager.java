package net.dreamerzero.chatregulator.config;

import com.velocitypowered.api.proxy.Player;

import net.dreamerzero.chatregulator.Regulator;
import net.dreamerzero.chatregulator.utils.PlaceholderUtils;
import net.dreamerzero.chatregulator.utils.TypeUtils;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.title.Title;

public class ConfigManager {
    /**
     * Get the warning format according to the configuration
     * @param infraction the type of infraction
     * @return the format of the warning
     */
    public static TypeUtils.WarningType getWarningType(TypeUtils.InfractionType infraction){
        String type;

        switch(infraction){
            case REGULAR: type = Regulator.getConfig().getString("infractions.warning-type"); break;
            case FLOOD: type = Regulator.getConfig().getString("flood.warning-type"); break;
            default: return TypeUtils.WarningType.MESSAGE;
        }

        if(type.contains("title")){
            return TypeUtils.WarningType.TITLE;
        } else if(type.contains("actionbar")){
            return TypeUtils.WarningType.ACTIONBAR;
        } else if(type.contains("message")){
            return TypeUtils.WarningType.MESSAGE;
        } else {
            return TypeUtils.WarningType.MESSAGE;
        }
    }

    /**
     * Send a message of some kind to the offender.
     * @param infractor offender
     * @param type the type of infraction
     */
    public static void sendWarningMessage(Audience infractor, TypeUtils.InfractionType type){
        String message;
        if(type.equals(TypeUtils.InfractionType.FLOOD)){
            message = Regulator.getConfig().getString("flood.warning-message");
        } else {
            message = Regulator.getConfig().getString("infractions.warning-message");
        }

        switch(getWarningType(type)){
            case TITLE:
                if(!message.contains(";")){
                    infractor.showTitle(
                    Title.title(
                        Component.empty(),
                        MiniMessage.miniMessage().parse(
                            message)));
                } else {
                    String titleParts[] = message.split(";");
                    infractor.showTitle(
                        Title.title(
                            MiniMessage.miniMessage().parse(
                                titleParts[0]),
                            MiniMessage.miniMessage().parse(
                                titleParts[1])));
                }
                break;
            case MESSAGE: infractor.sendMessage(MiniMessage.miniMessage().parse(message)); break;
            case ACTIONBAR: infractor.sendActionBar(MiniMessage.miniMessage().parse(message)); break;
        }
    }

    /**
     * Sends an alert message to users who are in the audience with the required permissions
     * @param staff audience that has the required permission to receive the alert
     * @param infractor the player who committed the infraction
     * @param type the type of infraction
     */
    public static void sendAlertMessage(Audience staff, Player infractor, TypeUtils.InfractionType type){
        String message;
        switch(type){
            case FLOOD: message = Regulator.getConfig().getString("flood.alert-message"); break;
            case REGULAR: message = Regulator.getConfig().getString("infractions.alert-message"); break;
            default: message = null;
        }

        staff.sendMessage(
            MiniMessage.miniMessage().parse(
                message,
                PlaceholderUtils.getTemplates(infractor)));
    }
}
