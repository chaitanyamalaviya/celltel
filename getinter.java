import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class getinter {

	public static String path = "/Users/chaitanya/desktop/study/4.2/CE4015-SimulationandModeling/Project/PCS_TEST_DETERMINSTIC_S213141.csv";

	public static void main() {

		BufferedReader br = null;
		String line = "";
		int count = 0;
		ArrayList<Number> ele = new ArrayList<Number>();

		try {
			br = new BufferedReader(new FileReader(path));
			while ((line = br.readLine()) != null) {

				String[] entry = line.split(",");

				if (count != 0)
					ele.add(count,
							Double.parseDouble(entry[1])
									- (double) ele.get(count - 1));
				else
					ele.add(0, Double.parseDouble(entry[1]));

				count++;

			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		saveToCsv(
				"/Users/chaitanya/desktop/study/4.2/CE4015-SimulationandModeling/Project/interarrival.csv",
				ele);

	}

	static void saveToCsv(String filePath, ArrayList<Number> contents) {

		try {
			FileWriter writer = new FileWriter(filePath, true);

			Iterator<Number> iterator = contents.iterator();

			while (iterator.hasNext()) {

				writer.write(iterator.next().toString());
				writer.append(',');

			}

			writer.write(" ");
			writer.append("\n");

			writer.flush();
			writer.close();

		} catch (IOException e) {

			System.out.println(e.getMessage());

		}

	}
}
