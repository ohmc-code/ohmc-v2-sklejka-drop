package dev.chudziudgi.ohmc.drop.paper.litecommands;

import dev.chudziudgi.ohmc.drop.paper.multification.LobbyMultification;
import dev.rollczi.litecommands.handler.result.ResultHandlerChain;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.permission.MissingPermissions;
import dev.rollczi.litecommands.permission.MissingPermissionsHandler;
import org.bukkit.command.CommandSender;

public class PermissionHandler implements MissingPermissionsHandler<CommandSender> {

    private final LobbyMultification multification;

    public PermissionHandler(LobbyMultification multification) {
        this.multification = multification;
    }

    @Override
    public void handle(
            Invocation<CommandSender> invocation,
            MissingPermissions missingPermissions,
            ResultHandlerChain<CommandSender> chain
    ) {
        this.multification.create()
                .viewer(invocation.sender())
                .notice(notice -> notice.noPermission)
                .placeholder("{PERMISSIONS}", missingPermissions.asJoinedText())
                .send();
    }
}
