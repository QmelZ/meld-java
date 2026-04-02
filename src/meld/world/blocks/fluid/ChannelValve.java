package meld.world.blocks.fluid;

import arc.Core;
import arc.audio.Sound;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.TextureRegion;
import mindustry.gen.Sounds;

//A switch + liquid junction
public class ChannelValve extends FlexibleSizeJunction {
    public Sound clickSound = Sounds.click;

    public TextureRegion onRegion;

    @Override
    public void load() {
        super.load();
        onRegion = Core.atlas.find(name + "-on", "clear");
    }

    public ChannelValve(String name) {
        super(name);
        configurable = true;
        update = true;
        drawDisabled = false;
        autoResetEnabled = false;
        configureSound = Sounds.none;

        config(Boolean.class, (ValveBuild entity, Boolean b) -> entity.enabled = b);
    }

    public class ValveBuild extends FlexibleBuild{

        @Override
        public boolean configTapped(){
            configure(!enabled);
            clickSound.at(this);
            return false;
        }

        @Override
        public Boolean config(){
            return enabled;
        }

        @Override
        public void draw(){
            super.draw();

            if(enabled){
                Draw.rect(onRegion, x, y);
            }
        }
    }
}
