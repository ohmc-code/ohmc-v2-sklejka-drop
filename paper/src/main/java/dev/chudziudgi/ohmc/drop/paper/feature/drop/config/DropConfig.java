package dev.chudziudgi.ohmc.drop.paper.feature.drop.config;

import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Comment;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

public class DropConfig extends OkaeriConfig {

    @Comment({
            "# Bloki z których leci drop.",
            "# Każdy przedmiot z listy 'drops' może wypaść z dowolnego z tych bloków."
    })
    public List<Material> blocks = new ArrayList<>(List.of(
            Material.STONE,
            Material.DEEPSLATE,
            Material.DIAMOND_ORE,
            Material.DEEPSLATE_DIAMOND_ORE,
            Material.EMERALD_ORE,
            Material.DEEPSLATE_EMERALD_ORE,
            Material.GOLD_ORE,
            Material.DEEPSLATE_GOLD_ORE,
            Material.IRON_ORE,
            Material.DEEPSLATE_IRON_ORE,
            Material.NETHERRACK
    ));

    @Comment("# Czy usunąć naturalny drop z tych bloków (żeby leciał tylko custom drop).")
    public boolean removeNaturalDrops = true;

    @Comment("# Ile dodatkowej szansy na drop (w %) daje każdy poziom Fortuny.")
    public double fortuneBonusPerLevel = 5.0;

    @Comment({
            "# Lista przedmiotów które mogą wypaść. Każdy losowany jest niezależnie przy kopaniu bloku z 'blocks'.",
            "# dropItem     - przedmiot który wypada",
            "# minAmount    - minimalna losowa ilość",
            "# maxAmount    - maksymalna losowa ilość",
            "# chance       - szansa na wypadnięcie (0-100)",
            "# minExp       - minimalna losowa ilość expa",
            "# maxExp       - maksymalna losowa ilość expa",
            "# displayName  - nazwa wyświetlana w menu /drop oraz w wiadomości",
            "# lore         - opis w menu /drop",
            "# itemModel    - custom model data / item model (opcjonalne)"
    })
    public List<DropData> drops = new ArrayList<>(List.of(
            new DropData(
                    Material.DIAMOND,
                    1,
                    2,
                    5.0,
                    2,
                    5,
                    "<aqua>Diament",
                    List.of("<gray>Szansa: <yellow>5%", "<gray>Ilość: <yellow>1-2"),
                    List.of()
            ),
            new DropData(
                    Material.EMERALD,
                    1,
                    3,
                    10.0,
                    1,
                    3,
                    "<green>Szmaragd",
                    List.of("<gray>Szansa: <yellow>10%", "<gray>Ilość: <yellow>1-3"),
                    List.of()
            ),
            new DropData(
                    Material.GOLD_NUGGET,
                    2,
                    6,
                    25.0,
                    0,
                    2,
                    "<gold>Bryłka złota",
                    List.of("<gray>Szansa: <yellow>25%", "<gray>Ilość: <yellow>2-6"),
                    List.of()
            )
    ));
}
