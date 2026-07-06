package dev.chudziudgi.ohmc.drop.paper.litecommands;

import com.eternalcode.multification.notice.Notice;
import dev.chudziudgi.ohmc.drop.paper.multification.LobbyMultification;
import dev.rollczi.litecommands.handler.result.ResultHandler;
import dev.rollczi.litecommands.handler.result.ResultHandlerChain;
import dev.rollczi.litecommands.invocation.Invocation;
import org.bukkit.command.CommandSender;

public class NoticeHandler implements ResultHandler<CommandSender, Notice> {

    private final LobbyMultification multification;

    public NoticeHandler(
            LobbyMultification multification
    ) {
        this.multification = multification;
    }

    @Override
    public void handle(Invocation<CommandSender> invocation, Notice result, ResultHandlerChain<CommandSender> chain) {
        this.multification.create()
                .viewer(invocation.sender())
                .notice(result)
                .send();
    }
}
