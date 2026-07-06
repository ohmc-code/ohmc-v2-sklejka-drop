package dev.chudziudgi.ohmc.drop.paper.reload;

import com.eternalcode.multification.notice.Notice;
import dev.chudziudgi.ohmc.drop.paper.config.impl.PluginMessages;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.permission.Permission;

@Command(name = "drop reload")
@Permission("ohmc.drop.reload")
public class ReloadCommand {

    private final ReloadManager reloadManager;
    private final PluginMessages messages;

    public ReloadCommand(ReloadManager reloadManager, PluginMessages messages) {
        this.reloadManager = reloadManager;
        this.messages = messages;
    }

    @Execute
    public Notice reload() {
        this.reloadManager.reloadAll();
        return this.messages.reloadSuccess;
    }
}

