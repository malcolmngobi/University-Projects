package TableStuff;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * An attempt on Requirement 7 but failed to implement it correctly
 * @author Mally
 *
 */
public class CellTextColour extends DefaultTableCellRenderer {

	
	
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column){
		Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		
		double val = Double.parseDouble((String) value);
		
		int num = 0;
		
		if(isSelected && Double.parseDouble((String) table.getValueAt(0, 6)) == val){
			cell.setForeground(Color.black);
			num = 2;
		}
		else{
			num = 3;
		}
		
		for(int i = 0; i< table.getRowCount() - num; i++){
			double value1 = Double.parseDouble((String) table.getValueAt(i, 6));
			double value2 = Double.parseDouble((String) table.getValueAt(i+1, 6));
			
			if(val > value2){
				cell.setForeground(Color.green.darker());
			}
			else if(val < value2){
				cell.setForeground(Color.red);
			}
		}

		if(!isSelected && Double.parseDouble((String) table.getValueAt(table.getRowCount() - 1, 6)) == val){
			cell.setForeground(Color.black);
		}

		return cell;

	}
}
