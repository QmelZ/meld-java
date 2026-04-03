package meld.world.blocks.crafting.modules.rework;

import arc.util.*;
import meld.world.blocks.crafting.*;
import meld.world.blocks.crafting.ModularCrafter.*;
import mindustry.type.*;
import java.util.*;

public class ProduceItemModule extends CrafterModule{
    public ItemStack[] items;
    /// Pin to consume efficiency from.
    public int inputPin;
    /// Pin used to store crafting progress.
    public int progressPin;
    public float time = 60f;

    public ProduceItemModule(int inputPin, int progressPin){
        this.inputPin = inputPin;
        this.progressPin = progressPin;
    }

    @Override
    public void update(ModularCrafterBuild build){
        if(!fits(build, items)) return;

        float progress = build.getPin(progressPin);
        progress += build.getPin(inputPin) * build.timeScale() * Time.delta;
        build.setPin(inputPin, 0f);

        while(progress > time && fits(build, items)){
            for(ItemStack stack : items){
                build.items.add(stack.item, stack.amount);
            }
            progress -= time;
        }
        build.setPin(progressPin, progress);
    }

    public static boolean fits(ModularCrafterBuild build, ItemStack[] itemStacks){
        return Arrays.stream(itemStacks).allMatch(stack -> build.items.get(stack.item) + stack.amount <= build.block.itemCapacity);
    }

    @Override
    public void setup(ModularCrafter block){
        block.hasItems = true;
        for(ItemStack stack : items){
            block.dumpedItems.add(stack.item);
        }
    }
}
