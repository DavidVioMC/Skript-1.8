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
package ch.njol.skript.util;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.EntityEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import ch.njol.skript.Skript;
import ch.njol.skript.aliases.ItemType;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.SyntaxElement;
import ch.njol.skript.lang.SyntaxElementInfo;
import ch.njol.skript.localization.Language;
import ch.njol.skript.localization.LanguageChangeListener;
import ch.njol.skript.localization.Noun;
import ch.njol.skript.variables.Variables;
import ch.njol.util.Kleenean;
import ch.njol.util.StringUtils;
import ch.njol.util.coll.iterator.SingleItemIterator;
import ch.njol.yggdrasil.YggdrasilSerializable;

/**
 * @author Peter Güttinger
 */
public final class VisualEffect implements SyntaxElement, YggdrasilSerializable {

	private final static String LANGUAGE_NODE = "visual effects";
	static final boolean newEffectData = Skript.classExists("org.bukkit.block.data.BlockData");
	
	public static enum Type implements YggdrasilSerializable {
		ENDER_SIGNAL(Effect.ENDER_SIGNAL),
		MOBSPAWNER_FLAMES(Effect.MOBSPAWNER_FLAMES),
		POTION_BREAK(Effect.POTION_BREAK) {
			@Override
			public Object getData(final @Nullable Object raw, final Location l) {
				return new PotionEffect(raw == null ? PotionEffectType.SPEED : (PotionEffectType) raw, 1, 0);
			}
		},
		SMOKE(Effect.SMOKE) {
			@Override
			public Object getData(final @Nullable Object raw, final Location l) {
				if (raw == null)
					return BlockFace.SELF;
				return Direction.getFacing(((Direction) raw).getDirection(l), false); // TODO allow this to not be a literal
			}
		},
		HURT(EntityEffect.HURT),
		// Omit DEATH, because it causes client glitches
		WOLF_SMOKE(EntityEffect.WOLF_SMOKE),
		WOLF_HEARTS(EntityEffect.WOLF_HEARTS),
		WOLF_SHAKE(EntityEffect.WOLF_SHAKE),
		SHEEP_EAT("SHEEP_EAT", true), // Was once mistakenly removed from Spigot
		IRON_GOLEM_ROSE(EntityEffect.IRON_GOLEM_ROSE),
		VILLAGER_HEARTS(EntityEffect.VILLAGER_HEART),
		VILLAGER_ENTITY_ANGRY(EntityEffect.VILLAGER_ANGRY),
		VILLAGER_ENTITY_HAPPY(EntityEffect.VILLAGER_HAPPY),
		WITCH_MAGIC(EntityEffect.WITCH_MAGIC),
		ZOMBIE_TRANSFORM(EntityEffect.ZOMBIE_TRANSFORM),
		FIREWORK_EXPLODE(EntityEffect.FIREWORK_EXPLODE),
		
		// Spigot 2017 entity effects update (1.10+)
		ARROW_PARTICLES("ARROW_PARTICLES", true),
		RABBIT_JUMP("RABBIT_JUMP", true),
		LOVE_HEARTS("LOVE_HEARTS", true),
		SQUID_ROTATE("SQUID_ROTATE", true),
		ENTITY_POOF("ENTITY_POOF", true),
		GUARDIAN_TARGET("GUARDIAN_TARGET", true),
		SHIELD_BLOCK("SHIELD_BLOCK", true),
		SHIELD_BREAK("SHIELD_BREAK", true),
		ARMOR_STAND_HIT("ARMOR_STAND_HIT", true),
		THORNS_HURT("THORNS_HURT", true),
		IRON_GOLEM_SHEATH("IRON_GOLEM_SHEATH", true),
		TOTEM_RESURRECT("TOTEM_RESURRECT", true),
		HURT_DROWN("HURT_DROWN", true),
		HURT_EXPLOSIION("HURT_EXPLOSION", true),
		
		// Particles
		FIREWORKS_SPARK(Particle.FIREWORKS_SPARK),
		CRIT(Particle.CRIT),
		MAGIC_CRIT(Particle.CRIT_MAGIC),
		POTION_SWIRL(Particle.SPELL_MOB) {
			@Override
			public boolean isColorable() {
				return true;
			}
		},
		POTION_SWIRL_TRANSPARENT(Particle.SPELL_MOB_AMBIENT) {
			@Override
			public boolean isColorable() {
				return true;
			}
		},
		SPELL(Particle.SPELL),
		INSTANT_SPELL(Particle.SPELL_INSTANT),
		WITCH_SPELL(Particle.SPELL_WITCH),
		NOTE(Particle.NOTE),
		PORTAL(Particle.PORTAL),
		FLYING_GLYPH(Particle.ENCHANTMENT_TABLE),
		FLAME(Particle.FLAME),
		LAVA_POP(Particle.LAVA),
		FOOTSTEP("FOOTSTEP"), // 1.13 removed
		SPLASH(Particle.WATER_SPLASH),
		PARTICLE_SMOKE(Particle.SMOKE_NORMAL), // Why separate particle... ?
		EXPLOSION_HUGE(Particle.EXPLOSION_HUGE),
		EXPLOSION_LARGE(Particle.EXPLOSION_LARGE),
		EXPLOSION(Particle.EXPLOSION_NORMAL),
		VOID_FOG(Particle.SUSPENDED_DEPTH),
		SMALL_SMOKE(Particle.TOWN_AURA),
		CLOUD(Particle.CLOUD),
		COLOURED_DUST(Particle.REDSTONE) {
			@Override
			public boolean isColorable() {
				return true;
			}
		},
		SNOWBALL_BREAK(Particle.SNOWBALL),
		WATER_DRIP(Particle.DRIP_WATER),
		LAVA_DRIP(Particle.DRIP_LAVA),
		SNOW_SHOVEL(Particle.SNOW_SHOVEL),
		SLIME(Particle.SLIME),
		HEART(Particle.HEART),
		ANGRY_VILLAGER(Particle.VILLAGER_ANGRY),
		HAPPY_VILLAGER(Particle.VILLAGER_HAPPY),
		LARGE_SMOKE(Particle.SMOKE_LARGE),
		ITEM_CRACK(Particle.ITEM_CRACK) {
			@Override
			public Object getData(final @Nullable Object raw, final Location l) {
				if (raw == null)
					return Material.IRON_SWORD;
				else if (raw instanceof ItemType) {
					ItemStack rand = ((ItemType) raw).getRandom();
					if (rand == null) return Material.IRON_SWORD;
					Material type = rand.getType();
					assert type != null;
					return type;
				} else {
					return raw;
				}
			}
		},
		BLOCK_BREAK(Particle.BLOCK_CRACK) {
			@Override
			public Object getData(final @Nullable Object raw, final Location l) {
				if (raw == null)
					return Material.STONE.getData();
				else if (raw instanceof ItemType) {
					if (newEffectData) {
						ItemStack rand = ((ItemType) raw).getRandom();
						if (rand == null)
							return Bukkit.createBlockData(Material.STONE);
						return Bukkit.createBlockData(rand.getType());
					} else {
						ItemStack rand = ((ItemType) raw).getRandom();
						if (rand == null)
							return Material.STONE.getData();
						@SuppressWarnings("deprecation")
						MaterialData type = rand.getData();
						assert type != null;
						return type;
					}
				} else {
					return raw;
				}
			}
		},
		BLOCK_DUST(Particle.BLOCK_DUST) {
			@Override
			public Object getData(final @Nullable Object raw, final Location l) {
				if (raw == null)
					return Material.STONE.getData();
				else if (raw instanceof ItemType) {
					if (newEffectData) {
						ItemStack rand = ((ItemType) raw).getRandom();
						if (rand == null)
							return Bukkit.createBlockData(Material.STONE);
						return Bukkit.createBlockData(rand.getType());
					} else {
						ItemStack rand = ((ItemType) raw).getRandom();
						if (rand == null)
							return Material.STONE.getData();
						@SuppressWarnings("deprecation")
						MaterialData type = rand.getData();
						assert type != null;
						return type;
					}
				} else {
					return raw;
				}
			}
		},
		
		// 1.11 particles
		TOTEM("TOTEM"),
		SPIT("SPIT"),
		
		// 1.13 particles
		SQUID_INK("SQUID_INK"),
		BUBBLE_POP("BUBBLE_POP"),
		CURRENT_DOWN("CURRENT_DOWN"),
		BUBBLE_COLUMN_UP("BUBBLE_COLUMN_UP"),
		NAUTILUS("NAUTILUS"),
		DOLPHIN("DOLPHIN"),
		
		// 1.14 particles
		SNEEZE("SNEEZE"),
		CAMPFIRE_COSY_SMOKE("CAMPFIRE_COSY_SMOKE"),
		CAMPFIRE_SIGNAL_SMOKE("CAMPFIRE_SIGNAL_SMOKE"),
		COMPOSTER("COMPOSTER"),
		FLASH("FLASH"),
		FALLING_LAVA("FALLING_LAVA"),
		LANDING_LAVA("LANDING_LAVA"),
		FALLING_WATER("FALLING_WATER");
		
		
		@Nullable
		final Object effect;
		@Nullable
		final String name;
		
		private Type(final Effect effect) {
			this.effect = effect;
			this.name = effect.name();
		}
		
		private Type(final EntityEffect effect) {
			this.effect = effect;
			this.name = null;
		}
		
		private Type(final Particle effect) {
			this.effect = effect;
			this.name = null;
		}
		
		private Type(final String name) {
			this(name, false);
		}
		
		private Type(final String name, boolean entityEffect) {
			this.name = null;
			if (entityEffect) {
				EntityEffect real = null;
				try {
					real = EntityEffect.valueOf(name);
				} catch (IllegalArgumentException e) {
					// This Spigot version is idiotic
				}
				this.effect = real;
			} else {
				Particle real = null;
				try {
					real = Particle.valueOf(name);
				} catch (IllegalArgumentException e) {
					// This MC version does not support this particle...
				}
				this.effect = real;
			}
		}
		
		/**
		 * Converts the data from the pattern to the data required by Bukkit
		 */
		@Nullable
		public Object getData(final @Nullable Object raw, final Location l) {
			assert raw == null;
			return null;
		}
		
		/**
		 * Checks if this effect has color support.
		 */
		public boolean isColorable() {
			return false;
		}
		
		/**
		 * Gets Minecraft name of the effect, if it exists.
		 * @return Name or null if effect uses numeric id instead.
		 */
		@Nullable
		public String getMinecraftName() {
			return this.name;
		}
	}
	
	private final static String TYPE_ID = "VisualEffect.Type";
	static {
		Variables.yggdrasil.registerSingleClass(Type.class, TYPE_ID);
		Variables.yggdrasil.registerSingleClass(Effect.class, "Bukkit_Effect");
		Variables.yggdrasil.registerSingleClass(EntityEffect.class, "Bukkit_EntityEffect");
	}
	
	@Nullable
	static SyntaxElementInfo<VisualEffect> info;
	final static List<Type> types = new ArrayList<>(Type.values().length);
	final static Noun[] names = new Noun[Type.values().length];
	static {
		Language.addListener(new LanguageChangeListener() {
			@Override
			public void onLanguageChange() {
				final Type[] ts = Type.values();
				types.clear();
				final List<String> patterns = new ArrayList<>(ts.length);
				for (int i = 0; i < ts.length; i++) {
					final String node = LANGUAGE_NODE + "." + ts[i].name();
					final String pattern = Language.get_(node + ".pattern");
					if (pattern == null) {
						if (Skript.testing())
							Skript.warning("Missing pattern at '" + (node + ".pattern") + "' in the " + Language.getName() + " language file");
					} else {
						types.add(ts[i]);
						if (ts[i].isColorable())
							patterns.add(pattern);
						else {
							String dVarExpr = Language.get_(LANGUAGE_NODE + ".area_expression");
							if (dVarExpr == null) dVarExpr = "";
							patterns.add(pattern + " " + dVarExpr);
						}
					}
					if (names[i] == null)
						names[i] = new Noun(node + ".name");
				}
				final String[] ps = patterns.toArray(new String[patterns.size()]);
				assert ps != null;

				info = new SyntaxElementInfo<>(ps, VisualEffect.class, getOriginPath(VisualEffect.class));
			}
		});
	}

	static String getOriginPath(Class<VisualEffect> c){
		String path = c.getName();
		if (path != null){
			return path;
		}
		return "<null>";
	}
	
	private Type type;
	@Nullable
	private Object data;
	private float speed = 0;
	private float dX, dY, dZ = 0;
	
	/**
	 * For parsing & deserialisation
	 */
	@SuppressWarnings("null")
	public VisualEffect() {}
	
	@SuppressWarnings("null")
	@Override
	public boolean init(final Expression<?>[] exprs, final int matchedPattern, final Kleenean isDelayed, final ParseResult parseResult) {
		type = types.get(matchedPattern);
		
		if (type.effect == null) {
			Skript.error("Minecraft " + Skript.getMinecraftVersion() + " version does not support particle " + type);
			return false;
		}
		
		if (type.isColorable()) {
			for (Expression<?> expr : exprs) {
				if (expr == null) continue;
				else if (expr.getReturnType().isAssignableFrom(Color.class)) {
					org.bukkit.Color color = ((Color) expr.getSingle(null)).asBukkitColor();
					
					/*
					 * Colored particles use dX, dY and dZ as RGB values which
					 * have range from 0 to 1.
					 * 
					 * For now, only speed exactly 1 is allowed.
					 */
					dX = color.getRed() / 255.0f + 0.00001f;
					dY = color.getGreen() / 255.0f;
					dZ = color.getBlue() / 255.0f;
					speed = 1;
				} else {
					Skript.exception("Color not specified for colored particle");
				}
			}
		} else {
			int numberParams = 0;
			for (Expression<?> expr : exprs) {
				if (expr.getReturnType() == Long.class || expr.getReturnType() == Integer.class || expr.getReturnType() == Number.class)
					numberParams++;
			}
			
			int dPos = 0; // Data index
			Expression<?> expr = exprs[0];
			if (expr.getReturnType() != Long.class && expr.getReturnType() != Integer.class && expr.getReturnType() != Number.class) {
				dPos = 1;
				data = exprs[0].getSingle(null);
			}
			
			if (numberParams == 1) // Only speed
				speed = ((Number) exprs[dPos].getSingle(null)).floatValue();
			else if (numberParams == 3) { // Only dX, dY, dZ
				dX = ((Number) exprs[dPos].getSingle(null)).floatValue();
				dY = ((Number) exprs[dPos + 1].getSingle(null)).floatValue();
				dZ = ((Number) exprs[dPos + 2].getSingle(null)).floatValue();
			} else if (numberParams == 4){ // Both present
				dX = ((Number) exprs[dPos].getSingle(null)).floatValue();
				dY = ((Number) exprs[dPos + 1].getSingle(null)).floatValue();
				dZ = ((Number) exprs[dPos + 2].getSingle(null)).floatValue();
				speed = ((Number) exprs[dPos + 3].getSingle(null)).floatValue();
			}
		}
		
		return true;
	}
	
	/**
	 * Entity effects are always played on entities.
	 * @return If this is an entity effect.
	 */
	public boolean isEntityEffect() {
		return type.effect instanceof EntityEffect;
	}
	
	/**
	 * Particles are implemented differently from traditional effects in
	 * Minecraft. Most new visual effects are particles.
	 * @return If this is a particle effect.
	 */
	public boolean isParticle() {
		return type.effect instanceof Particle;
	}
	
	@Nullable
	public static VisualEffect parse(final String s) {
		final SyntaxElementInfo<VisualEffect> info = VisualEffect.info;
		if (info == null)
			return null;
		return SkriptParser.parseStatic(Noun.stripIndefiniteArticle(s), new SingleItemIterator<>(info), null);
	}
	
	public void play(final @Nullable Player[] ps, final Location l, final @Nullable Entity e) {
		play(ps, l, e, 0, 32);
	}
	
	public void play(final @Nullable Player[] ps, final Location l, final @Nullable Entity e, final int count, final int radius) {
		assert e == null || l.equals(e.getLocation());
		if (isEntityEffect()) {
			if (e != null) {
				assert type.effect != null;
				e.playEffect((EntityEffect) type.effect);
			}
		} else {
			if (type.effect instanceof Particle) {
				// Particle effect...
				Particle particle = (Particle) type.effect;
				assert particle != null;
				Object pData = type.getData(data, l);
				
				assert type.effect != null;
				// Check that data has correct type (otherwise bad things will happen)
				if (pData != null && !((Particle) type.effect).getDataType().isAssignableFrom(pData.getClass())) {
					pData = null;
					if (Skript.debug())
						Skript.warning("Incompatible particle data, resetting it!");
				}
				
				if (ps == null) {
					// Colored particles must be played one at time; otherwise, colors are broken
					if (type.isColorable()) {
						for (int i = 0; i < count; i++) {
							l.getWorld().spawnParticle(particle, l, 0, dX, dY, dZ, speed, pData);
						}
					} else {
						l.getWorld().spawnParticle(particle, l, count, dX, dY, dZ, speed, pData);
					}
				} else {
					for (final Player p : ps) {
						if (type.isColorable()) {
							for (int i = 0; i < count; i++) {
								p.spawnParticle(particle, l, 0, dX, dY, dZ, speed, pData);
							}
						} else {
							p.spawnParticle(particle, l, count, dX, dY, dZ, speed, pData);
						}
					}
				}
			} else {
				// Non-particle effect (whatever Spigot API says, there are a few)
				Effect effect = (Effect) type.effect;
				assert effect != null;
				if (ps == null) {
					//l.getWorld().spigot().playEffect(l, (Effect) type.effect, 0, 0, dX, dY, dZ, speed, count, radius);
					l.getWorld().playEffect(l, effect, 0, radius);
				} else {
					for (final Player p : ps)
						p.playEffect(l, effect, type.getData(data, l));
				}
			}
		}
	}
	
	@Override
	public String toString() {
		return toString(0);
	}
	
	public String toString(final int flags) {
		return names[type.ordinal()].toString(flags);
	}
	
	public static String getAllNames() {
		return StringUtils.join(names, ", ");
	}
	
	/**
	 * Gets Bukkit effect backing this visual effect. It may be either
	 * {@link Effect}, {@link EntityEffect} or {@link Particle}.
	 * @return Backing effect.
	 * @throws IllegalStateException When this is called before the effect
	 * is initialized. Note that
	 * {@link #init(Expression[], int, Kleenean, ParseResult)} may fail when
	 * the used Minecraft version lacks support for effect used.
	 */
	public Object getEffect() {
		Object effect = type.effect;
		if (effect == null) { // init() not called or returned false
			throw new IllegalStateException("effect not initialized");
		}
		return effect;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + type.hashCode();
		final Object d = data;
		result = prime * result + ((d == null) ? 0 : d.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(final @Nullable Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof VisualEffect))
			return false;
		final VisualEffect other = (VisualEffect) obj;
		if (type != other.type)
			return false;
		final Object d = data;
		if (d == null) {
			if (other.data != null)
				return false;
		} else if (!d.equals(other.data)) {
			return false;
		}
		return true;
	}
	
}
