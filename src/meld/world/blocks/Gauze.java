package meld.world.blocks;

import arc.math.Mathf;
import arc.struct.Seq;
import mindustry.content.Fx;
import mindustry.gen.Building;
import mindustry.graphics.Pal;
import mindustry.world.Block;
import mindustry.world.Tile;

public class Gauze extends Block {
    public float reload = 60;
    public int propagateMaxRange = 10;

    public float healAmount = 200;
    public float maxFract = 0.1f;

    Seq<Building> searching = new Seq<>(), qued = new Seq<>(), explored = new Seq<>();

    public Gauze(String name) {
        super(name);
        update = true;
    }


    public class GauzeBuild extends Building {

        public float time;

        @Override
        public void updateTile() {
            super.updateTile();
            time += efficiency;
            if(time >= reload){
                propagate();
                time %= reload;
            }
        }

        //Only propagates through 1x1 blocks and their adjacent blocks
        public void propagate(){
            explored.clear();
            searching.clear();
            qued.clear();

            explored.add(this);
            searching.addAll(this.proximity);

            float charge = healAmount;

            for(int i = 0; i < propagateMaxRange; i++){
                for(Building build: searching){
                    Fx.healBlock.at(build.x, build.y, 1);
                    if(build.damaged()){
                        /**
                         * Max of a few numbers
                         * -Missing health
                         * -maxFract * build.maxHealth
                         * -Remaining charge
                         * -0
                         */
                        float healAmount = Mathf.maxZero(Math.min(Math.min(build.maxHealth * maxFract, build.block.health - build.health), charge));
                        if(healAmount > 0){
                            Fx.healBlockFull.at(build.x, build.y, 0, Pal.heal, build.block);
                            build.heal(healAmount);
                            charge -= healAmount;
                        }
                    }

                    if(build.block.size == 1) build.proximity.each( b -> !explored.contains(b), qued::addUnique);
                }

                explored.add(searching);
                searching.set(qued);
                qued.clear();

                //No more healing to expand, goodbye world
                if(charge == 0) break;
            }
        }
    }
}
