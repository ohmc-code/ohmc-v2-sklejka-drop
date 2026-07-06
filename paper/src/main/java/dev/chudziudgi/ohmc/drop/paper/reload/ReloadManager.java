package dev.chudziudgi.ohmc.drop.paper.reload;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class ReloadManager {

    private final List<Reloadable> reloadables = new ArrayList<>();
    private final Logger logger;

    public ReloadManager(Logger logger) {
        this.logger = logger;
    }

    public void register(Reloadable reloadable) {
        this.reloadables.add(reloadable);
    }

    public void reloadAll() {
        for (Reloadable reloadable : this.reloadables) {
            try {
                reloadable.reload();
                this.logger.info("Reloaded: " + reloadable.getClass().getSimpleName());
            }
            catch (Exception exception) {
                this.logger.severe("Failed to reload " + reloadable.getClass().getSimpleName() + ": " + exception.getMessage());
            }
        }
    }
}