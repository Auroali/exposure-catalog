package com.auroali.exposurecatalog.datagen;

import com.auroali.exposurecatalog.common.catalog.CatalogEntry;
import com.auroali.exposurecatalog.datagen.builders.CatalogEntryBuilder;
import com.auroali.exposurecatalog.datagen.providers.ExposureCatalogProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;

import java.util.function.Consumer;

public class ECCatalogProvider extends ExposureCatalogProvider {
    public ECCatalogProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    protected void generate(Consumer<CatalogEntry> consumer) {
        for (EntityType<?> type : BuiltInRegistries.ENTITY_TYPE)
            CatalogEntryBuilder.builder(type)
              .offerTo(consumer);
    }
}
