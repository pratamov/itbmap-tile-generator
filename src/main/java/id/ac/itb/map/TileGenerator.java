package id.ac.itb.map;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.imageio.ImageIO;

public class TileGenerator {

	public static void main(String[] args) throws IOException {
		if (args.length == 2){
			String mapName = args[0];
			String rawMapImagePath = args[1];
			File mapFile = new File(rawMapImagePath);
			
			List<Integer> detailLevels = Arrays.asList(125, 250, 500, 1000);
			for (int detailLevel : detailLevels){
				createTileFolder(mapFile.getParent() + File.separator + mapName, detailLevel);
				createTile(rawMapImagePath, mapName, detailLevel);
			}
		}
		else{
			System.out.println("Usage : [map name] [raw map image path]");
		}
	}
	
	public static void createTileFolder(String directory, int detailLevel){
		File mapFolder = new File(directory + File.separator + detailLevel);
		if (!mapFolder.exists())
			mapFolder.mkdirs();
	}

	public static void createTile(String rawMapImagePath, String mapName, int detailLevel) throws IOException {
		
		File mapFile = new File(rawMapImagePath);
		
		String mapFileName = mapFile.getName();
		
		String mapExtension = mapFileName.substring(mapFileName.lastIndexOf(".") + 1);
		
		FileInputStream fis = new FileInputStream(mapFile);
		BufferedImage mapBufferedImage = ImageIO.read(fis);
		
		int cols = (int) (detailLevel / 62.5);
		int rows = cols;
		
		int chunkWidth = mapBufferedImage.getWidth() / cols;
		int chunkHeight = mapBufferedImage.getHeight() / rows;

		System.out.println(cols + ":" + rows);
		System.out.println(chunkWidth + ":" + chunkHeight);
		
		for (int x = 0; x < rows; x++) {
			for (int y = 0; y < cols; y++) {
				BufferedImage imageChunk = new BufferedImage(chunkWidth, chunkHeight, mapBufferedImage.getType());

				Graphics2D gr = imageChunk.createGraphics();
				gr.drawImage(mapBufferedImage, 0, 0, chunkWidth, chunkHeight, chunkWidth * y, chunkHeight * x,
						chunkWidth * y + chunkWidth, chunkHeight * x + chunkHeight, null);
				gr.dispose();
				
				BufferedImage newImage = new BufferedImage(256, 256, BufferedImage.TYPE_INT_RGB);
				Graphics g = newImage.createGraphics();
				g.drawImage(imageChunk, 0, 0, 256, 256, null);
				g.dispose();
				
				String filename = y + "_" + x + "." + mapExtension;
				File file = new File(mapFile.getParent() + File.separator + mapName + File.separator + detailLevel + File.separator + filename);
				try {
					ImageIO.write(newImage, mapExtension, file);
				} catch (IOException e) {
				}
			}
		}
	}
}
