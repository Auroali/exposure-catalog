package com.auroali.exposurecatalog.datagen.builders;

import com.auroali.exposurecatalog.common.catalog.CatalogEntry;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.phys.Vec3;

import java.util.function.Consumer;

public class CatalogEntryBuilder {
    final EntityType<?> type;
    Vec3 guiOffset;
    Vec3 guiScale;
    Component description;

    private CatalogEntryBuilder(EntityType<?> type, Vec3 guiOffset, Vec3 guiScale, Component description) {
        this.type = type;
        this.guiOffset = guiOffset;
        this.guiScale = guiScale;
        this.description = description;
    }

    public static CatalogEntryBuilder builder(EntityType<?> type) {
        return new CatalogEntryBuilder(type, Vec3.ZERO, new Vec3(1.d, 1.d, 1.d), Component.empty());
    }

    public CatalogEntryBuilder offset(Vec3 offset) {
        this.guiOffset = offset;
        return this;
    }

    public CatalogEntryBuilder scale(Vec3 scale) {
        this.guiScale = scale;
        return this;
    }

    public CatalogEntryBuilder offset(double x, double y, double z) {
        return this.offset(new Vec3(x, y, z));
    }

    public CatalogEntryBuilder scale(double x, double y, double z) {
        return this.scale(new Vec3(x, y, z));
    }

    public CatalogEntryBuilder description(Component text) {
        this.description = text;
        return this;
    }

    public CatalogEntryBuilder description(String text) {
        return this.description(Component.literal(text));
    }

    public CatalogEntryBuilder translatableDescription(String key) {
        return this.description(Component.translatable(key));
    }

    public CatalogEntry build() {
        return new CatalogEntry(this.type, this.guiOffset, this.guiScale, this.description);
    }

    public void offerTo(Consumer<CatalogEntry> consumer) {
        consumer.accept(this.build());
    }
}
