package com.auroali.exposurecatalog.client.screens;

import com.auroali.exposurecatalog.common.catalog.CatalogEntry;
import com.auroali.exposurecatalog.common.components.CatalogTrackerComponent;
import com.auroali.exposurecatalog.common.components.ECEntityComponents;
import com.google.common.base.Predicates;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.Locale;
import java.util.function.Predicate;

public class CatalogQuery {
    public static Predicate<CatalogEntry> checkName(String query) {
        if (query == null || query.isEmpty())
            return Predicates.alwaysTrue();

        return entry -> {
            CatalogTrackerComponent component = ECEntityComponents.CATALOG_TRACKER.get(Minecraft.getInstance().player);
            if (!component.hasCataloguedEntity(entry.entity()))
                return false;

            String translationKey = entry.entity().getDescriptionId();
            Component name = Component.translatable(translationKey);
            return name.getString().toLowerCase(Locale.ROOT).contains(query.toLowerCase(Locale.ROOT));
        };
    }
}
