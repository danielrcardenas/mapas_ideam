package co.gov.ideamredd.reportes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.gdal.gdal.Band;
import org.gdal.gdal.Dataset;
import org.gdal.gdal.RasterAttributeTable;
import org.gdal.gdal.gdal;
import org.gdal.gdalconst.gdalconstConstants;

public class GDALInfo {

	private String archivo;
	private Dataset hDataset;
	private Band hBand;
	private int iBand;
	private boolean bShowRAT = true;
	private ArrayList<String> valores = new ArrayList<String>();
	private Integer numeroItems;
	private Integer numeroPixeles;

	public void obtenerInformacionPixeles() {
		
		String pixelsLine = getLineWithPixels();
		//System.out.println(pixelsLine);
		String pixelsLineClean = pixelsLine.substring(26);
		
	
		String[] parts = pixelsLineClean.split("\\|");
		int numClases = this.getNumeroItems();
		for (int clases = 0; clases <= getNumeroItems(); clases++) {
			valores.add(parts[clases]);
			//numeroPixeles+=Integer.valueOf(rat.GetValueAsString(row, col));
		}
		System.out.println(pixelsLineClean);
	/*	
			gdal.AllRegister();
			hDataset = gdal.Open(archivo, gdalconstConstants.GA_ReadOnly);
			if (hDataset == null) {
				System.err
						.println("GDALOpen failed - " + gdal.GetLastErrorNo());
				System.err.println(gdal.GetLastErrorMsg());
			} else {
				numeroPixeles=0;
				for (iBand = 0; iBand < hDataset.getRasterCount(); iBand++) {
					hBand = hDataset.GetRasterBand(iBand + 1);
					int[] blockXSize = new int[1];
					int[] blockYSize = new int[1];
					hBand.GetBlockSize(blockXSize, blockYSize);
					RasterAttributeTable rat = hBand.GetDefaultRAT();
					if (bShowRAT && rat != null) {
						int colCount = rat.GetColumnCount();
						int rowCount = rat.GetRowCount();
						for (int row = 1; row < rowCount; row++) {
							if (row <= numeroItems)
								for (int col = 0; col < colCount; col++) {
									valores.add(rat.GetValueAsString(row, col));
									numeroPixeles+=Integer.valueOf(rat.GetValueAsString(row, col));
								}
						}
					}
				}
				hDataset.delete();
			}*/
		
	}
	private String getLineWithPixels(){
		final Process p;
		try {
	
			//p = Runtime.getRuntime().exec("CMD /C gdalinfo -stats -norat -nogcp -noct corpo.img");
			p = Runtime.getRuntime().exec("CMD /C gdalinfo -stats -norat -nogcp -noct " + getArchivo());
    	     BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
		     String line = null; 

		     try {
		        while ((line = input.readLine()) != null)
		        	if (line.contains("STATISTICS_HISTOBINVALUES")) {
		        		return line.trim();

		             }

		     } catch (IOException e) {
		            e.printStackTrace();
		     }
		    
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return "none";
	}

	public String getArchivo() {
		return archivo;
	}

	public void setArchivo(String archivo) {
		this.archivo = archivo;
	}

	public ArrayList<String> getValores() {
		return valores;
	}

	public void setValores(ArrayList<String> valores) {
		this.valores = valores;
	}

	public static void main(String[] a) {
		//GDALInfo i = new GDALInfo();
		//i.setArchivo("C:/ima/bnbareahidro2000.img");
		//i.obtenerInformacionPixeles();
		Integer[] periodos = new Integer[2];
		periodos[0] = 2005;
		periodos[1] = 2010;
		AnalisisResultadosWPSBosque analizador =  new AnalisisResultadosWPSBosque();
		analizador.nuevoAnalisisResultadosWPSBosque(2, 2, "no imorta ahora", periodos);
	}

	public Integer getNumeroItems() {
		return numeroItems;
	}

	public void setNumeroItems(Integer numeroItems) {
		this.numeroItems = numeroItems;
	}

	public Integer getNumeroPixeles() {
		return numeroPixeles;
	}

	public void setNumeroPixeles(Integer numeroPixeles) {
		this.numeroPixeles = numeroPixeles;
	}

}
