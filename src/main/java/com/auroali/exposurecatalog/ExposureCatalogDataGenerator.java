package com.auroali.exposurecatalog;

import com.auroali.exposurecatalog.datagen.ECCatalogProvider;
import com.auroali.exposurecatalog.datagen.ECLangProvider;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class ExposureCatalogDataGenerator implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
        pack.addProvider(ECCatalogProvider::new);
        pack.addProvider(ECLangProvider::new);
    }
}
