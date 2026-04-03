package meld.world.blocks.crafting.modules.rework;

import meld.world.blocks.crafting.ModularCrafter.*;

public class ConsumeHighestModule extends CrafterModule{
    public int[] inputPins;
    /// Pin to provide efficiency on.
    public int[] outputPins;

    public ConsumeHighestModule(int... outputPins){
        this.outputPins = outputPins;
    }

    public void update(ModularCrafterBuild build){
        //Get the highest input pin
        float input = 0f;
        int inputPin = 0;
        for(int i : inputPins){
            if(build.getPin(i) > input){
                input = build.getPin(i);
                inputPin = i;
            }
        }

        //Find the least consumed output, it signals inactivity
        float output = 0f;
        for(int o : outputPins) output = Math.max(output, build.getPin(o));

        if(input > output){
            //Just swap pin contents
            build.setPin(inputPin, output);
            for(int o : outputPins) build.setPin(o, input);
        }

    }
}
