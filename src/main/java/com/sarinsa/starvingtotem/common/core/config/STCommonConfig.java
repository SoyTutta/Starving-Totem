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

    public static final class Common {

        private static final List<? extends String> defaultCakes = Arrays.asList(
                Items.CAKE.getRegistryName().toString()
        );

        private final ForgeConfigSpec.ConfigValue<List<? extends String>> cakeList;

        private Common(ForgeConfigSpec.Builder configBuilder) {
            configBuilder.push("general");

            this.cakeList = configBuilder.comment("A list of items accepted as cakes that can be offered to a Family Altar.")
                            .defineList("cake_list", () -> defaultCakes, (o) -> o instanceof String);

            configBuilder.pop();
        }

        public List<? extends String> getCakeConfig() {
            return this.cakeList.get();
        }
    }
}
