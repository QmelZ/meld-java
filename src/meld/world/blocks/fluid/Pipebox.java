package meld.world.blocks.fluid;

import arc.Core;
import arc.audio.Sound;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.TextureRegion;
import arc.math.geom.Point2;
import arc.math.geom.Position;
import arc.util.Eachable;
import arc.util.Log;
import arc.util.Tmp;
import arc.util.io.Reads;
import arc.util.io.Writes;
import mindustry.Vars;
import mindustry.content.Fx;
import mindustry.entities.units.BuildPlan;
import mindustry.game.Team;
import mindustry.gen.Building;
import mindustry.gen.Sounds;
import mindustry.type.Liquid;
import mindustry.world.Tile;
import mindustry.world.blocks.production.BeamDrill;

public class Pipebox extends FlexibleSizeJunction{
    public Sound clickSound = Sounds.click;

    public TextureRegion onRegion, directionRegion;

    public TextureRegion[] markerRegions = new TextureRegion[2];

    @Override
    public void load() {
        super.load();
        onRegion = Core.atlas.find(name + "-on", "clear");
        markerRegions[0] = Core.atlas.find(name + "-marker");
        markerRegions[1] = Core.atlas.find(name + "-marker-on");
        directionRegion = Core.atlas.find(name + "-direction", "clear");
    }

    public Pipebox(String name) {
        super(name);
        configurable = true;
        update = true;
        drawDisabled = false;
        autoResetEnabled = false;
        configureSound = Sounds.none;

        enabledToggles = false;
        saveConfig = true;
        rotate = true;

        config(Boolean.class, (PipeboxBuild entity, Boolean b) -> {
            entity.invert = b;
        });
    }

    @Override
    public TextureRegion[] icons() {
        return new TextureRegion[]{region, markerRegions[0], directionRegion};
    }

    @Override
    public void drawPlanRegion(BuildPlan plan, Eachable<BuildPlan> list){
        if(plan.config instanceof Boolean b && b) {
            Draw.rect(onRegion, plan.drawx(), plan.drawy());
            Draw.rect(markerRegions[1], plan.drawx(), plan.drawy(), plan.rotation * 90);
        }
        else {
            Draw.rect(region, plan.drawx(), plan.drawy());
            Draw.rect(markerRegions[0], plan.drawx(), plan.drawy(), plan.rotation * 90);
        }
        Draw.rect(directionRegion, plan.drawx(), plan.drawy(), plan.rotation * 90);
    }

    //The rotation offsets themselves
    public int[] rotMappings = new int[]{
            90, -90, 90, -90
    };

    //actually offsets to dir because lazy
    public int[] dirMappings = new int[]{
            1, -1, 1, -1
    };

    public class PipeboxBuild extends FlexibleBuild{

        public boolean invert = false;

        @Override
        public boolean configTapped(){
            configure(!invert);
            clickSound.at(this);
            return false;
        }

        @Override
        public Boolean config(){
            return invert;
        }

        @Override
        public void draw(){
            super.draw();

            if(invert){
                Draw.rect(onRegion, x, y);
            }
            Draw.rect(markerRegions[invert ? 1 : 0], x, y, rotation * 90);
            Draw.rect(directionRegion, x, y, rotation * 90);
        }

        @Override
        public float addedRotation(Building source, int dir) {
            return rotMappings[dir] * (invert ^ rotation % 2 == 0 ? -1 : 1);
        }
        @Override
        public int transportRotation(Building source, int dir) {
            return (dir + dirMappings[dir] * (invert ^ rotation % 2 == 0 ? -1 : 1)) % 4;
        }

        @Override
        public void write(Writes write) {
            super.write(write);
            write.bool(invert);
        }

        @Override
        public void read(Reads read, byte revision) {
            super.read(read, revision);
            invert = read.bool();
        }
    }
}
