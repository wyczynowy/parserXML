package xmlParser;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.DocumentBuilder;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSOutput;
import org.w3c.dom.ls.LSSerializer;
import org.xml.sax.SAXException;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class Main {

	static String inputFolderPath = "C:\\EXAMPLES\\";
	static String outputFolderPath = "C:\\EXAMPLES\\OUTPUT\\";

	public static void main(String argv[]) {

		File folder = new File(inputFolderPath);
		File[] listOfFiles = folder.listFiles();

		for (File file : listOfFiles) {
			if (file.isFile()) {

				System.err.println("MODYFIKACJA PLIKU: \"" + file.getName() + "\"");

				Document dokument = wczytajDokumentXML(inputFolderPath + file.getName());
				modyfikujDokumentXML(dokument);
				zapiszDokumentXML(dokument, outputFolderPath + file.getName());

				System.err.println("KONIEC MODYFIKACJI I ZAPIS PLIKU: \"" + file.getName() + "\" DO WSKAZANEJ LOKALIZACJI\r\n");

			}
		}

		System.out.println("========== MODYFIKACJA PLIKÓW PRZEBIEGŁA POMYŚLNIE ! ==========");
	}

	private static Document wczytajDokumentXML(String sciezkaDoDokumentuXML) {
		File fXmlFile = new File(sciezkaDoDokumentuXML);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder;
		Document dokumentXML = null;
		try {
			dBuilder = dbFactory.newDocumentBuilder();
			dokumentXML = dBuilder.parse(fXmlFile);
		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}
		return dokumentXML;
	}

	private static void zapiszDokumentXML(Document dokument, String sciezkaDoDokumentuXML) {
		final DOMImplementationLS dom = (DOMImplementationLS) dokument.getImplementation();
		final LSSerializer serializer = dom.createLSSerializer();
		final LSOutput destination = dom.createLSOutput();
		serializer.getNewLine();
		destination.setEncoding("UTF-8");
		FileOutputStream out;
		try {
			out = new FileOutputStream(sciezkaDoDokumentuXML);
			destination.setByteStream(out);
			serializer.write(dokument, destination);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	private static void modyfikujDokumentXML(Document dokument) {

		NodeList nList = dokument.getElementsByTagName("note");

		for (int temp = 0; temp < nList.getLength(); temp++) {

			Node nNode = nList.item(temp);

			if (nNode.getNodeType() == Node.ELEMENT_NODE) {

				Element eElement = (Element) nNode;
				Attr attrWartoscPola = eElement.getAttributeNode("important_level");
				String wartoscPola = "";
				int wartoscLiczbowaPola = 0;
				if (attrWartoscPola != null) {
					wartoscPola = attrWartoscPola.getValue();
					try {
						wartoscLiczbowaPola = Integer.valueOf(wartoscPola);
					} catch (NumberFormatException nfe) {

					}

					if (wartoscLiczbowaPola > 0) {

						/* ZMIENIAMY WARTOŚĆ ATRYBUTU */
						if ((wartoscLiczbowaPola >= 2) && (wartoscLiczbowaPola <= 3)) {
							attrWartoscPola.setValue((wartoscLiczbowaPola - 1) + "");
							System.out.println("Pomniejszam wartość pola: " + wartoscLiczbowaPola + " o 1");
						} else if ((wartoscLiczbowaPola >= 4) && (wartoscLiczbowaPola <= 5)) {
							attrWartoscPola.setValue((wartoscLiczbowaPola - 2) + "");
							System.out.println("Pomniejszam wartość pola: " + wartoscLiczbowaPola + " o 2");
						} else {
							System.out.println("Wartość pola: " + wartoscLiczbowaPola + " pozostaje bez zmian");
						}
						/* KONIEC ZMIAN WARTOŚCI ATRYBUTU */
					} else {
						System.out.println("Wartość pola: \"" + attrWartoscPola.getValue() + "\" pozostaje bez zmian");
					}
				}
			}
		}
	}
}