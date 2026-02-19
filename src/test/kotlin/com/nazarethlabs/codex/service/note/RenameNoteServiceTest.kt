package com.nazarethlabs.codex.service.note

import com.intellij.openapi.project.Project
import com.nazarethlabs.codex.dto.Note
import com.nazarethlabs.codex.helper.DialogHelper
import com.nazarethlabs.codex.helper.FileHelper
import com.nazarethlabs.codex.repository.NoteStorageRepository
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.mockStatic
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class RenameNoteServiceTest {
    @Mock
    private lateinit var project: Project

    @Mock
    private lateinit var openNotesService: OpenNotesService

    @InjectMocks
    private lateinit var renameNoteService: RenameNoteService

    @Test
    fun `should open note after successful rename`() {
        val note = Note(id = "1", title = "Old Title", filePath = "/notes/Old Title.md")
        val newTitle = "New Title"
        val newFilePath = "/notes/New Title.md"

        mockStatic(DialogHelper::class.java).use { mockedDialog ->
            mockStatic(FileHelper::class.java).use { mockedFileHelper ->
                mockStatic(NoteStorageRepository::class.java).use { mockedRepo ->
                    val repository = org.mockito.Mockito.mock(NoteStorageRepository::class.java)

                    mockedDialog
                        .`when`<String?> {
                            DialogHelper.showInputDialog(
                                project,
                                org.mockito.ArgumentMatchers.anyString(),
                                org.mockito.ArgumentMatchers.anyString(),
                                "Old Title",
                            )
                        }.thenReturn(newTitle)

                    mockedFileHelper.`when`<Boolean> { FileHelper.getParentPath("/notes/Old Title.md") }.thenReturn("/notes")
                    mockedFileHelper.`when`<Boolean> { FileHelper.fileExists("/notes", "New Title.md") }.thenReturn(false)
                    mockedFileHelper.`when`<Boolean> { FileHelper.renameFile("/notes/Old Title.md", "New Title.md") }.thenReturn(true)
                    mockedFileHelper
                        .`when`<String> {
                            FileHelper.getNewFilePath(
                                "/notes/Old Title.md",
                                "New Title.md",
                            )
                        }.thenReturn(newFilePath)

                    mockedRepo.`when`<NoteStorageRepository> { NoteStorageRepository.getInstance() }.thenReturn(repository)

                    renameNoteService.rename(project, note)

                    verify(openNotesService).openAll(project, listOf(note))
                }
            }
        }
    }

    @Test
    fun `should not open note when rename fails`() {
        val note = Note(id = "1", title = "Old Title", filePath = "/notes/Old Title.md")
        val newTitle = "New Title"

        mockStatic(DialogHelper::class.java).use { mockedDialog ->
            mockStatic(FileHelper::class.java).use { mockedFileHelper ->
                mockStatic(NoteStorageRepository::class.java).use {
                    mockedDialog
                        .`when`<String?> {
                            DialogHelper.showInputDialog(
                                project,
                                org.mockito.ArgumentMatchers.anyString(),
                                org.mockito.ArgumentMatchers.anyString(),
                                "Old Title",
                            )
                        }.thenReturn(newTitle)

                    mockedFileHelper.`when`<String> { FileHelper.getParentPath("/notes/Old Title.md") }.thenReturn("/notes")
                    mockedFileHelper.`when`<Boolean> { FileHelper.fileExists("/notes", "New Title.md") }.thenReturn(false)
                    mockedFileHelper.`when`<Boolean> { FileHelper.renameFile("/notes/Old Title.md", "New Title.md") }.thenReturn(false)

                    renameNoteService.rename(project, note)

                    org.mockito.Mockito.verifyNoInteractions(openNotesService)
                }
            }
        }
    }

    @Test
    fun `should not rename when user cancels dialog`() {
        val note = Note(id = "1", title = "Old Title", filePath = "/notes/Old Title.md")

        mockStatic(DialogHelper::class.java).use { mockedDialog ->
            mockStatic(FileHelper::class.java).use { mockedFileHelper ->
                mockedFileHelper.`when`<String> { FileHelper.getParentPath("/notes/Old Title.md") }.thenReturn("/notes")

                mockedDialog
                    .`when`<String?> {
                        DialogHelper.showInputDialog(
                            project,
                            org.mockito.ArgumentMatchers.anyString(),
                            org.mockito.ArgumentMatchers.anyString(),
                            "Old Title",
                        )
                    }.thenReturn(null)

                renameNoteService.rename(project, note)

                org.mockito.Mockito.verifyNoInteractions(openNotesService)
            }
        }
    }
}
