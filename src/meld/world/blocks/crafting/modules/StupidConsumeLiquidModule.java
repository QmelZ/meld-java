package meld.world.blocks.crafting.modules;

import arc.util.*;
import meld.world.blocks.crafting.*;
import meld.world.blocks.crafting.ModularCrafter.*;
import mindustry.type.*;

public class StupidConsumeLiquidModule extends CrafterModule{
    public LiquidStack[] liquids;
    /// Pin to provide efficiency on.
    public int outputPin;

    public float baseEfficiency = 0;
    public float efficiencyIncrease = 1;

    public StupidConsumeLiquidModule(int outputPin){
        this.outputPin = outputPin;
    }

    @Override
    public void update(ModularCrafterBuild build){
        float output = build.getPin(outputPin);
        //Get the amount of efficiency that was eaten
        float consumed = (efficiencyIncrease - output - baseEfficiency) / (efficiencyIncrease - baseEfficiency);

        if(output < baseEfficiency) build.setPin(outputPin, baseEfficiency);

        //if efficiency has been used
        if(consumed > 0f){
            float min = 10000f;
            for(LiquidStack stack : liquids){
                min = Math.min(build.liquids.get(stack.liquid) / (stack.amount * consumed * Time.delta), min);
            }

            if(min > 0.00001f){
                for(LiquidStack stack : liquids){
                    build.liquids.remove(stack.liquid, stack.amount * min);
                }
                build.setPin(outputPin, baseEfficiency + efficiencyIncrease * min);
            }
        }
    }

    @Override
    public void setup(ModularCrafter block){
        block.hasLiquids = true;
        for(LiquidStack stack : liquids){
            block.acceptedLiquids.add(stack.liquid);
        }
    }
}
