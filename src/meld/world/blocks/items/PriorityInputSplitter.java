package meld.world.blocks.items;

import mindustry.gen.Building;
import mindustry.type.Item;
import mindustry.world.Block;
import mindustry.world.Edges;
import mindustry.world.blocks.distribution.DuctRouter;
import mindustry.world.blocks.distribution.OverflowDuct;

//hdasdjcdf ok fine it's TIME
public class PriorityInputSplitter extends OverflowDuct {
    public PriorityInputSplitter(String name) {
        super(name);
        itemCapacity = 4;
    }

    public float threshold;

    public class PriorityInputBuild extends OverflowDuctBuild{
        @Override
        public boolean acceptItem(Building source, Item item){
            return current == null &&
                    items.total() < itemCapacity/2 ?
                    true :
                    items.total() < itemCapacity && (Edges.getFacingEdge(source.tile, tile).relativeTo(tile) == rotation);
        }
    }
}
