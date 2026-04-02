package meld.world.blocks.crafting.modules;

import arc.util.*;
import meld.world.blocks.crafting.*;
import meld.world.blocks.crafting.ModularCrafter.*;
import mindustry.type.*;

public class StupidProduceLiquidModule extends CrafterModule{
    public LiquidStack[] liquids;
    /// Pin to consume efficiency from.
    public int inputPin;

    public boolean dumpExtraLiquid = false;

    public StupidProduceLiquidModule(int inputPin){
        this.inputPin = inputPin;
    }

    //Entirely GenericCrafter copy-paste, probably Sucks Ass.
    @Override
    public void update(ModularCrafterBuild build){
        //check full
        boolean allFull = true;
        for(LiquidStack stack : liquids){
            if(build.liquids.get(stack.liquid) >= build.block.liquidCapacity - 0.001f){
                if(!dumpExtraLiquid) return;
            }else{
                //if there's still space left, it's not full for all liquids
                allFull = false;
            }
        }

        //if there is no space left for any liquid, it can't produce
        if(allFull) return;

        //continuously output based on efficiency
        for(LiquidStack stack : liquids){
            build.handleLiquid(build, stack.liquid,
                Math.min(
                    stack.amount * build.getPin(inputPin) * build.timeScale() * Time.delta,
                    build.block.liquidCapacity - build.liquids.get(stack.liquid)
                )
            );
        }

        build.setPin(inputPin, 0f);
    }

    @Override
    public void setup(ModularCrafter block){
        block.hasLiquids = true;
        for(LiquidStack stack : liquids){
            block.dumpedLiquids.add(stack.liquid);
        }
    }
}
