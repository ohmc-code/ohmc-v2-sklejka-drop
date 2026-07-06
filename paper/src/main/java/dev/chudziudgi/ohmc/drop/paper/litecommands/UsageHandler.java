package dev.chudziudgi.ohmc.drop.paper.litecommands;

import dev.chudziudgi.ohmc.drop.paper.multification.LobbyMultification;
import dev.rollczi.litecommands.handler.result.ResultHandlerChain;
import dev.rollczi.litecommands.invalidusage.InvalidUsage;
import dev.rollczi.litecommands.invalidusage.InvalidUsageHandler;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.schematic.Schematic;
import org.bukkit.command.CommandSender;

public class UsageHandler implements InvalidUsageHandler<CommandSender> {

    private final LobbyMultification multification;

    public UsageHandler(LobbyMultification multification) {
        this.multification = multification;
    }

    @Override
    public void handle(
            Invocation<CommandSender> invocation,
            InvalidUsage<CommandSender> result,
            ResultHandlerChain<CommandSender> chain
    ) {
        Schematic schematic = result.getSchematic();

        if (schematic.isOnlyFirst()) {
            this.multification.create()
                    .notice(notice -> notice.correctUsage)
                    .placeholder("{USAGE}", schematic.first())
                    .viewer(invocation.sender())
                    .send();

            return;
        }

        this.multification.create()
                .notice(notice -> notice.correctUsageHeader)
                .placeholder("{USAGE}", schematic.first())
                .viewer(invocation.sender())
                .send();

        for (String usage : schematic.all()) {
            this.multification.create()
                    .notice(notice -> notice.correctUsageEntry)
                    .placeholder("{USAGE}", usage)
                    .viewer(invocation.sender())
                    .send();
        }
    }
}
