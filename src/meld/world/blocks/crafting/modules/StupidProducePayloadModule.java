package meld.world.blocks.crafting.modules;

import arc.*;
import arc.util.*;
import meld.world.blocks.crafting.*;
import meld.world.blocks.crafting.ModularCrafter.*;
import mindustry.game.EventType.*;
import mindustry.gen.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.blocks.payloads.*;

import java.util.*;

public class StupidProducePayloadModule extends CrafterModule{
    public PayloadStack[] payloads;
    /// Pin to consume efficiency from.
    public int inputPin;
    /// Pin used to store crafting progress.
    public int progressPin;
    public float time = 60f;

    public StupidProducePayloadModule(int inputPin, int progressPin){
        this.inputPin = inputPin;
        this.progressPin = progressPin;
    }

    @Override
    public void update(ModularCrafterBuild build){
        if(outputFits(build)){
            float progress = build.getPin(progressPin);
            progress += build.getPin(inputPin) * build.timeScale() * Time.delta;
            build.setPin(inputPin, 0f);

            while(progress > time && outputFits(build)){
                for(PayloadStack stack : payloads){
                    build.payloads.add(stack.item, stack.amount);
                }
                progress -= time;
            }
            build.setPin(progressPin, progress);
        }

        //output
        if(build.payload == null && build.payloads.any()){
            for(PayloadStack stack : payloads){
                if(build.payloads.contains(stack.item)){
                    build.payloads.remove(stack.item);

                    if(stack.item instanceof UnitType type){
                        build.payload = new UnitPayload(type.create(build.team));
                        Unit p = ((UnitPayload)build.payload).unit;
                            /*if(commandPos != null && p.isCommandable()){
                                p.command().commandPosition(commandPos);
                            }*/
                        Events.fire(new UnitCreateEvent(p, build));
                    }else if(stack.item instanceof Block block){
                        build.payload = new BuildPayload(block, build.team);
                    }
                    build.payVector.setZero();
                    build.payRotation = build.rotdeg();
                    break;
                }
            }
        }

        if(build.payload != null && outputs(build.payload)){
            build.moveOutPayload();
        }
    }

    public boolean outputs(Payload pay){
        for(PayloadStack stack : payloads){
            if(stack.item == pay.content()) return true;
        }
        return false;
    }

    public boolean outputFits(ModularCrafterBuild build){
        return Arrays.stream(payloads).allMatch(stack -> build.payloads.get(stack.item) + stack.amount <= build.modular.payloadCapacity);
    }

    @Override
    public void setup(ModularCrafter block){
        block.rotate = true;
        block.outputsPayload = true;
        for(PayloadStack stack : payloads){
            block.dumpedPayloads.add(stack.item);
        }
    }
}
