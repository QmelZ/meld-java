package meld.world.blocks.fluid;

import arc.math.geom.Geometry;
import arc.math.geom.Point2;
import meld.world.WorldUtil;
import mindustry.Vars;
import mindustry.gen.Building;
import mindustry.type.Liquid;
import mindustry.world.blocks.liquid.LiquidJunction;

public class FlexibleSizeJunction extends LiquidJunction {
    public FlexibleSizeJunction(String name) {
        super(name);
    }

    public class FlexibleBuild extends LiquidJunctionBuild{

        @Override
        public Building getLiquidDestination(Building source, Liquid liquid){
            if(!enabled) return this;

            int dir = WorldUtil.relativeTo(source.tile.x, source.tile.y, tile.x, tile.y);

            Point2 offset = Geometry.d4(dir);

            Building next = Vars.world.build(source.tile.x + offset.x * (size + 1), source.tile.y + offset.y * (size + 1));

            if(next == null || (!next.acceptLiquid(this, liquid) && !(next.block instanceof LiquidJunction))){
                return this;
            }
            return next.getLiquidDestination(this, liquid);
        }

    }
}
