package com.orangopontotango.scrollbindings;

import net.minecraft.client.gui.components.debug.DebugScreenDisplayer;
import net.minecraft.client.gui.components.debug.DebugScreenEntry;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;

import java.util.LinkedList;

public class DebugEntryCps implements DebugScreenEntry {
    private static final long WINDOW_MS = 1000;

    private static final LinkedList<Long> leftClicks = new LinkedList<>();
    private static final LinkedList<Long> rightClicks = new LinkedList<>();
    private static final LinkedList<Long> scrollUps = new LinkedList<>();
    private static final LinkedList<Long> scrollDowns = new LinkedList<>();

    public static void recordLeftClick() {
        leftClicks.add(System.currentTimeMillis());
    }

    public static void recordRightClick() {
        rightClicks.add(System.currentTimeMillis());
    }

    public static void recordScrollUp() {
        scrollUps.add(System.currentTimeMillis());
    }

    public static void recordScrollDown() {
        scrollDowns.add(System.currentTimeMillis());
    }

    private static int countRecent(LinkedList<Long> list) {
        long cutoff = System.currentTimeMillis() - WINDOW_MS;
        while (!list.isEmpty() && list.peek() < cutoff) {
            list.poll();
        }
        return list.size();
    }

    @Override
    public void display(DebugScreenDisplayer displayer, Level level, LevelChunk clientChunk, LevelChunk serverChunk) {
        int left = countRecent(leftClicks);
        int right = countRecent(rightClicks);
        int up = countRecent(scrollUps);
        int down = countRecent(scrollDowns);
        int total = left + right + up + down;

        displayer.addLine("CPS: " + total + " [L:" + left + " R:" + right + " S\u2191:" + up + " S\u2193:" + down + "]");
    }

    @Override
    public boolean isAllowed(boolean isOverlay) {
        return true;
    }
}
