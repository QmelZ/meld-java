package meld.world.blocks.crafting.modules;

import meld.world.blocks.crafting.ModularCrafter;

public class AdditionModule extends ModularCrafter.CrafterModule {
    int[] inputPins;
    int outputPin;

    public AdditionModule(int outputPin, int... inputPins){
        this.outputPin = outputPin;
        this.inputPins = inputPins;
    }
}
