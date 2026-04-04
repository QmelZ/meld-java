package meld.content;

import arc.math.Interp;
import meld.Meld;
import mindustry.content.Blocks;
import mindustry.content.Fx;
import mindustry.entities.bullet.BasicBulletType;
import mindustry.entities.bullet.BulletType;
import mindustry.entities.bullet.ExplosionBulletType;
import mindustry.entities.bullet.RailBulletType;

public class MeldBullets {

    public static BulletType

            pulsarBlast, pulsarShrapnel;

    public static void load(){

        pulsarShrapnel = new BasicBulletType(){{
            sprite = Meld.prefix("clump");
            shrinkX = 1;
            shrinkY = 0.2f;

            width = 5;
            height = 12;
            shrinkInterp = Interp.pow2In;
            speed = 12;
            drag = 0.02f;

            lifetime = 30;
            damage = 10;
            pierce = true;
            pierceBuilding = true;
            pierceDamageFactor = 0.5f;
            lightRadius = 0;

            lightningDamage = 5;
            lightning = 1;
            lightningLength = 12;

            setDefaults = false;
            despawnEffect = hitEffect = Fx.none;
            despawnHit = false;

            collideTerrain = true;
        }};

        pulsarBlast = new ExplosionBulletType(){{
            lightningDamage = 5;
            lightning = 12;
            lightningLength = 48;

            fragOffsetMin = fragOffsetMax = 0;
            fragLifeMin = 0.5f;

            fragBullets = 120;
            fragSpread = 3;
            fragRandomSpread = 0;
            fragBullet = pulsarShrapnel;
            killShooter = false;
        }};

    }
}
