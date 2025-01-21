package com.auroali.exposurecatalog.common.catalog;

import com.auroali.exposurecatalog.ExposureCatalog;
import com.auroali.exposurecatalog.common.registry.ECRegistries;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.EntityType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class CatalogEntryReloader implements IdentifiableResourceReloadListener {
    public static final String FOLDER = "catalog_entries";
    private static final Gson GSON = new Gson();
    private static final FileToIdConverter FINDER = new FileToIdConverter(FOLDER, ".json");
    private static final ResourceLocation ID = ExposureCatalog.id(FOLDER);

    public CatalogEntryReloader() {
    }

    @Override
    public ResourceLocation getFabricId() {
        return ID;
    }

    @Override
    public CompletableFuture<Void> reload(PreparationBarrier preparationBarrier, ResourceManager resourceManager, ProfilerFiller profilerFiller, ProfilerFiller profilerFiller2, Executor prepareExecutor, Executor applyExecutor) {
        return CompletableFuture.supplyAsync(() -> FINDER.listMatchingResources(resourceManager), prepareExecutor)
          .thenApply(resources -> {
              HashMap<ResourceLocation, JsonObject> map = new HashMap<>(resources.size());
              for (Map.Entry<ResourceLocation, Resource> entry : resources.entrySet()) {
                  ResourceLocation id = FINDER.fileToId(entry.getKey());
                  try {
                      JsonObject object = GSON.fromJson(entry.getValue().openAsReader(), JsonObject.class);
                      map.put(id, object);
                  } catch (JsonParseException | IOException e) {
                      ExposureCatalog.LOGGER.error("Could not parse json for catalog entry {}", id, e);
                  }
              }
              return map;
          })
          .thenCompose(preparationBarrier::wait)
          .thenAcceptAsync(map -> {
              List<CatalogEntry> catalog = new ArrayList<>(map.size());
              map.forEach((id, json) -> {
                  EntityType<?> type = BuiltInRegistries.ENTITY_TYPE.get(id);
                  try {
                      CatalogEntry entry = CatalogEntry.fromJson(type, json);
                      catalog.add(entry);
                  } catch (JsonParseException e) {
                      ExposureCatalog.LOGGER.error("Failed to load catalog entry {}", id, e);
                  }
              });
              ECRegistries.CATALOG.load(catalog);
          });
    }
}
