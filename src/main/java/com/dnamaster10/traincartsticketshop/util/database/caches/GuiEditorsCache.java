package com.dnamaster10.traincartsticketshop.util.database.caches;

import com.dnamaster10.traincartsticketshop.util.database.accessors.GuiDataAccessor;
import com.dnamaster10.traincartsticketshop.util.database.accessors.GuiEditorsDataAccessor;
import com.dnamaster10.traincartsticketshop.util.database.accessors.PlayerDataAccessor;
import com.dnamaster10.traincartsticketshop.util.database.databaseobjects.GuiDatabaseObject;
import com.dnamaster10.traincartsticketshop.util.database.databaseobjects.GuiEditorDatabaseObject;
import com.dnamaster10.traincartsticketshop.util.database.databaseobjects.PlayerDatabaseObject;
import com.dnamaster10.traincartsticketshop.util.exceptions.QueryException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Holds caches of information relating to Gui editors. Should only be accessed via a GuiEditorsDataAccessor
 * @see com.dnamaster10.traincartsticketshop.util.database.accessors.GuiEditorsDataAccessor
 */
public class GuiEditorsCache {
    final ConcurrentHashMap<Integer, List<String>> guiIdEditorsMap = new ConcurrentHashMap<>();
    final ConcurrentHashMap<String, List<Integer>> editorGuisMap = new ConcurrentHashMap<>();

    public void initialize() throws QueryException {
        GuiEditorsDataAccessor editorsAccessor = new GuiEditorsDataAccessor();
        List<GuiEditorDatabaseObject> editors = editorsAccessor.getAllGuiEditorsFromDatabase();
        for (GuiEditorDatabaseObject editor : editors) {
            if (!guiIdEditorsMap.containsKey(editor.guiId())) guiIdEditorsMap.put(editor.guiId(), new ArrayList<>());
            guiIdEditorsMap.get(editor.guiId()).add(editor.editorUuid());

            if (!editorGuisMap.containsKey(editor.editorUuid())) editorGuisMap.put(editor.editorUuid(), new ArrayList<>());
            editorGuisMap.get(editor.editorUuid()).add(editor.guiId());
        }
    }

    public boolean checkGuiEditorByUuid(int guiId, String uuid) {
        if (!guiIdEditorsMap.containsKey(guiId)) return false;
        return guiIdEditorsMap.get(guiId).contains(uuid);
    }

    public List<String> getEditorUsernamesForGui(int id) {
        if (!guiIdEditorsMap.containsKey(id)) return new ArrayList<>();
        List<String> usernames = new ArrayList<>();
        PlayerDataAccessor playerAccessor = new PlayerDataAccessor();
        for (String uuid : guiIdEditorsMap.get(id)) {
            PlayerDatabaseObject player = playerAccessor.getPlayerByUuid(uuid);
            usernames.add(player.username());
        }
        return usernames;
    }
    public List<GuiDatabaseObject> getGuisEditableByEditor(String uuid) {
        //Returns a list of guis where a player is registered as an editor.
        if (!editorGuisMap.containsKey(uuid)) return new ArrayList<>();
        List<GuiDatabaseObject> guis = new ArrayList<>();
        GuiDataAccessor guiAccessor = new GuiDataAccessor();
        for (int guiId : editorGuisMap.get(uuid)) {
            GuiDatabaseObject gui = guiAccessor.getGuiById(guiId);
            guis.add(gui);
        }
        return guis;
    }

    public void addGuiEditor(String uuid, int guiId) {
        if (guiIdEditorsMap.containsKey(guiId)) {
            List<String> editors = guiIdEditorsMap.get(guiId);
            if (!editors.contains(uuid)) editors.add(uuid);
        } else {
            List<String> editors = new ArrayList<>();
            editors.add(uuid);
            guiIdEditorsMap.put(guiId, editors);
        }
        if (editorGuisMap.containsKey(uuid)) {
            List<Integer> guis = editorGuisMap.get(uuid);
            if (!guis.contains(guiId)) guis.add(guiId);
        } else {
            List<Integer> guis = new ArrayList<>();
            guis.add(guiId);
            editorGuisMap.put(uuid, guis);
        }

    }

    public void removeGuiEditor(String uuid, int guiId) {
        guiIdEditorsMap.get(guiId).remove(uuid);
        editorGuisMap.get(uuid).remove(Integer.valueOf(guiId));
    }
    public void removeGui(int guiId) {
        if (!guiIdEditorsMap.containsKey(guiId)) return;
        List<String> editors = guiIdEditorsMap.get(guiId);
        guiIdEditorsMap.remove(guiId);

        for (String uuid : editors) {
            if (!editorGuisMap.containsKey(uuid)) continue;
            List<Integer> guis = editorGuisMap.get(uuid);
            guis.remove(Integer.valueOf(guiId));
            if (guis.isEmpty()) editorGuisMap.remove(uuid);
        }
    }
}
