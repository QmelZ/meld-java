package meld.world.blocks.fluid;

import arc.math.geom.Point2;
import arc.math.geom.Position;
import arc.util.Tmp;
import mindustry.Vars;
import mindustry.content.Fx;
import mindustry.gen.Building;
import mindustry.type.Liquid;

public class Pipebox extends ChannelValve{
    public Pipebox(String name) {
        super(name);
        enabledToggles = false;
        saveConfig = true;
    }

    //The rotation offsets themselves
    public int[] rotMappings = new int[]{
            90, -90, 90, -90
    };

    //actually offsets to dir because lazy
    public int[] dirMappings = new int[]{
            1, -1, 1, -1
    };

    public class PipeboxBuild extends ValveBuild{

        @Override
        public float addedRotation(Building source, int dir) {
            return rotMappings[dir] * (enabled ? 1 : -1);
        }
        @Override
        public int transportRotation(Building source, int dir) {
            return (dir + dirMappings[dir] * (enabled ? 1 : -1)) % 4;
        }
    }
}
