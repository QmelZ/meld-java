package meld.world.blocks.crafting.modules.rework;

import meld.world.blocks.crafting.ModularCrafter;
import meld.world.blocks.crafting.ModularCrafter.*;
import mindustry.world.meta.Attribute;

public class AttributeModule extends CrafterModule {
    /// Pin to provide efficiency on.
    public int outputPin;
    /// Pin to store the efficiency that should be output. Don't let other modules touch it.
    public int storagePin;

    public Attribute attribute;
    public float baseEfficiency = 1;
    public float boostScale = 1;
    public float maxBoost = 1;
    /// The minimum attribute efficiency needed for it to affect output.
    public float minEfficiency = -1;

    public AttributeModule(int outputPin, int storagePin){
        this.outputPin = outputPin;
        this.storagePin = storagePin;
    }

    @Override
    public void update(ModularCrafter.ModularCrafterBuild build) {
        build.setPin(outputPin, build.getPin(storagePin));
    }

    @Override
    public void setup(ModularCrafter block){
        block.hook(BlockEvent.Defaults.proximityUpdate, this);
    }

    @Override
    public void on_event(ModularCrafterBuild build){
        float efficiency = baseEfficiency + Math.min(maxBoost, boostScale * build.block.sumAttribute(attribute, build.tileX(), build.tileY()) + attribute.env());

        if(efficiency < minEfficiency) {
            build.setPin(storagePin, 0);
            return;
        }

        build.setPin(storagePin, efficiency);
    }
}