/*
 * The MIT License
 *
 * Copyright 2017 Arnaud Hamon
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
package com.github.ptitnoony.gameutils;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 *
 * @author Arnaud HAMON-KEROMEN
 */
public class PlayerXmlUtils {

    /**
     * Player XML element
     */
    public static final String PLAYER_ELEMENT = "PLAYER";

    /**
     * Player first name XML attribute
     */
    public static final String FIRST_NAME = "firstName";

    /**
     * Player last name XML attribute
     */
    public static final String LAST_NAME = "lastName";

    /**
     * Player nickname XML attribute
     */
    public static final String NICK_NAME = "nickName";

    /**
     * Player first id XML attribute
     */
    public static final String ID = "id";

    private static final Logger LOG = Logger.getGlobal();

    private PlayerXmlUtils() {
        // private utility class
    }

    /**
     * Parse a player form an XML element.
     *
     * @param playerElement the player XML element
     * @return the parsed player
     */
    public static Player parsePlayer(Element playerElement) {
        String firstName = playerElement.getAttribute(FIRST_NAME);
        String lastName = playerElement.getAttribute(LAST_NAME);
        String nickName = playerElement.getAttribute(NICK_NAME);
        if (playerElement.hasAttribute(ID)) {
            int id = Integer.parseInt(playerElement.getAttribute(ID));
            return PlayerFactory.createPlayer(id, firstName, lastName, nickName);
        }
        return PlayerFactory.createPlayer(firstName, lastName, nickName);
    }

    /**
     * Save a player to an XML element.
     *
     * @param doc the XML document
     * @param parentElement the element to append the player to
     * @param player the player to save
     */
    public static void createPlayerXmlElement(Document doc, Element parentElement, Player player) {
        Element playerElement = doc.createElement(PLAYER_ELEMENT);
        playerElement.setAttribute(ID, Integer.toString(player.getID()));
        playerElement.setAttribute(FIRST_NAME, player.getFirstName());
        playerElement.setAttribute(LAST_NAME, player.getLastName());
        parentElement.appendChild(playerElement);
    }

    /**
     * Parse players from an XML file.
     *
     * @param file the file to parse
     * @return list of players saved in the given file
     */
    public static List<Player> parsePlayersXMLFile(File file) {
        DocumentBuilderFactory builderFactory;
        builderFactory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder docBuilder = builderFactory.newDocumentBuilder();
            InputSource source = new InputSource(file.getAbsolutePath());
            Document document = docBuilder.parse(source);
            Element e = document.getDocumentElement();
            NodeList nodes = e.getElementsByTagName(PLAYER_ELEMENT);
            List<Player> result = new LinkedList<>();
            for (int i = 0; i < nodes.getLength(); i++) {
                result.add(parsePlayer((Element) nodes.item(i)));
            }
            return Collections.unmodifiableList(result);
        } catch (ParserConfigurationException e) {
            LOG.log(Level.SEVERE, "Error while creating document builder {0}", e);
        } catch (SAXException | IOException ex) {
            LOG.log(Level.SEVERE, "Exception parsing players xml file:: {0}", ex);
        }
        return Collections.emptyList();
    }

}
