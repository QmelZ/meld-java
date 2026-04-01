package meld.world.blocks.crafting.recipe;

import meld.world.blocks.Gauze;
import mindustry.type.ItemStack;

public class SpoolRecipe extends Recipe<Gauze, Gauze.GauzeBuild> {

    public SpoolRecipe(ItemStack ammo, float energy){
        this.ammo = ammo;
        this.energy = energy;
    }

    public ItemStack ammo;
    public float energy;

    @Override
    public boolean valid(Gauze block, Gauze.GauzeBuild build) {
        return build.items.has(ammo.item, ammo.amount) && build.energy + energy <= block.spoolStorage;
    }

    @Override
    public void apply(Gauze block, Gauze.GauzeBuild build) {
        build.removeStack(ammo.item, ammo.amount);
        build.energy += energy;
    }
}
