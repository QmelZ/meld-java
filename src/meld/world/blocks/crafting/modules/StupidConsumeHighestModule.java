package meld.world.blocks.crafting.modules;

import meld.world.blocks.crafting.ModularCrafter.*;

public class StupidConsumeHighestModule extends CrafterModule{
    public int[] inputPins;
    /// Pin to provide efficiency on.
    public int outputPin;

    public StupidConsumeHighestModule(int outputPin){
        this.outputPin = outputPin;
    }

    public void update(ModularCrafterBuild build){
        float output = build.getPin(outputPin);

        //Get highest
        float max = 0f;
        int maxPin = 0;
        for(int i : inputPins){
            if(build.getPin(i) > max){
                max = build.getPin(i);
                maxPin = i;
            }
        }

        if(max > output){
            //Just swap pin contents
            build.setPin(maxPin, output);
            build.setPin(outputPin, max);
        }

    }
}
