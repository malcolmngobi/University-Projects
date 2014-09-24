package fyp;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
/**
 *
 * @author Dr. Andrew Coles 
 * @website http://www.inf.kcl.ac.uk/staff/andrew/
 */
public class VideoSegment implements Comparable<VideoSegment> {
    
    /** @brief The frame number where this should start playing in the output video */
    public int outputStartFrame;
        
    /** @brief Input video filename */
    private String inputFilename;

    /** @brief Skip to this frame in the input video */
    private int inputStartFrame;

    /** @brief Read this many frames from the input video */
    private int inputFrameCount;
    
    /** @brief Number of frames read so far */
    private int framesRead = 0;
    
    private int WIDTH = 640;
    private int HEIGHT = 480;
    public static final int BPP = 3;
    
    public VideoSegment(int o, String fn, int sf, int fc, int wd, int ht) {
        outputStartFrame = o;
        inputFilename = fn;
        inputStartFrame = sf;
        inputFrameCount = fc;
        WIDTH = wd;
        HEIGHT = ht;
    }
    
    private byte[] inputVideoBuffer;
    private Process p = null;
    
    private DataInputStream videoData;
    
    private BufferedImage thumbnail;
    
    
    /** @brief Get a thumbnail for the first frame of this video */
    public BufferedImage getThumbnail() {
        if (thumbnail == null) {
            Process thumbnailP = null;
            try {
                ArrayList<String> cmdArray = new ArrayList(12);
                cmdArray.add("ffmpeg");
                cmdArray.add("-i");
                cmdArray.add(inputFilename);
                cmdArray.add("-ss");
                cmdArray.add(Integer.toString(inputStartFrame / 50));
                if ((inputStartFrame % 50) != 0) {
                    int centisecs = (inputStartFrame % 50) * 2;
                    if (centisecs < 0) {
                        cmdArray.set(4, cmdArray.get(4) + ".0" + Integer.toString(centisecs));
                    } else {
                        cmdArray.set(4, cmdArray.get(4) + "." + Integer.toString(centisecs));
                    }
                }
                cmdArray.add("-vframes");
                cmdArray.add("1");
                cmdArray.add("-f");
                cmdArray.add("rawvideo");
                cmdArray.add("-pix_fmt");
                cmdArray.add("rgb24");
                cmdArray.add("-s");
                cmdArray.add(Integer.toString(WIDTH) + "x" + Integer.toString(HEIGHT));
                cmdArray.add("-");

                ProcessBuilder pb = new ProcessBuilder(cmdArray);
                pb.redirectError(ProcessBuilder.Redirect.INHERIT);

                thumbnailP = pb.start();

                //MonitorThread otErr = new MonitorThread(p.getErrorStream(), System.err);
                //otErr.start();

                DataInputStream thumbnailVideoData = new DataInputStream(thumbnailP.getInputStream());

                byte[] thumbnailVideoBuffer = new byte[WIDTH * HEIGHT * BPP];
                
                thumbnailVideoData.readFully(thumbnailVideoBuffer);
                
                thumbnailVideoData.close();
                
                thumbnail = new BufferedImage(WIDTH / 4, HEIGHT / 4, BufferedImage.TYPE_INT_ARGB);
                
                {
                    
                    int bytePos = 0;
                    int x,y;


                    for (y = 0; y < HEIGHT; ++y) {
                        for (x = 0; x < WIDTH; ++x, bytePos += 3) {
                            Color rgb = new Color(thumbnailVideoBuffer[bytePos] & 0xFF,
                                                  thumbnailVideoBuffer[bytePos + 1] & 0xFF,
                                                  thumbnailVideoBuffer[bytePos + 2] & 0xFF);
                            thumbnail.setRGB(x/4, y/4, rgb.getRGB());
                        }
                    }
                }
                
                
            }
            catch (Exception e) {
                System.err.println("Error creating thumbnail for " + inputFilename + ": " + e.getMessage());
                thumbnail = null;
            }
            finally {
                if (thumbnailP != null) {
                    thumbnailP.destroy();
                }
            }
        
        }
        
        return thumbnail;
    }
    
    /** @brief Open the video file, ready for processing */
    public void openFile() throws IOException {
        // open file and skip frames
        
        ArrayList<String> cmdArray = new ArrayList(12);
        cmdArray.add("ffmpeg");
        cmdArray.add("-i");
        cmdArray.add(inputFilename);
        cmdArray.add("-ss");
        cmdArray.add(Integer.toString(inputStartFrame / 50));
        if ((inputStartFrame % 50) != 0) {
            int centisecs = (inputStartFrame % 50) * 2;
            if (centisecs < 0) {
                cmdArray.set(4, cmdArray.get(4) + ".0" + Integer.toString(centisecs));
            } else {
                cmdArray.set(4, cmdArray.get(4) + "." + Integer.toString(centisecs));
            }
        }
        cmdArray.add("-f");
        cmdArray.add("rawvideo");
        cmdArray.add("-pix_fmt");
        cmdArray.add("rgb24");
        cmdArray.add("-s");
        cmdArray.add(Integer.toString(WIDTH) + "x" + Integer.toString(HEIGHT));
        cmdArray.add("-");
        
        ProcessBuilder pb = new ProcessBuilder(cmdArray);
        pb.redirectError(ProcessBuilder.Redirect.INHERIT);

        p = pb.start();
        
        //MonitorThread otErr = new MonitorThread(p.getErrorStream(), System.err);
        //otErr.start();
        
        videoData = new DataInputStream(p.getInputStream());
        
        inputVideoBuffer = new byte[WIDTH * HEIGHT * BPP];
        
        
    }
    
    /** @brief Get the next frame of video
     *
     *  The video is returned in RGB24 format, i.e. one byte for red, one byte for green, one byte for blue.
     *  The array contains the pixels in the video one at a time, starting from the top left, and working down one row at a time,
     *  with red-green-blue bytes for each pixel.
     */
    public BufferedImage getNextFrame() throws IOException {
        if (p == null) {
            return null;
        }
        
        //int bytesRead = videoData.read(inputVideoBuffer);

        try {
            videoData.readFully(inputVideoBuffer);
        }
        catch (IOException e) {
            System.err.println("Could not read fully from the input stream, asked for " + inputVideoBuffer.length);
            e.printStackTrace();
            System.err.println(e.getMessage());
            videoData.close();
            p = null;
            return null;
        }
        
        int bytesRead = inputVideoBuffer.length;
        
        if (bytesRead != inputVideoBuffer.length) {
            System.err.println("Only read " + bytesRead + " from the input stream, asked for " + inputVideoBuffer.length);
            videoData.close();
            p = null;
            return null;
        }
        
        ++framesRead;
        
        if (p != null && framesRead == inputFrameCount) {
            System.err.println("Frames read is " + framesRead + ", which equals the limit of " + inputFrameCount);
            
            videoData.close();
            p.destroy();
            p = null;            
        }
        
        //Mally edited Dr. Coles Class
        BufferedImage nextThumbnail = new BufferedImage(WIDTH / 4, HEIGHT / 4, BufferedImage.TYPE_INT_ARGB);
                
                {
                    
                    int bytePos = 0;
                    int x,y;


                    for (y = 0; y < HEIGHT; ++y) {
                        for (x = 0; x < WIDTH; ++x, bytePos += 3) {
                            Color rgb = new Color(inputVideoBuffer[bytePos] & 0xFF,
                                                  inputVideoBuffer[bytePos + 1] & 0xFF,
                                                  inputVideoBuffer[bytePos + 2] & 0xFF);
                            nextThumbnail.setRGB(x/4, y/4, rgb.getRGB());
                        }
                    }
                }//End of additional code 
        
        return nextThumbnail;
    }
    
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("begin_videosegment\n");
        
        str.append(Integer.toString(outputStartFrame));
        str.append("\n");
        
        str.append(inputFilename);
        str.append("\n");
        
        str.append(Integer.toString(inputStartFrame));
        str.append("\n");
        
        str.append(Integer.toString(inputFrameCount));
        str.append("\n");
        
        
        str.append("end_videosegment\n");
        
        return str.toString();
    }
    
    public String getFilename() {
        return inputFilename;    
    }
    
    public int getInputStartFrame() {
        return inputStartFrame;
    }
    
    public int getInputFrameCount() {
        return inputFrameCount;
    }
    
    public int getOutputStartFrame() {
        return outputStartFrame;
    }
    
    public void updateFigures(int newTrim, int newToPlay, int newPosition) {
        if (newTrim != inputStartFrame) {
            thumbnail = null;
        }
        inputStartFrame = newTrim;
        inputFrameCount = newToPlay;
        outputStartFrame = newPosition;
    }
    
    public static ArrayList<VideoSegment> readVideoSegments(BufferedReader r) throws IOException {
        
        {
            String magic = r.readLine();
            if (!(magic.equals("begin_videosegments"))) {
                throw new java.io.StreamCorruptedException("Expected begin_videosegments, got" + magic);
            }
        }
        
        String lcString = r.readLine();
        int listCount = Integer.parseInt(lcString);        
        ArrayList<VideoSegment> l = new ArrayList<VideoSegment>(listCount);
        
        for (int s = 0; s < listCount; ++s) {
            l.add(new VideoSegment(Integer.parseInt(r.readLine()),
                                   r.readLine(),
                                   Integer.parseInt(r.readLine()),
                                   Integer.parseInt(r.readLine()), 0,0));
        }
        
        {
            String magic = r.readLine();
            if (!(magic.equals("end_videosegments"))) {
                throw new java.io.StreamCorruptedException("Expected end_videosegments, got" + magic);
            }
        }
        
        
        return l;
    }

    @Override
    public int compareTo(VideoSegment other) {
        return outputStartFrame - other.outputStartFrame;
        
    }
    
    
}
