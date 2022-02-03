package me.dreamerzero.chatregulator.listener.command;

import java.util.function.Predicate;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.event.Continuation;
import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.command.CommandExecuteEvent;
import com.velocitypowered.api.proxy.Player;

import org.jetbrains.annotations.ApiStatus.Internal;

import me.dreamerzero.chatregulator.ChatRegulator;
import me.dreamerzero.chatregulator.config.Configuration;
import me.dreamerzero.chatregulator.config.MainConfig;
import me.dreamerzero.chatregulator.enums.Permissions;
import me.dreamerzero.chatregulator.modules.CommandSpy;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.placeholder.Placeholder;
import net.kyori.adventure.text.minimessage.placeholder.PlaceholderResolver;

@Internal
public class SpyListener {
    private final MiniMessage mm;

    public SpyListener(){
        this.mm = MiniMessage.miniMessage();
    }

    @Subscribe(order = PostOrder.LAST)
    public void onCommand(final CommandExecuteEvent event, final Continuation continuation){
        final CommandSource source = event.getCommandSource();
        final MainConfig.CommandSpy config = Configuration.getConfig().getCommandSpyConfig();
        if(!event.getResult().isAllowed()
            || !(source instanceof Player)
            || !config.enabled()
            || source.hasPermission(Permissions.BYPASS_COMMANDSPY)){
            continuation.resume();
            return;
        }
        final String command = event.getCommand();

        if(CommandSpy.shouldAnnounce(source, command, config)){
            ChatRegulator.getInstance().getProxy().getAllPlayers().stream()
                .filter(PERMISSION_PREDICATE)
                .forEach(p -> p.sendMessage(
                    mm.deserialize(
                        Configuration.getMessages().getCommandSpyMessages().getMessage(),
                        PlaceholderResolver.placeholders(
                            Placeholder.raw("command", command),
                            Placeholder.raw("player", ((Player)source).getUsername())
                        )
                    )
                ));
        }
        continuation.resume();
    }

    private static final Predicate<CommandSource> PERMISSION_PREDICATE = s -> s.hasPermission(Permissions.COMMANDSPY_ALERT);
}
