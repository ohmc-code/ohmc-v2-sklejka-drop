package dev.chudziudgi.ohmc.drop.paper.multification;

import static dev.chudziudgi.ohmc.drop.paper.DropPlugin.MINI_MESSAGE;

import com.eternalcode.multification.adventure.AudienceConverter;
import com.eternalcode.multification.bukkit.BukkitMultification;
import com.eternalcode.multification.shared.Replacer;
import com.eternalcode.multification.translation.TranslationProvider;
import dev.chudziudgi.ohmc.drop.paper.config.impl.PluginMessages;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.ComponentSerializer;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class LobbyMultification extends BukkitMultification<PluginMessages> {

    private final PluginMessages configuration;

    public LobbyMultification(PluginMessages configuration) {
        this.configuration = configuration;
    }

    @Override
    protected @NotNull TranslationProvider<PluginMessages> translationProvider() {
        return locale -> this.configuration;
    }

    @Override
    protected @NotNull ComponentSerializer<Component, Component, String> serializer() {
        return MINI_MESSAGE;
    }

    @Override
    public @NotNull Replacer<CommandSender> globalReplacer() {
        return (viewer, text) -> {
            return text;
        };
    }

    @Override
    protected @NotNull AudienceConverter<CommandSender> audienceConverter() {
        return commandSender -> commandSender;
    }
}
