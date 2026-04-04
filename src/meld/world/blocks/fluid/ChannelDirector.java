package meld.world.blocks.fluid;

import arc.Core;
import arc.graphics.g2d.Draw;
import arc.util.Log;
import meld.world.WorldUtil;
import mindustry.gen.Building;

public class ChannelDirector extends FlexibleSizeJunction{

    @Override
    public void load() {
        super.load();
    }
    public ChannelDirector(String name) {
        super(name);
        rotate = true;
        quickRotate = true;
    }

    public class DirectorBuild extends FlexibleBuild{

        @Override
        public boolean acceptDirection(Building source, Building junction, int dir) {
            return dir == rotation;
        }

        @Override
        public void draw(){
            Draw.rect(region, x, y,  rotation * 90);
        }
    }
}
