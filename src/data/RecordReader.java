package data;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/*
 * Generic record reader
 * readNext() read each line of the dataset and build a Record
 */
public abstract class RecordReader implements Closeable {
	protected Schema schema;
	protected String rawfilename;
	protected BufferedReader fileReader;
	
	public RecordReader(Schema schema, String filename) throws FileNotFoundException {
		this.schema = schema;
		this.rawfilename = filename;
		fileReader = new BufferedReader(new FileReader(filename));
	}
	
	public void close() {
		if (fileReader != null)
			try {
				fileReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
	
	public abstract Record readNext();
	
}
