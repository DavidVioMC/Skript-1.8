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
package ch.njol.skript.util.slot;

import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.eclipse.jdt.annotation.Nullable;

import ch.njol.skript.Skript;
import ch.njol.skript.bukkitutil.PlayerUtils;
import ch.njol.skript.registrations.Classes;
import ch.njol.skript.util.BlockInventoryHolder;

/**
 * Represents a slot in some inventory.
 */
public class InventorySlot extends SlotWithIndex {
	
	private final Inventory invi;
	private final int index;
	
	public InventorySlot(final Inventory invi, final int index) {
		assert invi != null;
		assert index >= 0;
		this.invi = invi;
		this.index = index;
	}
	
	public Inventory getInventory() {
		return invi;
	}
	
	@Override
	public int getIndex() {
		return index;
	}
	
	@Override
	@Nullable
	public ItemStack getItem() {
		ItemStack item = invi.getItem(index);
		return item == null  ? new ItemStack(Material.AIR, 1) : item.clone();
	}
	
	@Override
	public void setItem(final @Nullable ItemStack item) {
		invi.setItem(index, item != null && item.getType() != Material.AIR ? item : null);
		if (invi instanceof PlayerInventory)
			PlayerUtils.updateInventory((Player) invi.getHolder());
	}
	
	@Override
	public String toString(@Nullable Event e, boolean debug) {
		InventoryHolder holder = invi.getHolder();
		
		if (holder instanceof BlockState)
			holder = new BlockInventoryHolder((BlockState) holder);
		
		if (invi.getHolder() != null) {
			if (invi instanceof CraftingInventory) // 4x4 crafting grid is contained in player too!
				return "crafting slot " + index + " of " + Classes.toString(holder);
			
			return "inventory slot " + index + " of " + Classes.toString(holder);
		}
		return "inventory slot " + index + " of " + Classes.toString(invi);
	}
	
}
