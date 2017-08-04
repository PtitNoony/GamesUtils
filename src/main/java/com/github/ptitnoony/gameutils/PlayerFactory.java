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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 *
 * @author Arnaud HAMON-KEROMEN
 */
public class PlayerFactory {

    private static final Map<Integer, Player> PLAYERS = new HashMap<>();

    private static final int ID_INCR = 7;

    private static int nextUniqueID = 1;

    private PlayerFactory() {
        // private utility constructor
    }

    /**
     * Get the players already created. This method is time consuming as it
     * creates a new unmodifiable list.
     *
     * @return list of existing players
     */
    public static List<Player> getCreatedPlayers() {
        // doc that time consuming method
        return PLAYERS.entrySet().stream().map(entry -> entry.getValue()).collect(Collectors.toList());
    }

    /**
     * Create a player using the given parameters.
     *
     * @param firstName player first name
     * @param lastName player last name
     * @param nickName player nickname
     * @return the created player
     */
    public static Player createPlayer(String firstName, String lastName, String nickName) {
        while (PLAYERS.containsKey(nextUniqueID)) {
            nextUniqueID++;
        }
        final Player player = new PlayerImpl(nextUniqueID, firstName, lastName, nickName);
        PLAYERS.put(nextUniqueID, player);
        incrementUniqueID();
        return player;
    }

    /**
     * Create a player with the given parameters. Will throw an exception if the
     * unique id is already used by another player.
     *
     * @param id the player unique id
     * @param firstName player first name
     * @param lastName player last name
     * @param nickName player nickname
     * @return the created player
     */
    public static Player createPlayer(int id, String firstName, String lastName, String nickName) {
        if (PLAYERS.containsKey(id)) {
            //TODO: message
            throw new IllegalStateException("Error");

        }
        final Player player = new PlayerImpl(id, firstName, lastName, nickName);
        PLAYERS.put(id, player);
        return player;
    }

    /**
     * Get a created player using its unique id
     *
     * @param playerID the player unique id
     * @return the corresponding created player if exists, null otherwise
     */
    public static Player getPlayerFromID(int playerID) {
        return PLAYERS.get(playerID);
    }

    /**
     * Tests if given attributes are suitable for creating a new player.
     *
     * @param firstName player first name
     * @param lastName player last name
     * @param nickName player nick name
     * @return if the player attributes are valid for player creation.
     */
    public static boolean areValidPlayerAttibutes(String firstName, String lastName, String nickName) {
        if (firstName == null || lastName == null || nickName == null) {
            return false;
        }
        //TODO: test existence
        return !firstName.trim().isEmpty() && !lastName.trim().isEmpty() && !nickName.trim().isEmpty();
    }

    private static void incrementUniqueID() {
        nextUniqueID++;
        while (PLAYERS.containsKey(nextUniqueID)) {
            nextUniqueID += ID_INCR;
        }
    }

    private static class PlayerImpl implements Player {

        private final int pUniqueID;
        private final String pFirstName;
        private final String pLastName;
        private final String pNickName;

        private PlayerImpl(int uniqueID, String firstName, String lastName, String nickName) {
            pUniqueID = uniqueID;
            pFirstName = firstName;
            pLastName = lastName;
            pNickName = nickName;
        }

        @Override
        public int getID() {
            return pUniqueID;
        }

        @Override
        public String getFirstName() {
            return pFirstName;
        }

        @Override
        public String getLastName() {
            return pLastName;
        }

        @Override
        public String getNickName() {
            return pNickName;
        }

        @Override
        public int hashCode() {
            int hash = 5;
            hash = 97 * hash + this.pUniqueID;
            hash = 97 * hash + Objects.hashCode(this.pFirstName);
            hash = 97 * hash + Objects.hashCode(this.pLastName);
            hash = 97 * hash + Objects.hashCode(this.pNickName);
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final PlayerImpl other = (PlayerImpl) obj;
            if (this.pUniqueID != other.pUniqueID) {
                return false;
            }
            if (!Objects.equals(this.pFirstName, other.pFirstName)) {
                return false;
            }
            if (!Objects.equals(this.pLastName, other.pLastName)) {
                return false;
            }
            return Objects.equals(this.pNickName, other.pNickName);
        }

        @Override
        public String toString() {
            return pFirstName + " " + pLastName + " (" + pNickName + ")";
        }

    }
}
