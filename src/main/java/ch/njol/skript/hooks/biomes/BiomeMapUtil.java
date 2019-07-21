/**
 *   This file is part of Skript.
 *
 *  Skript is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Skript is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Skript.  If not, see <http://www.gnu.org/licenses/>.
 *
 *
 * Copyright 2011-2017 Peter Güttinger and contributors
 */
package ch.njol.skript.hooks.biomes;

import org.bukkit.block.Biome;
import org.eclipse.jdt.annotation.Nullable;

/**
 * Hooks to provide MC < 1.13 support.
 */
public class BiomeMapUtil {
	
	public enum To19Mapping {
		SWAMP("SWAMPLAND"),
		FOREST(Biome.FOREST),
		TAIGA(Biome.TAIGA),
		DESERT(Biome.DESERT),
		PLAINS(Biome.PLAINS),
		NETHER("HELL"),
		THE_END("SKY"),
		OCEAN(Biome.OCEAN),
		RIVER(Biome.RIVER),
		MOUNTAINS("EXTREME_HILLS"),
		FROZEN_OCEAN(Biome.FROZEN_OCEAN),
		FROZEN_RIVER(Biome.FROZEN_RIVER),
		SNOWY_MOUNTAINS("ICE_MOUNTAINS"),
		MUSHROOM_FIELDS("MUSHROOM_ISLAND"),
		DESERT_HILLS(Biome.DESERT_HILLS),
		WOODED_HILLS("FOREST_HILLS"),
		TAIGA_HILLS(Biome.TAIGA_HILLS),
		JUNGLE(Biome.JUNGLE),
		JUNGLE_HILLS(Biome.JUNGLE_HILLS),
		JUNGLE_EDGE(Biome.JUNGLE_EDGE),
		DEEP_OCEAN(Biome.DEEP_OCEAN),
		STONE_SHORE("STONE_BEACH"),
		SNOWY_BEACH("COLD_BEACH"),
		BIRCH_FOREST(Biome.BIRCH_FOREST),
		BIRCH_FOREST_HILLS(Biome.BIRCH_FOREST_HILLS),
		DARK_FOREST("ROOFED_FOREST"),
		SAVANNA(Biome.SAVANNA),
		BADLANDS("MESA");
		
		public static @Nullable To19Mapping getMapping(Biome biome) {
			To19Mapping[] values = values();
			
			for (To19Mapping value : values) {
				if (value.getHandle().equals(biome)) {
					return value;
				}
			}
			
			return null;
		}
		
		private Biome handle;
		
		To19Mapping(Biome handle) {
			this.handle = handle;
		}
		
		To19Mapping(String name) {
			this.handle = Biome.valueOf(name);
		}
		
		public Biome getHandle() {
			return this.handle;
		}
	}
}
