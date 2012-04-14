package me.meta1203.plugins.wasteland;

import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.util.Vector;

public class TreePopulator extends BlockPopulator {

	@Override
	public void populate(World arg0, Random arg1, Chunk arg2) {
		genTree(arg2, arg1);
	}
	
	public void genTree(Chunk c, Random r) {
		// System.out.println("Generating tree");
		int testTree = r.nextInt(10);
		if (testTree == 0) {
			int x = r.nextInt(16);
			int z = r.nextInt(16);
			int xPos = x + c.getX()*16;
			int zPos = z + c.getZ()*16;
			int yPos = c.getWorld().getHighestBlockYAt(xPos, zPos);
			genTree(x, yPos, z, c);
		}
	}
	
	public void genTree(int x, int y, int z, Chunk c) {
		// System.out.println("Generating tree2");
		Random r = new Random();
		
		int base = r.nextInt(7);
		
		x &= 0xf;
		z &= 0xf;
		// System.out.println("Generating tree base");
		for (int count = y; count < y + base; count++) {
			c.getBlock(x, count, z).setType(Material.LOG);
		}
		
		Vector center = new Vector(x,y,z);
		// System.out.println("Generating tree branches");
		for (int x1 = x-3; x1 <= x+3; x1++) {
			for (int y1 = y+base-3; y1 <= y+base+3; y1++) {
				for (int z1 = z-3; z1 <= z+3; z1++) {
					Vector current = new Vector(x1,y1,z1);
					if ((current.distance(center) <= 3) && (r.nextInt(15) == 0)) {
						if (r.nextInt(3) == 0) {
							c.getBlock(x1, y1, z1).setType(Material.LOG);
						}
					}
				}
			}
		}
		
	}
}