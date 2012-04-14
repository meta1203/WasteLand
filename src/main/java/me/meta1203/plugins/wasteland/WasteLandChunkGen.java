package me.meta1203.plugins.wasteland;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.util.noise.SimplexOctaveGenerator;

public class WasteLandChunkGen extends ChunkGenerator {
	SimplexOctaveGenerator sng = null;
	
	public WasteLandChunkGen(long seed) {
		sng = new SimplexOctaveGenerator(seed, 16);
	}
	
	public WasteLandChunkGen() {
	}
	
	public boolean canSpawn(World world, int x, int z) {
        return true;
    }
	
	public List<BlockPopulator> getDefaultPopulators(World world) {
        return Arrays.asList(new Populator_Ores(), new Populator_Caves(), new TreePopulator(),
        		new Populator_Lava_Lakes());
    }
	
	public byte[][] generateBlockSections(World world, Random random, int xChunk, int zChunk, BiomeGrid biomes) {
		if (sng == null) {
			sng = new SimplexOctaveGenerator(random, 16);
		}
		
		byte[][] result = new byte[16][];
		
		for (int x = 0; x < 16; x++) {
			for (int z = 0; z < 16; z++) {
				double noise_raw1 = sng.noise((x + xChunk * 16) / 250.0f, (z + zChunk * 16) / 250.0f, 1, 0.6) * 20;
				int noise = (int)noise_raw1 + 64;
				// System.out.println(noise);
				applyHeightMap(x,z,result,noise);
				genWater(x,z,result);
				biomes.setBiome(x, z, Biome.DESERT);
			}
		}
		
		return result;
	}
	
	private static void setMaterialAt(byte[][] chunk_data, int x, int y, int z, Material material) {
		int sec_id = (y >> 4);
		int yy = y & 0xF;
		if (chunk_data[sec_id] == null) {
			chunk_data[sec_id] = new byte[4096];
		}
		chunk_data[sec_id][(yy << 8) | (z << 4) | x] = (byte) material.getId();
	}
	
	private void applyHeightMap(int x, int z, byte[][] chunk_data, int currheight) {
		for (int y = currheight; y < currheight+4; y++) {
			setMaterialAt(chunk_data, x, y, z, Material.DIRT);
		}
		for (int y = 0; y <= currheight; y++) {
			setMaterialAt(chunk_data, x, y, z, Material.STONE);
		}
	}
	
	private void genWater(int x, int z, byte[][] chunk_data) {
		int y = 42;
		while (y > 20) {
			if (getMaterialAt(chunk_data, x, y, z) == Material.AIR) {
				setMaterialAt(chunk_data, x, y, z, Material.STATIONARY_WATER);
			}
			y--;
		}
	}
	
	private static Material getMaterialAt(byte[][] chunk_data, int x, int y, int z) {
		int sec_id = (y >> 4);
		int yy = y & 0xF;
		if (chunk_data[sec_id] == null) {
			return Material.AIR;
		} else {
			return Material.getMaterial(chunk_data[sec_id][(yy << 8) | (z << 4) | x]);
		}
	}
}