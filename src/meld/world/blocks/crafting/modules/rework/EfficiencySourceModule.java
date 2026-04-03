package meld.world.blocks.crafting.modules.rework;

import meld.world.blocks.crafting.ModularCrafter.*;

public class EfficiencySourceModule extends CrafterModule{
    /// Pin to provide efficiency on.
    public int outputPin;
    public float baseEfficiency = 1f;

    public EfficiencySourceModule(int outputPin){
        this.outputPin = outputPin;
    }

    @Override
    public void update(ModularCrafterBuild build){
        build.setPin(outputPin, baseEfficiency);
    }
}
