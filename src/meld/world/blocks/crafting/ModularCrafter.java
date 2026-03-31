package meld.world.blocks.crafting;

import arc.struct.IntFloatMap;
import arc.struct.ObjectMap;
import arc.struct.Seq;
import arc.util.Log;
import arc.util.Time;
import mindustry.game.Team;
import mindustry.gen.Building;
import mindustry.type.Item;
import mindustry.type.ItemStack;
import mindustry.type.Liquid;
import mindustry.world.Block;
import mindustry.world.blocks.production.GenericCrafter;
import mindustry.world.meta.Attribute;

import java.util.HashMap;

public class ModularCrafter extends Block {

    //Modules updates every update, listeners are for modules with specific events
    public Seq<CrafterModule> modules = new Seq<CrafterModule>();
    public ObjectMap<Object, Seq<CrafterModule>> listeners = new ObjectMap<Object, Seq<CrafterModule>>();

    //list of items/liquids which this block accepts
    public Seq<Liquid> acceptedLiquids = new Seq<>();
    public Seq<Item> acceptedItems = new Seq<>();

    //List of items/liquids which get dumped
    public Seq<Liquid> dumpedLiquids = new Seq<>();
    public Seq<Item> dumpedItems = new Seq<>();

    public boolean replaceBars = true;

    //Default float data array
    public IntFloatMap defaultData = new IntFloatMap();

    public ModularCrafter(String name) {
        super(name);
        update = true;
        solid = true;
    }

    @Override
    public void setBars() {
        super.setBars();

        if(replaceBars && acceptedLiquids.size + dumpedLiquids.size > 0){
            removeBar("liquid");

            for(Liquid liquid: acceptedLiquids){
                addLiquidBar(liquid);
            }
            for(Liquid liquid: dumpedLiquids){
                addLiquidBar(liquid);
            }
        }
    }

    public static void trigger(ModularCrafter block, ModularCrafterBuild build, Object event){
        Seq<CrafterModule> events = block.listeners.get(event);
        if(events != null) events.each(c -> c.update(build));
    }

    public void hook(Object event, CrafterModule module){
        Seq<CrafterModule> events = listeners.get(event);
        if(events == null) {
            listeners.put(event, Seq.with(module));
            return;
        }
        events.add(module);
    }

    public void hookAll(Object event, CrafterModule... module){
        Seq<CrafterModule> events = listeners.get(event);
        if(events == null) {
            listeners.put(event, Seq.with(module));
            return;
        }
        events.add(module);
    }

    public static class BlockEvent{
        public enum Defaults{
            update,
            proximityUpdate
        }
    }

    public static abstract class CrafterModule{
        public void update(ModularCrafterBuild build){

        }
    }

    public static abstract class CraftingModule extends CrafterModule{
        public int efficiencyPin = -1;
        public int[] efficiencyPins;
        public int progressPin;
        public float craftTime = 60;

        public boolean canCraft(ModularCrafterBuild build){
            return true;
        };

        public void update(ModularCrafterBuild build){
            if(!canCraft(build)) return;

            float efficiency = 1;
            if(efficiencyPin != -1) efficiency = build.getPin(efficiencyPin);
            if(efficiencyPins != null){
                for(int i: efficiencyPins){
                    efficiency *= build.getPin(i);
                }
            }

            float progress = build.getPin(progressPin);
            progress += efficiency * Time.delta;

            if(progress > craftTime) {
                craft(build);
                progress %= craftTime;
            }
            build.setPin(progressPin, progress);
        }

        public abstract void craft(ModularCrafterBuild build);
    }

    public static class AttributeModule extends CrafterModule{
        public int efficiencyPin;

        public Attribute attribute;
        public float baseEfficiency = 1;
        public float boostScale = 1;
        public float maxBoost = 1;
        public float minEfficiency = -1;

        @Override
        public void update(ModularCrafterBuild build) {
            float efficiency = baseEfficiency + Math.min(maxBoost, boostScale * build.block.sumAttribute(attribute, build.tileX(), build.tileY()) + attribute.env());

            Log.info(efficiency);

            if(minEfficiency != -1 && efficiency < minEfficiency) {
                build.setPin(efficiencyPin, 0);
                return;
            }

            build.data.put(efficiencyPin, efficiency);
        }
    }

    public static class ItemCraftingModule extends CraftingModule{
        public ItemStack outputItem;
        public ItemStack[] outputItems;

        public ItemStack[] inputItems;

        @Override
        public void update(ModularCrafterBuild build) {
            super.update(build);
        }

        @Override
        public boolean canCraft(ModularCrafterBuild build) {
            if(outputItems != null){
                for(var output : outputItems){
                    if(build.items.get(output.item) + output.amount >= build.block.itemCapacity){
                        return false;
                    }
                }
            }
            if(outputItem != null && build.items.get(outputItem.item) + outputItem.amount >= build.block.itemCapacity) return false;
            return true;
        }

        @Override
        public void craft(ModularCrafterBuild build) {
            if(outputItem != null) build.items.add(outputItem.item, outputItem.amount);

            if(inputItems != null) build.items.remove(inputItems);

            if(outputItems != null) {
                for (ItemStack item : outputItems) {
                    build.items.add(item.item, item.amount);
                }
            }
        }
    }

    //Just some static finals to help readability
    public static class ModOUT{
        public static final float ON = 1, OFF = 0;
        public static final int
            ZERO = 0,
            ONE = 1,
            TWO = 2,
            THREE = 3,
            FOUR = 4,
            FIVE = 5,
            SIX = 6,
            SEVEN = 7,
            EIGHT = 8,
            NINE = 9,
            TEN = 10,
            ELEVEN = 11,
            TWELVE = 12;
    };
    public static class ModIN{
        public static final float ON = 1, OFF = 0;
        public static final int
            ZERO = 0,
            ONE = 1,
            TWO = 2,
            THREE = 3,
            FOUR = 4,
            FIVE = 5,
            SIX = 6,
            SEVEN = 7,
            EIGHT = 8,
            NINE = 9,
            TEN = 10,
            ELEVEN = 11,
            TWELVE = 12;
    };

    public static final float ON = 1, OFF = 0;

    public class ModularCrafterBuild extends Building {

        public ModularCrafter modular;
        public IntFloatMap data = new IntFloatMap();

        @Override
        public void created() {
            super.created();
            data.putAll(defaultData);
        }

        @Override
        public void updateTile() {
            super.updateTile();
            this.dumpOutputs();
        }

        public void dumpOutputs(){
            dumpedLiquids.each(this::dumpLiquid);
            dumpedItems.each(this::dump);
        }

        public void setPin(int pin, float value){
            data.put(pin, value);
        };

        public float getPin(int pin){
            return data.get(pin);
        }

        @Override
        public boolean acceptLiquid(Building source, Liquid liquid) {
            return super.acceptLiquid(source, liquid) || acceptedLiquids.contains(liquid);
        }

        @Override
        public boolean acceptItem(Building source, Item item) {
            return (this.block.consumesItem(item) || acceptedItems.contains(item)) && this.items.get(item) < this.getMaximumAccepted(item);
        }

        @Override
        public Building create(Block block, Team team) {
            modular = (ModularCrafter) block;
            return super.create(block, team);
        }

        @Override
        public void onProximityUpdate() {
            super.onProximityUpdate();
            trigger(modular, this, BlockEvent.Defaults.proximityUpdate);
        }

        @Override
        public void update() {
            super.update();
            modules.each(c -> c.update(this));
        }
    }
}
