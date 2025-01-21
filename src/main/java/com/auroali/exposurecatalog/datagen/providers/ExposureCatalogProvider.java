package com.auroali.exposurecatalog.datagen.providers;

import com.auroali.exposurecatalog.common.catalog.CatalogEntry;
import com.auroali.exposurecatalog.common.catalog.CatalogEntryReloader;
import com.google.gson.JsonObject;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public abstract class ExposureCatalogProvider implements DataProvider {
    protected final FabricDataOutput output;
    private final PackOutput.PathProvider pathResolver;

    protected ExposureCatalogProvider(FabricDataOutput output) {
        this.output = output;
        this.pathResolver = output.createPathProvider(PackOutput.Target.DATA_PACK, CatalogEntryReloader.FOLDER);
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cachedOutput) {
        List<CatalogEntry> entries = new ArrayList<>();
        Set<ResourceLocation> ids = new HashSet<>();
        this.generate(entries::add);
        List<CompletableFuture<?>> futures = new ArrayList<>();
        for (CatalogEntry entry : entries) {
            ResourceLocation id = BuiltInRegistries.ENTITY_TYPE.getKey(entry.entity());
            if (!ids.add(id))
                throw new IllegalStateException("Duplicate catalog entry " + id);

            JsonObject object = new JsonObject();
            entry.toJson(object);
            futures.add(DataProvider.saveStable(cachedOutput, object, this.pathResolver.json(id)));
        }
        return CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new));
    }

    protected abstract void generate(Consumer<CatalogEntry> consumer);

    @Override
    public String getName() {
        return "Exposure Catalog Provider";
    }
}
