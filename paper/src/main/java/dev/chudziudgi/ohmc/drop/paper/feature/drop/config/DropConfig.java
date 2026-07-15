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
            Material.DEEPSLATE
    ));

    @Comment("# Czy usunąć naturalny drop z tych bloków (żeby leciał tylko custom drop).")
    public boolean removeNaturalDrops = true;

    @Comment({
            "# Całkowicie wyłącz drop z JAKICHKOLWIEK rud w Minecraft.",
            "# Gdy true - kopanie dowolnej rudy (diamentowej, złota, żelaza, itd.) nie daje NIC:",
            "# ani przedmiotu, ani expa, ani custom dropu."
    })
    public boolean disableAllOreDrops = true;

    @Comment({
            "# Przedmioty sterowane przełącznikiem ON/OFF w rogu menu /drop.",
            "# Domyślnie cobblestone i cobbled deepslate - jeden przycisk włącza/wyłącza oba naraz.",
            "# Te przedmioty nie pojawiają się jako osobne kafelki w siatce menu."
    })
    public List<Material> stoneToggleItems = new ArrayList<>(List.of(
            Material.COBBLESTONE,
            Material.COBBLED_DEEPSLATE
    ));

    @Comment("# Ile dodatkowej szansy na drop (w %) daje każdy poziom Fortuny.")
    public double fortuneBonusPerLevel = 5.0;

    @Comment({
            "# Lista przedmiotów które mogą wypaść. Każdy losowany jest niezależnie przy kopaniu bloku z 'blocks'.",
            "# sourceBlock  - (opcjonalne) blok z którego leci ten drop. Brak = leci z każdego bloku z 'blocks'.",
            "#                Ustaw np. STONE aby cobblestone leciał tylko z kamienia.",
            "# dropItem     - przedmiot który wypada",
            "# minAmount    - minimalna losowa ilość",
            "# maxAmount    - maksymalna losowa ilość",
            "# chance       - szansa na wypadnięcie (0-100)",
            "# minExp       - minimalna losowa ilość expa",
            "# maxExp       - maksymalna losowa ilość expa",
            "# displayName  - nazwa wyświetlana w menu /drop oraz w wiadomości",
            "# lore         - opis w menu /drop",
            "# itemModel    - custom model data / item model (opcjonalne)",
            "#",
            "# Każdy przedmiot z tej listy można wyłączyć indywidualnie w menu /drop -",
            "# wyłączony przedmiot nie leci danemu graczowi (dotyczy też cobblestone i cobbled deepslate)."
    })
    public List<DropData> drops = new ArrayList<>(List.of(
            new DropData(
                    Material.STONE,
                    Material.COBBLESTONE,
                    1,
                    1,
                    100.0,
                    0,
                    0,
                    "<gray>Bruk",
                    List.of("<gray>Leci z: <white>kamienia", "<gray>Sterowany przyciskiem ON/OFF w menu"),
                    List.of()
            ),
            new DropData(
                    Material.DEEPSLATE,
                    Material.COBBLED_DEEPSLATE,
                    1,
                    1,
                    100.0,
                    0,
                    0,
                    "<dark_gray>Bruk z głębi",
                    List.of("<gray>Leci z: <white>deepslate", "<gray>Sterowany przyciskiem ON/OFF w menu"),
                    List.of()
            ),
            new DropData(
                    Material.GOLD_NUGGET,
                    1,
                    2,
                    5.0,
                    0,
                    1,
                    "<gold>Odłamek złota",
                    List.of("<gray>Szansa: <yellow>5%", "<gray>Ilość: <yellow>1-2"),
                    List.of()
            ),
            new DropData(
                    Material.IRON_NUGGET,
                    1,
                    2,
                    3.0,
                    0,
                    1,
                    "<white>Srebro",
                    List.of("<gray>Szansa: <yellow>3%", "<gray>Ilość: <yellow>1-2"),
                    List.of()
            )
    ));
}
