package etu1947.framework.utile;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.Part;

@MultipartConfig
public class Fichiers {
    String nomFichier;
    byte[] bytesFichier;

    public String getNomFichier(){
        return this.nomFichier;
    }
    public byte[] getBytesFichier(){
        return bytesFichier;
    }

    public void setNomFichier(String nom){
        this.nomFichier = nom;
    }
    public void setBytesFichier(byte[] temp){
        this.bytesFichier = temp;
    }

    public Fichiers(){

    }

    public Fichiers forImage(HttpServletRequest request, String nomImage) throws Exception{

        Fichiers fichier = new Fichiers();

        try {
            
            System.out.println(nomImage);
            Part filePart = request.getPart(nomImage);
            String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
            InputStream fileStream = filePart.getInputStream();
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            byte[] buffy = new byte[2000];
            int i = 0;

            while((i = fileStream.read(buffy)) != -1){
                output.write(buffy, 0, i);
            }

            fichier.setNomFichier(fileName);
            fichier.setBytesFichier(buffy);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return fichier;
    }
}
