package org.systemx.qlearning.state;
import java.beans.XMLEncoder;
import java.beans.XMLDecoder;
import java.io.*;

import org.systemx.qlearning.commun.FileParser;

public class StatesListGroup {
	
    public static void write(StatesList statesList, String filename) throws Exception{
    	FileParser.writeFile(filename + ".temp", statesList);
    	FileParser.renameFile(filename + ".temp",filename);
    }

    public static StatesList read(String filename) throws Exception {
    	return FileParser.parseFile(filename);
    }
}
