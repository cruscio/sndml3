package servicenow.core;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

public class WriterMetrics {

	private final String name;
	private int inserted = 0;
	private int updated = 0;
	private int deleted = 0;
	private int skipped = 0;
	private Date started = null;
	private Date finished = null;

	public WriterMetrics(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public WriterMetrics start() {
		if (started == null) started = new Date();
		return this;
	}
	
	public WriterMetrics finish() {
		finished = new Date();
		return this;
	}
	
	public DateTime getStarted() {
		return new DateTime(this.started);
	}
	
	public DateTime getFinished() {
		return new DateTime(this.finished);
	}
	
	public int getElapsedSec() {
		return (int) ((finished.getTime() - started.getTime()) / 1000);
	}
		
	public int getProcessed() {
		return getInserted() + getUpdated() + getDeleted() + getSkipped();
	}
	
	public int getInserted() {
		return this.inserted;
	}
	
	public int getUpdated() {
		return this.updated;
	}
	
	public int getDeleted() {
		return this.deleted;
	}
	
	public int getSkipped() {
		return this.skipped;
	}
	
	public void incrementInserted() {
		addInserted(1);
	}
	
	public void incrementUpdated() {
		addUpdated(1);
	}
	
	public void incrementDeleted() {
		addDeleted(1);
	}
	
	public void incrementSkipped() {
		addSkipped(1);
	}

	public synchronized void addInserted(int count) {
		inserted += count;
	}
	
	public synchronized void addUpdated(int count) {
		updated += count;
	}
	
	public synchronized void addDeleted(int count) {
		deleted += count;
	}
	
	public synchronized void addSkipped(int count) {
		skipped += count;
	}
	
	public synchronized void add(WriterMetrics stats) {
		if (started == null || started.getTime() > stats.started.getTime()) started = stats.started;
		if (finished == null || finished.getTime() < stats.finished.getTime()) finished = stats.finished;
		inserted += stats.inserted;
		updated += stats.updated;
		deleted += stats.deleted;
		skipped += stats.skipped;
	}
	
	public void write(PrintWriter writer) throws IOException {
		write(writer, null);
	}

	public void write(PrintWriter writer, String prefix) throws IOException {
		assert writer != null;
		if (prefix == null) prefix = "";
		if (prefix.length() > 0) prefix = prefix + ".";
		writer.println(prefix + "start="     + getStarted());
		writer.println(prefix + "finish="    + getFinished());
		writer.println(prefix + "elapsed="   + String.valueOf(getElapsedSec()));
		writer.println(prefix + "inserted="  + String.valueOf(getInserted()));
		writer.println(prefix + "updated="   + String.valueOf(getUpdated()));
		writer.println(prefix + "deleted="   + String.valueOf(getDeleted()));
		writer.println(prefix + "skipped="   + String.valueOf(getSkipped()));
		writer.println(prefix + "processed=" + String.valueOf(getProcessed()));		
	}
	
}
