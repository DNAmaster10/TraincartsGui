package com.dnamaster10.traincartsticketshop.objects.guis.interfaces;

/**
 * Implemented by Guis which have multiple pages.
 */
public interface Pageable {
    /**
     * Opens the next page in the current Gui.
     */
    void nextPage();

    /**
     * Opens the previous page in the current Gui.
     */
    void prevPage();
}
