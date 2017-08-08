package co.gov.ideamredd.reportes;

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
		{
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
			}
		}
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
		GDALInfo i = new GDALInfo();
		i.setArchivo("C:/ima/bnbareahidro2000.img");
		i.obtenerInformacionPixeles();
		
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
