package dev.chudziudgi.ohmc.drop.paper.feature.drop.database;

import com.eternalcode.commons.scheduler.Scheduler;
import dev.chudziudgi.ohmc.drop.paper.database.AbstractRepositoryOrmLite;
import dev.chudziudgi.ohmc.drop.paper.database.DatabaseManager;
import org.bukkit.Material;

import java.util.EnumSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class DropRepository extends AbstractRepositoryOrmLite {

    public DropRepository(DatabaseManager databaseManager, Scheduler scheduler) {
        super(databaseManager, scheduler);
    }

    public CompletableFuture<Set<Material>> getDisabled(UUID playerUuid) {
        return this.selectSafe(DropTable.class, playerUuid)
                .thenApply(optional -> optional
                        .map(table -> deserialize(table.disabledBlocks()))
                        .orElseGet(() -> EnumSet.noneOf(Material.class)));
    }

    public CompletableFuture<?> setDisabled(UUID playerUuid, Set<Material> disabled) {
        return this.save(DropTable.class, new DropTable(playerUuid, serialize(disabled)));
    }

    private static Set<Material> deserialize(String raw) {
        Set<Material> materials = EnumSet.noneOf(Material.class);
        if (raw == null || raw.isBlank()) {
            return materials;
        }
        for (String name : raw.split(",")) {
            Material material = Material.matchMaterial(name.trim());
            if (material != null) {
                materials.add(material);
            }
        }
        return materials;
    }

    private static String serialize(Set<Material> materials) {
        return materials.stream()
                .map(Material::name)
                .collect(Collectors.joining(","));
    }
}
