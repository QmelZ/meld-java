package meld.world.blocks.fluid;

import arc.math.geom.Geometry;
import arc.math.geom.Point2;
import arc.math.geom.Position;
import arc.util.Log;
import arc.util.Tmp;
import meld.world.WorldUtil;
import mindustry.Vars;
import mindustry.content.Fx;
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
            return getLiquidDestination(source, liquid, null);
        }

        public Building getLiquidDestination(Building source, Liquid liquid, Position otherOffset){
            if(!enabled) return this;

            //Find which direction the offset should go
            int dir = WorldUtil.relativeTo(source.tile.x, source.tile.y, tile.x, tile.y);
            Point2 direction = Geometry.d4(dir);

            //Direction offset vector
            Tmp.v1.set(direction.x, direction.y).scl(size);

            //Tile offset vector, can grab from previous junction if it's already been calculated
            if(otherOffset != null){
                Tmp.v2.set(otherOffset);
            }
            else {
                int tolerance = size/2;
                Tmp.v2.set(source.tile.x, source.tile.y).sub(tile.x, tile.y).clamp(-tolerance, -tolerance, tolerance, tolerance);
            }

            //Add the tile offset vector and the direction offset vectors to the tile
            Tmp.v3.set(tile.x, tile.y).add(Tmp.v2).add(Tmp.v1);

            Tmp.v3.scl(Vars.tilesize);

            Building next = Vars.world.buildWorld(Tmp.v3.x, Tmp.v3.y);

            if(next == null || (!next.acceptLiquid(this, liquid) && !(next.block instanceof LiquidJunction)) && next != this){
                return this;
            }
            if(next instanceof FlexibleBuild b){
                return b.getLiquidDestination(this, liquid, Tmp.v2);
            }
            return next.getLiquidDestination(this, liquid);
        }
    }
}
