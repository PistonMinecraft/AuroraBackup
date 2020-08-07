package cn.pistonmc.aurorabackup.aurorabackup.utils;

import cn.pistonmc.aurorabackup.aurorabackup.AuroraBackup;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.world.World;

import java.io.*;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.function.Consumer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Utils {
    private static Logger logger = AuroraBackup.INSTANCE.getLogger();
    private static Task SaveWorldTask;
    private static World world;

    public static void CreateWorldBackup(String WorldName){
        try{
            File Path = new File("./WorldBackup");
            if(!Path.exists()){
                Path.mkdir();
            }

            if(Sponge.getServer().getWorld(WorldName).isPresent()){
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
                world = Sponge.getServer().getWorld(WorldName).get();
                SaveWorldTask = Task.builder().execute(SaveWorld::new).submit(AuroraBackup.INSTANCE.getPluginContainer());
                String FileName = formatter.format(new Date())  + "_" + WorldName + ".zip";
                ZipOutputStream File = new ZipOutputStream(new FileOutputStream(new File("./WorldBackup/" + FileName)));
                Compress(world.getDirectory(),File,  WorldName);
                File.close();
                logger.info(AuroraBackup.INSTANCE.MessageNode.getNode("Message","Create","Success").getString());
            }else{
                logger.warn(AuroraBackup.INSTANCE.MessageNode.getNode("Message","Create","Error").getString());
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private static void Compress(Path SourcePath, ZipOutputStream ZIPOutStream, String ZIPName) throws IOException {
        byte[] Buffer = new byte[4096];

        if(SourcePath.toFile().isFile()){
            ZIPOutStream.putNextEntry(new ZipEntry(ZIPName));
            int Length;
            FileInputStream InputStream = new FileInputStream(SourcePath.toFile());
            while ((Length = InputStream.read(Buffer)) != -1){
                ZIPOutStream.write(Buffer,0,Length);
            }
            ZIPOutStream.closeEntry();
            InputStream.close();
        }else {
            File[] ListFiles = SourcePath.toFile().listFiles();
            if(ListFiles == null || ListFiles.length == 0){
                ZIPOutStream.putNextEntry(new ZipEntry(ZIPName + "/"));
                ZIPOutStream.closeEntry();
            }else {
                for (File file : ListFiles) {
                    Compress(file.toPath(),ZIPOutStream,ZIPName + "/" + file.getName());
                }
            }
        }
    }

    private  static class SaveWorld implements Consumer<Task> {
        @Override
        public void accept(Task task) {
            try {
                world.save();
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                SaveWorldTask.cancel();
            }
        }
    }
}
