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
        assertEquals(1, allWorkspaces.size());
        assertEquals("Room A", allWorkspaces.get(0).getName());
        assertEquals(WorkspaceType.PRIVATE, allWorkspaces.get(0).getType());
    }

    @Test
    void testDeleteWorkspaceRemovesFromList() {
        Workspace workspace = new Workspace("Desk 1", WorkspaceType.OPEN, 10.0);
        workspaceService.createWorkspace(workspace);

        int id = workspace.getId();
        workspaceService.deleteWorkspace(id);

        List<Workspace> remaining = workspaceService.getAllWorkspaces();
        assertTrue(remaining.isEmpty());
    }
}