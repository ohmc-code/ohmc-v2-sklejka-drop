package dev.chudziudgi.ohmc.drop.paper.feature.crafting.command;

import dev.chudziudgi.ohmc.drop.paper.feature.crafting.CraftingService;
import dev.chudziudgi.ohmc.drop.paper.feature.crafting.CustomRecipe;
import dev.chudziudgi.ohmc.drop.paper.feature.crafting.inventory.CraftingCreatorInventory;
import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.permission.Permission;
import org.bukkit.entity.Player;

@Command(name = "craftingadmin")
@Permission("ohmc.command.craftingadmin")
public class CraftingAdminCommand {

    private final CraftingService craftingService;
    private final CraftingCreatorInventory craftingCreatorInventory;

    public CraftingAdminCommand(
            CraftingService craftingService,
            CraftingCreatorInventory craftingCreatorInventory
    ) {
        this.craftingService = craftingService;
        this.craftingCreatorInventory = craftingCreatorInventory;
    }

    @Execute(name = "create")
    public void create(@Context Player player, @Arg String name) {
        CustomRecipe existing = this.craftingService.findRecipe(name);
        if (existing != null) {
            this.craftingService.getMultification().create()
                    .viewer(player)
                    .notice(messages -> messages.recipeAlreadyExists)
                    .placeholder("{RECIPE_NAME}", name)
                    .send();
            return;
        }

        this.craftingCreatorInventory.open(player, name);
    }

    @Execute(name = "delete")
    public void delete(@Context Player player, @Arg String name) {
        boolean removed = this.craftingService.removeRecipe(name);

        if (removed) {
            this.craftingService.getMultification().create()
                    .viewer(player)
                    .notice(messages -> messages.recipeDeleted)
                    .placeholder("{RECIPE_NAME}", name)
                    .send();
        } else {
            this.craftingService.getMultification().create()
                    .viewer(player)
                    .notice(messages -> messages.recipeNotFound)
                    .placeholder("{RECIPE_NAME}", name)
                    .send();
        }
    }

    @Execute(name = "list")
    public void list(@Context Player player) {
        var recipes = this.craftingService.getConfiguration().recipes;

        if (recipes.isEmpty()) {
            this.craftingService.getMultification().create()
                    .viewer(player)
                    .notice(messages -> messages.recipeListEmpty)
                    .send();
            return;
        }

        this.craftingService.getMultification().create()
                .viewer(player)
                .notice(messages -> messages.recipeListHeader)
                .placeholder("{COUNT}", String.valueOf(recipes.size()))
                .send();

        for (CustomRecipe recipe : recipes) {
            this.craftingService.getMultification().create()
                    .viewer(player)
                    .notice(messages -> messages.recipeListEntry)
                    .placeholder("{RECIPE_NAME}", recipe.name())
                    .placeholder("{RESULT}", recipe.result().getType().name())
                    .send();
        }
    }
}
