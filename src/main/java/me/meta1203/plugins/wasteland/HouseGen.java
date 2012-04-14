package me.meta1203.plugins.wasteland;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;
import org.jnbt.*;

public class HouseGen extends BlockPopulator {
	
	private List<byte[][][]> houses = new ArrayList<byte[][][]>();
	
	public HouseGen() {
		houses.add(houseGet("/house1"));
		houses.add(houseGet("/house2"));
	}
	
	@Override
	public void populate(World arg0, Random arg1, Chunk arg2) {
		int rand1 = arg1.nextInt(100);
		if (rand1 == 0) {
			int rand2 = arg1.nextInt(2);
			genHouse(rand2, arg2);
		}
	}
	
	private byte[][][] houseGet(String file) {
		byte[][][] house1 = null;
		try {
			NBTInputStream rawHouse1 = new NBTInputStream(Wasteland.class.getResourceAsStream(file));
			CompoundTag ct = (CompoundTag)rawHouse1.readTag();
			house1 = new byte [((ShortTag)ct.getValue().get("Width")).getValue()][((ShortTag)ct.getValue().get("Height")).getValue()][((ShortTag)ct.getValue().get("Length")).getValue()];
			
			int width = house1.length;
			int height = house1[0].length;
			System.out.println(((ByteArrayTag)ct.getValue().get("Blocks")).getValue().length);
			
			for (int x = 0; x < house1.length-1; x++) {
				for (int y = 0; y < house1[x].length-1; y++) {
					for (int z = 0; z < house1[x][y].length-1; z++) {
						if (z*width*height+z*width+y >= ((ByteArrayTag)ct.getValue().get("Blocks")).getValue().length) {
							System.out.println(z*width*height+z*width+y);
							continue;
						}
						house1[x][y][z] = ((ByteArrayTag)ct.getValue().get("Blocks")).getValue()[z*width*height+z*width+y];
					}
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return house1;
	}
	
	private void genHouse(int houseNum, Chunk c) {
		byte[][][] toBuild = houses.get(houseNum);
		Random r = new Random();
		int x = r.nextInt(16);
		int z = r.nextInt(16);
		x += c.getX() * 16;
		z += c.getZ() * 16;
		World w = c.getWorld();
		int y = w.getHighestBlockYAt(x,z);
		
		for (int x1 = x; x1 < x + toBuild.length; x1++) {
			for (int y1 = y; y1 < y + toBuild[0].length; y1++) {
				for (int z1 = z; z1 < z + toBuild[0][0].length; z1++) {
					w.getBlockAt(x1, y1, z1).setTypeId(toBuild[x1-x][y1-y][z1-z]);
				}
			}
		}
		
	}
}
