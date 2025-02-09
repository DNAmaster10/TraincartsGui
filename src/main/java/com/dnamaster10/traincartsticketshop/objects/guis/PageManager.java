package com.dnamaster10.traincartsticketshop.objects.guis;

import java.util.HashMap;

/**
 * Holds multiple pages, and manages those pages.
 * Often used as a cache for the contents of a Gui instead of needing to query the database for individual pages.
 *
 * @see Page
 * @see com.dnamaster10.traincartsticketshop.objects.guis.interfaces.Pageable
 */
public class PageManager {
    private HashMap<Integer, Page> pages = new HashMap<>();
    private int currentPageNumber;

    /**
     * @param startingPage The page which the PageManager should start at.
     */
    public PageManager(int startingPage) {
        currentPageNumber = startingPage;
    }

    /**
     * Adds a page to the page manager.
     *
     * @param pageNumber The page number for the page being added
     * @param page The page to be added
     */
    public void addPage(int pageNumber, Page page) {
        pages.put(pageNumber, page);
    }

    /**
     * Checks whether the page manager has a page at the specified page number
     *
     * @param pageNumber The page number to check for
     * @return True if the page exists
     */
    public boolean hasPage(int pageNumber) {
        return pages.containsKey(pageNumber);
    }

    /**
     * Returns the page at the specified page number.
     *
     * @param pageNumber The page number to get
     * @return The page at the specified page number
     */
    public Page getPage(int pageNumber) {
        return pages.get(pageNumber);
    }

    /**
     * Clears all cached pages from the page manager.
     */
    public void clearCache() {
        pages = new HashMap<>();
    }

    /**
     * Gets the current page number.
     *
     * @return The current page number
     */
    public int getCurrentPageNumber() {
        return currentPageNumber;
    }

    /**
     * Sets the current page number.
     *
     * @param pageNumber The new page number
     */
    public void setCurrentPageNumber(int pageNumber) {
        currentPageNumber = pageNumber;
    }
}
