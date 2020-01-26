package test;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class Exercise1 {

	private List<ReportItem> report = new ArrayList<ReportItem>();

	private final String regex = "^(\\S+) (\\S+) (\\S+) " + "\\[([\\w:/]+\\s[+\\-]\\d{4})\\] \"(\\S+)"
			+ " (\\S+)\\s*(\\S+)?\\s*\" (\\d{3}) (\\S+)";

	private final Pattern pattern = Pattern.compile(regex);

	private final String OK = "200";

	// apply precompiled regex for each log file
	private void apply(final Map<String, ReportItem> tmpReport, final String record) {

		final Matcher matcher = pattern.matcher(record);


		while (matcher.find()) {

			String IP = matcher.group(1);
			String status = matcher.group(8);
			String bytes = matcher.group(9);

			if (status.equalsIgnoreCase(OK))
				try {
					if (tmpReport.containsKey(IP)) {
						tmpReport.get(IP).update(Integer.parseInt(bytes));
					} else {
						tmpReport.put(IP, new ReportItem(IP, Integer.parseInt(bytes)));
					}
				} catch (NumberFormatException e) {
					// System.err.println("skipping: " + record);
				}

		}

	}

	public void sortDesc() {
		report.sort((x, y) -> (y.getCount() - x.getCount()));
	}

	private void applyCSVFormat(Writer writer, ReportItem item, long n, long totalBytes) {

		try {
			writer.write(String.format("%s,%d,%.2f,%d,%.2f\n", item.getIp(), item.getCount(),
					item.getCount() / (double) n * 100.0, item.getBytes(), item.getBytes() / (double) totalBytes * 100.0));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	// convert to csv on the output stream
	public void toCsv(String filePath) throws IOException {

		try (FileWriter writer = new FileWriter(filePath)) {

			// write header
			writer.write("IP,NREQ,NREC_PERC,TOTB,TOTB_PERC\n");

			long totalBytes = report.stream().mapToLong(i -> i.getBytes()).sum();
			report.forEach(x -> applyCSVFormat(writer, x, report.size(), totalBytes));

		} catch (IOException e) {
			throw e;
		}
	}

	public void parseFile(String filePath) throws IOException {
		try (Stream<String> stream = Files.lines(Paths.get(filePath))) {

			final Map<String, ReportItem> tmpReport = new HashMap<String, ReportItem>();
			stream.forEach(x -> this.apply(tmpReport, x));
			
			this.report = new ArrayList<ReportItem>(tmpReport.values());

		} catch (IOException e) {
			throw e;
		}
		

	}

	private void applyJSONFormat(Writer writer, int[] holder, ReportItem item, long n, long totalBytes) {

		try {
			writer.write(String.format("{ \"IP\":\"%s\",\"count\":%d,\"count_perc\":%.2f,\"bytes\":%d,\"bytes_perc\":%.2f}",
					item.getIp(), item.getCount(), item.getCount() / (double) n * 100.0, item.getBytes(),
					item.getBytes() / (double) totalBytes * 100.0));
			
			
			if (holder[0]<n-1) {
				writer.write(",");
			}
			
			holder[0]++;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private void toJson(String filePath) throws IOException {
		long totalBytes = report.stream().mapToLong(i -> i.getBytes()).sum();

		try (FileWriter writer = new FileWriter(filePath)) {
			// write header
			writer.write("[");

			final int[] holder = new int[] { 0 }; // trick to avoid last "," in json
			report.forEach(x -> applyJSONFormat(writer, holder, x,  report.size(), totalBytes));

			// write header
			writer.write("]");
		} catch (IOException e) {
			throw e;
		}
	}

	public static void main(String argv[]) throws IOException {

		Exercise1 ex = new Exercise1();
		
		ex.parseFile("./data/log.log");
		
		ex.sortDesc();
		
		ex.toCsv("./reports/ipaddr.csv");
		
		ex.toJson("./reports/ipaddr.json");

	}

}

/**
 * minimal class to store the item of the aggrgation the encapsulation has been
 * removed to increase readability
 * 
 * @author giacomoveneri
 *
 */
class ReportItem {
	private String ip;
	private int count = 0;
	private long bytes = 0;

	public ReportItem(String ip, long bytes) {
		super();
		this.ip = ip;
		this.count = 1;
		this.bytes = bytes;
	}

	public void update(long bytes) {
		this.count += 1;
		this.bytes = bytes;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public long getBytes() {
		return bytes;
	}

	public void setBytes(long bytes) {
		this.bytes = bytes;
	}

	@Override
	public String toString() {
		return "ReportItem [ip=" + ip + ", count=" + count + ", bytes=" + bytes + "]";
	}
}
