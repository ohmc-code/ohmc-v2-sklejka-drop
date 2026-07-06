package dev.chudziudgi.ohmc.drop.paper.feature.drop;

import dev.chudziudgi.ohmc.drop.paper.feature.drop.inventory.DropInventory;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.permission.Permission;
import org.bukkit.entity.Player;

@Command(name = "drop")
@Permission("ohmc.drop")
public class DropCommand {

    private final DropInventory inventory;

    public DropCommand(DropInventory inventory) {
        this.inventory = inventory;
    }

    @Execute
    public void open(@Context Player player) {
        this.inventory.open(player);
    }
}
