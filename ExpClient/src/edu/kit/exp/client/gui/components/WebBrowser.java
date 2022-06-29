package edu.kit.exp.client.gui.components;

import edu.kit.exp.client.gui.ClientGuiController;
import edu.kit.exp.client.gui.MainFrame;
import edu.kit.exp.common.files.FileManager;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.embed.swing.JFXPanel;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * The Class WebBrowser provides a simple web browser. Add it to an AWT or Swing
 * container and load a web page with the function loadURL(String URL)
 * 
 */
public class WebBrowser extends JFXPanel {

	private static final long serialVersionUID = -6631900946600232936L;

	/**
	 * The JFXPanel on which the scene is created and the browser components are
	 * added to
	 */
	private final JFXPanel jfxPanel = this;

	/** The WebEngine. */
	private WebEngine engine;
	
	/** The WebView. */
	private WebView view;

	/**
	 * The boolean variable which indicates if the web page is loaded
	 * completely. It prevents the recording of data before the user sees the
	 * web page.
	 * 
	 */
	private boolean pageLoaded = false;

	/** The BufferedWriter used to write the tracking data. */
	private BufferedWriter writer = null;

	/**
	 * The boolean variable which indicates that the first chosen URL has been
	 * loaded.
	 * 
	 */
	private boolean firstURLInitialized = false;

	/**
	 * This constructor instantiates a new web browser.
	 * 
	 */

	private boolean logUserInput = false;

	public WebBrowser() {
		this(false);
	}

	public WebBrowser(boolean logUserInput) {
		this.logUserInput = logUserInput;
		createBrowserComponents();

		// Prevent Thread-Queue behind the Platform-runLater Method to surprisingly stop
		// Source: http://stackoverflow.com/questions/29302837/javafx-platform-runlater-never-running
		Platform.setImplicitExit(false);
	}

	/**
	 * Method which creates the WebView and WebEngine and calls the needed
	 * methods to create all necessary components of the browser.
	 */
	private void createBrowserComponents() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {

				view = new WebView();
				engine = view.getEngine();

				if (logUserInput) {
					addOutputWriter();
					addTrackingListeners();
				}

				jfxPanel.setScene(new Scene(view));

			}
		});
	}

	/**
	 * Method which creates a BufferedWriter to write data from the WebBrowser
	 * to a CSV file. It also adds a WindowListener to close the BufferedWriter
	 * when the WebBrowser's window is closed.
	 * 
	 */
	private void addOutputWriter() {

		try {
			FileWriter fileWriter = FileManager.getInstance().getFileWriter("TrackingData", ClientGuiController.getInstance().getClientId());
			writer = new BufferedWriter(fileWriter);
			writer.write("Event;URL;Time;V-ScrollPosition;H-ScrollPosition;X-Position;Y-Position;KeyOrButton");
			writer.newLine();
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(0);
		}

		MainFrame.getCurrentScreen().addComponentListener(new ComponentAdapter() {

			@Override
			public void componentHidden(ComponentEvent e) {
				try {
					writer.flush();
					writer.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
	}

	/**
	 * Method which adds the needed listeners to WebView and WebEngine.
	 * 
	 */
	private void addTrackingListeners() {

		// The URL-change listener.
		engine.getLoadWorker().stateProperty().addListener(new ChangeListener<Worker.State>() {
			@Override
			public void changed(ObservableValue<? extends Worker.State> observableValue, Worker.State state,
					Worker.State newState) {
				if (newState.equals(Worker.State.RUNNING)) {
					view.setVisible(false);
				}
				if (newState.equals(Worker.State.SUCCEEDED)) {
					view.setVisible(true);
					pageLoaded = true;
					long time = System.currentTimeMillis();
					if (firstURLInitialized) {
						writeEventToCSV("URL-Change", engine.getLocation(), time, -1, -1, -1, -1,
								"same as previous entry");
					} else {
						writeEventToCSV("first URL", engine.getLocation(), time, -1, -1, -1, -1, "None");
						firstURLInitialized = true;
					}
				} else if (newState.equals(Worker.State.FAILED)) {
					if (!firstURLInitialized) {
						JOptionPane.showMessageDialog(jfxPanel, "Failed to load the page, exit the program", "Fehler",
								JOptionPane.ERROR_MESSAGE);
						System.exit(0);
					} else {
						long time = System.currentTimeMillis();
						writeEventToCSV("Failed to load", engine.getLocation(), time, -1, -1, -1, -1, "None");
						JOptionPane.showMessageDialog(jfxPanel, "Failed to load the page, return to previous page",
								"Fehler", JOptionPane.ERROR_MESSAGE);
						engine.reload();
					}
				} else {
					pageLoaded = false;
				}
			}

		});

		// The scroll listener.
		view.setOnScroll(new EventHandler<ScrollEvent>() {
			@Override
			public void handle(ScrollEvent event) {
				if (pageLoaded == true) {
					long time = System.currentTimeMillis();
					writeEventToCSV("Scrolling", engine.getLocation(), time, getVerticalScrollPosition(),
							getHorizontalScrollPosition(), event.getX(), event.getY(), "None");
				}
			}
		});

		// The mouse-motion listener.
		view.setOnMouseMoved(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				if (pageLoaded == true) {
					long time = System.currentTimeMillis();
					writeEventToCSV("MouseMotion", engine.getLocation(), time, getVerticalScrollPosition(),
							getHorizontalScrollPosition(), event.getX(), event.getY(), "None");
				}
			}
		});

		// The mouse-click listener.
		view.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (pageLoaded == true) {
					long time = System.currentTimeMillis();
					if (event.isPrimaryButtonDown()) {
						writeEventToCSV("MouseButtonPressed", engine.getLocation(), time, getVerticalScrollPosition(),
								getHorizontalScrollPosition(), event.getX(), event.getY(), "Left Mouse Button");
					}
					if (event.isSecondaryButtonDown()) {
						writeEventToCSV("MouseButtonPressed", engine.getLocation(), time, getVerticalScrollPosition(),
								getHorizontalScrollPosition(), event.getX(), event.getY(), "Right Mouse Button");
					}
					if (event.isMiddleButtonDown()) {
						writeEventToCSV("MouseButtonPressed", engine.getLocation(), time, getVerticalScrollPosition(),
								getHorizontalScrollPosition(), event.getX(), event.getY(), "Middle Mouse Button");
					}
				}
			}
		});

		// The mouse-drag listener.
		view.setOnMouseDragged(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (pageLoaded == true) {
					long time = System.currentTimeMillis();
					writeEventToCSV("Mouse-Drag", engine.getLocation(), time, getVerticalScrollPosition(),
							getHorizontalScrollPosition(), event.getX(), event.getY(), "None");
				}
			}
		});

		// The mouse-released listener.
		view.setOnMouseReleased(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (pageLoaded == true) {
					long time = System.currentTimeMillis();
					writeEventToCSV("MouseButtonReleased", engine.getLocation(), time, getVerticalScrollPosition(),
							getHorizontalScrollPosition(), event.getX(), event.getY(), "None");
				}
			}
		});

		// The keyboard listener.
		view.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				String pressedKey = event.getCode().getName();
				long time = System.currentTimeMillis();

				if (pageLoaded = true) {
					if (pressedKey.equalsIgnoreCase("UP") | pressedKey.equalsIgnoreCase("DOWN")
							| pressedKey.equalsIgnoreCase("Page UP") | pressedKey.equalsIgnoreCase("Page DOWN")
							| pressedKey.equalsIgnoreCase("End") | pressedKey.equalsIgnoreCase("Tab")
							| pressedKey.equalsIgnoreCase("Space")) {
						writeEventToCSV("PositionKeys", engine.getLocation(), time, getVerticalScrollPosition(),
								getHorizontalScrollPosition(), MouseInfo.getPointerInfo().getLocation().getX(),
								MouseInfo.getPointerInfo().getLocation().getY(), pressedKey);
					}
				}

				if (pressedKey.equalsIgnoreCase("Backspace")) {
					writeEventToCSV("Backspace", engine.getLocation(), time, -1, -1, -1, -1, pressedKey);
				}

			}
		});

	}

	/**
	 * Method to load a new URL in the browser.
	 * 
	 * @param url
	 *            A String which contains the URL which is to be loaded.
	 */
	public void loadURL(String url) {

		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				engine.load(toURL(url));
			}
		});
	}

	/**
	 * Formats a String to an URL String.
	 * 
	 * @param url
	 *            String which is to be formated.
	 * @return the formatted URL String
	 */
	private static String toURL(String url) {
		try {
			return new URL(url).toExternalForm();
		} catch (MalformedURLException exception) {
			return "http://" + url;
		}
	}

	/**
	 * Gets the Position of the vertical scroll bar in a web view.
	 * 
	 * @return The vertical scroll position.
	 */
	private int getVerticalScrollPosition() {
		return (Integer) engine.executeScript("document.body.scrollTop");
	}

	/**
	 * Gets the Position of the horizontal scroll bar in a web view.
	 * 
	 * @return The horizontal scroll position.
	 */
	private int getHorizontalScrollPosition() {
		return (Integer) engine.executeScript("document.body.scrollLeft");
	}

	/**
	 * Writes the data from an event to a CSV file.
	 * 
	 * @param event
	 *            The name of the event.
	 * @param url
	 *            The URL of the current web page when the event is thrown.
	 * @param time
	 *            The time stamp when the event is thrown.
	 * @param vScrollPos
	 *            The position of the vertical scroll bar when the event is
	 *            thrown.
	 * @param hScrollPos
	 *            The position of the horizontal scroll bar when the event is
	 *            thrown.
	 * @param xPos
	 *            The X-position of the mouse pointer when the event is thrown.
	 * @param yPos
	 *            The Y-position of the mouse pointer when the event is thrown.
	 * @param pressedKey
	 *            The key or mouse button used to trigger the event.
	 */
	private void writeEventToCSV(String event, String url, long time, double vScrollPos, double hScrollPos, double xPos,
			double yPos, String pressedKey) {

		try {
			writer.write("\"" + event + "\";");
			writer.write("\"" + url + "\";");
			writer.write("" + time + ";");
			writer.write("" + vScrollPos + ";");
			writer.write("" + hScrollPos + ";");
			writer.write("" + xPos + ";");
			writer.write("" + yPos + ";");
			writer.write("\"" + pressedKey + "\"");
			writer.newLine();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
}