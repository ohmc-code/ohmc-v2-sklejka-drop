package dev.chudziudgi.ohmc.drop.paper.feature.crafting.command;

import dev.chudziudgi.ohmc.drop.paper.feature.crafting.inventory.RecipeCraftingInventory;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import org.bukkit.entity.Player;

@Command(name = "craftingi")
public class CraftingCommand {

    private final RecipeCraftingInventory recipeCraftingInventory;

    public CraftingCommand(
            RecipeCraftingInventory recipeCraftingInventory
    ) {
        this.recipeCraftingInventory = recipeCraftingInventory;
    }

    @Execute
    public void execute(@Context Player player) {
        this.recipeCraftingInventory.open(player);
    }

}
