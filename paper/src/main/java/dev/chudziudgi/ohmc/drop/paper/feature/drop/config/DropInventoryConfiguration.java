package dev.chudziudgi.ohmc.drop.paper.feature.drop.config;

import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Comment;
import org.bukkit.Material;

import java.util.List;

public class DropInventoryConfiguration extends OkaeriConfig {

    @Comment("# Tytuł menu /drop")
    public String title = "<gradient:#00ffff:#0088ff>⛏ Twoje dropy</gradient>";

    @Comment({
            "# Struktura menu /drop",
            "# x - sloty na przedmioty do przełączania (drop wł/wył)",
            "# # - item tapety (wypełni puste miejsca)",
            "# < - przycisk poprzedniej strony",
            "# > - przycisk następnej strony",
            "# c - przycisk zamknięcia"
    })
    public List<String> structure = List.of(
            "#########",
            "#xxxxxxx#",
            "#xxxxxxx#",
            "#xxxxxxx#",
            "#xxxxxxx#",
            "#<##c##>#"
    );

    @Comment("# Wygląd przedmiotu gdy drop jest WŁĄCZONY. Placeholdery: {ITEM}, {CHANCE}, {MIN}, {MAX}")
    public ToggleItem dropEnabled = new ToggleItem(
            "<green>✔ {ITEM}",
            List.of(
                    "<gray>Szansa: <yellow>{CHANCE}%",
                    "<gray>Ilość: <yellow>{MIN}-{MAX}",
                    "",
                    "<green>Drop jest WŁĄCZONY",
                    "<gray>Kliknij aby <red>wyłączyć</red> drop"
            ),
            List.of()
    );

    @Comment("# Wygląd przedmiotu gdy drop jest WYŁĄCZONY. Placeholdery: {ITEM}, {CHANCE}, {MIN}, {MAX}")
    public ToggleItem dropDisabled = new ToggleItem(
            "<red>✘ {ITEM}",
            List.of(
                    "<gray>Szansa: <yellow>{CHANCE}%",
                    "<gray>Ilość: <yellow>{MIN}-{MAX}",
                    "",
                    "<red>Drop jest WYŁĄCZONY",
                    "<gray>Kliknij aby <green>włączyć</green> drop"
            ),
            List.of()
    );

    @Comment("# Item tapety (wypełnia puste sloty)")
    public CustomItem wallpaper = new CustomItem(
            Material.BLACK_STAINED_GLASS_PANE,
            " ",
            List.of(),
            List.of()
    );

    @Comment("# Przycisk zamknięcia menu")
    public CustomItem close = new CustomItem(
            Material.BARRIER,
            "<red>✘ Zamknij",
            List.of(),
            List.of()
    );

    @Comment("# Przycisk poprzedniej strony")
    public CustomItem previousPage = new CustomItem(
            Material.ARROW,
            "<gray>◀ Poprzednia strona",
            List.of("<gray>Strona <yellow>{CURRENT}/{MAX}"),
            List.of()
    );

    @Comment("# Przycisk następnej strony")
    public CustomItem nextPage = new CustomItem(
            Material.ARROW,
            "<gray>▶ Następna strona",
            List.of("<gray>Strona <yellow>{CURRENT}/{MAX}"),
            List.of()
    );

    public static class ToggleItem extends OkaeriConfig {
        public String name;
        public List<String> lore;
        public List<String> itemModel;

        public ToggleItem() {
        }

        public ToggleItem(String name, List<String> lore, List<String> itemModel) {
            this.name = name;
            this.lore = lore;
            this.itemModel = itemModel;
        }
    }

    public static class CustomItem extends OkaeriConfig {
        public Material material;
        public String name;
        public List<String> lore;
        public List<String> itemModel;

        public CustomItem() {
        }

        public CustomItem(Material material, String name, List<String> lore, List<String> itemModel) {
            this.material = material;
            this.name = name;
            this.lore = lore;
            this.itemModel = itemModel;
        }
    }
}
