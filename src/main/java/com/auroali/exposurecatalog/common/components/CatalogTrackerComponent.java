package com.auroali.exposurecatalog.common.components;

import com.auroali.exposurecatalog.ExposureCatalog;
import dev.onyxstudios.cca.api.v3.component.Component;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class CatalogTrackerComponent implements Component, AutoSyncedComponent, Iterable<EntityType<?>> {
    private final Player holder;
    private Set<EntityType<?>> cataloguedEntities;

    public CatalogTrackerComponent(Player holder) {
        this.holder = holder;
        this.cataloguedEntities = new HashSet<>();
    }

    public boolean hasCataloguedEntity(EntityType<?> type) {
        return this.cataloguedEntities.contains(type);
    }

    public void addEntityToCatalog(EntityType<?> type) {
        this.cataloguedEntities.add(type);
    }

    @Override
    public void readFromNbt(CompoundTag compoundTag) {
        ListTag cataloguedEntitiesTag = compoundTag.getList("CataloguedEntities", Tag.TAG_STRING);
        Set<EntityType<?>> cataloguedEntities = new HashSet<>();
        for (int i = 0; i < cataloguedEntitiesTag.size(); i++) {
            ResourceLocation id = ResourceLocation.tryParse(cataloguedEntitiesTag.getString(i));
            if (id == null)
                ExposureCatalog.LOGGER.warn("Could not parse id {}", cataloguedEntitiesTag.getString(i));

            BuiltInRegistries.ENTITY_TYPE.getOptional(id)
              .ifPresentOrElse(
                type -> {
                    if (!cataloguedEntities.add(type))
                        ExposureCatalog.LOGGER.warn("Duplicated catalogued entity {}", id);
                },
                () -> ExposureCatalog.LOGGER.error("Unknown catalogued entity {}", id)
              );
        }
        this.cataloguedEntities = cataloguedEntities;
    }

    @Override
    public void writeToNbt(CompoundTag compoundTag) {
        ListTag cataloguedEntities = new ListTag();
        for (EntityType<?> entity : this.cataloguedEntities) {
            ResourceLocation location = BuiltInRegistries.ENTITY_TYPE.getKey(entity);
            cataloguedEntities.add(StringTag.valueOf(location.toString()));
        }
        compoundTag.put("CataloguedEntities", cataloguedEntities);
    }

    @Override
    public boolean shouldSyncWith(ServerPlayer player) {
        return player == this.holder;
    }

    @Override
    public void writeSyncPacket(FriendlyByteBuf buf, ServerPlayer recipient) {
        buf.writeCollection(this.cataloguedEntities, (buffer, type) -> buffer.writeId(BuiltInRegistries.ENTITY_TYPE, type));
    }

    @Override
    public void applySyncPacket(FriendlyByteBuf buf) {
        this.cataloguedEntities = buf.readCollection(HashSet::new, buffer -> buffer.readById(BuiltInRegistries.ENTITY_TYPE));
    }

    public boolean removeCataloguedEntity(EntityType<?> type) {
        return this.cataloguedEntities.remove(type);
    }

    @Override
    public @NotNull Iterator<EntityType<?>> iterator() {
        return this.cataloguedEntities.iterator();
    }

    public Collection<EntityType<?>> getCataloguedEntities() {
        return this.cataloguedEntities;
    }
}
