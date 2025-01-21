package com.auroali.exposurecatalog.common.registry;

import com.auroali.exposurecatalog.common.catalog.CatalogEntry;
import net.minecraft.world.entity.EntityType;

import java.util.*;

public class CatalogRegistry {
    private Map<EntityType<?>, CatalogEntry> entries = Collections.emptyMap();

    public Collection<CatalogEntry> getEntries() {
        return this.entries.values();
    }

    public Optional<CatalogEntry> getFor(EntityType<?> type) {
        CatalogEntry entry = this.entries.get(type);
        return entry == null ? Optional.empty() : Optional.of(entry);
    }

    public void load(Collection<CatalogEntry> from) {
        this.entries = new HashMap<>(from.size());
        for (CatalogEntry entry : from) {
            this.entries.put(entry.entity(), entry);
        }
    }
}
