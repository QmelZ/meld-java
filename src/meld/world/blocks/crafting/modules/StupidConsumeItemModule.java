package meld.world.blocks.crafting.modules;

import arc.util.*;
import meld.world.blocks.crafting.*;
import meld.world.blocks.crafting.ModularCrafter.*;
import mindustry.type.*;

public class StupidConsumeItemModule extends CrafterModule{
    public ItemStack[] items;
    /// Pin to provide efficiency on.
    public int outputPin;
    /// Pin to store consumption progress.
    public int progressPin;
    public float time = 60f;

    public float baseEfficiency = 0f;
    public float efficiencyIncrease = 1f;

    public StupidConsumeItemModule(int outputPin, int progressPin){
        this.outputPin = outputPin;
        this.progressPin = progressPin;
    }

    @Override
    public void update(ModularCrafterBuild build){
        float output = build.getPin(outputPin);
        float progress = build.getPin(progressPin);
        //Get the amount of efficiency that was eaten
        float consumed = (efficiencyIncrease - output - baseEfficiency) / (efficiencyIncrease - baseEfficiency);

        if(output < baseEfficiency) build.setPin(outputPin, baseEfficiency);

        //if efficiency has been used
        if(consumed > 0f && build.items.has(items)){
            build.setPin(outputPin, baseEfficiency + efficiencyIncrease);
            progress += consumed * Time.delta;

            //consumption
            while(progress > time && build.items.has(items)){

                build.items.remove(items);
                progress -= time;
            }
            build.setPin(progressPin, progress);
        }
    }

    @Override
    public void setup(ModularCrafter block){
        block.hasItems = true;
        for(ItemStack stack : items){
            block.acceptedItems.add(stack.item);
        }
    }
}
