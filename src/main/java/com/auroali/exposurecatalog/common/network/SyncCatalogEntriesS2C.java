package com.auroali.exposurecatalog.common.network;

import com.auroali.exposurecatalog.ExposureCatalog;
import com.auroali.exposurecatalog.common.catalog.CatalogEntry;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.Collection;

public record SyncCatalogEntriesS2C(Collection<CatalogEntry> entries) implements FabricPacket {
    public static final PacketType<SyncCatalogEntriesS2C> ID = PacketType.create(ExposureCatalog.id("sync_catalog_entries"), SyncCatalogEntriesS2C::new);

    public SyncCatalogEntriesS2C(FriendlyByteBuf buf) {
        this(buf.<CatalogEntry, Collection<CatalogEntry>>readCollection(ArrayList::new, buffer -> {
            EntityType<?> type = buffer.readById(BuiltInRegistries.ENTITY_TYPE);
            double guiOffsetX = buffer.readDouble();
            double guiOffsetY = buffer.readDouble();
            double guiOffsetZ = buffer.readDouble();
            double guiScaleX = buffer.readDouble();
            double guiScaleY = buffer.readDouble();
            double guiScaleZ = buffer.readDouble();
            Component text = buffer.readComponent();
            return new CatalogEntry(
              type,
              new Vec3(guiOffsetX, guiOffsetY, guiOffsetZ),
              new Vec3(guiScaleX, guiScaleY, guiScaleZ),
              text
            );
        }));
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeCollection(this.entries(), (buffer, entry) -> {
            buffer.writeId(BuiltInRegistries.ENTITY_TYPE, entry.entity());
            buffer.writeDouble(entry.guiOffset().x());
            buffer.writeDouble(entry.guiOffset().y());
            buffer.writeDouble(entry.guiOffset().z());
            buffer.writeDouble(entry.guiScale().x());
            buffer.writeDouble(entry.guiScale().y());
            buffer.writeDouble(entry.guiScale().z());
            buffer.writeComponent(entry.text());
        });
    }

    @Override
    public PacketType<?> getType() {
        return ID;
    }
}
