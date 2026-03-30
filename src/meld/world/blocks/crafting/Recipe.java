package meld.world.blocks.crafting;

import mindustry.gen.Building;

public abstract class Recipe {

    public abstract boolean valid(Building building);
    public abstract void apply(Building building);
}
