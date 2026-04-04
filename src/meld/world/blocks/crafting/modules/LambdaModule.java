package meld.world.blocks.crafting.modules;

import arc.func.Cons;
import meld.world.blocks.crafting.ModularCrafter;

public class LambdaModule extends ModularCrafter.CrafterModule {
    public Cons<ModularCrafter.ModularCrafterBuild> updater;

    @Override
    public void update(ModularCrafter.ModularCrafterBuild build) {
        updater.get(build);
    }

}
