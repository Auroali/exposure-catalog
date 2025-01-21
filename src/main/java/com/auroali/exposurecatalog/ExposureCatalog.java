package com.auroali.exposurecatalog;

import com.auroali.exposurecatalog.common.catalog.CatalogEntry;
import com.auroali.exposurecatalog.common.catalog.CatalogEntryReloader;
import com.auroali.exposurecatalog.common.components.CatalogTrackerComponent;
import com.auroali.exposurecatalog.common.components.ECEntityComponents;
import com.auroali.exposurecatalog.common.network.CatalogToastS2C;
import com.auroali.exposurecatalog.common.network.SyncCatalogEntriesS2C;
import com.auroali.exposurecatalog.common.registry.ECRegistries;
import io.github.mortuusars.exposure.camera.infrastructure.FrameData;
import io.github.mortuusars.exposure.fabric.api.event.FrameAddedCallback;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.entity.EntityType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

public class ExposureCatalog implements ModInitializer {
    public static final String MODID = "exposurecatalog";

    // This logger is used to write text to the console and the log file.
    // It is considered best practice to use your mod id as the logger's name.
    // That way, it's clear which mod wrote info, warnings, and errors.
    public static final Logger LOGGER = LoggerFactory.getLogger(MODID);

    @Override
    public void onInitialize() {
        ResourceManagerHelper.get(PackType.SERVER_DATA)
          .registerReloadListener(new CatalogEntryReloader());

        ServerLifecycleEvents.SYNC_DATA_PACK_CONTENTS.register((player, joining) -> {
            Collection<CatalogEntry> entries = ECRegistries.CATALOG.getEntries();
            ServerPlayNetworking.send(player, new SyncCatalogEntriesS2C(entries));
        });

        FrameAddedCallback.EVENT.register((serverPlayer, itemStack, compoundTag) -> {
            if (!compoundTag.contains(FrameData.ENTITIES_IN_FRAME))
                return;

            CatalogTrackerComponent catalog = ECEntityComponents.CATALOG_TRACKER.get(serverPlayer);

            ListTag entitiesInFrame = compoundTag.getList(FrameData.ENTITIES_IN_FRAME, Tag.TAG_COMPOUND);
            int numCatalogued = 0;
            for (int i = 0; i < entitiesInFrame.size(); i++) {
                CompoundTag entity = entitiesInFrame.getCompound(i);
                ResourceLocation id = ResourceLocation.tryParse(entity.getString(FrameData.ENTITY_ID));
                EntityType<?> entityType = BuiltInRegistries.ENTITY_TYPE.get(id);
                if (!catalog.hasCataloguedEntity(entityType)) {
                    numCatalogued++;
                    catalog.addEntityToCatalog(entityType);
                }
            }
            
            if (numCatalogued > 0) {
                ServerPlayNetworking.send(serverPlayer, new CatalogToastS2C(numCatalogued));
                ECEntityComponents.CATALOG_TRACKER.sync(serverPlayer);
            }
        });
    }

    public static ResourceLocation id(String id) {
        return new ResourceLocation(MODID, id);
    }
}