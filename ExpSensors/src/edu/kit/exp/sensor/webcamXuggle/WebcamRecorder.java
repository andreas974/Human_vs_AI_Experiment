package edu.kit.exp.sensor.webcamXuggle;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamResolution;
import com.xuggle.mediatool.IMediaWriter;
import com.xuggle.mediatool.ToolFactory;
import com.xuggle.xuggler.ICodec;
import com.xuggle.xuggler.IContainerFormat;
import com.xuggle.xuggler.IPixelFormat;
import com.xuggle.xuggler.IVideoPicture;
import com.xuggle.xuggler.video.ConverterFactory;
import com.xuggle.xuggler.video.IConverter;
import edu.kit.exp.common.Constants;
import edu.kit.exp.common.LogHandler;
import edu.kit.exp.common.RecordingException;
import edu.kit.exp.common.sensor.ISensorRecorder;

import java.awt.*;
import java.awt.image.BufferedImage;

public class WebcamRecorder extends ISensorRecorder<WebcamRecorderConfiguration> {

    private static final String MENU_TEXT = "Webcam";

    private long mSecBetweenFrames;
    private long startTime;

    private Webcam webcam;
    private ICodec.ID codec;
    private Dimension resolution;
    private IContainerFormat containerFormat;

    private IMediaWriter writer;

	
	@Override
	public String getMenuText() {
		return MENU_TEXT;
	}

	@Override
	public boolean configureRecorder(WebcamRecorderConfiguration configuration) {

	    // Choose webcamXuggle
        this.webcam = Webcam.getDefault();
        Webcam.getDiscoveryService().setEnabled(false);
        Webcam.getDiscoveryService().stop();

        if (webcam == null) {
            LogHandler.printException(new Exception("Webcam is null"));
            return false;
        }

        // Set framerate
        this.mSecBetweenFrames = 1000/configuration.fps;

        // Set codec
        containerFormat = IContainerFormat.make();
        switch (configuration.codec) {
            case H264:
                codec = ICodec.ID.CODEC_ID_H264;
                containerFormat.setOutputFormat("mp4", null, "application/mp4");
                break;
            case MJPEG:
                codec = ICodec.ID.CODEC_ID_MJPEG;
                containerFormat.setOutputFormat("avi", null, "application/avi");
                break;
            case THEORA:
                codec = ICodec.ID.CODEC_ID_THEORA;
                containerFormat.setOutputFormat("ogg", null, "application/ogg");
                break;
            default:
                codec = ICodec.ID.CODEC_ID_THEORA;
                containerFormat.setOutputFormat("ogg", null, "application/ogg");
                break;
        }

        // Set resolution
        switch (configuration.resolution) {
            case HD720:
                resolution = WebcamResolution.HD720.getSize();
                Dimension[] nonStandardResolutions = new Dimension[] {resolution};
                webcam.setCustomViewSizes(nonStandardResolutions);
                break;
            case VGA:
                resolution = WebcamResolution.VGA.getSize();
                break;
            case QVGA:
                resolution = WebcamResolution.QVGA.getSize();
                break;
            case QQVGA:
                resolution = WebcamResolution.SVGA.getSize();
                break;
            default:
                resolution = WebcamResolution.VGA.getSize();
                break;
        }

        writer = ToolFactory.makeWriter("video" + Constants.getComputername() + System.currentTimeMillis() + ".mp4");

        // Manually set the container format (because it can't detect it by
        // filename)
        writer.getContainer().setFormat(containerFormat);

        // Add the video stream to the writer

        writer.addVideoStream(0, 0, codec, resolution.width, resolution.height);

        writer.setForceInterleave(false);
        // Open the webcamXuggle
        webcam.setViewSize(resolution);

        return true;
    }

    @Override
    public void Recording() throws RecordingException {
        webcam.open(true);
        int i = 0;
        startTime = System.currentTimeMillis();
        LogHandler.printInfo("Webcam start time for " + Constants.getComputername() + ":" + startTime);
        System.out.println("Webcam start time for " + Constants.getComputername() + ":" + startTime);

        try {

            while (this.isCapturingActive()) {

                long time1 = System.currentTimeMillis();

                BufferedImage image = ConverterFactory.convertToType(webcam.getImage(), BufferedImage.TYPE_3BYTE_BGR);
                IConverter converter = ConverterFactory.createConverter(image, IPixelFormat.Type.YUV420P);

                IVideoPicture frame = converter.toPicture(image, (time1 - startTime) * 1000);


                frame.setKeyFrame(0 == i++); // Set whether frame is key frame
                frame.setQuality(100); // Set whether quality has changed

                writer.encodeVideo(0, frame);
                long time2 = System.currentTimeMillis();

                try {
                    if ((time2 - time1) < mSecBetweenFrames) {
                        Thread.sleep(mSecBetweenFrames + time1 - time2);
                    }
                } catch (InterruptedException e) {
                    LogHandler.printException(e, "Exception while waiting to write next video frame");
                }
            }
        } catch (Exception e) {
            LogHandler.printException(e, "Exception while recording video");
        }
    }


	@Override
	public void cleanupRecorder() {

        LogHandler.printInfo("Finishing up video recording...");

        if (writer != null) {
            writer.close();
        }

        if (webcam != null) {
            webcam.close();
        }

        LogHandler.printInfo("Closed video device.");
	}

}
