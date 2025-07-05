package org.example.service;

import org.example.entity.Workspace;
import org.example.enumtype.WorkspaceType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WorkspaceServiceTest {

    private WorkspaceService workspaceService;

    @BeforeEach
    void setUp() {
        workspaceService = new WorkspaceService();
    }

    @Test
    void testCreateWorkspaceAddsToList() {
        Workspace workspace = new Workspace("Room A", WorkspaceType.PRIVATE, 50.0);
        workspaceService.createWorkspace(workspace);

        List<Workspace> allWorkspaces = workspaceService.getAllWorkspaces();
        assertFalse(allWorkspaces.isEmpty());
        Workspace saved = allWorkspaces.get(0);

        assertEquals("Room A", saved.getName());
        assertEquals(WorkspaceType.PRIVATE, saved.getType());
    }

    @Test
    void testDeleteWorkspaceRemovesFromList() {
        Workspace workspace = new Workspace("Desk 1", WorkspaceType.OPEN, 10.0);
        workspaceService.createWorkspace(workspace);

        int id = workspaceService.getAllWorkspaces().get(0).getId();
        workspaceService.deleteWorkspace(id);

        List<Workspace> remaining = workspaceService.getAllWorkspaces();
        assertTrue(remaining.isEmpty());
    }
}
