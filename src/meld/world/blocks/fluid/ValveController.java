package meld.world.blocks.fluid;

import arc.Core;
import arc.audio.Sound;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.graphics.g2d.TextureRegion;
import arc.math.Mathf;
import arc.scene.ui.layout.Table;
import arc.struct.Seq;
import arc.util.Eachable;
import arc.util.Log;
import arc.util.Tmp;
import arc.util.io.Reads;
import arc.util.io.Writes;
import mindustry.Vars;
import mindustry.entities.units.BuildPlan;
import mindustry.gen.Building;
import mindustry.gen.Icon;
import mindustry.gen.Sounds;
import mindustry.graphics.Layer;
import mindustry.graphics.Pal;
import mindustry.ui.Styles;
import mindustry.world.blocks.power.BeamNode;

import static mindustry.Vars.ui;

//When detecting nearby fluids, turn nearby valves off. Can be inverted.
public class ValveController extends FlexibleSizeJunction{

    public int updateTimer = timers++;
    public float minPressure = 0.1f;

    public float padding = 5/4f;

    public Sound clickSound = Sounds.click;

    public TextureRegion[] signRegions = new TextureRegion[2];
    public TextureRegion bottomRegion = new TextureRegion();

    public Color indicatorColor = Pal.health, overflowColor = Pal.surge, fillColor = Pal.heal;

    @Override
    public void load() {
        super.load();
        bottomRegion = Core.atlas.find(name + "-bottom");
        signRegions[0] = Core.atlas.find(name + "-sign", "clear");
        signRegions[1] = Core.atlas.find(name + "-sign-inverted", "clear");
    }

    @Override
    public TextureRegion[] icons() {
        return new TextureRegion[]{bottomRegion, signRegions[0], region};
    }

    public ValveController(String name) {
        super(name);
        configurable = true;
        saveConfig = true;
        update = true;
        drawDisabled = false;
        autoResetEnabled = false;
        configureSound = Sounds.none;

        rotate = true;
        quickRotate = true;

        config(Boolean.class, (ValveControllerBuild entity, Boolean b) -> entity.invert = b);
        config(Float.class, (ValveControllerBuild entity, Float f) -> entity.threshold = f);
        config(Float[].class, (ValveControllerBuild entity, Float[] floats) -> {
            entity.threshold = floats[0];
            entity.invert = floats[1] == -1f;
        });
    }

    public void drawThreshold(float x, float y, float threshold){
        Fill.rect(x, y - (1 - threshold)/2f * (Vars.tilesize) - threshold * padding + padding, Vars.tilesize, (Vars.tilesize - padding * 2) * threshold);
    }

    @Override
    public void drawPlanRegion(BuildPlan plan, Eachable<BuildPlan> list){
        float threshold = minPressure;
        boolean invert = false;

        if(plan.config instanceof Float[] floats){
            threshold = floats[0];
            invert = floats[1] == -1f;
        }

        Draw.z(Layer.blockUnder);
        Draw.rect(bottomRegion, plan.drawx(), plan.drawy());

        Draw.z(Layer.block);

        Draw.color(indicatorColor);
        drawThreshold(plan.drawx(), plan.drawy(), threshold);
        Draw.color();
        Draw.rect(signRegions[invert ? 1 : 0], plan.drawx(), plan.drawy(), plan.rotation * 90);
        Draw.rect(region, plan.drawx(), plan.drawy());
    }


    public class ValveControllerBuild extends FlexibleBuild{

        public boolean invert = false;
        public float threshold = minPressure;
        public float lastTotal = 0;

        @Override
        public void updateTile() {
            super.updateTile();
            enablingLogic();
        }

        public void enablingLogic(){

            lastTotal = 0;
            proximity.each(b -> {
                if(b.liquids != null) lastTotal += b.liquids.currentAmount()/b.block.liquidCapacity;
            });

            boolean enable = lastTotal >= threshold;

            if(invert) enable = !enable;

            Building b = tile.nearbyBuild(rotation);
            if(b == null) return;

            if(b.block.configurable) b.configure(enable);
            else b.enabled = enable;
        }

        @Override
        public boolean configTapped(){
            /*
            configure(!invert);

             */
            clickSound.at(this);
            return true;
        }

        @Override
        public void buildConfiguration(Table table){
            table.slider(0, 1, 0.1f, threshold, Vars.net.active(), this::configure).size(240, 40f);
            table.row();
            table.button(Icon.pencil, Styles.cleari, () -> {
                configure(!invert);
            }).size(40f);
        }

        @Override
        public boolean onConfigureBuildTapped(Building other){
            if(this == other){
                deselect();
                return false;
            }

            return true;
        }

        @Override
        public Float[] config(){
            return new Float[]{
                    threshold, invert ? -1f : 1f
            };
        }

        @Override
        public void draw(){
            Draw.z(Layer.blockUnder);

            Draw.rect(bottomRegion, x, y);

            Draw.z(Layer.block);


            //Im sure theres a simpler formula but like... come on

            //
            float fin2 = Mathf.clamp(lastTotal);
            Draw.color(fillColor);
            drawThreshold(x, y, fin2);

            float fin = threshold;
            Draw.color(indicatorColor);
            drawThreshold(x, y, fin);

            float finFree = Math.min(fin, fin2);
            Draw.color(overflowColor);
            drawThreshold(x, y, finFree);

            Draw.color();

            //Draw.rect(region, x, y);
            Draw.rect(signRegions[invert ? 1 : 0], x, y, rotation * 90);

            Draw.rect(region, x, y);
        }

        @Override
        public void write(Writes write) {
            super.write(write);
            write.bool(invert);
            write.f(threshold);
        }

        @Override
        public void read(Reads read, byte revision) {
            super.read(read, revision);
            invert = read.bool();
            threshold = read.f();
        }
    }
}
