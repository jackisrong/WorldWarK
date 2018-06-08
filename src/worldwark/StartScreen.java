package worldwark;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.sound.sampled.FloatControl;

public class StartScreen {

    private final WorldWarK w;

    public StartScreen(WorldWarK w) {
	this.w = w;
	try {
	    w.cabinBold = Font.createFont(Font.TRUETYPE_FONT, new File("assets/fonts/CabinBold.ttf"));
	} catch (Exception e) {
	    System.out.println("ERROR: Font file CabinBold.ttf cannot be opened.");
	}

	try {
	    w.cabinRegular = Font.createFont(Font.TRUETYPE_FONT, new File("assets/fonts/CabinRegular.ttf"));
	} catch (Exception e) {
	    System.out.println("ERROR: Font file CabinRegular.ttf cannot be opened.");
	}

	try {
	    w.wartorn = Font.createFont(Font.TRUETYPE_FONT, new File("assets/fonts/Wartorn.ttf"));
	} catch (Exception e) {
	    System.out.println("ERROR: Font file Warton.ttf cannot be opened.");
	}
    }

    public void drawMusic(Graphics2D g2) {
	// Draw low volume button
	g2.setColor(Color.WHITE);
	Rectangle2D lowButton = new Rectangle2D.Double(100, 278, 100, 30);
	g2.draw(lowButton);
	w.startScreenButtons.add(lowButton);
	g2.drawString("LOWER", 115, 300);

	// Draw current volume
	g2.drawString(Float.toString(w.volume), 245, 300);

	// Draw high volume button
	g2.setColor(Color.WHITE);
	Rectangle2D highButton = new Rectangle2D.Double(330, 278, 100, 30);
	g2.draw(highButton);
	w.startScreenButtons.add(highButton);
	g2.drawString("HIGHER", 345, 300);

	// Draw low sound fx button
	g2.setColor(Color.WHITE);
	Rectangle2D lowFxButton = new Rectangle2D.Double(100, 378, 100, 30);
	g2.draw(lowFxButton);
	w.startScreenButtons.add(lowFxButton);
	g2.drawString("LOWER", 115, 400);

	// Draw current fx volume
	g2.drawString(Float.toString(w.fxVolume), 245, 400);

	// Draw high sound fx button
	g2.setColor(Color.WHITE);
	Rectangle2D highFxButton = new Rectangle2D.Double(330, 378, 100, 30);
	g2.draw(highFxButton);
	w.startScreenButtons.add(highFxButton);
	g2.drawString("HIGHER", 345, 400);

	// Print heading
	g2.setColor(Color.WHITE);
	g2.setFont(w.wartorn.deriveFont(50f));
	g2.drawString("SETTINGS", 80, 170);

	// Print subheading
	g2.setFont(w.cabinRegular.deriveFont(20f));
	g2.drawString("Your democratic freedom", 140, 210);

	// Print music heading font
	g2.setColor(Color.PINK);
	g2.setFont(w.cabinBold.deriveFont(20f));
	g2.drawString("Music Volume", 100, 260);
	g2.drawString("Sound Effects Volume", 100, 360);
    }

    public void readDrawFile(Graphics2D g2, String file, int titleXPos, int subtitleXPos) {
	// Read file
	ArrayList<String> content = new ArrayList<>();
	BufferedReader inputStream = null;
	String line;
	try {
	    inputStream = new BufferedReader(new FileReader("assets/txt/" + file + ".txt"));
	    do {
		line = inputStream.readLine();
		if (line != null) {
		    content.add(line);
		}
	    } while (line != null);
	} catch (IOException e) {
	    System.out.println("ERROR: Cannot open " + file + ".txt.");
	} finally {
	    if (inputStream != null) {
		try {
		    inputStream.close();
		} catch (IOException e) {
		    System.out.println("ERROR: Cannot close inputStream.");
		}
	    }
	}

	// Print heading
	g2.setColor(Color.WHITE);
	g2.setFont(w.wartorn.deriveFont(50f));
	if (content.get(0).charAt(0) == '^') {
	    g2.drawString(content.get(0).substring(1), titleXPos, 170);
	}

	// Print subheading
	g2.setFont(w.cabinRegular.deriveFont(20f));
	if (content.get(1).charAt(0) == '*') {
	    g2.drawString(content.get(1).substring(1), subtitleXPos, 210);
	}

	// Print content
	final int headingXPos = 90;
	final int nameXPos = 130;
	int textYPos = 260;

	g2.setColor(Color.PINK);
	for (int i = 2; i < content.size(); i++) {
	    if (content.get(i).charAt(0) == '$') {
		g2.setFont(w.cabinBold.deriveFont(20f));
		g2.drawString(content.get(i).substring(1), headingXPos, textYPos);
	    } else if (content.get(i).charAt(0) == '%') {
		g2.setFont(w.cabinRegular.deriveFont(20f));
		g2.drawString(content.get(i).substring(1), nameXPos, textYPos);
	    }
	    if (i + 1 < content.size() && content.get(i + 1).charAt(0) == '$') {
		textYPos += 10;
	    }
	    textYPos += 30;
	}
    }

    public void drawStartScreen(Graphics2D g2) {
	// Paint start screen background
	BufferedImage image;
	try {
	    image = ImageIO.read(new File("assets/img/startScreenBackground.png"));
	} catch (IOException e) {
	    System.out.println("ERROR: startScreenBackground.png cannot be read.");
	    image = null;
	}
	g2.drawImage(image, 0, 0, null);

	// Paint start screen content
	g2.setFont(w.cabinBold.deriveFont(20f));

	// Paint button rectangles
	g2.setColor(Color.BLACK);
	Rectangle2D instructionsButton = new Rectangle2D.Double(80, 550, 145, 40);
	w.startScreenButtons.add(instructionsButton);
	g2.fill(instructionsButton);
	g2.draw(instructionsButton);
	Rectangle2D controlsButton = new Rectangle2D.Double(280, 550, 145, 40);
	w.startScreenButtons.add(controlsButton);
	g2.fill(controlsButton);
	g2.draw(controlsButton);
	Rectangle2D powerUpsButton = new Rectangle2D.Double(80, 600, 145, 40);
	w.startScreenButtons.add(powerUpsButton);
	g2.fill(powerUpsButton);
	g2.draw(powerUpsButton);
	Rectangle2D enemiesButton = new Rectangle2D.Double(280, 600, 145, 40);
	w.startScreenButtons.add(enemiesButton);
	g2.fill(enemiesButton);
	g2.draw(enemiesButton);
	Rectangle2D creditsButton = new Rectangle2D.Double(380, 750, 92, 40);
	w.startScreenButtons.add(creditsButton);
	g2.fill(creditsButton);
	g2.draw(creditsButton);
	Rectangle2D settingsButton = new Rectangle2D.Double(30, 750, 100, 40);
	w.startScreenButtons.add(settingsButton);
	g2.fill(settingsButton);
	g2.draw(settingsButton);

	// Paint button text
	g2.setColor(Color.WHITE);
	g2.drawString("HOW TO PLAY", 86, 578);
	g2.drawString("CONTROLS", 300, 578);
	g2.drawString("POWER UPS", 96, 628);
	g2.drawString("ENEMIES", 312, 628);
	g2.drawString("CREDITS", 386, 778);
	g2.drawString("SETTINGS", 35, 778);

	// Check if a start screen button has been clicked
	if (w.clickedStartScreenButton != null) {
	    // Draw window background rectangle
	    g2.setColor(new Color(0, 0, 0, 250));
	    g2.fillRect(50, 80, 400, 700);

	    // Draw close button
	    g2.setColor(Color.RED);
	    Rectangle2D closeButton = new Rectangle2D.Double(370, 80, 80, 30);
	    g2.fill(closeButton);
	    w.startScreenButtons.add(closeButton);
	    g2.setColor(Color.WHITE);
	    g2.drawString("CLOSE", 377, 102);

	    if (w.clickedStartScreenButton.equals(instructionsButton)) {
		readDrawFile(g2, "instructions", 60, 70);
	    } else if (w.clickedStartScreenButton.equals(controlsButton)) {
		readDrawFile(g2, "controls", 60, 100);
	    } else if (w.clickedStartScreenButton.equals(powerUpsButton)) {
		readDrawFile(g2, "powerUps", 45, 80);
	    } else if (w.clickedStartScreenButton.equals(enemiesButton)) {
		readDrawFile(g2, "enemies", 100, 45);
	    } else if (w.clickedStartScreenButton.equals(creditsButton)) {
		readDrawFile(g2, "credits", 100, 140);
	    } else if (w.clickedStartScreenButton.equals(settingsButton)) {
		drawMusic(g2);
	    } else if (w.clickedStartScreenButton.equals(new Rectangle2D.Double(370, 80, 80, 30))) {
		// Remove dialogue boxes if close button is pressed
		w.clickedStartScreenButton = null;
		w.startScreenButtons.clear();
		w.repaint();
	    } else {
		// Change volume based on volume button pressed
		drawMusic(g2);
		w.audioControl = (FloatControl) w.clip.getControl(FloatControl.Type.MASTER_GAIN);
		float range = w.audioControl.getMaximum() - w.audioControl.getMinimum();
		if (w.clickedStartScreenButton.equals(new Rectangle2D.Double(100, 278, 100, 30))) {
		    w.volume -= 0.1;
		    w.volume = Math.max(Math.round(w.volume * 10) / (float) 10.0, 0);
		} else if (w.clickedStartScreenButton.equals(new Rectangle2D.Double(330, 278, 100, 30))) {
		    w.volume += 0.1;
		    w.volume = Math.min(Math.round(w.volume * 10) / (float) 10.0, 1);
		} else if (w.clickedStartScreenButton.equals(new Rectangle2D.Double(100, 378, 100, 30))) {
		    w.fxVolume -= 0.1;
		    w.fxVolume = Math.max(Math.round(w.fxVolume * 10) / (float) 10.0, 0);
		} else if (w.clickedStartScreenButton.equals(new Rectangle2D.Double(330, 378, 100, 30))) {
		    w.fxVolume += 0.1;
		    w.fxVolume = Math.min(Math.round(w.fxVolume * 10) / (float) 10.0, 1);
		}
		w.clickedStartScreenButton = new Rectangle2D.Double(30, 750, 100, 40);
		float gain = (range * w.volume) + w.audioControl.getMinimum();
		w.audioControl.setValue(gain);
		w.clip.start();

		// Save volume setting to file
		FileWriter outputStream = null;
		try {
		    outputStream = new FileWriter("assets/data/volume.txt");
		    outputStream.write(w.volume + "\r\n");
		} catch (Exception e) {
		    System.out.println("ERROR: Cannot write to volume.txt");
		} finally {
		    if (outputStream != null) {
			try {
			    outputStream.close();
			} catch (IOException e) {
			    System.out.println("ERROR: Cannot close outputStream");
			}
		    }
		}

		// Save fx volume to file
		try {
		    outputStream = new FileWriter("assets/data/fxVolume.txt");
		    outputStream.write(w.fxVolume + "\r\n");
		} catch (Exception e) {
		    System.out.println("ERROR: Cannot write to fxVolume.txt");
		} finally {
		    try {
			outputStream.close();
		    } catch (IOException e) {
			System.out.println("ERROR: Cannot close outputStream");
		    }
		}
		w.repaint();
	    }
	}
    }
}
