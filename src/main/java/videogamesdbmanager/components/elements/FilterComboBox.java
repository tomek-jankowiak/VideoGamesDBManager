package videogamesdbmanager.components.elements;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

public class FilterComboBox extends JComboBox {
  private final List<String> array;

  public FilterComboBox(List<String> array) {
    this.array = array;
    this.setEditable(true);
    final JTextField textfield = (JTextField) this.getEditor().getEditorComponent();
    textfield.addKeyListener(new KeyAdapter() {
      public void keyReleased(KeyEvent ke) {
        SwingUtilities.invokeLater(() -> comboFilter(textfield.getText()));
      }
    });

  }

  public void comboFilter(String enteredText) {
    if (!this.isPopupVisible()) {
      this.showPopup();
    }

    List<String> filterArray = new ArrayList<>();
    for (String value : array) {
      if (value.toLowerCase().contains(enteredText.toLowerCase())) {
        filterArray.add(value);
      }
    }
    if (filterArray.size() > 0) {
      DefaultComboBoxModel model = (DefaultComboBoxModel) this.getModel();
      model.removeAllElements();
      for (String s : filterArray)
        model.addElement(s);

      JTextField textfield = (JTextField) this.getEditor().getEditorComponent();
      textfield.setText(enteredText);
    }
  }
}
