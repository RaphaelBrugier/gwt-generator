/*
 * This file is part of the GWTUML project and was written by Mounier Florian <mounier-dot-florian.at.gmail'dot'com> for Objet Direct
 * <http://wwww.objetdirect.com>
 * 
 * Copyright © 2009 Objet Direct Contact: gwtuml@googlegroups.com
 * 
 * GWTUML is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version.
 * 
 * GWTUML is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License along with GWTUML. If not, see <http://www.gnu.org/licenses/>.
 */
package com.objetdirect.gwt.gen.client;

import java.util.HashMap;
import java.util.Map.Entry;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.RequiresResize;
import com.google.gwt.user.client.ui.SimplePanel;
import com.objetdirect.gwt.umlapi.client.artifacts.ClassArtifact;
import com.objetdirect.gwt.umlapi.client.artifacts.LifeLineArtifact;
import com.objetdirect.gwt.umlapi.client.artifacts.ObjectArtifact;
import com.objetdirect.gwt.umlapi.client.engine.Direction;
import com.objetdirect.gwt.umlapi.client.engine.GeometryManager;
import com.objetdirect.gwt.umlapi.client.engine.Point;
import com.objetdirect.gwt.umlapi.client.engine.Scheduler;
import com.objetdirect.gwt.umlapi.client.gfx.GfxManager;
import com.objetdirect.gwt.umlapi.client.helpers.GWTUMLDrawerHelper;
import com.objetdirect.gwt.umlapi.client.helpers.HotKeyManager;
import com.objetdirect.gwt.umlapi.client.helpers.Mouse;
import com.objetdirect.gwt.umlapi.client.helpers.OptionsManager;
import com.objetdirect.gwt.umlapi.client.helpers.Session;
import com.objetdirect.gwt.umlapi.client.helpers.ThemeManager;
import com.objetdirect.gwt.umlapi.client.helpers.UMLCanvas;
import com.objetdirect.gwt.umlapi.client.helpers.ThemeManager.Theme;
import com.objetdirect.gwt.umlapi.client.umlcomponents.UMLClassAttribute;
import com.objetdirect.gwt.umlapi.client.umlcomponents.UMLDiagram;
import com.objetdirect.gwt.umlapi.client.umlcomponents.UMLVisibility;
import com.objetdirect.gwt.umlapi.client.umlcomponents.UMLDiagram.Type;

/**
 * This panel is an intermediate panel that contains the graphic canvas <br>
 * And can draw a shadow around it
 * 
 * @author Florian Mounier (mounier-dot-florian.at.gmail'dot'com)
 */
public class DrawerPanel extends AbsolutePanel implements RequiresResize {

	private SimplePanel	bottomLeftCornerShadow;

	private SimplePanel bottomRightCornerShadow;

	private SimplePanel bottomShadow;
	private final UMLCanvas uMLCanvas;

	private SimplePanel rightShadow;
	private SimplePanel topRightCornerShadow;
	private int height;
	private int width;

	private FocusPanel topLeft = new FocusPanel();
	private FocusPanel top = new FocusPanel();
	private FocusPanel topRight = new FocusPanel();
	private FocusPanel right = new FocusPanel();
	private FocusPanel bottomRight = new FocusPanel();
	private FocusPanel bottom = new FocusPanel();
	private FocusPanel bottomLeft = new FocusPanel();
	private FocusPanel left = new FocusPanel();

	private HashMap<FocusPanel, Direction>	directionPanels;

//	private ResizeHandler					resizeHandler;

	public DrawerPanel(Type diagramType) {
		this(new UMLCanvas(new UMLDiagram(diagramType), Window.getClientWidth() - 0,  Window.getClientHeight() - 30));
	}
	
	public DrawerPanel(Type diagramType, int width, int height) {
		this(new UMLCanvas(new UMLDiagram(diagramType), width,  height));
	}
	
	/**
	 * Default constructor of a DrawerPanel
	 * 
	 */
	public DrawerPanel(final UMLCanvas uMLCanvas) {
		super();
		setupGfxPlatform();
		Log.info("Creating drawer");

		this.uMLCanvas = uMLCanvas;
		this.add(this.uMLCanvas.getContainer());

		setUpSidePanels();

		Log.info("Canvas added");
		setupShadow();

		// TODO : under chrome redraw doesn't work if the canvas is at a
		// different point than (0,0) tatami ? dojo ? chrome ?
		// example : this.setSpacing(50);
		Log.info("Setting active canvas");
		Session.setActiveCanvas(this.uMLCanvas);
		Log.info("Disabling browser events");
		GWTUMLDrawerHelper.disableBrowserEvents();
		Log.info("Init end");
	}
	
	private void setupGfxPlatform() {
		HotKeyManager.forceStaticInit();
		HotKeyManager.setInputEnabled(false);
		ThemeManager.setCurrentTheme((Theme.getThemeFromIndex(OptionsManager.get("Theme"))));
		GfxManager.setPlatform(OptionsManager.get("GraphicEngine"));
		GeometryManager.setPlatform(OptionsManager.get("GeometryStyle"));
		
		this.width = Window.getClientWidth() - 0;
		this.height = Window.getClientHeight() - 30;
	}
	
	private void setUpSidePanels() {
		directionPanels	= new HashMap<FocusPanel, Direction>();
		
		directionPanels.put(this.topLeft, Direction.UP_LEFT);
		directionPanels.put(this.top, Direction.UP);
		directionPanels.put(this.topRight, Direction.UP_RIGHT);
		directionPanels.put(this.right, Direction.RIGHT);
		directionPanels.put(this.bottomRight, Direction.DOWN_RIGHT);
		directionPanels.put(this.bottom, Direction.DOWN);
		directionPanels.put(this.bottomLeft, Direction.DOWN_LEFT);
		directionPanels.put(this.left, Direction.LEFT);
		
		final int directionPanelSizes = OptionsManager.get("DirectionPanelSizes");

		final HashMap<FocusPanel, Point> panelsSizes = this.makeDirectionPanelsSizes(directionPanelSizes);
		final HashMap<FocusPanel, Point> panelsPositions = this.makeDirectionPanelsPositions(directionPanelSizes);

		for (final Entry<FocusPanel, Direction> panelEntry : this.directionPanels.entrySet()) {
			final FocusPanel panel = panelEntry.getKey();
			final Direction direction = panelEntry.getValue();
			DOM.setStyleAttribute(panel.getElement(), "backgroundColor", ThemeManager.getTheme().getDirectionPanelColor().toString());
			DOM.setStyleAttribute(panel.getElement(), "opacity", Double.toString(((double) OptionsManager.get("DirectionPanelOpacity")) / 100));
			panel.addMouseOverHandler(new MouseOverHandler() {
				@Override
				public void onMouseOver(final MouseOverEvent event) {

					for (double d = ((double) OptionsManager.get("DirectionPanelOpacity")) / 100; d <= ((double) OptionsManager.get("DirectionPanelMaxOpacity")) / 100; d += 0.05) {
						final double opacity = Math.ceil(d * 100) / 100;

						new Scheduler.Task("Opacifying") {
							@Override
							public void process() {
								DOM.setStyleAttribute(panel.getElement(), "opacity", Double.toString(opacity));
							}
						};
					}
					new Scheduler.Task("MovingAllArtifacts") {
						@Override
						public void process() {
							Scheduler.cancel("MovingAllArtifactsRecursive");
							DrawerPanel.this.uMLCanvas.moveAll(direction.withSpeed(Direction.getDependingOnQualityLevelSpeed()), true);
						}
					};
				}
			});
			panel.addMouseOutHandler(new MouseOutHandler() {
				@Override
				public void onMouseOut(final MouseOutEvent event) {
					Scheduler.cancel("Opacifying");
					Scheduler.cancel("MovingAllArtifacts");
					Scheduler.cancel("MovingAllArtifactsRecursive");
					for (final FocusPanel onePanel : DrawerPanel.this.directionPanels.keySet()) {
						double currentOpacity = 0;
						try {
							currentOpacity = Math.ceil(Double.parseDouble(DOM.getStyleAttribute(onePanel.getElement(), "opacity")) * 100) / 100;
						} catch (final Exception ex) {
							Log.error("Unable to parse element opacity : " + ex);
						}
						for (double d = currentOpacity; d >= ((double) OptionsManager.get("DirectionPanelOpacity")) / 100; d -= 0.05) {
							final double opacity = Math.ceil(d * 100) / 100;

							new Scheduler.Task("Desopacifying") {
								@Override
								public void process() {
									DOM.setStyleAttribute(onePanel.getElement(), "opacity", Double.toString(opacity));
								}
							};
						}
					}
				}
			});
			panel.addMouseDownHandler(new MouseDownHandler() {
				@Override
				public void onMouseDown(final MouseDownEvent event) {
					DOM.setStyleAttribute(panel.getElement(), "backgroundColor", ThemeManager.getTheme().getDirectionPanelPressedColor().toString());
					Scheduler.cancel("MovingAllArtifactsRecursive");
				}
			});
			panel.addMouseUpHandler(new MouseUpHandler() {
				@Override
				public void onMouseUp(final MouseUpEvent event) {
					DOM.setStyleAttribute(panel.getElement(), "backgroundColor", ThemeManager.getTheme().getDirectionPanelColor().toString());
					DrawerPanel.this.uMLCanvas.moveAll(direction.withSpeed(Math.min(DrawerPanel.this.uMLCanvas.getContainer().getOffsetHeight(), DrawerPanel.this.uMLCanvas
							.getContainer().getOffsetWidth())), false);
				}
			});
			panel.addMouseMoveHandler(new MouseMoveHandler() {
				@Override
				public void onMouseMove(final MouseMoveEvent event) {
					Mouse.move(new Point(event.getClientX(), event.getClientY()), event.getNativeButton(), event.isControlKeyDown(), event.isAltKeyDown(),
							event.isShiftKeyDown(), event.isMetaKeyDown());
				}
			});
			final Point panelPosition = panelsPositions.get(panel);
			final Point panelSize = panelsSizes.get(panel);
			panel.setPixelSize(panelSize.getX(), panelSize.getY());
			this.add(panel, panelPosition.getX(), panelPosition.getY());

		}
	}

	
	private void setupShadow() {
		final boolean isShadowed = OptionsManager.get("Shadowed") == 1;
		if (isShadowed) {
			Log.info("Making shadow");
			this.makeShadow();
		} else {
			this.uMLCanvas.getContainer().setStylePrimaryName("canvas");
		}
	}

	
	/**
	 * Getter for the uMLCanvas
	 * 
	 * @return the uMLCanvas
	 */
	public final UMLCanvas getUMLCanvas() {
		return this.uMLCanvas;
	}
	

	/**
	 * Setter for the width
	 * 
	 * @param width
	 *            the width to set
	 */
	public final void setWidth(final int width) {
		this.width = width;
	}

	public void addDefaultNode() {
		final Type type = UMLDiagram.Type.getUMLDiagramFromIndex(OptionsManager.get("DiagramType"));
		addDefaultNode(type);
	}
	
	public void addDefaultNode(Type type) {
		if (type.isClassType()) {
			final ClassArtifact defaultclass = new ClassArtifact(uMLCanvas, "Class1");
			defaultclass.setLocation(new Point(this.width / 2, this.height / 2));
			this.uMLCanvas.add(defaultclass);
		}
		if (type.isObjectType()) {
			final ObjectArtifact defaultobject = new ObjectArtifact(uMLCanvas, "obj1", "Object1");
			defaultobject.setLocation(new Point(this.width / 3, this.height / 3));
			this.uMLCanvas.add(defaultobject);
		}
		if (type == Type.SEQUENCE) {
			final LifeLineArtifact defaultLifeLineArtifact = new LifeLineArtifact(uMLCanvas, "LifeLine1", "ll1");
			defaultLifeLineArtifact.setLocation(new Point(this.width / 2, this.height / 2));
			this.uMLCanvas.add(defaultLifeLineArtifact);
		}
	}
	
	public void addWelcomeClass() {
		final ClassArtifact defaultclass = new ClassArtifact(uMLCanvas, "You can right click on a class.");
		defaultclass.addAttribute(new UMLClassAttribute(UMLVisibility.PRIVATE, "String", "  or right click on the canvas."));
		defaultclass.setLocation(new Point(this.width / 2, this.height / 2));
		this.uMLCanvas.add(defaultclass);
	}

	void clearShadow() {
		this.remove(this.bottomShadow);
		this.remove(this.rightShadow);
		this.remove(this.bottomRightCornerShadow);
		this.remove(this.topRightCornerShadow);
		this.remove(this.bottomLeftCornerShadow);
	}

	void makeShadow() {
		final int shadowSize = 8;

		this.setWidth(this.width + shadowSize + this.getAbsoluteLeft() + "px");
		this.setHeight(this.height + shadowSize + this.getAbsoluteTop() + "px");

		this.bottomShadow = new SimplePanel();
		this.bottomShadow.setPixelSize(this.width - shadowSize, shadowSize);
		this.bottomShadow.setStylePrimaryName("bottomShadow");
		this.add(this.bottomShadow, shadowSize, this.height);

		this.rightShadow = new SimplePanel();
		this.rightShadow.setPixelSize(shadowSize, this.height - shadowSize);
		this.rightShadow.setStylePrimaryName("rightShadow");
		this.add(this.rightShadow, this.width, shadowSize);

		this.bottomRightCornerShadow = new SimplePanel();
		this.bottomRightCornerShadow.setPixelSize(shadowSize, shadowSize);
		this.bottomRightCornerShadow.setStylePrimaryName("bottomRightCornerShadow");
		this.add(this.bottomRightCornerShadow, this.width, this.height);

		this.topRightCornerShadow = new SimplePanel();
		this.topRightCornerShadow.setPixelSize(shadowSize, shadowSize);
		this.topRightCornerShadow.setStylePrimaryName("topRightCornerShadow");
		this.add(this.topRightCornerShadow, this.width, 0);

		this.bottomLeftCornerShadow = new SimplePanel();
		this.bottomLeftCornerShadow.setPixelSize(shadowSize, shadowSize);
		this.bottomLeftCornerShadow.setStylePrimaryName("bottomLeftCornerShadow");
		this.add(this.bottomLeftCornerShadow, 0, this.height);
	}

	@Override
	protected void onLoad() {
		super.onLoad();
		this.uMLCanvas.onLoad();
	}

	private HashMap<FocusPanel, Point> makeDirectionPanelsPositions(final int directionPanelSizes) {
		HashMap<FocusPanel, Point> directionPanelsPositions = new HashMap<FocusPanel, Point>();
		
		directionPanelsPositions.put(this.topLeft, Point.getOrigin());
		directionPanelsPositions.put(this.top, new Point(directionPanelSizes, 0));
		directionPanelsPositions.put(this.topRight, new Point(this.width - directionPanelSizes, 0));
		directionPanelsPositions.put(this.right, new Point(this.width - directionPanelSizes, directionPanelSizes));
		directionPanelsPositions.put(this.bottomRight, new Point(this.width - directionPanelSizes, this.height - directionPanelSizes));
		directionPanelsPositions.put(this.bottom, new Point(directionPanelSizes, this.height - directionPanelSizes));
		directionPanelsPositions.put(this.bottomLeft, new Point(0, this.height - directionPanelSizes));
		directionPanelsPositions.put(this.left, new Point(0, directionPanelSizes));
		
		return directionPanelsPositions;
	}

	private HashMap<FocusPanel, Point> makeDirectionPanelsSizes(final int directionPanelSizes) {
		HashMap<FocusPanel, Point> directionPanelsSizes =  new HashMap<FocusPanel, Point>();
		
		directionPanelsSizes.put(this.topLeft, new Point(directionPanelSizes, directionPanelSizes));
		directionPanelsSizes.put(this.top, new Point(this.width - 2 * directionPanelSizes, directionPanelSizes));
		directionPanelsSizes.put(this.topRight, new Point(directionPanelSizes, directionPanelSizes));
		directionPanelsSizes.put(this.right, new Point(directionPanelSizes, this.height - 2 * directionPanelSizes));
		directionPanelsSizes.put(this.bottomRight, new Point(directionPanelSizes, directionPanelSizes));
		directionPanelsSizes.put(this.bottom, new Point(this.width - 2 * directionPanelSizes, directionPanelSizes));
		directionPanelsSizes.put(this.bottomLeft, new Point(directionPanelSizes, directionPanelSizes));
		directionPanelsSizes.put(this.left, new Point(directionPanelSizes, this.height - 2 * directionPanelSizes));
		
		return directionPanelsSizes;
	}

	@Override
	public void onResize() {
		final int directionPanelSizes = OptionsManager.get("DirectionPanelSizes");
		int parentHeight = getParent().getOffsetHeight();
		int parentWidth = getParent().getOffsetWidth();
		
		int width = parentWidth - 10;
		int height = parentHeight - 10;
		
		this.width = width;
		this.height = height;
		this.setPixelSize(this.width, this.height);
		this.uMLCanvas.getContainer().setPixelSize(this.width, this.height);
		GfxManager.getPlatform().setSize(this.uMLCanvas.getDrawingCanvas(), this.width, this.height);
		clearShadow();
		makeShadow();
		
		final HashMap<FocusPanel, Point> panelsNewSizes = makeDirectionPanelsSizes(directionPanelSizes);
		final HashMap<FocusPanel, Point> panelsNewPositions = makeDirectionPanelsPositions(directionPanelSizes);
		for (final FocusPanel panel : DrawerPanel.this.directionPanels.keySet()) {
			final Point panelPosition = panelsNewPositions.get(panel);
			final Point panelSize = panelsNewSizes.get(panel);
			panel.setPixelSize(panelSize.getX(), panelSize.getY());
			DrawerPanel.this.setWidgetPosition(panel, panelPosition.getX(), panelPosition.getY());
		}
		
		this.uMLCanvas.clearArrows();
		this.uMLCanvas.makeArrows();
		
		Log.debug("DrawerPanel::OnResize() parentHeight = " + parentHeight +"   parentWidth = " +parentWidth);
	}
}
