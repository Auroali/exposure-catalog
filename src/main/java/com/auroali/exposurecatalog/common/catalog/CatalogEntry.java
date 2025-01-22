package com.auroali.exposurecatalog.common.catalog;

import com.auroali.exposurecatalog.ExposureCatalog;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mojang.serialization.JsonOps;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.locale.Language;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.phys.Vec3;

public record CatalogEntry(EntityType<?> entity, Vec3 guiOffset, Vec3 guiScale, Component text, CompoundTag tag) {

    public static CatalogEntry fromJson(EntityType<?> entity, JsonObject object) {
        if (!object.has("description"))
            throw new JsonParseException("Missing description field");

        Component text = Component.Serializer.fromJson(object.get("description"));
        double guiOffsetX = 0.d;
        double guiOffsetY = 0.d;
        double guiOffsetZ = 0.d;
        double guiScaleX = 1.d;
        double guiScaleY = 1.d;
        double guiScaleZ = 1.d;
        CompoundTag tag = null;


        if (object.has("guiOffset")) {
            JsonArray guiOffset = object.getAsJsonArray("guiOffset");
            guiOffsetX = guiOffset.get(0).getAsDouble();
            guiOffsetY = guiOffset.get(1).getAsDouble();
            guiOffsetZ = guiOffset.get(2).getAsDouble();
        }

        if (object.has("guiScale")) {
            JsonArray guiScale = object.getAsJsonArray("guiScale");
            guiScaleX = guiScale.get(0).getAsDouble();
            guiScaleY = guiScale.get(1).getAsDouble();
            guiScaleZ = guiScale.get(2).getAsDouble();
        }

        if (object.has("nbt")) {
            tag = CompoundTag.CODEC.parse(JsonOps.INSTANCE, object.get("nbt"))
              .resultOrPartial(ExposureCatalog.LOGGER::error)
              .orElse(null);
        }

        // warn if a description is missing a translation key in dev
        if (
          FabricLoader.getInstance().isDevelopmentEnvironment()
            && text != null
            && text.getContents() instanceof TranslatableContents contents
        ) {
            if (!Language.getInstance().has(contents.getKey()))
                ExposureCatalog.LOGGER.warn("Untranslated description key {} for catalog entry {}", contents.getKey(), BuiltInRegistries.ENTITY_TYPE.getKey(entity));
        }

        return new CatalogEntry(
          entity,
          new Vec3(guiOffsetX, guiOffsetY, guiOffsetZ),
          new Vec3(guiScaleX, guiScaleY, guiScaleZ),
          text,
          tag
        );
    }

    public void toJson(JsonObject object) {
        JsonArray guiOffset = new JsonArray(3);
        guiOffset.add(this.guiOffset.x());
        guiOffset.add(this.guiOffset.y());
        guiOffset.add(this.guiOffset.z());
        JsonArray guiScale = new JsonArray(3);
        guiScale.add(this.guiScale.x());
        guiScale.add(this.guiScale.y());
        guiScale.add(this.guiScale.z());

        object.add("guiScale", guiScale);
        object.add("guiOffset", guiOffset);
        object.add("description", Component.Serializer.toJsonTree(this.text));

        if (this.tag != null) {
            CompoundTag.CODEC.encodeStart(JsonOps.INSTANCE, this.tag)
              .resultOrPartial(ExposureCatalog.LOGGER::error)
              .ifPresent(element -> object.add("nbt", element));
        }
    }
}
