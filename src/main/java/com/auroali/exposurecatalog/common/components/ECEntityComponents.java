package com.auroali.exposurecatalog.common.components;

import com.auroali.exposurecatalog.ExposureCatalog;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import dev.onyxstudios.cca.api.v3.entity.RespawnCopyStrategy;

public class ECEntityComponents implements EntityComponentInitializer {
    public static final ComponentKey<CatalogTrackerComponent> CATALOG_TRACKER = ComponentRegistry.getOrCreate(ExposureCatalog.id("catalog"), CatalogTrackerComponent.class);

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry entityComponentFactoryRegistry) {
        entityComponentFactoryRegistry.registerForPlayers(CATALOG_TRACKER, CatalogTrackerComponent::new, RespawnCopyStrategy.ALWAYS_COPY);
    }
}
