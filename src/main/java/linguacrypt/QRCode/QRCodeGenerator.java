package linguacrypt.QRCode;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import linguacrypt.model.Card;
import linguacrypt.model.Color;

public class QRCodeGenerator {

    private static BufferedImage deepCopy(BufferedImage bi) {
        ColorModel cm = bi.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = bi.copyData(null);
        return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
    }
    
    private static void resizeQRCodeImage(BufferedImage image) throws IOException{
        // Find the bounding box of the QR code in the original image
        int width = image.getWidth();
        int height = image.getHeight();
        int minX = width, minY = height, maxX = 0, maxY = 0;

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (image.getRGB(i, j) != 0xFFFFFFFF) { // Not white
                    if (i < minX) {
                        minX = i;
                    }
                    if (i > maxX) {
                        maxX = i;
                    }
                    if (j < minY) {
                        minY = j;
                    }
                    if (j > maxY) {
                        maxY = j;
                    }
                }
            }
        }

        System.out.println("minX: " + minX + " maxX: " + maxX + " minY: " + minY + " maxY: " + maxY);

        BufferedImage copyImg = deepCopy(image);
        BufferedImage resizedImg = copyImg.getSubimage(minX,minY, maxX-minX, maxY-minY);
        ImageIO.write(resizedImg, "png", new File("src/main/resources/imgs/qrcode_resized.jpg"));
        
    }

    public static BufferedImage generateQRCodeImage(String barcodeText) throws Exception {
        HashMap<EncodeHintType, String> hints = new HashMap<>();
        hints.put(EncodeHintType.QR_COMPACT, "true");
        hints.put(EncodeHintType.MARGIN, "500");
        hints.put(EncodeHintType.ERROR_CORRECTION, "L");
        QRCodeWriter barcodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = barcodeWriter.encode(barcodeText, BarcodeFormat.QR_CODE, 30, 30,
                hints);

        return MatrixToImageWriter.toBufferedImage(bitMatrix);
    }

    public static int generateQRCodeImage(ArrayList<ArrayList<Card>> grid, String filePath) throws Exception {
        // Create the QR code for the given grid, saves it to the filePath,
        // returns 0 if successful

        int width = grid.size();
        int height = grid.get(0).size();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                Card card = grid.get(i).get(j);
                switch (card.getColor()) {
                    case Color.RED:
                        image.setRGB(i, j, 0xFF0000);
                        break;
                    case Color.BLUE:
                        image.setRGB(i, j, 0x0000FF);
                        break;
                    case Color.WHITE:
                        image.setRGB(i, j, 0xFFFFDD);
                        break;
                    case Color.BLACK:
                        image.setRGB(i, j, 0x000000);
                        break;
                    default:
                        image.setRGB(i, j, 0xFFFFFF);
                        break;
                }
            }
        }

        // Save the image to a file
        File outputfile = new File("grid.png");
        ImageIO.write(image, "png", outputfile);

        String encodedfile = Base64.getEncoder().encodeToString(Files.readAllBytes(outputfile.toPath()));
        String data_uri = "data:image/png;base64," + encodedfile;
        String payload = "<!DOCTYPE html>\"<html><img src=\"" + data_uri
                + "\" style=\"width:100vw;image-rendering: pixelated;\"/></html>";
        String html_content = "data:text/html;base64," + Base64.getEncoder().encodeToString(payload.getBytes());

        HashMap<EncodeHintType, String> hints = new HashMap<>();
        hints.put(EncodeHintType.QR_COMPACT, "true");
        hints.put(EncodeHintType.MARGIN, "500");
        hints.put(EncodeHintType.ERROR_CORRECTION, "L");
        QRCodeWriter barcodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = barcodeWriter.encode(html_content, BarcodeFormat.QR_CODE, 30, 30,
                hints);

        // Save the image to a file
        File qrFile = new File(filePath);
        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", qrFile.toPath());

        File qrImage = new File(filePath);
        BufferedImage qrImageBuffered = ImageIO.read(qrImage);
        resizeQRCodeImage(qrImageBuffered);
        return 0;
    }

}
