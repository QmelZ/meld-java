package meld.world.blocks.crafting.modules;

import meld.world.blocks.LiquidUtil;
import meld.world.blocks.crafting.ModularCrafter;
import mindustry.type.ItemStack;
import mindustry.type.Liquid;
import mindustry.type.LiquidStack;

//If item requirement is not met, set the output to 0. Otherwise, set the output to one.
public class ConsumeGate extends ModularCrafter.CrafterModule {

    public ConsumeGate(ItemStack[] items, int outputPin){
        this.items = items;
        this.outputPin = outputPin;
    }
    public ConsumeGate(LiquidStack[] liquids, int outputPin){
        this.liquids = liquids;
        this.outputPin = outputPin;
    }
    public ConsumeGate(ItemStack[] items, LiquidStack[] liquids, int outputPin){
        this.items = items;
        this.liquids = liquids;
        this.outputPin = outputPin;
    }

    public ItemStack[] items;
    public LiquidStack[] liquids;

    public int outputPin;

    @Override
    public void update(ModularCrafter.ModularCrafterBuild build) {
        float out = 1;

        if(liquids != null && !LiquidUtil.has(liquids, build)) out = 0;

        //Set the output to zero if the build doesn't have the items, otherwise set it to whatever the out is
        build.setPin(outputPin,build.items.has(items) ? out : 0);
    }
}
