package meld.world.blocks.crafting.modules;

import meld.world.blocks.crafting.ModularCrafter.*;

public class StupidConsumeAllModule extends CrafterModule{
    public int[] inputPins;
    /// Pin to provide efficiency on.
    public int[] outputPins;

    public StupidConsumeAllModule(int... outputPins){
        this.outputPins = outputPins;
    }

    public void update(ModularCrafterBuild build){
        float input = 1f;
        for(int i : inputPins) input *= build.getPin(i);

        //Find the least consumed efficiency, it signals inactivity
        float output = 0f;
        for(int o : outputPins) output = Math.max(output, build.getPin(o));

        //If consuming would give us more efficiency, do so
        if(input > output){
            //Subtract the usable efficiency
            for(int i : inputPins) build.setPin(i, build.getPin(i) - input);
            for(int o : outputPins) build.setPin(o, input);
        }

    }
}

