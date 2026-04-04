package meld.world.blocks.crafting;

import mindustry.world.blocks.production.GenericCrafter;
import mindustry.world.consumers.ConsumeLiquid;
import mindustry.world.consumers.ConsumeLiquids;

public class MeldGenericCrafter extends GenericCrafter {
    public MeldGenericCrafter(String name) {
        super(name);
    }

    public interface BarConsumer{
        void setBars(MeldGenericCrafter crafter);
    }

    @Override
    public void setBars() {
        super.setBars();

        for(var consl : consumers){
            if(consl instanceof BarConsumer barCons) barCons.setBars(this);
        }
    }

    public class MeldGenericCrafterBuild extends GenericCrafterBuild{

    }
}
