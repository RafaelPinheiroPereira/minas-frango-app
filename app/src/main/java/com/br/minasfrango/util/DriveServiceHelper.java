package com.br.minasfrango.util;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;
import android.util.Pair;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.api.client.http.ByteArrayContent;
import com.google.api.client.http.FileContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class DriveServiceHelper {
    private final Executor mExecutor = Executors.newSingleThreadExecutor();
    private final Drive mDriveService;

    public DriveServiceHelper(Drive driveService) {
        mDriveService = driveService;
    }

    /** Creates a text file in the user's My Drive folder and returns its file ID. */
    public Task<String> createFile() {
        return Tasks.call(
                mExecutor,
                () -> {
                    File metadata =
                            new File()
                                    .setParents(Collections.singletonList("root"))
                                    .setMimeType("image/*")
                                    .setName("Untitled file");

                    File googleFile = mDriveService.files().create(metadata).execute();
                    if (googleFile == null) {
                        throw new IOException("Null result when requesting file creation.");
                    }

                    return googleFile.getId();
                });
    }

    /**
     * Opens the file identified by {@code fileId} and returns a {@link Pair} of its name and
     * contents.
     */
    public Task<Pair<String, String>> readFile(String fileId) {
        return Tasks.call(
                mExecutor,
                () -> {
                    // Retrieve the metadata as a File object.
                    File metadata = mDriveService.files().get(fileId).execute();
                    String name = metadata.getName();

                    // Stream the file contents to a String.
                    try (InputStream is =
                                    mDriveService.files().get(fileId).executeMediaAsInputStream();
                            BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
                        StringBuilder stringBuilder = new StringBuilder();
                        String line;

                        while ((line = reader.readLine()) != null) {
                            stringBuilder.append(line);
                        }
                        String contents = stringBuilder.toString();

                        return Pair.create(name, contents);
                    }
                });
    }

    /**
     * Updates the file identified by {@code fileId} with the given {@code name} and {@code
     * content}.
     */
    public Task<Void> saveFile(String fileId, String name, String content) {
        return Tasks.call(
                mExecutor,
                () -> {
                    // Create a File containing any metadata changes.
                    File metadata = new File().setName(name);

                    // Convert content to an AbstractInputStreamContent instance.
                    ByteArrayContent contentStream =
                            ByteArrayContent.fromString("image/*", content);

                    // Update the metadata and contents.

                    mDriveService.files().update(fileId, metadata, contentStream).execute();
                    return null;
                });
    }

    /**
     * Returns a {@link FileList} containing all the visible files in the user's My Drive.
     *
     * <p>The returned list will only contain files visible to this app, i.e. those which were
     * created by this app. To perform operations on files not created by the app, the project must
     * request Drive Full Scope in the <a href="https://play.google.com/apps/publish">Google
     * Developer's Console</a> and be submitted to Google for verification.
     */
    public Task<FileList> queryFiles() {
        return Tasks.call(
                mExecutor,
                new Callable<FileList>() {
                    @Override
                    public FileList call() throws Exception {
                        return mDriveService.files().list().setSpaces("drive").execute();
                    }
                });
    }



    public Task<FileList> queryFolders() {
        return Tasks.call(
                mExecutor,
                new Callable<FileList>() {
                    @Override
                    public FileList call() throws Exception {

                        String pageToken = null;
                        FileList result = null;
                        do {
                            result =
                                    mDriveService
                                            .files()
                                            .list()
                                            .setQ("mimeType='application/vnd.google-apps.folder'")
                                            .setSpaces("drive")
                                            .setFields("nextPageToken, files(id, name)")
                                            .setPageToken(pageToken)
                                            .execute();
                            for (File file : result.getFiles()) {
                                System.out.printf(
                                        "Found file: %s (%s)\n", file.getName(), file.getId());
                            }
                            pageToken = result.getNextPageToken();
                        } while (pageToken != null);

                        return result;
                    }
                });
    }

    /** Returns an {@link Intent} for opening the Storage Access Framework file picker. */
    public Intent createFilePickerIntent() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");

        return intent;
    }

    /**
     * Opens the file at the {@code uri} returned by a Storage Access Framework {@link Intent}
     * created by {@link #createFilePickerIntent()} using the given {@code contentResolver}.
     */
    public Task<Pair<String, String>> openFileUsingStorageAccessFramework(
            ContentResolver contentResolver, Uri uri) {
        return Tasks.call(
                mExecutor,
                () -> {
                    // Retrieve the document's display name from its metadata.
                    String name;
                    try (Cursor cursor = contentResolver.query(uri, null, null, null, null)) {
                        if (cursor != null && cursor.moveToFirst()) {
                            int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                            name = cursor.getString(nameIndex);
                        } else {
                            throw new IOException("Empty cursor returned for file.");
                        }
                    }

                    // Read the document's contents as a String.
                    String content;
                    try (InputStream is = contentResolver.openInputStream(uri);
                            BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
                        StringBuilder stringBuilder = new StringBuilder();
                        String line;
                        while ((line = reader.readLine()) != null) {
                            stringBuilder.append(line);
                        }
                        content = stringBuilder.toString();
                    }

                    return Pair.create(name, content);
                });
    }

    public Task<String> criarPastaNoDrive(String idPastaPai, String nomeDaPasta) {

        return Tasks.call(
                mExecutor,
                new Callable<String>() {
                    @Override
                    public String call() throws Exception {

                        File fileMetadata = new File();
                        fileMetadata.setName(nomeDaPasta);
                        fileMetadata.setParents(Collections.singletonList(idPastaPai));
                        fileMetadata.setMimeType("application/vnd.google-apps.folder");

                        File file = null;

                        file = mDriveService.files().create(fileMetadata).setFields("id, parents").execute();

                        if (file != null) {
                            return file.getId();
                        } else {
                            throw new IOException("Null result when requesting file creation.");
                        }
                    }
                });
    }

    public Task<String> inserirArquivoNaPasta(String idDaPasta, String nomeArquivo) {

        return Tasks.call(
                mExecutor,
                new Callable<String>() {
                    @Override
                    public String call() throws Exception {
                        File fileMetadata = new File();
                        fileMetadata.setName("photo.jpg");
                        fileMetadata.setParents(Collections.singletonList(idDaPasta));
                        java.io.File filePath = new java.io.File(nomeArquivo);
                        FileContent mediaContent = new FileContent("image/jpeg", filePath);
                        File file =
                                mDriveService
                                        .files()
                                        .create(fileMetadata, mediaContent)
                                        .setFields("id, parents")
                                        .execute();

                        if (file != null) {
                            System.out.println("Arquivo ID: " + file.getId());
                            return file.getId();
                        } else {
                            throw new IOException("Null result when requesting file creation.");
                        }
                    }
                });
    }

    public Task<Boolean> verificarPermissoesDoUsuarioNaPasta(String idDaPasta) {
        return Tasks.call(
                mExecutor,
                new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        File file = mDriveService.files().get(idDaPasta).execute();

                        return file.getCapabilities().getCanEdit().booleanValue();
                    }
                });
    }

    /**
     * Adicionar Pasta Compartilhada ao Meu Drive no aplicativo, é necessário pois a query só
     * encontra os arquivos do app
     *
     * @param idPasta da pasta Compartilhada no Google Drive
     * @return
     */
    public Task<String> adicionarPastaCompartilhadaAoDrive(String idPasta) {

        return Tasks.call(
                mExecutor,
                new Callable<String>() {
                    @Override
                    public String call() throws Exception {
                        File fileMetadata = new File();
                        fileMetadata.setParents(Collections.singletonList("root"));
                        fileMetadata.setId(idPasta);
                        fileMetadata.setMimeType("application/vnd.google-apps.folder");
                        File file = null;

                        file = mDriveService.files().create(fileMetadata).execute();

                        if (file != null) {

                            return file.getId();
                        } else {
                            throw new IOException("Não foi possível adicionar a pasta compartilhada ao Google Drive.\nEntre em contato com o Suporte do Sistema.");
                        }
                    }
                });
    }
}
