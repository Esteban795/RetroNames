package linguacrypt.QRCode;

import java.awt.image.BufferedImage;
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

public class QRCodeGenerator {

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
                    case RED:
                        image.setRGB(j, i, 0xFF0000);
                        break;
                    case BLUE:
                        image.setRGB(j, i, 0x0000FF);
                        break;
                    case WHITE:
                        image.setRGB(j, i, 0xFFFFDD);
                        break;
                    case BLACK:
                        image.setRGB(j, i, 0x000000);
                        break;
                    default:
                        image.setRGB(j, i, 0xFFFFFF);
                        break;
                }
            }
        }
        // Save the image to a file
        File outputfile = new File("grid.png");
        ImageIO.write(image, "png", outputfile);

        String encodedfile = Base64.getEncoder().encodeToString(Files.readAllBytes(outputfile.toPath()));
        String data_uri = "data:image/png;base64," + encodedfile;
        String payload = "<!DOCTYPE html><html><img src=\"" + data_uri
                + "\" style=\"width:100vw;image-rendering: pixelated;\"/></html>";
        String html_content = "data:text/html;base64," + Base64.getEncoder().encodeToString(payload.getBytes());

        HashMap<EncodeHintType, String> hints = new HashMap<>();
        hints.put(EncodeHintType.QR_COMPACT, "true");
        hints.put(EncodeHintType.MARGIN, "0"); // Adjust margin if necessary
        hints.put(EncodeHintType.ERROR_CORRECTION, "L");

        QRCodeWriter barcodeWriter = new QRCodeWriter();
        int qrCodeSize = 500; // Increase the size of the QR code
        BitMatrix bitMatrix = barcodeWriter.encode(html_content, BarcodeFormat.QR_CODE, qrCodeSize, qrCodeSize, hints);

        // Save the QR code image to a file
        File qrFile = new File(filePath);
        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", qrFile.toPath());

        File qrImage = new File(filePath);
        BufferedImage qrImageBuffered = ImageIO.read(qrImage);
        resizeQRCodeImage(qrImageBuffered);
        return 0;
    }

    private static void resizeQRCodeImage(BufferedImage image) throws IOException {
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

        // System.out.println("minX: " + minX + " maxX: " + maxX + " minY: " + minY + "
        // maxY: " + maxY);

        BufferedImage copyImg = deepCopy(image);
        BufferedImage resizedImg = copyImg.getSubimage(minX - 10, minY - 10, maxX - minX + 20, maxY - minY + 20);
        ImageIO.write(resizedImg, "png", new File("src/main/resources/imgs/qrcode_resized.png"));
    }

    private static BufferedImage deepCopy(BufferedImage bi) {
        BufferedImage copy = new BufferedImage(bi.getWidth(), bi.getHeight(), bi.getType());
        for (int i = 0; i < bi.getWidth(); i++) {
            for (int j = 0; j < bi.getHeight(); j++) {
                copy.setRGB(i, j, bi.getRGB(i, j));
            }
        }
        return copy;
    }
}
