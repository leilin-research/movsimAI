package org.systemx.qlearning.state;
import java.beans.XMLEncoder;
import java.beans.XMLDecoder;
import java.io.*;

import org.systemx.qlearning.commun.FileParser;

public class StatesListGroup {
	
    public static void write(StatesList statesList, String filename) throws Exception{
    	FileParser.writeFile(filename, statesList);
    }

    public static StatesList read(String filename) throws Exception {
    	return FileParser.parseFile(filename);
    }
    
//    public static void write(StatesList StatesList, String filename) throws Exception{
//        XMLEncoder encoder =
//           new XMLEncoder(
//              new BufferedOutputStream(
//                new FileOutputStream(filename)));
//        encoder.writeObject(StatesList);
//        encoder.close();
//    }
//
//    public static StatesList read(String filename) throws Exception {
//        XMLDecoder decoder =
//            new XMLDecoder(new BufferedInputStream(
//                new FileInputStream(filename)));
//        StatesList StatesList = (StatesList)decoder.readObject();
//        decoder.close();
//        return StatesList;
//    }
}
