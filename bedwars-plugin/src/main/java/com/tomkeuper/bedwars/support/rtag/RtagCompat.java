/*
 * BedWars2023 - A bed wars mini-game.
 * Copyright (C) 2024 Tomas Keuper
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 * Contact e-mail: contact@fyreblox.com
 */

package com.tomkeuper.bedwars.support.rtag;

import com.saicone.rtag.util.MC;
import com.saicone.rtag.util.ServerInstance;
import com.saicone.rtag.util.reflect.Remapper;

/**
 * Workarounds for Rtag issues. Kept in its own class so Rtag classes are only resolved
 * after slimjar has loaded the library.
 */
public final class RtagCompat {

    private RtagCompat() {
    }

    /**
     * Since Rtag 1.5.15, {@code ComponentType} looks up {@code DataComponentType#codec()} as a method,
     * but the Mojang-to-Spigot remapper only maps {@code codec} as a field. On Spigot 1.20.5+ the
     * lookup then fails and every item edit throws. Paper runs Mojang-mapped and is not affected.
     * <p>
     * {@code codec()} is obfuscated as {@code b()} in every version from 1.20.5 to 1.21.11.
     */
    public static void patchRemapper() {
        if (ServerInstance.Type.MOJANG_MAPPED || !MC.version().isNewerThanOrEquals(MC.V_1_20_5)) {
            return;
        }
        Remapper.MOJANG_TO_SPIGOT.add(new Remapper.Mapping("net.minecraft.core.component.DataComponentType") {{
            method("com.mojang.serialization.Codec", "codec").to("b");
        }});
    }
}
