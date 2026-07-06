package dev.chudziudgi.ohmc.drop.paper.config;

import com.eternalcode.commons.bukkit.position.Position;
import eu.okaeri.configs.schema.GenericsPair;
import eu.okaeri.configs.serdes.BidirectionalTransformer;
import eu.okaeri.configs.serdes.SerdesContext;
import org.checkerframework.checker.nullness.qual.NonNull;

public class PositionTransformer extends BidirectionalTransformer<Position, String> {

    @Override
    public GenericsPair<Position, String> getPair() {
        return this.genericsPair(Position.class, String.class);
    }

    @Override
    public String leftToRight(@NonNull Position position, @NonNull SerdesContext context) {
        return position.toString();
    }

    @Override
    public Position rightToLeft(@NonNull String data, @NonNull SerdesContext context) {
        return Position.parse(data);
    }
}
