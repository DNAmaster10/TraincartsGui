package com.dnamaster10.traincartsticketshop.util.database.databaseaccessorinterfaces;

import com.dnamaster10.traincartsticketshop.util.database.databaseobjects.LinkDatabaseObject;
import com.dnamaster10.traincartsticketshop.util.exceptions.ModificationException;
import com.dnamaster10.traincartsticketshop.util.exceptions.QueryException;

import java.util.List;

public interface LinksDatabaseAccessor {
    LinkDatabaseObject[] getLinksByGuiId(int guiId, int page) throws QueryException;
    LinkDatabaseObject[] searchLinks(int guiId, int offset, String searchTerm) throws QueryException;
    int getTotalLinks(int guiId) throws QueryException;
    int getTotalLinkSearchResults(int guiId, String searchTerm) throws QueryException;

    void saveLinkPage(int guiId, int page, List<LinkDatabaseObject> links) throws ModificationException;
}
