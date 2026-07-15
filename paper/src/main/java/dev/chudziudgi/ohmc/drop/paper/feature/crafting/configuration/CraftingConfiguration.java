package dev.chudziudgi.ohmc.drop.paper.feature.crafting.configuration;

import dev.chudziudgi.ohmc.drop.paper.feature.crafting.CustomRecipe;
import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Comment;

import java.util.ArrayList;
import java.util.List;

public class CraftingConfiguration extends OkaeriConfig {

    @Comment("# Lista custom receptur")
    public List<CustomRecipe> recipes = new ArrayList<>();
}
