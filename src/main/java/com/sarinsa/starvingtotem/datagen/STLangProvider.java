package com.sarinsa.starvingtotem.datagen;

import com.sarinsa.starvingtotem.common.core.StarvingTotem;
import com.sarinsa.starvingtotem.common.core.registry.STEffects;
import com.sarinsa.starvingtotem.common.core.registry.STEntities;
import com.sarinsa.starvingtotem.common.core.registry.STItems;
import com.sarinsa.starvingtotem.common.util.References;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.LanguageProvider;

public class STLangProvider extends LanguageProvider {

    public STLangProvider(DataGenerator gen) {
        super(gen, StarvingTotem.MODID, "en_us");
    }

    @Override
    protected void addTranslations() {
        this.addItem(STItems.FAMILY_ALTAR, "Family Altar");

        this.addEntityType(STEntities.FAMILY_ALTAR, "Family Altar");

        this.addEffect(STEffects.BITTER_CURSE, "Bitter Curse");
        this.addEffect(STEffects.SWEET_BLESSING, "Sweet Blessing");

        this.add(References.ALTAR_INTERACT_TEXT, "The Altar prefers sweeter offerings...");
    }
}
