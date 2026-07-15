package dev.chudziudgi.ohmc.drop.paper.feature.crafting.configuration;

import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Comment;
import org.bukkit.Material;

import java.util.Arrays;
import java.util.List;

public class CraftingInventoryConfiguration extends OkaeriConfig {

    @Comment("# Tytuł głównego menu z recepturami")
    public String title = "<gradient:#00ff87:#60efff>Craftingi</gradient>";

    @Comment("# Struktura głównego menu")
    public List<String> structure = Arrays.asList(
            "# # # # # # # # #",
            "# x x x x x x x #",
            "# x x x x x x x #",
            "# x x x x x x x #",
            "# x x x x x x x #",
            "# < # # c # # > #"
    );

    @Comment("# Konfiguracja tła")
    public WallpaperConfig wallpaper = new WallpaperConfig();

    @Comment("# Konfiguracja przycisków nawigacji")
    public NavigationItems navigation = new NavigationItems();

    public RecipeButtonConfig recipeButtonConfig = new RecipeButtonConfig();

    @Comment("# Konfiguracja menu craftingu")
    public CraftingMenuConfig craftingMenu = new CraftingMenuConfig();

    public static class WallpaperConfig extends OkaeriConfig {
        public Material material = Material.GRAY_STAINED_GLASS_PANE;
        public String name = " ";
    }

    public static class NavigationItems extends OkaeriConfig {

        @Comment("# Czy ukrywać przycisk gdy niemożliwy (np. previous na pierwszej stronie)")
        public boolean hideWhenImpossible = false;
        @Comment("# Item pokazywany gdy przycisk jest niedostępny (jeśli hideWhenImpossible = false)")
        public CustomItem previousPageDisabled = new CustomItem(
                Material.GRAY_STAINED_GLASS_PANE,
                "<dark_gray>◀ Brak poprzedniej strony",
                List.of("<dark_gray>Jesteś na pierwszej stronie"),
                List.of()
        );

        public CustomItem previousPage = new CustomItem(
                Material.ARROW,
                "<gray>◀ Poprzednia strona",
                List.of("<gray>Strona <yellow>{CURRENT}/{MAX}"),
                List.of()
        );

        public CustomItem nextPageDisabled = new CustomItem(
                Material.GRAY_STAINED_GLASS_PANE,
                "<dark_gray>▶ Brak następnej strony",
                List.of("<dark_gray>Jesteś na ostatniej stronie"),
                List.of()
        );

        public CustomItem nextPage = new CustomItem(
                Material.ARROW,
                "<gray>▶ Następna strona",
                List.of("<gray>Strona <yellow>{CURRENT}/{MAX}"),
                List.of()
        );

        public CustomItem back = new CustomItem(
                Material.ARROW,
                "<yellow>← Powrót",
                List.of(
                        "",
                        "<gray>Kliknij aby wrócić do listy"),
                List.of()
        );

        public CustomItem close = new CustomItem(
                Material.BARRIER,
                "<red><bold>Zamknij",
                List.of(
                        "",
                        "<gray>Kliknij aby zamknąć menu"),
                List.of()
        );
    }

    public static class CraftingMenuConfig extends OkaeriConfig {
        @Comment("# Tytuł menu craftingu")
        public String title = "<gradient:#00ff87:#60efff>Crafting: {RECIPE_NAME}</gradient>";

        @Comment("# Struktura menu craftingu")
        public List<String> structure = Arrays.asList(
                "# # # # # # # # #",
                "# i i i # # # # #",
                "# i i i # # r # #",
                "# i i i # # # # #",
                "# # # # # # # # #",
                "# # # # b # # # #"
        );

        @Comment("# Konfiguracja tła")
        public WallpaperConfig wallpaper = new WallpaperConfig();

        public List<String> resultSlot = Arrays.asList(
                "",
                "<green>Kliknij aby scraftować!"
        );
    }

    public static class RecipeButtonConfig extends OkaeriConfig {
        public String name = "<yellow>{name}";
        public List<String> lore = Arrays.asList(
                "",
                "<gray>Kliknij aby otworzyć podgląd craftingu"
        );
    }

    public static class CustomItem extends OkaeriConfig {
        public Material material;
        public String name;
        public List<String> lore;
        public List<String> itemModel;

        public CustomItem() {}

        public CustomItem(Material material, String name, List<String> lore, List<String> itemModel) {
            this.material = material;
            this.name = name;
            this.lore = lore;
            this.itemModel = itemModel;
        }
    }
}
