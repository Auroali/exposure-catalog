package com.auroali.exposurecatalog.client.screens;

import com.auroali.exposurecatalog.ExposureCatalog;
import com.auroali.exposurecatalog.client.widgets.CatalogWidget;
import com.auroali.exposurecatalog.common.catalog.CatalogEntry;
import com.auroali.exposurecatalog.common.components.CatalogTrackerComponent;
import com.auroali.exposurecatalog.common.components.ECEntityComponents;
import com.auroali.exposurecatalog.common.registry.ECRegistries;
import net.minecraft.client.GameNarrator;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.Nullable;

public class CatalogScreen extends Screen {
    public static final ResourceLocation TEXTURES = ExposureCatalog.id("textures/gui/catalog.png");

    private int ticks;
    private double scrollOffset;
    private int rows;

    public CatalogScreen() {
        super(GameNarrator.NO_TITLE);
    }

    @Override
    protected void init() {
        super.init();
        int entryIndex = 0;
        int widgetX = (this.width - 256) / 2 + 9;
        int widgetY = (this.height - 153) / 2 + 18;
        CatalogTrackerComponent catalog = ECEntityComponents.CATALOG_TRACKER.get(this.minecraft.player);
        while (entryIndex < 50) {
            for (CatalogEntry entry : ECRegistries.CATALOG.getEntries()) {
                boolean unlocked = catalog.hasCataloguedEntity(entry.entity());
                int x = (entryIndex % 5) * 32;
                int y = (entryIndex / 5) * 32;
                this.addRenderableWidget(CatalogWidget.fromEntry(entry, widgetX + x, widgetY + y, unlocked));
                entryIndex++;
            }
        }
        this.rows = entryIndex / 5;
        this.updateWidgetOffsets();
    }

    @Override
    public void tick() {
        super.tick();
        for (GuiEventListener widget : this.children()) {
            if (widget instanceof CatalogWidget catalogWidget)
                catalogWidget.tick();
        }
        this.ticks++;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int i, int j, float delta) {
        this.renderBackground(guiGraphics);
        super.render(guiGraphics, i, j, delta);
        int x = (this.width - 256) / 2;
        int y = (this.height - 153) / 2;
        guiGraphics.blit(TEXTURES, x + 175, y + 18 + (int) (this.getScrollPercent() * 113), 0, 186, 12, 15);
        if (this.getFocused() instanceof CatalogWidget catalogWidget && catalogWidget.isUnlocked()) {
            guiGraphics.enableScissor(x + 193, y + 18, x + 248, y + 73);
            catalogWidget.renderCatalogEntity(guiGraphics, x + 193, y + 18, 55, 55, 55.f, catalogWidget.getRotation(this.ticks + delta));
            guiGraphics.disableScissor();
            guiGraphics.drawWordWrap(
              this.minecraft.font,
              catalogWidget.getDescription(),
              x + 193,
              y + 77,
              55,
              -1
            );
        }
    }

    private double getScrollPercent() {
        return this.scrollOffset / this.rows;
    }

    @Override
    public void renderBackground(GuiGraphics guiGraphics) {
        super.renderBackground(guiGraphics);
        int x = (this.width - 256) / 2;
        int y = (this.height - 153) / 2;
        guiGraphics.blit(TEXTURES, x, y, 0, 0, 256, 153);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        if (!this.canScroll())
            return super.mouseScrolled(mouseX, mouseY, delta);

        this.scrollOffset = Mth.clamp(this.scrollOffset - delta, 0, this.rows);
        this.updateWidgetOffsets();
        return true;
    }

    public boolean canScroll() {
        return this.rows > 4;
    }

    public void updateWidgetOffsets() {
        int y = (this.height - 153) / 2;
        for (GuiEventListener widget : this.children()) {
            if (widget instanceof CatalogWidget catalogWidget) {
                catalogWidget.setY(catalogWidget.getInitialY() - 32 * (int) this.scrollOffset);
                catalogWidget.visible = catalogWidget.getY() >= y + 18 && catalogWidget.getY() < y + 145;
            }
        }
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        int x = (this.width - 256) / 2;
        int y = (this.height - 153) / 2;
        if (!this.canScroll() || mouseX < x + 175 || mouseX > x + 186 || mouseY < y + 18 || mouseY > y + 145)
            return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);

        this.scrollOffset = Mth.clamp((mouseY - (y + 18)) / 127.d * this.rows, 0, this.rows);
        this.updateWidgetOffsets();
        return true;
    }

    @Override
    public void setFocused(@Nullable GuiEventListener focused) {
        super.setFocused(focused);
        if (focused instanceof CatalogWidget catalogWidget) {
            int i = this.children().indexOf(catalogWidget);
            int row = i / 5;
            if (!this.isRowVisible(row)) {
                this.scrollOffset = row;
                this.updateWidgetOffsets();
            }
        }
    }

    private boolean isRowVisible(int row) {
        int currentRow = (int) this.scrollOffset;
        return row <= currentRow + 3 && row >= currentRow;
    }
}
