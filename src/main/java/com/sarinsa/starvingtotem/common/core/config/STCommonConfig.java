package com.sarinsa.starvingtotem.common.core.config;

import net.minecraft.item.Items;
import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Arrays;
import java.util.List;

public class STCommonConfig {

    public static final Common COMMON;
    public static final ForgeConfigSpec COMMON_SPEC;

    static {
        Pair<Common, ForgeConfigSpec> commonPair = new ForgeConfigSpec.Builder().configure(Common::new);
        COMMON = commonPair.getLeft();
        COMMON_SPEC = commonPair.getRight();
    }

    @SuppressWarnings("all")
    public static final class Common {

        private static final List<? extends String> defaultCakes = Arrays.asList(
                Items.CAKE.getRegistryName().toString(),
                Items.PUMPKIN_PIE.getRegistryName().toString(),
                // oh man
                "farmersdelight:cake_slice",
                "farmersdelight:apple_pie",
                "farmersdelight:apple_pie_slice",
                "farmersdelight:chocolate_pie",
                "farmersdelight:chocolate_pie_slice",
                "farmersdelight:sweet_berry_cheesecake",
                "farmersdelight:sweet_berry_cheesecake_slice"
        );

        private final ForgeConfigSpec.ConfigValue<List<? extends String>> cakeList;
        private final ForgeConfigSpec.DoubleValue keepTotemChance;

        private Common(ForgeConfigSpec.Builder configBuilder) {
            configBuilder.push("general");

            this.cakeList = configBuilder.comment("A list of items accepted as cakes that can be offered to a Family Altar.")
                            .defineList("cake_list", defaultCakes, (o) -> o instanceof String);

            this.keepTotemChance = configBuilder.comment("The chance for keeping the used Totem of Undying upon death when the player " +
                    "has the Sweet Blessing effect. Value ranges from 0.0 (0% chance) to 1.0 (100% chance).")
                            .defineInRange("keep_totem_chance", 0.25D, 0.0D, 1.0D);


            configBuilder.pop();
        }

        public List<? extends String> getCakeConfig() {
            return this.cakeList.get();
        }

        public double getKeepTotemChance() {
            return this.keepTotemChance.get();
        }
    }
}
