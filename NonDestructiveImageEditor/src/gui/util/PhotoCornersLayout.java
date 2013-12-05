package gui.util;
/* PhotoCornersLayout.java
 * 
 * Copyright (c) 2013 David Haegele
 *
 * (MIT License)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager2;
import java.util.HashMap;
import java.util.Map;

/**
 * This LayoutManager allows you to specify layouts of children, like they
 * were photos you wanted to arrange on the parent using two photo corners.
 * <br>
 * This means you specify the location of the top-left and bottom-right 
 * corner on the parent. The child will fit into these bounds and shrink or
 * grow as the parent changes dimension.
 * <p>
 * <b>Now the cool thing:</b> you can specify a corner to always keep the same 
 * distance (e.g. 20px) to one (or 2) of the parents borders. Or you can 
 * make it keep a relative distance of e.g. 10% of the parents width or height
 * to one of its borders.
 * <br>
 * Say you want your child component to keep a distance of 10px to the parents
 * left border, 50px to the parents right border, 20px to the parents bottom
 * and only use the bottom half of the parents area which translates to have a
 * relative distance of 0.5 of the parents height to the parents top border.
 * No Problem. Top-left photo corner gets coordinates (10, 0.5) and 
 * bottom-right gets (-50,-20).
 * <p>
 * <i><b>But how do I pass these constraints to my parent component?</i></b>
 * <br>
 * When adding the child component to the parent component, use a String as
 * constraint argument that contains the coordinates of the photo corners.
 * <br><code>parent.add(child,"topleft(10, 0.5)bottomright(-50,-20)");</code>
 * <p>
 * <h2><u><b>Details on phrasing photo corner constraints:</b></u></h2><p><b>
 * >> General constraint expression << </b><br>
 * The constraint String is always formated like this: <br>
 * "topleft(<i>x1</i>,<i>y1</i>)bottomright(<i>x2</i>,<i>y2</i>)" <br>
 * The variables <i>x1, x2, y1, y2</i> can get values of type integer 
 * (e.g. 20), float (e.g. 0.5) or fraction (e.g. 1/2). x1 and y1 specify the 
 * location and behaviour of the top-left photo corner, x2 and y2 correspond
 * to the bottom-right one.
 * <p><b>
 * >> Fixed distance to parents border (sticky photo corner) << </b><br>
 * To specify a fixed distance you have to use an <b>integer</b> value for the 
 * corresponding variable. x1 refers to the horizontal distance of the 
 * top-left photo corner to the parents left border, <i>when taking a positive
 * value (!)</i>.<br>
 * When taking <i>a negative value</i>, x1 determines the horizontal distance
 * of the top-left corner to the parents <i>right (!)</i> border. <br>
 * The same applies to x2 for the bottom-right photo corner (positive value 
 * refers to distance to left border, negative value to the right border, not 
 * vice versa!).<br>
 * For the vertical distances y1 and y2 are used. A positive value for y1 
 * specifies the horizontal distance of the top-left corner to the parents top 
 * border. A negative value corresponds to the parents bottom border. Same 
 * applies to y2 (positive -> top, negative -> bottom)
 * <p><b>
 * >> Relative distance to parents border (gliding photo corner) << </b><br>
 * To spcify a relative distance you have to use a <b>float or fraction</b>
 * value for the corresponding variable. The value has to be within 
 * <i>[0.0 .. 1.0]</i>. To setup a relative horizontal distance to the 
 * parents left border, use a nonegative value. Choosing x1=0.0 will
 * lign up the top-left photo corner with the parents left border. x1=0.5
 * (or x1=1/2) will put the photo corner in horizontal center of the parent.
 * x1=1.0 makes the corner line up with the parents right border. The same
 * applies to x2 for the bottom-right photo corner. <br>
 * For the vertical location y1=0.0 will align the top-left corner with the
 * parents top border y1=1.0 makes it line up with the parents bottom border.
 * y2 behaves similar, controling the vertical location of the bottom-right
 * corner.
 * <p><b>
 * >> Mixed corner coordinates (photo corners on rails) << </b><br>
 * When mixing value types for a photo corner coordinate, like setting
 * a float value for x1 and an integer for y1, the corner will only glide 
 * horizontally or vertically. "topleft(0.2,10)bottomright(-20,-20)" will
 * make the topleft corner glide only horizontally, while keeping a fixed 
 * distance of 10pixels to the top. The bottom-right photo corner will stick
 * to the parents bottom-right corner at a fixed distance of 20px to each
 * side.
 * 
 * @author David Haegele
 * @version 1.1 - 26.11.13
 *
 */
public class PhotoCornersLayout implements LayoutManager2 {
	
	/** Stores all constraint objects of the respective components */
	protected Map<Component, Constraint> constraintsMap = new HashMap<>();
	
	@Override
	@Deprecated
	public void addLayoutComponent(String name, Component comp) {
		// Do nothing
	}

	@Override
	public void layoutContainer(Container parent) {
		synchronized (parent.getTreeLock()) {
			for(Component c: parent.getComponents()){
				layoutComponent(parent, c);
			}
		}
	}
	
	/** 
	 * lays out the specified component
	 * @param parent containing the component
	 * @param child component to be laid out (sized and positioned)
	 */
	private void layoutComponent(Container parent, Component child){
		float width = parent.getWidth();
		float height = parent.getHeight();
		Constraint c = constraintsMap.get(child);
		if(c==null){
			child.doLayout();
			return;
		}
		
		int x1Pos = 0;
		if(c.isRelativeXL){
			if(c.isLeftAnchored){
				x1Pos =(int) (width*c.xL);
			} else {
				x1Pos =(int) (width - (width*c.xL));
			}
		} else {
			if(c.isLeftAnchored){
				x1Pos = (int)c.xL;
			} else {
				x1Pos = (int) (width - c.xL);
			}
		}
		
		int y1Pos = 0;
		if(c.isRelativeYT){
			if(c.isTopAnchored){
				y1Pos = (int) (height * c.yT);
			} else {
				y1Pos =(int) (height - (height*c.yT));
			}
		} else {
			if(c.isTopAnchored){
				y1Pos = (int) c.yT;
			} else {
				y1Pos = (int) (height - c.yT);
			}
		}
		
		int x2Pos = 0;
		if(c.isRelativeXR){
			if(c.isRightAnchored){
				x2Pos =(int) (width - (width*c.xR));
			} else {
				x2Pos =(int) (width*c.xR);
			}
		} else {
			if(c.isRightAnchored){
				x2Pos = (int) (width - c.xR);
			} else {
				x2Pos = (int)c.xR;
			}
		}
		
		int y2Pos = 0;
		if(c.isRelativeYB){
			if(c.isBottomAnchored){
				y2Pos =(int) (height - (height*c.yB));
			} else {
				y2Pos = (int) (height * c.yB);
			}
		} else {
			if(c.isBottomAnchored){
				y2Pos = (int) (height - c.yB);
			} else {
				y2Pos = (int) c.yB;
			}
		}
		
		// apply
		child.setSize(x2Pos-x1Pos, y2Pos-y1Pos);
		child.setLocation(x1Pos, y1Pos);
//		child.doLayout(); // not yet sure if that is necessary
//		System.out.println("x1 " + x1Pos);
//		System.out.println("x2 " + x2Pos);
//		System.out.println("y1 " + y1Pos);
//		System.out.println("y2 " + y2Pos);
	}
	
	
	/**
	 * Adds the specified component to the layout, using the specified
	 * constraint object. The second argument (constraint object) has to be a
	 * correctly formatted String.
	 * <p>
	 * <h2><u><b>Rules for phrasing photo corner constraints:</b></u></h2>
	 * <p>
	 * <b> >> General constraint expression << </b><br>
	 * The constraint String is always formated like this: <br>
	 * "topleft(<i>x1</i>,<i>y1</i>)bottomright(<i>x2</i>,<i>y2</i>)" <br>
	 * The variables <i>x1, x2, y1, y2</i> can get values of type integer (e.g.
	 * 20), float (e.g. 0.5) or fraction (e.g. 1/2). x1 and y1 specify the
	 * location and behaviour of the top-left photo corner, x2 and y2 correspond
	 * to the bottom-right one.
	 * <p>
	 * <b> >> Fixed distance to parents border (sticky photo corner) << </b><br>
	 * To specify a fixed distance you have to use an <b>integer</b> value for
	 * the corresponding variable. x1 refers to the horizontal distance of the
	 * top-left photo corner to the parents left border, <i>when taking a
	 * positive value (!)</i>.<br>
	 * When taking <i>a negative value</i>, x1 determines the horizontal
	 * distance of the top-left corner to the parents <i>right (!)</i> border. <br>
	 * The same applies to x2 for the bottom-right photo corner (positive value
	 * refers to distance to left border, negative value to the right border,
	 * not vice versa!).<br>
	 * For the vertical distances y1 and y2 are used. A positive value for y1
	 * specifies the horizontal distance of the top-left corner to the parents
	 * top border. A negative value corresponds to the parents bottom border.
	 * Same applies to y2 (positive -> top, negative -> bottom)
	 * <p>
	 * <b> >> Relative distance to parents border (gliding photo corner) << </b>
	 * <br>
	 * To spcify a relative distance you have to use a <b>float or fraction</b>
	 * value for the corresponding variable. The value has to be within
	 * <i>[0.0 .. 1.0]</i>. To setup a relative horizontal distance to the
	 * parents left border, use a nonegative value. Choosing x1=0.0 will lign up
	 * the top-left photo corner with the parents left border. x1=0.5 (or
	 * x1=1/2) will put the photo corner in horizontal center of the parent.
	 * x1=1.0 makes the corner line up with the parents right border. The same
	 * applies to x2 for the bottom-right photo corner. <br>
	 * For the vertical location y1=0.0 will align the top-left corner with the
	 * parents top border y1=1.0 makes it line up with the parents bottom
	 * border. y2 behaves similar, controling the vertical location of the
	 * bottom-right corner.
	 * <p>
	 * <b> >> Mixed corner coordinates (photo corners on rails) << </b><br>
	 * When mixing value types for a photo corner coordinate, like setting a
	 * float value for x1 and an integer for y1, the corner will only glide
	 * horizontally or vertically. "topleft(0.2,10)bottomright(-20,-20)" will
	 * make the topleft corner glide only horizontally, while keeping a fixed
	 * distance of 10pixels to the top. The bottom-right photo corner will stick
	 * to the parents bottom-right corner at a fixed distance of 20px to each
	 * side.
	 */
	@Override
	public void addLayoutComponent(Component comp, Object constraints) {
		if(constraints == null){
			System.err.println("Warning by " + this.getClass().getCanonicalName() + 
					": no constraint specified for Component \n\t" + 
					comp.toString() + "\n\tof Class " + comp.getClass().getCanonicalName()+
					"\n\tComponent will possibly be displayed incorrectly.");
		} else if(!(constraints instanceof String)){
			System.err.println("Warning by " + this.getClass().getName() + 
					": unsupported constraint specified for Component \n\t" + 
					comp.toString() + "\n\tof Class " + comp.getClass().getName() +
					"\n\tConstraint will be ignored. Component will possibly be displayed incorrectly.");
		} else {
			
		Constraint c = Constraint.parseConstraints((String) constraints);
		constraintsMap.put(comp, c);
		}

	}

	@Override
	public void removeLayoutComponent(Component comp) {
		constraintsMap.remove(comp);
	}
	
	@Override
	@Deprecated
	public float getLayoutAlignmentX(Container target) {
		return 0;
	}

	@Override
	@Deprecated
	public float getLayoutAlignmentY(Container target) {
		return 0;
	}

	@Override
	public void invalidateLayout(Container target) {
	}
	
	
	@Override
	public Dimension minimumLayoutSize(Container parent) {
		float minH= 0;
		float minW= 0;
		synchronized (parent.getTreeLock()) {
			for(Component comp: this.constraintsMap.keySet()){
				Dimension minDim = comp.getMinimumSize();
				if(minDim == null){
					continue;
				}
				float mW = minDim.width;
				float mH = minDim.height;
				Constraint c = this.constraintsMap.get(comp);
				
				// WIDTH
				if(c.isRelativeXL){
					if(c.isRelativeXR){
						if(c.isLeftAnchored && c.isRightAnchored){
							mW = minDim.width/(1-c.xL-c.xR);
						} else if(c.isLeftAnchored && !c.isRightAnchored){
							mW = minDim.width/(c.xR-c.xL);
						} else if(!c.isLeftAnchored && c.isRightAnchored){
							mW = minDim.width/(c.xL-c.xR);
						} else {
							mW = minDim.width/(c.xL+c.xR -1);
						}
					} else {
						if(c.isLeftAnchored && c.isRightAnchored){
							mW = c.xR + minDim.width/(1-c.xL);
						} else if(c.isLeftAnchored && !c.isRightAnchored){
							// nicht zu verwenden
						} else if(!c.isLeftAnchored && c.isRightAnchored){
							mW = (minDim.width + c.xR)/(c.xL);
						} else {
							// nicht zu verwenden
						}
					}
				} else {
					if(c.isRelativeXR){
						if(c.isLeftAnchored && c.isRightAnchored){
							mW = c.xL + minDim.width/(1-c.xR);
						} else if(c.isLeftAnchored && !c.isRightAnchored){
							mW = (minDim.width + c.xL)/(c.xR);
						} else if(!c.isLeftAnchored && c.isRightAnchored){
							// nicht zu verwenden
						} else {
							// nicht zu verwenden
						}
					} else {
						if(c.isLeftAnchored && c.isRightAnchored){
							mW = minDim.width + c.xL + c.xR;
						} else if(c.isLeftAnchored && !c.isRightAnchored){
							// nicht zu verwenden
						} else if(!c.isLeftAnchored && c.isRightAnchored){
							// nicht zu verwenden
						} else {
							// nicht zu verwenden
						}
					}
						
				}
				minW = minW > mW ? minW:mW; // falls neues groesser -> uebernehmen
				
				// HEIGHT
				if(c.isRelativeYT){
					if(c.isRelativeYB){
						if(c.isTopAnchored && c.isBottomAnchored){
							mH = minDim.height/(1-c.yT-c.yB);
						} else if(c.isLeftAnchored && !c.isBottomAnchored){
							mH = minDim.height/(c.yB-c.yT);
						} else if(!c.isLeftAnchored && c.isBottomAnchored){
							mH = minDim.height/(c.yT-c.yB);
						} else {
							mH = minDim.height/(c.yT+c.yB -1);
						}
					} else {
						if(c.isTopAnchored && c.isBottomAnchored){
							mH = c.yB + minDim.height/(1-c.yT);
						} else if(c.isTopAnchored && !c.isBottomAnchored){
							// nicht zu verwenden
						} else if(!c.isTopAnchored && c.isBottomAnchored){
							mH = (minDim.height + c.yB)/(c.yT);
						} else {
							// nicht zu verwenden
						}
					}
				} else {
					if(c.isRelativeYB){
						if(c.isTopAnchored && c.isBottomAnchored){
							mH = c.yT + minDim.height/(1-c.yB);
						} else if(c.isTopAnchored && !c.isBottomAnchored){
							mH = (minDim.height + c.yT)/(c.yB);
						} else if(!c.isTopAnchored && c.isBottomAnchored){
							// nicht zu verwenden
						} else {
							// nicht zu verwenden
						}
					} else {
						if(c.isTopAnchored && c.isBottomAnchored){
							mH = minDim.height + c.yT + c.yB;
						} else if(c.isTopAnchored && !c.isBottomAnchored){
							// nicht zu verwenden
						} else if(!c.isTopAnchored && c.isBottomAnchored){
							// nicht zu verwenden
						} else {
							// nicht zu verwenden
						}
					}
						
				}
				minH = minH > mH ? minH:mH; // falls neues groesser -> uebernehmen
			}
		}
		return new Dimension((int)minW,(int)minH);
	}


	@Override
	public Dimension preferredLayoutSize(Container parent) {
		float prefH= 100;
		float prefW= 100;
		synchronized (parent.getTreeLock()) {
			for(Component comp: this.constraintsMap.keySet()){
				Dimension prefDim = comp.getPreferredSize();
				if(prefDim == null){
					continue;
				}
				float mW = prefDim.width;
				float mH = prefDim.height;
				Constraint c = this.constraintsMap.get(comp);
				
				// WIDTH
				if(c.isRelativeXL){
					if(c.isRelativeXR){
						if(c.isLeftAnchored && c.isRightAnchored){
							mW = prefDim.width/(1-c.xL-c.xR);
						} else if(c.isLeftAnchored && !c.isRightAnchored){
							mW = prefDim.width/(c.xR-c.xL);
						} else if(!c.isLeftAnchored && c.isRightAnchored){
							mW = prefDim.width/(c.xL-c.xR);
						} else {
							mW = prefDim.width/(c.xL+c.xR -1);
						}
					} else {
						if(c.isLeftAnchored && c.isRightAnchored){
							mW = c.xR + prefDim.width/(1-c.xL);
						} else if(c.isLeftAnchored && !c.isRightAnchored){
							// nicht zu verwenden
						} else if(!c.isLeftAnchored && c.isRightAnchored){
							mW = (prefDim.width + c.xR)/(c.xL);
						} else {
							// nicht zu verwenden
						}
					}
				} else {
					if(c.isRelativeXR){
						if(c.isLeftAnchored && c.isRightAnchored){
							mW = c.xL + prefDim.width/(1-c.xR);
						} else if(c.isLeftAnchored && !c.isRightAnchored){
							mW = (prefDim.width + c.xL)/(c.xR);
						} else if(!c.isLeftAnchored && c.isRightAnchored){
							// nicht zu verwenden
						} else {
							// nicht zu verwenden
						}
					} else {
						if(c.isLeftAnchored && c.isRightAnchored){
							mW = prefDim.width + c.xL + c.xR;
						} else if(c.isLeftAnchored && !c.isRightAnchored){
							// nicht zu verwenden
						} else if(!c.isLeftAnchored && c.isRightAnchored){
							// nicht zu verwenden
						} else {
							// nicht zu verwenden
						}
					}
						
				}
				prefW = prefW > mW+5 ? prefW:mW+5; // falls neues groesser -> uebernehmen
				
				// HEIGHT
				if(c.isRelativeYT){
					if(c.isRelativeYB){
						if(c.isTopAnchored && c.isBottomAnchored){
							mH = prefDim.height/(1-c.yT-c.yB);
						} else if(c.isLeftAnchored && !c.isBottomAnchored){
							mH = prefDim.height/(c.yB-c.yT);
						} else if(!c.isLeftAnchored && c.isBottomAnchored){
							mH = prefDim.height/(c.yT-c.yB);
						} else {
							mH = prefDim.height/(c.yT+c.yB -1);
						}
					} else {
						if(c.isTopAnchored && c.isBottomAnchored){
							mH = c.yB + prefDim.height/(1-c.yT);
						} else if(c.isTopAnchored && !c.isBottomAnchored){
							// nicht zu verwenden
						} else if(!c.isTopAnchored && c.isBottomAnchored){
							mH = (prefDim.height + c.yB)/(c.yT);
						} else {
							// nicht zu verwenden
						}
					}
				} else {
					if(c.isRelativeYB){
						if(c.isTopAnchored && c.isBottomAnchored){
							mH = c.yT + prefDim.height/(1-c.yB);
						} else if(c.isTopAnchored && !c.isBottomAnchored){
							mH = (prefDim.height + c.yT)/(c.yB);
						} else if(!c.isTopAnchored && c.isBottomAnchored){
							// nicht zu verwenden
						} else {
							// nicht zu verwenden
						}
					} else {
						if(c.isTopAnchored && c.isBottomAnchored){
							mH = prefDim.height + c.yT + c.yB;
						} else if(c.isTopAnchored && !c.isBottomAnchored){
							// nicht zu verwenden
						} else if(!c.isTopAnchored && c.isBottomAnchored){
							// nicht zu verwenden
						} else {
							// nicht zu verwenden
						}
					}
						
				}
				prefH = prefH > mH+5 ? prefH:mH+5; // falls neues groesser -> uebernehmen
			}
		}
		return new Dimension((int)prefW,(int)prefH);
	}


	@Override
	public Dimension maximumLayoutSize(Container target) {
		return new Dimension(2147483647, 2147483647);
	}


	protected static class Constraint {
		boolean isRelativeXL;
		boolean isLeftAnchored;
		float xL;
		
		boolean isRelativeYT;
		boolean isTopAnchored;
		float yT;
		
		boolean isRelativeXR;
		boolean isRightAnchored;
		float xR;
		
		boolean isRelativeYB;
		boolean isBottomAnchored;
		float yB;
		
		static Constraint parseConstraints(String constraints) {
			Constraint c = new Constraint();
			try {
			String tlString = constraints.toLowerCase().replace("topleft(", "#");
			String brString = constraints.toLowerCase().replace("bottomright(", "#");
			
			int i = tlString.indexOf('#');
			tlString = tlString.substring(i+1, tlString.indexOf(')', i));
			
			i = brString.indexOf('#');
			brString = brString.substring(i+1, brString.indexOf(')', i));
			
			String[] tl = tlString.split(",");
			tl[0] = tl[0].trim();
			tl[1] = tl[1].trim();
			
			String[] br = brString.split(",");
			br[0] = br[0].trim();
			br[1] = br[1].trim();
			
			c.isLeftAnchored = !tl[0].contains("-");
			c.isTopAnchored = !tl[1].contains("-");
			c.isRightAnchored = br[0].contains("-");
			c.isBottomAnchored = br[1].contains("-");
			
			c.isRelativeXL = tl[0].contains(".")||tl[0].contains("/");
			c.isRelativeYT = tl[1].contains(".")||tl[1].contains("/");
			c.isRelativeXR = br[0].contains(".")||br[0].contains("/");
			c.isRelativeYB = br[1].contains(".")||br[1].contains("/");
			
			if(tl[0].contains("/")){
				float f1 = Float.parseFloat(tl[0].split("/")[0]);
				float f2 = Float.parseFloat(tl[0].split("/")[1]);
				c.xL = Math.abs(f1/f2);
			} else {
				c.xL = Math.abs(Float.parseFloat(tl[0]));
			}
			
			if(tl[1].contains("/")){
				float f1 = Float.parseFloat(tl[1].split("/")[0]);
				float f2 = Float.parseFloat(tl[1].split("/")[1]);
				c.yT = Math.abs(f1/f2);
			} else {
				c.yT = Math.abs(Float.parseFloat(tl[1]));
			}
			
			if(br[0].contains("/")){
				float f1 = Float.parseFloat(br[0].split("/")[0]);
				float f2 = Float.parseFloat(br[0].split("/")[1]);
				c.xR = Math.abs(f1/f2);
			} else {
				c.xR = Math.abs(Float.parseFloat(br[0]));
			}
			
			if(br[1].contains("/")){
				float f1 = Float.parseFloat(br[1].split("/")[0]);
				float f2 = Float.parseFloat(br[1].split("/")[1]);
				c.yB = Math.abs(f1/f2);
			} else {
				c.yB = Math.abs(Float.parseFloat(br[1]));
			}
			} catch(Exception e) {
				synchronized (System.err) {
					System.err.println("Exception caught while parsing constraints :" + constraints);
					e.printStackTrace();
				}
			}
			return c;
		}
	}
}
