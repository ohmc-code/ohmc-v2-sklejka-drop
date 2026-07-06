package dev.chudziudgi.ohmc.drop.paper.feature.drop.database;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.UUID;

@DatabaseTable(tableName = "ohmc_drop_disabled")
public class DropTable {

    @DatabaseField(columnName = "player_uuid", id = true, canBeNull = false)
    private UUID playerUuid;

    @DatabaseField(columnName = "disabled_blocks", dataType = DataType.LONG_STRING)
    private String disabledBlocks;

    public DropTable() {
    }

    public DropTable(UUID playerUuid, String disabledBlocks) {
        this.playerUuid = playerUuid;
        this.disabledBlocks = disabledBlocks;
    }

    public UUID playerUuid() {
        return this.playerUuid;
    }

    public String disabledBlocks() {
        return this.disabledBlocks;
    }

    public void setDisabledBlocks(String disabledBlocks) {
        this.disabledBlocks = disabledBlocks;
    }
}
