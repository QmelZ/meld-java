package meld.content;

import arc.graphics.Color;
import arc.struct.ObjectFloatMap;
import mindustry.type.Liquid;

public class MeldLiquids {

    public static Liquid aether, aspect, meld, fumes,
    pollutantMixture;

    public static ObjectFloatMap<Liquid> aetherEfficiencies = new ObjectFloatMap<>();

    public static void load(){
        aether = new Liquid("aether"){{
            gas = true;
            color = Color.valueOf("cb8650");
            temperature = 0.6f;
        }};

        aspect = new Liquid("aspect"){{
            gas = true;
            flammability = 1;
            explosiveness = 2;
            color = Color.valueOf("cbdbfc");
            temperature = 0.6f;
        }};

        meld = new Liquid("meld"){{
            gas = true;
            color = Color.valueOf("e4aad5");
            temperature = 0.6f;
        }};

        fumes = new Liquid("fumes"){{
            gas = true;
            color = Color.valueOf("5b4739");
            temperature = 0.6f;
        }};

        pollutantMixture = new Liquid("pollutant-mixture"){{
            gas = true;
            color = Color.valueOf("6a634d");
            temperature = 0.6f;
        }};

        aetherEfficiencies.put(aether, 1);
        aetherEfficiencies.put(pollutantMixture, 0.5f);

    }
}
