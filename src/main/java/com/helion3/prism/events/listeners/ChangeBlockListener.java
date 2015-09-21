/**
 * This file is part of Prism, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2015 Helion3 http://helion3.com/
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
package com.helion3.prism.events.listeners;

import org.spongepowered.api.block.BlockTransaction;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.BreakBlockEvent;
import org.spongepowered.api.event.block.ChangeBlockEvent;
import org.spongepowered.api.event.block.PlaceBlockEvent;

import com.google.common.base.Optional;
import com.helion3.prism.Prism;
import com.helion3.prism.api.records.PrismRecord;

public class ChangeBlockListener {
    private final boolean hearBreak = Prism.getConfig().getNode("events", "block", "break").getBoolean();
    private final boolean hearPlace = Prism.getConfig().getNode("events", "block", "place").getBoolean();

    /**
     * Listens to the base change block event.
     *
     * @param event
     */
    @Listener
    public void onChangeBlock(final ChangeBlockEvent event) {
        Optional<Player> player = event.getCause().first(Player.class);

        for (BlockTransaction transaction : event.getTransactions()) {
            PrismRecord record = new PrismRecord();

            // Player-caused
            if (player.isPresent()) {
                record.player(player.get());
            } else {
                // @todo handle this
            }

            if (event instanceof BreakBlockEvent) {
                if (hearBreak) {
                    record.brokeBlock(transaction);
                }
            }
            else if (event instanceof PlaceBlockEvent && hearPlace) {
                if (hearPlace) {
                    record.placedBlock(transaction);
                }
            }

            if (record.isValid()) {
                record.save();
            }
        }
    }
}