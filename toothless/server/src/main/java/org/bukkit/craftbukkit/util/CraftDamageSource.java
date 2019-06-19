package org.bukkit.craftbukkit.util;

import net.minecraft.server.DamageSource;

// Util class to create custom DamageSources.
public final class CraftDamageSource extends DamageSource {
    private CraftDamageSource(String identifier) {
        super(identifier);
    }

    public static DamageSource copyOf(final DamageSource original) {
        CraftDamageSource newSource = new CraftDamageSource(original.translationIndex);

        // Check ignoresArmor
        if (original.ignoresArmor()) {
            newSource.setIgnoreArmor();
        }

        // Check magic
        if (original.isMagic()) {
            newSource.setMagic();
        }

        // Check fire
        if (original.isExplosion()) {
            newSource.setExplosion();
        }

        return newSource;
    }
}
