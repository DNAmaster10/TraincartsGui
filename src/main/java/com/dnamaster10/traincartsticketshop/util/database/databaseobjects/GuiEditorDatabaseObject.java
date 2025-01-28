package com.dnamaster10.traincartsticketshop.util.database.databaseobjects;

/**
 * Holds information pertaining to a specific Gui editor.
 *
 * @param guiId The Gui ID for this editor
 * @param editorUuid The UUID of the player
 */
public record GuiEditorDatabaseObject(
        int guiId,
        String editorUuid
) {}
