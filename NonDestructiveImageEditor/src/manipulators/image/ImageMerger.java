package manipulators.image;

import model.IPixelCollection;
import model.PixelArray;
import model.ResolutionWrapper;

public class ImageMerger {
	
	private static ImageMerger singleton = new ImageMerger();
	
	private ImageMerger() {
	}
	
	public static ImageMerger getInstance(){
		return singleton;
	}
	
	public PixelArray merge(PixelArray bottomLayer, PixelArray topLayerr){
		// TODO: handle different layersizes
		PixelArray merged = bottomLayer;
		ResolutionWrapper topLayer = new ResolutionWrapper(topLayerr, bottomLayer.getWidth(), bottomLayer.getHeight());
		for(int x = 0; x < merged.getWidth(); x++){
			for(int y = 0; y < merged.getHeight(); y++){
				
				int[] mergrgba = new int[4];
				int[] botrgba = bottomLayer.getRGBA(x, y);
				if(x < topLayer.getWidth() && y < topLayer.getHeight()){
					int[] toprgba = topLayer.getRGBA(x, y);
					int botalpha = botrgba[3];
					int topalpha = toprgba[3];
					int alpharemainder = 255 - topalpha;
					
					int topRA = (toprgba[0]*topalpha)/255;
					int topGA = (toprgba[1]*topalpha)/255;
					int topBA = (toprgba[2]*topalpha)/255;
					
					int botRA = (botrgba[0]*botalpha)/255;
					int botGA = (botrgba[1]*botalpha)/255;
					int botBA = (botrgba[2]*botalpha)/255;
					
					if(true){
					mergrgba[0] = ((toprgba[0]*topalpha)/255) + ((botrgba[0]*botalpha)*alpharemainder)/(255*255);
					mergrgba[1] = ((toprgba[1]*topalpha)/255) + ((botrgba[1]*botalpha)*alpharemainder)/(255*255);
					mergrgba[2] = ((toprgba[2]*topalpha)/255) + ((botrgba[2]*botalpha)*alpharemainder)/(255*255);
//					mergrgba[0] = topRA + (botRA*alpharemainder)/255;
//					mergrgba[1] = topGA + (botGA*alpharemainder)/255;
//					mergrgba[2] = topBA + (botBA*alpharemainder)/255;
					mergrgba[3] = botalpha + topalpha;
					} else {
						// 1st part for opacity	--		-- blend function part --		-- 2nd part for opacity
						mergrgba[0] = botrgba[0] + ((	((toprgba[0] * botrgba[0])>>8)	*topalpha)/255);
						mergrgba[1] = botrgba[1] + ((	((toprgba[1] * botrgba[1])>>8)	*topalpha)/255);
						mergrgba[2] = botrgba[2] + ((	((toprgba[2] * botrgba[2])>>8)	*topalpha)/255);
						mergrgba[3] = botalpha;
					}
					merged.setRGBA(x, y, mergrgba);
				} else {
					merged.setRGBA(x, y, new int[]{botrgba[0],botrgba[1],botrgba[2],botrgba[3]});
				}
				
			}
		}
		return merged;
	}
}
